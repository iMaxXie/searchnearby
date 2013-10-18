package com.beyond.SearchNearBy;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.LocationData;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created with IntelliJ IDEA.
 * User: xieyang
 * Date: 10/16/13
 * Time: 9:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class POIData {

	private BDLocation bdLoc = new BDLocation();

	public BDLocation getBDLocation(){
		return bdLoc;
	}

	public void getCurrentByGPS(Context content){
		LocationClient locClient = new LocationClient(content);
		LocationClientOption locOption = new LocationClientOption();
		locOption.setOpenGps(true);//打开gps
		locOption.setCoorType("bd09ll");//设置坐标类型
		locOption.setAddrType("all");
		locOption.setScanSpan(1000);//设置超时
		locClient.setLocOption(locOption);

		locClient.registerLocationListener(new BDLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation bdLocation) {
				Log.d("POIData", "onReceiveLocation address " + bdLocation.getAddrStr());
				Log.d("POIData", "onReceiveLocation Latitude " + bdLocation.getLatitude());
				Log.d("POIData", "onReceiveLocation Longitude " + bdLocation.getLongitude());

				bdLoc = bdLocation;
			}

			@Override
			public void onReceivePoi(BDLocation bdLocation) {
				if (bdLocation == null){
					return ;
				}
			}
		});
		locClient.start();
		locClient.requestLocation();
	}


	public String getCurrentByIP(){
		try {
			for(Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();en.hasMoreElements();)
			{
				NetworkInterface intf = en.nextElement();
				for(Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();){
					InetAddress inetAddress = enumIpAddr.nextElement();
					if(!inetAddress.isLoopbackAddress() &&
							InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())){

							return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
		return null;
	}


	public String getPOIbyLocation(GeoPoint point,String keyword, int range, int page, int count){

		String url = "https://api.weibo.com/2/location/pois/search/by_geo.json?";
		String result = null;

		url = url+"coordinate="+((float)(point.getLongitudeE6()/1E6))+"%2C"+
				((float)(point.getLatitudeE6()/1E6));
		url = url+"&q="+keyword+"&access_token=2.00QtFwpDkXWZ4D46080bf290oAqL4D";
		url = url+"&sr=1"+"&range="+range+"&page="+page+"&count="+count;

		try {
			HttpGet request = new HttpGet(url);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(request);

			result = EntityUtils.toString(response.getEntity(),"UTF-8");
		} catch (IOException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}

		return result;
	}
}
