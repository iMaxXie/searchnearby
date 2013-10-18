package com.beyond.SearchNearBy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: xieyang
 * Date: 10/16/13
 * Time: 12:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class PoiCatalogueShowActivity extends Activity {
	private static final int SHOW_IN_LIST_MODE = 0;	//列表展示模式
	private static final int SHOW_IN_MAP_MODE = 1;	//地图展示模式
	private int current_page = 1;	//当前数据页
	private int current_mode = SHOW_IN_LIST_MODE;	//当前展示模式

	private static final String TAG="PoiCatalogueShowActivity";
	private static final String strkey="783597182b97a05e39ade3876480efd8";

	private POIData poiData = null;
	private MapController mapController = null;
	private MapView cata_mapview = null;
	private PoiOverlay poiOverlay = null;
	private PoiOverlay mylocation_overlay = null;

	private ListView cata_list = null;				//列表试图
	private RelativeLayout cata_map = null;			//地图试图
	private SimpleAdapter listadapter = null;
	private ArrayList<HashMap<String,String>> poilist = null;	//记录数据

	//测试数据，当前坐标点
	private double longitude = 34.261301;
	private double latitude = 108.948034;
//	private double longitude = 0.0;
//	private double latitude = 0.0;
	private GeoPoint location = null;

	private ImageButton mode_btn = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MapManagerApplication app = (MapManagerApplication) this.getApplication();
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(this);
			app.mBMapManager.init(Content.strkey,new MapManagerApplication.MyGeneralListener());
		}
		setContentView(R.layout.poi_catalogue_show);

		initActivity();
	}


	//初始化Activity
	private void initActivity() {
		//定位
		poiData = new POIData();
//		poiData.getCurrentByGPS(this);
//
//		longitude = poiData.getBDLocation().getLongitude();
//		latitude = poiData.getBDLocation().getLatitude();
		Log.d(TAG, "onReceiveLocation Longitude " + longitude);
		Log.d(TAG, "onReceiveLocation Latitude " + latitude);
		location = new GeoPoint((int)(longitude*1E6),(int)(latitude*1E6));


		mode_btn = (ImageButton) findViewById(R.id.catalogue_list_mode);
		mode_btn.setImageResource(R.drawable.ic_action_map);

		cata_map = (RelativeLayout) findViewById(R.id.catalogue_map_view);
		cata_list = (ListView) findViewById(R.id.catalogue_list_view);
		//默认以列表模式展示数据
		cata_map.setVisibility(View.GONE);

		initCatalogueMap();
		refreshCatalogueList();
	}

	//初始化地图
	private void initCatalogueMap(){

		Log.d(TAG,"init MapView!");
		cata_mapview = (MapView) findViewById(R.id.catalogue_list_map);
		mapController = cata_mapview.getController();

		mapController.setZoom(17);			//设置初始缩放等级
		mapController.setCenter(location);	//设置中心点
		cata_mapview.setBuiltInZoomControls(true);
		Log.d(TAG,"initCatalogueMap , My Location: "+location);

		//标记自己地理位置
		Drawable mLocation = getResources().getDrawable(R.drawable.ic_current_loc);
		mylocation_overlay = new PoiOverlay(mLocation,cata_mapview);
		OverlayItem mOverlayItem = new OverlayItem(location,null,null);
		mylocation_overlay.addItem(mOverlayItem);
		cata_mapview.getOverlays().add(mylocation_overlay);

		Drawable marker = getResources().getDrawable(R.drawable.ic_loc_normal);
		poiOverlay = new PoiOverlay(marker,cata_mapview);

	}

	//刷新数据，同时刷新listview与mapview
	private void refreshCatalogueList(){

		AsyncTask<Object,Object,Object> task = new AsyncTask<Object, Object, Object>() {
			ProgressDialog progressDialog = new ProgressDialog(PoiCatalogueShowActivity.this);

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressDialog.setMessage("数据加载中...");
				progressDialog.show();
			}

			@Override
			protected Object doInBackground(Object... params) {
				poilist = formatJson();
				return null;
			}

			@Override
			protected void onPostExecute(Object o) {
				super.onPostExecute(o);
				//更新ListView数据
				if( listadapter == null){
					listadapter = new SimpleAdapter(PoiCatalogueShowActivity.this,poilist,
						R.layout.catalogue_list_item,
						new String[]{"name","address","distance"},
						new int[]{R.id.dept_name,R.id.dept_addr,R.id.dept_distance});
					cata_list.setAdapter(listadapter);
					cata_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							Intent intent = new Intent();
							intent.setClass(PoiCatalogueShowActivity.this,PoiCatalogueDetailActivity.class);
							startActivity(intent);
						}
					});
				}
				else {
					listadapter.notifyDataSetChanged();

					Log.d(TAG," notifyDataSetChanged: "+listadapter.getItem(0));
				}

				//更新地图poi
				poiOverlay.removeAll();
				for(int i=0; i<poilist.size();i++){
					double longtitude = Double.parseDouble(poilist.get(i).get("longtitude"));
					double latitude = Double.parseDouble(poilist.get(i).get("latitude"));

					GeoPoint point = new GeoPoint((int)(latitude*1E6),(int)(longtitude*1E6));
					OverlayItem overlayItem = new OverlayItem(point, poilist.get(i).get("name"),
							poilist.get(i).get("address"));
					poiOverlay.addItem(overlayItem);
				}
				if(cata_mapview.getOverlays().size()==1)
					cata_mapview.getOverlays().add(poiOverlay);
				cata_mapview.refresh();

				progressDialog.dismiss();
			}
		};
		task.execute(0);
	}


	//Json数据解析
	private ArrayList<HashMap<String,String>> formatJson(){
		String jsonstr = poiData.getPOIbyLocation(location,"中餐厅",3000,current_page,10);
		ArrayList<HashMap<String,String>>listdata = new ArrayList<HashMap<String, String>>();
		Log.d(TAG,"Json result:"+jsonstr);

		try {

			JSONObject jsonObject = new JSONObject(jsonstr);
			JSONArray array = jsonObject.optJSONArray("poilist");

			for(int i=0; i<array.length(); i++){
				HashMap<String,String> item = new HashMap<String, String>();
				Log.d(TAG,"index "+i+" : "+array.optJSONObject(i));
				item.put("name", array.optJSONObject(i).optString("name"));
				item.put("address", array.optJSONObject(i).optString("address"));
				item.put("distance", array.optJSONObject(i).optString("distance"));
				item.put("longtitude", array.optJSONObject(i).optString("x"));
				item.put("latitude", array.optJSONObject(i).optString("y"));
				listdata.add(item);
			}
		} catch (JSONException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}

		return listdata;
	}

	public void onListBackClick(View view){
		finish();
	}

	//响应列表模式与地图模式切换
	public void onModeChanged(View view){
		if(current_mode == SHOW_IN_LIST_MODE){
			mode_btn.setImageResource(R.drawable.ic_action_list);
			cata_map.setVisibility(View.VISIBLE);
			cata_list.setVisibility(View.GONE);
			current_mode = SHOW_IN_MAP_MODE;
			Log.d(TAG,"current_mode: SHOW_IN_MAP_MODE");
		}
		else if(current_mode == SHOW_IN_MAP_MODE){
			mode_btn.setImageResource(R.drawable.ic_action_map);
			cata_list.setVisibility(View.VISIBLE);
			cata_map.setVisibility(View.GONE);
			current_mode = SHOW_IN_LIST_MODE;
			Log.d(TAG,"current_mode: SHOW_IN_LIST_MODE");
		}

	}

	//响应翻页事件
	public void onPageChange(View view){
		Log.d(TAG,"onPageChange");
		if(current_page>0){
			if(view.getId() == R.id.previous)
				current_page--;
			if(view.getId() == R.id.next)
				current_page++;
			refreshCatalogueList();
		}
	}

	//定位到屏幕中心
	public void onLocationToCenter(View view){
		Log.d(TAG,"onLocationToCenter");
		mapController.animateTo(location);
	}

	class PoiOverlay extends ItemizedOverlay {

		public PoiOverlay(Drawable drawable, MapView mapView) {
			super(drawable, mapView);
		}
	}

	@Override
	protected void onResume() {
		cata_mapview.onResume();
		super.onResume();
	}

	@Override
	protected void onPause() {
		cata_mapview.onPause();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		cata_mapview.destroy();
		super.onDestroy();
	}
}