package com.beyond.SearchNearBy.model;


import android.content.Context;
import android.util.Log;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.beyond.SearchNearBy.Content;

public class LocationManager {
    private static final String TAG="snb.LocationManager";
    private BDLocation location;
    private LocationClient locClient;
    private LocationClientOption locOption;

    public LocationManager(Context context){
        locClient = new LocationClient(context);
        locOption = new LocationClientOption();
    }

    public static void getCurrentLocation(Context context,final GetCurrentLocListener listener){

        final LocationManager locationManager = new LocationManager(context);

        locationManager.locClient.setAK(Content.strkey);
        locationManager.locOption.setOpenGps(true);			//打开gps
        locationManager.locOption.setCoorType("bd09ll");	//设置坐标类型
        locationManager.locOption.setAddrType("all");
        locationManager.locOption.setScanSpan(1000);		//请求间隔
        locationManager.locClient.setLocOption(locationManager.locOption);

        locationManager.locClient.registerLocationListener(new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                Log.d(TAG, "onReceiveLocation LocType " + bdLocation.getLocType());
                if (!bdLocation.hasAddr()){
                    Log.d(TAG, "locating, please wait...");
                    bdLocation.setAddrStr("请稍等，定位中...");
                    //locationManager.location.setLocation(bdLocation);
                }else{
                    Log.d(TAG, "onReceiveLocation address " + bdLocation.getAddrStr());
                    Log.d(TAG, "onReceiveLocation latitude " + bdLocation.getLatitude());
                    Log.d(TAG, "onReceiveLocation longitude " + bdLocation.getLongitude());
                    locationManager.location = bdLocation;
                    locationManager.locClient.stop();
                    listener.getLoctioning(bdLocation);
                }
            }

            @Override
            public void onReceivePoi(BDLocation bdLocation) {
                if (bdLocation == null){
                    return ;
                }
            }
        });
        locationManager.locClient.start();
        //locClient.requestLocation();
        Log.d(TAG,"requestLocation"+locationManager.locClient.requestLocation());
    }

    public static void locationInBackground(Context context,GetCurrentLocListener listener){

    }

    public static interface GetCurrentLocListener{
        public void getLoctioning(BDLocation bdLocation);
    }
}
