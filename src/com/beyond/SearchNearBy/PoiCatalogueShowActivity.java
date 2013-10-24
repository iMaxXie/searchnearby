package com.beyond.SearchNearBy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.beyond.SearchNearBy.model.LocationManager;
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
    private static final String TAG="snb.PoiCatalogueShowActivity";

    private int current_page = 1;							//默认数据页
    private int max_page = 0;								//最大页数
    private int current_mode = Content.SHOW_IN_LIST_MODE;	//默认展示模式
    private int bundary = 2000;								//默认搜索范围
    private int record = 10;								    //默认数据页大小
    private String keyword = null;							//搜索关键字


    private MapController mapController = null;
    private MapView cata_mapview = null;
    private PoiOverlay poiOverlay = null;
    private MyLocationOverlay mylocation_overlay = null;
    private PopupOverlay popup = null;

    private ListView cata_list = null;				//列表试图
    private RelativeLayout cata_map = null;			//地图试图
    private SimpleAdapter listadapter = null;
    private ArrayList<HashMap<String,String>> poilist = null;	//记录列表数据
    private ArrayList<HashMap<String,String>> datalist = new ArrayList<HashMap<String, String>>();	//记录列表数据

    private double longitude = 0.0;
    private double latitude = 0.0;
    private GeoPoint location = null;

    private ImageButton mode_btn = null;
    private ProgressDialog progressDialog = null;
    private LinearLayout boundary_select;                  //选择范围
    private AlertDialog.Builder builder;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapManagerApplication app = (MapManagerApplication) this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(this);
            app.mBMapManager.init(Content.strkey,new MapManagerApplication.MyGeneralListener());
        }
        setContentView(R.layout.poi_catalogue_show);

        Intent getdata = getIntent();
        keyword = getdata.getStringExtra("keyword");
        Log.d(TAG, "intent.getStringExtra: " + keyword);


        initActivity();
        //
    }


    //初始化Activity
    private void initActivity() {

        mode_btn = (ImageButton) findViewById(R.id.catalogue_list_mode);
        mode_btn.setImageResource(R.drawable.ic_action_map);

        cata_map = (RelativeLayout) findViewById(R.id.catalogue_map_view);
        cata_mapview = (MapView) findViewById(R.id.catalogue_list_map);
        cata_list = (ListView) findViewById(R.id.catalogue_list_view);
        LinearLayout footer_more = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_more,cata_list,false);
        TextView textView = (TextView) footer_more.findViewById(R.id.item_range);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_page++;
                refreshCatalogueList();
            }
        });
        cata_list.addFooterView(footer_more);
        //默认以列表模式展示数据
        cata_map.setVisibility(View.GONE);


        builder = new AlertDialog.Builder(this);

        boundary_select = (LinearLayout) findViewById(R.id.boundary_select);
        boundary_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] radius = new String[]{"1000米内", "2000米内", "3000米内", "4000米内", "5000米内"};
                builder.setItems(radius, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int a) {
                        TextView textView = (TextView) findViewById(R.id.boundary_value);
                        textView.setText("范围:"+radius[a]);
                        bundary = (a+1)*1000;

                        //refreshCatalogueList();
                    }
                });
                builder.show();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("定位中....");
        progressDialog.show();
        LocationManager.getCurrentLocation(PoiCatalogueShowActivity.this,new LocationManager.GetCurrentLocListener(){
            @Override
            public void getLoctioning(BDLocation bdLocation) {
                longitude = bdLocation.getLongitude();
                latitude = bdLocation.getLatitude();
                Log.d(TAG, "onReceiveLocation Longitude " + longitude);
                Log.d(TAG, "onReceiveLocation Latitude " + latitude);
                location = new GeoPoint((int)(latitude*1E6),(int)(longitude*1E6));
                progressDialog.dismiss();
                initCatalogueMap();
                refreshCatalogueList();
            }
        });
    }

    //初始化地图
    private void initCatalogueMap(){

        mapController = cata_mapview.getController();

        mapController.setZoom(17);			//设置初始缩放等级
        mapController.setCenter(location);	//设置中心点
        cata_mapview.setBuiltInZoomControls(false);

        mylocation_overlay = new MyLocationOverlay(cata_mapview);
        LocationData locData = new LocationData();
        locData.latitude = latitude;
        locData.longitude = longitude;
        mylocation_overlay.setData(locData);
        cata_mapview.getOverlays().add(mylocation_overlay);

        //PopupOverlay
        popup = new PopupOverlay(cata_mapview,new PopupClickListener() {
            @Override
            public void onClickedPopup(int i) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        Drawable marker = getResources().getDrawable(R.drawable.ic_loc_normal);
        poiOverlay = new PoiOverlay(marker,cata_mapview);

    }

    //刷新数据，同时刷新listview与mapview
    private void refreshCatalogueList(){

        AsyncTask<Object,Object,Object> task = new AsyncTask<Object, Object, Object>() {

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
                    listadapter = new SimpleAdapter(PoiCatalogueShowActivity.this,datalist,
                            R.layout.catalogue_list_item,
                            new String[]{"name","address","distance"},
                            new int[]{R.id.dept_name,R.id.dept_addr,R.id.dept_distance});
                    cata_list.setAdapter(listadapter);
                    cata_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent();
                            intent.setClass(PoiCatalogueShowActivity.this,PoiCatalogueDetailActivity.class);
                            Log.d(TAG,datalist.get(position).get("longtitude"));
                            intent.putExtra("longtitude",datalist.get(position).get("longtitude"));
                            intent.putExtra("latitude",datalist.get(position).get("latitude"));
                            intent.putExtra("name",datalist.get(position).get("name"));
                            intent.putExtra("address",datalist.get(position).get("address"));
                            intent.putExtra("tel",datalist.get(position).get("tel"));
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
    private  ArrayList<HashMap<String,String>> formatJson(){
        String jsonstr = POIData.getPOIbyLocation(location,keyword,bundary,current_page,record);
        ArrayList<HashMap<String,String>>listdata = new ArrayList<HashMap<String, String>>();
        Log.d(TAG,"Json result:"+jsonstr);

        try {

            JSONObject jsonObject = new JSONObject(jsonstr);
            JSONArray array = jsonObject.optJSONArray("poilist");
            max_page = (jsonObject.optInt("total")/record)+1;

            for(int i=0; i<array.length(); i++){
                HashMap<String,String> item = new HashMap<String, String>();
                Log.d(TAG,"index "+i+" : "+array.optJSONObject(i));
                item.put("name", array.optJSONObject(i).optString("name"));
                item.put("address", array.optJSONObject(i).optString("address"));
                item.put("distance", array.optJSONObject(i).optString("distance")+"m");
                item.put("longtitude", array.optJSONObject(i).optString("x"));
                item.put("latitude", array.optJSONObject(i).optString("y"));
                item.put("tel", array.optJSONObject(i).optString("tel"));
                listdata.add(item);
                datalist.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return listdata;
    }

    public void onListBackClick(View view){
        finish();
    }

    public void onRefreshClick(View view){
        refreshCatalogueList();
    }

    //响应列表模式与地图模式切换
    public void onModeChanged(View view){
        if(current_mode == Content.SHOW_IN_LIST_MODE){
            mode_btn.setImageResource(R.drawable.ic_action_list);
            cata_map.setVisibility(View.VISIBLE);
            cata_list.setVisibility(View.GONE);
            current_mode = Content.SHOW_IN_MAP_MODE;
            Log.d(TAG,"current_mode: SHOW_IN_MAP_MODE");
        }
        else if(current_mode == Content.SHOW_IN_MAP_MODE){
            mode_btn.setImageResource(R.drawable.ic_action_map);
            cata_list.setVisibility(View.VISIBLE);
            cata_map.setVisibility(View.GONE);
            current_mode = Content.SHOW_IN_LIST_MODE;
            Log.d(TAG,"current_mode: SHOW_IN_LIST_MODE");
        }

    }

    //响应翻页事件
    public void onPageChange(View view){
        Log.d(TAG,"total page:"+max_page+" onPageChange current:"+current_page);
        if(view.getId() == R.id.previous && current_page>1) {
            current_page--;
        }
        if(view.getId() == R.id.next)
            current_page++;
        refreshCatalogueList();
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
//        Intent intent = new Intent(this,CurrentLocation.class);
//        bindService(intent,serviceCon,BIND_AUTO_CREATE);
        super.onResume();
    }

    @Override
    protected void onPause() {
        cata_mapview.onPause();
//        Log.d(TAG,"currentLoc"+currentLoc);
//        if(currentLoc!=null)
//            unbindService(serviceCon);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        cata_mapview.destroy();
        super.onDestroy();
    }
}