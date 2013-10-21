package com.beyond.SearchNearBy;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * Created with IntelliJ IDEA.
 * User: xieyang
 * Date: 10/17/13
 * Time: 10:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class PoiCatalogueDetailActivity extends Activity {
	private static final String TAG = "snb.PoiCatalogueDetailActivity";

    private CurrentLocation currentLoc;
    private ServiceConnection serviceCon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG,"onServiceConnected: ComponentName"+name.getClassName());
            CurrentLocation.ServiceBindler bindler = (CurrentLocation.ServiceBindler) service;
            currentLoc = bindler.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG,"onServiceDisconnected: ComponentName"+name.getClassName());
            currentLoc = null;
        }
    };

	private MapView detail_mapview = null;
	private MapController mapController = null;
    private PoiOverlay startOverlay = null;
    private PoiOverlay endOverlay = null;
    private Drawable startflag = null;
    private Drawable endflag = null;

    private double longitude = 0.0;
    private double latitude = 0.0;
    private GeoPoint start_loc = null;
    private GeoPoint end_loc = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MapManagerApplication app = (MapManagerApplication) this.getApplication();
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(this);
			app.mBMapManager.init(Content.strkey,new MapManagerApplication.MyGeneralListener());
		}
		setContentView(R.layout.poi_catalogue_detail);

        Intent intent = new Intent(this,CurrentLocation.class);
        bindService(intent,serviceCon,BIND_AUTO_CREATE);
        Log.d(TAG,"getIntent:"+getIntent().getStringExtra("longtitude"));
        longitude = Double.parseDouble(getIntent().getStringExtra("longtitude"));
        latitude = Double.parseDouble(getIntent().getStringExtra("latitude"));
        end_loc = new GeoPoint((int)(latitude*1E6),(int)(longitude*1E6));
        Log.d(TAG, "END Longitude " + longitude);
        Log.d(TAG, "END Latitude " + latitude);

        if(currentLoc == null){
            longitude =108.908089;
            latitude = 34.237386;
            Log.d(TAG, "测试数据");
        }else{
            Log.d(TAG, "定位数据");
            longitude = currentLoc.getBDLocation().getLongitude();
            latitude = currentLoc.getBDLocation().getLatitude();
        }
        start_loc = new GeoPoint((int)(latitude*1E6),(int)(longitude*1E6));
        Log.d(TAG, "START Longitude " + longitude);
        Log.d(TAG, "START Latitude " + latitude);

		initMapView();
	}

	private void initMapView(){
		Log.d(TAG, "init MapView!");
		detail_mapview = (MapView) findViewById(R.id.detail_map);
		mapController = detail_mapview.getController();

		mapController.setZoom(17);			//设置初始缩放等级
		mapController.setCenter(start_loc);	//设置中心点
		detail_mapview.setBuiltInZoomControls(true);

		//标记自己地理位置
        startflag = getResources().getDrawable(R.drawable.ic_loc_from);
        endflag = getResources().getDrawable(R.drawable.ic_loc_to);
		startOverlay = new PoiOverlay(startflag,detail_mapview);
		OverlayItem startItem = new OverlayItem(start_loc,null,null);
		startOverlay.addItem(startItem);
        endOverlay = new PoiOverlay(endflag,detail_mapview);
        OverlayItem endItem = new OverlayItem(end_loc,null,null);
        startOverlay.addItem(endItem);
		detail_mapview.getOverlays().add(startOverlay);
        detail_mapview.getOverlays().add(endOverlay);
	}

	public void onDetailBackClick(View view){
		finish();
	}

	public void onRouteStart(View view){

	}

	@Override
	protected void onResume() {
		super.onResume();
		detail_mapview.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		detail_mapview.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		detail_mapview.destroy();
	}

    class PoiOverlay extends ItemizedOverlay {

        public PoiOverlay(Drawable drawable, MapView mapView) {
            super(drawable, mapView);
        }
    }
}