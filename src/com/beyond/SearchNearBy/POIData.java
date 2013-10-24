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


	public static String getPOIbyLocation(GeoPoint point,String keyword, int range, int page, int count){

		String url = "https://api.weibo.com/2/location/pois/search/by_geo.json?";
        String access_token = "2.00QtFwpDkXWZ4D46080bf290oAqL4D";
        String appkey = "3096720804";
		String result = null;

		url = url+"coordinate="+((float)(point.getLongitudeE6()/1E6))+"%2C"+
				((float)(point.getLatitudeE6()/1E6));
		url = url+"&q="+keyword+"&access_token="+access_token;
		url = url+"&sr=1"+"&range="+range+"&page="+page+"&count="+count;

		try {
			HttpGet request = new HttpGet(url);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(request);

			result = EntityUtils.toString(response.getEntity(),"UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}
}
