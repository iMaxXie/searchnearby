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
import com.beyond.SearchNearBy.model.PoiManager;
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
    private boolean refresh = false;


    private MapController mapController = null;
    private MapView cata_mapview = null;
    private PoiOverlay poiOverlay = null;
    private MyLocationOverlay mylocation_overlay = null;
    private View mapPopWindow;

    private ListView cata_list = null;				//列表试图
    private RelativeLayout cata_map = null;			//地图试图
    private SimpleAdapter listadapter = null;
    private ArrayList<HashMap<String,String>> poilist = new ArrayList<HashMap<String, String>>();	//记录列表数据
    private ArrayList<HashMap<String,String>> datalist = new ArrayList<HashMap<String, String>>();	//记录列表数据

    private double longitude = 0.0;
    private double latitude = 0.0;
    private GeoPoint location = null;

    private ImageButton mode_btn = null;
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

        getIntentExtra();
        initComponent();
        initLocation();
        //
    }

    private void getIntentExtra(){
        keyword = getIntent().getStringExtra("keyword");
        latitude = getIntent().getDoubleExtra("mylatitude", 0.0);
        longitude = getIntent().getDoubleExtra("mylongitude",0.0);
        Log.d(TAG, "getIntent Longitude " + longitude);
        Log.d(TAG, "getIntent Latitude " + latitude);
    }

    //初始化Activity
    private void initComponent() {
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
                int i = current_page;
                current_page++;
                refreshCatalogueList();
                if(poilist == null) current_page =i;
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
                        datalist.clear();
                        poilist.clear();
                        current_page = 1;
                        refreshCatalogueList();
                    }
                });
                builder.show();
            }
        });

    }

    private void initLocation(){
        if(latitude == 0.0 && longitude == 0.0) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
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
        else {
            location = new GeoPoint((int)(latitude*1E6),(int)(longitude*1E6));
            initCatalogueMap();
            refreshCatalogueList();
        }
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

        Drawable marker = getResources().getDrawable(R.drawable.ic_loc_normal);
        poiOverlay = new PoiOverlay(marker,cata_mapview);

        mapPopWindow = LayoutInflater.from(this).inflate(R.layout.map_pop_window, null);
        mapPopWindow.setVisibility(View.GONE);
        cata_mapview.addView(mapPopWindow);

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
                String json = PoiManager.getPOIbyLocation(location,keyword,bundary,current_page,record);
                if(json!=null){
                    poilist = PoiManager.formatJson(json);
                    if(!refresh){
                        datalist.addAll(poilist);
                    }else{
                        refresh = false;
                    }
                    Log.d(TAG,"doInBackground:poilist = "+poilist.size());
                    Log.d(TAG,"doInBackground:datalist = "+datalist.size());
                }

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                //datalist.addAll(poilist);
                //更新ListView数据
                if(poilist == null){
                    //Toast.makeText(PoiCatalogueShowActivity.this,"",Toast.LENGTH_LONG);
                }
                else {
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
                                intent.putExtra("target_longtitude",Double.parseDouble(datalist.get(position).get("longtitude")));
                                intent.putExtra("target_latitude",Double.parseDouble(datalist.get(position).get("latitude")));
                                intent.putExtra("mylongtitude",longitude);
                                intent.putExtra("mylatitude",latitude);
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
                    mapPopWindow.setVisibility(View.GONE);
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
                }
                progressDialog.dismiss();
            }
        };
        task.execute(0);
    }


    //Json数据解析
//    private  ArrayList<HashMap<String,String>> formatJson(){
//        String jsonstr = POIData.getPOIbyLocation(location,keyword,bundary,current_page,record);
//        ArrayList<HashMap<String,String>>listdata = new ArrayList<HashMap<String, String>>();
//        Log.d(TAG,"Json result:"+jsonstr);
//
//        try {
//
//            JSONObject jsonObject = new JSONObject(jsonstr);
//            JSONArray array = jsonObject.optJSONArray("poilist");
//            max_page = (jsonObject.optInt("total")/record)+1;
//
//            if(array ==null) return null;
//
//            for(int i=0; i<array.length(); i++){
//                HashMap<String,String> item = new HashMap<String, String>();
//                Log.d(TAG,"index "+i+" : "+array.optJSONObject(i));
//                item.put("name", array.optJSONObject(i).optString("name"));
//                item.put("address", array.optJSONObject(i).optString("address"));
//                item.put("distance", array.optJSONObject(i).optString("distance")+"m");
//                item.put("longtitude", array.optJSONObject(i).optString("x"));
//                item.put("latitude", array.optJSONObject(i).optString("y"));
//                item.put("tel", array.optJSONObject(i).optString("tel"));
//                listdata.add(item);
//                datalist.add(item);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//        return listdata;
//    }

    public void onListBackClick(View view){
        finish();
    }

    public void onRefreshClick(View view){
        refresh = true;
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
        int i = current_page;
        Log.d(TAG,"total page:"+max_page+" onPageChange current:"+current_page);
        if(view.getId() == R.id.previous) i--;
        if(view.getId() == R.id.next) i++;
        refreshCatalogueList();
        if(poilist != null) current_page =i;
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

        @Override
        public boolean onTap(GeoPoint geoPoint, MapView mapView) {
            mapPopWindow.setVisibility(View.GONE);
            return super.onTap(geoPoint, mapView);
        }

        @Override
        protected boolean onTap(int i) {
            Log.d("BaiduMapDemo", "onTap " + i);
            com.baidu.mapapi.map.OverlayItem item = poiOverlay.getItem(i);
            GeoPoint point = item.getPoint();
            String title = item.getTitle();
            String content = item.getSnippet();

            TextView titleTextView = (TextView) mapPopWindow.findViewById(R.id.titleTextView);
            TextView contentTextView = (TextView)mapPopWindow.findViewById(R.id.contentTextView);
            titleTextView.setText(title);
            contentTextView.setText(content);
            contentTextView.setVisibility(View.VISIBLE);

            MapView.LayoutParams layoutParam  = new MapView.LayoutParams(
                    //控件宽,继承自ViewGroup.LayoutParams
                    MapView.LayoutParams.WRAP_CONTENT,
                    //控件高,继承自ViewGroup.LayoutParams
                    MapView.LayoutParams.WRAP_CONTENT,
                    //使控件固定在某个地理位置
                    point,
                    0,
                    -40,
                    //控件对齐方式
                    MapView.LayoutParams.BOTTOM_CENTER);

            mapPopWindow.setVisibility(View.VISIBLE);

            mapPopWindow.setLayoutParams(layoutParam);

            mapController.animateTo(point);
            return super.onTap(i);
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