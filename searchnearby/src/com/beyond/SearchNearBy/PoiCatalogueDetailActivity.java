package com.beyond.SearchNearBy;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;

/**
 * Created with IntelliJ IDEA.
 * User: xieyang
 * Date: 10/17/13
 * Time: 10:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class PoiCatalogueDetailActivity extends Activity {
	private static final String TAG = "PoiCatalogueDetailActivity";

	private MapView detail_mapview = null;
	private MapController mapController = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MapManagerApplication app = (MapManagerApplication) this.getApplication();
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(this);
			app.mBMapManager.init(Content.strkey,new MapManagerApplication.MyGeneralListener());
		}
		setContentView(R.layout.poi_catalogue_detail);

		initMapView();
	}

	private void initMapView(){
		Log.d(TAG, "init MapView!");
		detail_mapview = (MapView) findViewById(R.id.detail_map);
		mapController = detail_mapview.getController();

		mapController.setZoom(17);			//设置初始缩放等级
		//mapController.setCenter(location);	//设置中心点
		detail_mapview.setBuiltInZoomControls(true);
		//Log.d(TAG,"My Location: "+location);

		//标记自己地理位置
//		Drawable mLocation = getResources().getDrawable(R.drawable.ic_current_loc);
//		mylocation_overlay = new PoiOverlay(mLocation,cata_mapview);
//		OverlayItem mOverlayItem = new OverlayItem(location,null,null);
//		mylocation_overlay.addItem(mOverlayItem);
//		cata_mapview.getOverlays().add(mylocation_overlay);
//
//		Drawable marker = getResources().getDrawable(R.drawable.ic_loc_normal);
//		poiOverlay = new PoiOverlay(marker,cata_mapview);
	}

	public void onBackClick(View view){
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
}