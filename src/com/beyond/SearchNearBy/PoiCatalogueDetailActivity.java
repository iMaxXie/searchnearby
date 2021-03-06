package com.beyond.SearchNearBy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.search.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.beyond.SearchNearBy.model.LocationManager;

/**
 * Created with IntelliJ IDEA.
 * User: xieyang
 * Date: 10/17/13
 * Time: 10:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class PoiCatalogueDetailActivity extends Activity {
	private static final String TAG = "snb.PoiCatalogueDetailActivity";

    private MapManagerApplication app = null;
//    private CurrentLocation currentLoc;
//    private ServiceConnection serviceCon = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            Log.d(TAG,"onServiceConnected: ComponentName"+name.getClassName());
//            CurrentLocation.ServiceBindler bindler = (CurrentLocation.ServiceBindler) service;
//            currentLoc = bindler.getService();
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            Log.d(TAG,"onServiceDisconnected: ComponentName"+name.getClassName());
//            currentLoc = null;
//        }
//    };

	private MapView detail_mapview = null;
	private MapController mapController = null;
    private PoiOverlay startOverlay = null;
    private PoiOverlay endOverlay = null;
    private Drawable startflag = null;
    private Drawable endflag = null;

    private double longitude = 0.0;
    private double latitude = 0.0;
    private MKPlanNode stNode = null;
    private MKPlanNode edNode = null;

    private RouteOverlay routeOverlay = null;
    private TransitOverlay transitOverlay = null;
    private MKRoute route = null;                   //保存驾车/步行路线数据的变量，供浏览节点时使用
    private MKSearch mkSearch = null;

    private TextView route_distance = null;
    private TextView target_name = null;
    private TextView target_addr = null;
    private TextView target_tel = null;

    private ProgressDialog progressDialog = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (MapManagerApplication) this.getApplication();
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(this);
			app.mBMapManager.init(Content.strkey,new MapManagerApplication.MyGeneralListener());
		}
		setContentView(R.layout.poi_catalogue_detail);


        getIntentExtra();
        initComponent();

        if(stNode.pt.getLatitudeE6()==0||stNode.pt.getLongitudeE6()==0){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("定位中...");
            progressDialog.show();

            LocationManager.getCurrentLocation(this,new LocationManager.GetCurrentLocListener() {
                @Override
                public void getLoctioning(BDLocation bdLocation) {
                    latitude = bdLocation.getLatitude();
                    longitude = bdLocation.getLongitude();
                    progressDialog.dismiss();
                    stNode.pt = new GeoPoint((int)(latitude*1E6),(int)(longitude*1E6));
                    Log.d(TAG, "START Longitude " + longitude);
                    Log.d(TAG, "START Latitude " + latitude);
                    initMapView();
                }
            });
        } else{
            initMapView();
        }
	}


    private void getIntentExtra(){
        edNode = new MKPlanNode();
        stNode = new MKPlanNode();
        //目标点
        longitude =getIntent().getDoubleExtra("target_longtitude",0.0);
        latitude = getIntent().getDoubleExtra("target_latitude",0.0);
        edNode.pt = new GeoPoint((int)(latitude*1E6),(int)(longitude*1E6));
        Log.d(TAG, "END Longitude " + longitude);
        Log.d(TAG, "END Latitude " + latitude);

        longitude =getIntent().getDoubleExtra("mylongtitude",0.0);
        latitude = getIntent().getDoubleExtra("mylatitude",0.0);
        stNode.pt = new GeoPoint((int)(latitude*1E6),(int)(longitude*1E6));
        Log.d(TAG, "START Longitude " + longitude);
        Log.d(TAG, "START Latitude " + latitude);
    }

    private void initComponent(){
        route_distance = (TextView) findViewById(R.id.detail_route);
        route_distance.setVisibility(View.GONE);
        target_name = (TextView) findViewById(R.id.detail_name);
        target_name.setVisibility(View.GONE);
        target_addr = (TextView) findViewById(R.id.detail_addr);
        target_addr.setVisibility(View.GONE);
        target_name.setText(getIntent().getStringExtra("name"));
        target_addr.setText(getIntent().getStringExtra("address"));

        target_tel = (TextView) findViewById(R.id.detail_phone);


        detail_mapview = (MapView) findViewById(R.id.detail_map);
        mapController = detail_mapview.getController();
        mapController.setZoom(17);			//设置初始缩放等级
        detail_mapview.setVisibility(View.INVISIBLE);
        detail_mapview.setBuiltInZoomControls(false);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.route_choose);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rout_by_foot: mkSearch.walkingSearch("西安",stNode,"西安",edNode); break;
                    case R.id.rout_by_bus: mkSearch.transitSearch("西安",stNode,edNode); break;
                    case R.id.rout_by_car: mkSearch.drivingSearch("西安", stNode, "西安", edNode); break;
                }

            }
        });
    }

	private void initMapView(){
		Log.d(TAG, "init MapView!");

        mapController.setCenter(stNode.pt);	//设置中心点
        detail_mapview.setVisibility(View.VISIBLE);

		//设置起始点和终点
        startflag = getResources().getDrawable(R.drawable.ic_loc_from);
        endflag = getResources().getDrawable(R.drawable.ic_loc_to);
		startOverlay = new PoiOverlay(startflag,detail_mapview);
		OverlayItem startItem = new OverlayItem(stNode.pt,null,null);
		startOverlay.addItem(startItem);
        endOverlay = new PoiOverlay(endflag,detail_mapview);
        OverlayItem endItem = new OverlayItem(edNode.pt,null,null);
        endOverlay.addItem(endItem);
		detail_mapview.getOverlays().add(startOverlay);
        detail_mapview.getOverlays().add(endOverlay);

        target_name.setVisibility(View.VISIBLE);
        target_addr.setVisibility(View.VISIBLE);
        route_distance.setVisibility(View.VISIBLE);
        Log.d(TAG,"tel:"+getIntent().getStringExtra("tel"));
        if(!getIntent().getStringExtra("tel").equals(""))
            target_tel.setText(getIntent().getStringExtra("tel"));
        else target_tel.setText("暂无信息");

        mkSearch = new MKSearch();
        mkSearch.init(app.mBMapManager, new MKSearchListener(){
            @Override
            public void onGetPoiResult(MKPoiResult mkPoiResult, int i, int i2) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onGetTransitRouteResult(MKTransitRouteResult routeResult, int error) {
                if (error != 0 || routeResult == null) {
                    Toast.makeText(PoiCatalogueDetailActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                    return;
                }
                transitOverlay = new TransitOverlay(PoiCatalogueDetailActivity.this, detail_mapview);
                // 此处仅展示一个方案作为示例
                Log.d(TAG,"getRoute: "+routeResult.getPlan(0));
                transitOverlay.setData(routeResult.getPlan(0));
                //清除其他图层
                detail_mapview.getOverlays().clear();
                //添加路线图层
                detail_mapview.getOverlays().add(transitOverlay);
                //执行刷新使生效
                detail_mapview.refresh();
                // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
                detail_mapview.getController().zoomToSpan(transitOverlay.getLatSpanE6(), transitOverlay.getLonSpanE6());
                //移动地图到起点
                detail_mapview.getController().animateTo(routeResult.getStart().pt);
                //将路线数据保存给全局变量
                //route = routeResult.getPlan(0).getRoute(0);
                route_distance.setText("全程"+routeResult.getPlan(0).getDistance()+"米，大约耗时"+ routeResult.getPlan(0).getTime()/60+"分钟");
            }

            @Override
            public void onGetDrivingRouteResult(MKDrivingRouteResult routeResult, int error) {
                if (error != 0 || routeResult == null) {
                    Toast.makeText(PoiCatalogueDetailActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                    return;
                }
                routeOverlay = new RouteOverlay(PoiCatalogueDetailActivity.this, detail_mapview);
                // 此处仅展示一个方案作为示例
                Log.d(TAG,"getRoute: "+routeResult.getPlan(0).getRoute(0));
                routeOverlay.setData(routeResult.getPlan(0).getRoute(0));
                //清除其他图层
                detail_mapview.getOverlays().clear();
                //添加路线图层
                detail_mapview.getOverlays().add(routeOverlay);
                //执行刷新使生效
                detail_mapview.refresh();
                // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
                detail_mapview.getController().zoomToSpan(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6());
                //移动地图到起点
                detail_mapview.getController().animateTo(routeResult.getStart().pt);
                //将路线数据保存给全局变量
                route = routeResult.getPlan(0).getRoute(0);
                //重置路线节点索引，节点浏览时使用
                route_distance.setText("全程"+routeResult.getPlan(0).getDistance()+"米，大约耗时"+ routeResult.getPlan(0).getTime()/60+"分钟");
            }

            @Override
            public void onGetWalkingRouteResult(MKWalkingRouteResult routeResult, int error) {
                if (error != 0 || routeResult == null) {
                    Toast.makeText(PoiCatalogueDetailActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                    return;
                }
                routeOverlay = new RouteOverlay(PoiCatalogueDetailActivity.this, detail_mapview);
                // 此处仅展示一个方案作为示例
                Log.d(TAG,"getRoute: "+routeResult.getPlan(0).getRoute(0));
                routeOverlay.setData(routeResult.getPlan(0).getRoute(0));
                //清除其他图层
                detail_mapview.getOverlays().clear();
                //添加路线图层
                detail_mapview.getOverlays().add(routeOverlay);
                //执行刷新使生效
                detail_mapview.refresh();
                // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
                detail_mapview.getController().zoomToSpan(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6());
                //移动地图到起点
                detail_mapview.getController().animateTo(routeResult.getStart().pt);
                //将路线数据保存给全局变量
                route = routeResult.getPlan(0).getRoute(0);
                //重置路线节点索引，节点浏览时使用
                route_distance.setText("全程"+routeResult.getPlan(0).getDistance()+"米，大约耗时"+ routeResult.getPlan(0).getTime()/60+"分钟");
            }

            @Override
            public void onGetAddrResult(MKAddrInfo mkAddrInfo, int i) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onGetBusDetailResult(MKBusLineResult mkBusLineResult, int i) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onGetSuggestionResult(MKSuggestionResult mkSuggestionResult, int i) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onGetPoiDetailSearchResult(int i, int i2) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onGetShareUrlResult(MKShareUrlResult mkShareUrlResult, int i, int i2) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

	}

	public void onDetailBackClick(View view){
		finish();
	}

	public void onRouteStart(View view){

	}

	@Override
	protected void onResume() {
//        Intent intent = new Intent(this,CurrentLocation.class);
//        bindService(intent,serviceCon,BIND_AUTO_CREATE);
		detail_mapview.onResume();
        super.onResume();
	}

	@Override
	protected void onPause() {
//        unbindService(serviceCon);
		detail_mapview.onPause();
        super.onPause();
	}

	@Override
	protected void onDestroy() {
		detail_mapview.destroy();
        super.onDestroy();
	}

    class PoiOverlay extends ItemizedOverlay {

        public PoiOverlay(Drawable drawable, MapView mapView) {
            super(drawable, mapView);
        }
    }
}