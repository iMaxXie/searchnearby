package com.beyond.SearchNearBy.model;

import android.util.Log;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.beyond.SearchNearBy.Content;
import com.beyond.SearchNearBy.POIData;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-25
 * Time: 上午12:12
 * To change this template use File | Settings | File Templates.
 */
public class PoiManager {
    public static final String TAG = "snb.PoiManager";

    //新浪API返回poi经纬度偏移修正
    public static double[] amendOffSet(String lat,String log){
        double[] point = {0.0,0.0};
        point[0] = Double.parseDouble(lat)+Content.LATITUDE_OFFSET;
        point[1] = Double.parseDouble(log)+Content.LONGITUDE_OFFSET;
        return point;
    }

    //Json数据解析
    public static ArrayList<HashMap<String,String>> formatJson(String jsonstr){
        ArrayList<HashMap<String,String>>listdata = new ArrayList<HashMap<String, String>>();
        Log.d(TAG, "Json result:" + jsonstr);

        try {

            JSONObject jsonObject = new JSONObject(jsonstr);
            JSONArray array = jsonObject.optJSONArray("poilist");
            //max_page = (jsonObject.optInt("total")/record)+1;
            if(array ==null) return null;

            for(int i=0; i<array.length(); i++){
                HashMap<String,String> item = new HashMap<String, String>();
                double[] point = {0.0,0.0};
                point = amendOffSet(array.optJSONObject(i).optString("y"),array.optJSONObject(i).optString("x"));
                Log.d(TAG,"index "+i+" : "+array.optJSONObject(i));
                item.put("name", array.optJSONObject(i).optString("name"));
                item.put("address", array.optJSONObject(i).optString("address"));
                item.put("distance", array.optJSONObject(i).optString("distance")+"m");
                item.put("longtitude", Double.toString(point[1]));
                item.put("latitude", Double.toString(point[0]));
                item.put("tel", array.optJSONObject(i).optString("tel"));
                listdata.add(item);
                //datalist.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return listdata;
    }

    public static String getPOIbyLocation(GeoPoint point,String keyword, int range, int page, int count){

        String url = "https://api.weibo.com/2/location/pois/search/by_geo.json?";
        String appkey = "3096720804";
        String result = null;

        url = url+"coordinate="+((float)(point.getLongitudeE6()/1E6))+"%2C"+((float)(point.getLatitudeE6()/1E6))
                +"&q="+keyword+"&access_token="+ Content.ACCESS_TOKEN+"&sr=1"+"&range="+range+"&page="+page+"&count="+count;

        try {
            HttpGet request = new HttpGet(url);
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(request);

            result = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
