package com.beyond.SearchNearBy;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import org.apache.http.conn.util.InetAddressUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created with IntelliJ IDEA.
 * User: xieyang
 * Date: 10/19/13
 * Time: 7:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class CurrentLocation extends Service {
	private final String TAG="snb.CurrentLocation Service";

	private ServiceBindler bindler = new ServiceBindler();

	private BDLocation bdLoc = new BDLocation();
	private LocationClient locClient;
	private LocationClientOption locOption;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG," onCreate");
		getCurrentByGPS(this);
	}


	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG,"onBind ");
		return bindler;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.d(TAG,"onUnbind");
		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy() {
		Log.d(TAG,"onDestroy");
		super.onDestroy();
	}

	public BDLocation getBDLocation(){
		return bdLoc;
	}

	public void getCurrentByGPS(Context content){
		locClient = new LocationClient(content);
		locOption = new LocationClientOption();

		locClient.setAK(Content.strkey);
		locOption.setOpenGps(true);			//打开gps
		locOption.setCoorType("bd09ll");	//设置坐标类型
		locOption.setAddrType("all");
		locOption.setScanSpan(1000);		//请求间隔
		locClient.setLocOption(locOption);

		locClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation bdLocation) {
				Log.d(TAG, "onReceiveLocation LocType " + bdLocation.getLocType());
				if (!bdLocation.hasAddr()){
					Log.d(TAG, "locating, please wait...");
					bdLocation.setAddrStr("请稍等，定位中...");
					bdLoc = bdLocation;
				}else{
					Log.d(TAG, "onReceiveLocation address " + bdLocation.getAddrStr());
					Log.d(TAG, "onReceiveLocation latitude " + bdLocation.getLatitude());
					Log.d(TAG, "onReceiveLocation longitude " + bdLocation.getLongitude());
					bdLoc = bdLocation;

					locClient.stop();
				}
			}

			@Override
			public void onReceivePoi(BDLocation bdLocation) {
				if (bdLocation == null){
					return ;
				}
			}
		});
		locClient.start();
		//locClient.requestLocation();
		Log.d(TAG,"requestLocation"+locClient.requestLocation());
	}


//	public String getCurrentByIP(){
//		try {
//			for(Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();en.hasMoreElements();)
//			{
//				NetworkInterface intf = en.nextElement();
//				for(Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();){
//					InetAddress inetAddress = enumIpAddr.nextElement();
//					if(!inetAddress.isLoopbackAddress() &&
//							InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())){
//
//						return inetAddress.getHostAddress().toString();
//					}
//				}
//			}
//		} catch (SocketException e) {
//			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//		}
//		return null;
//	}

	class ServiceBindler extends Binder {
		public CurrentLocation getService(){
			return CurrentLocation.this;
		}
	}

}
