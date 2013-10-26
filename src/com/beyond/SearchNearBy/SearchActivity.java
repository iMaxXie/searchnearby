package com.beyond.SearchNearBy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.beyond.SearchNearBy.model.PoiManager;

public class SearchActivity extends Activity {
    private final String TAG = "snb.SearchActivity";

    private double mylongitude = 0.0;
    private double mylatitude = 0.0;

    private ListView listView = null;
    private EditText searchEdit = null;
    private SimpleAdapter searchAdapter;
    private GeoPoint location= null;

    private String keyword = null;

    private ArrayList<HashMap<String,String>> datalist = new ArrayList<HashMap<String, String>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        Log.d(TAG, "SearchActivity " );
        getIntentExtra();
        initComponent();
    }

    private void getIntentExtra(){
        keyword = getIntent().getStringExtra("keyword");
        mylatitude = getIntent().getDoubleExtra("mylatitude",0.0);
        mylongitude = getIntent().getDoubleExtra("mylongitude",0.0);
        Log.d(TAG, "getIntent Longitude " + mylongitude);
        Log.d(TAG, "getIntent Latitude " + mylatitude);
        location = new GeoPoint((int)(mylatitude*1E6),(int)(mylongitude*1E6));
    }

    private void initComponent(){
        searchEdit = (EditText) findViewById(R.id.search_key);
        listView = (ListView) findViewById(R.id.search_list);
        LinearLayout footer_more = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_more,listView,false);
        TextView textView = (TextView) footer_more.findViewById(R.id.item_range);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        listView.addFooterView(footer_more);
    }


    public void onSearchClick(View view){
        keyword = searchEdit.getText().toString();
        Log.d(TAG, " searchEdit: " + keyword);
        new AsyncTask<Object,Object,Object>(){
            ProgressDialog progressDialog = new ProgressDialog(SearchActivity.this);

            @Override
            protected void onPreExecute() {
                progressDialog.setMessage("查询中...");
                progressDialog.show();
                super.onPreExecute();
            }



            @Override
            protected Object doInBackground(Object... params) {
                String json = PoiManager.getPOIbyLocation(location, keyword, 2000, 1, 10);
                datalist = PoiManager.formatJson(json);
                Log.d(TAG, " datalist: " + datalist.size());
                return null;
            }
            @Override
            protected void onPostExecute(Object o) {

                progressDialog.dismiss();
                if(searchAdapter == null){
                    searchAdapter = new SimpleAdapter(SearchActivity.this,datalist,
                            R.layout.catalogue_list_item,
                            new String[]{"name","address","distance"},
                            new int[]{R.id.dept_name,R.id.dept_addr,R.id.dept_distance});
                    listView.setAdapter(searchAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position,long itemId) {
                            Intent intent = new Intent(SearchActivity.this,PoiCatalogueDetailActivity.class);
                            intent.putExtra("target_longtitude",Double.parseDouble(datalist.get(position).get("longtitude")));
                            intent.putExtra("target_latitude",Double.parseDouble(datalist.get(position).get("latitude")));
                            intent.putExtra("mylongtitude",mylongitude);
                            intent.putExtra("mylatitude",mylatitude);
                            intent.putExtra("name",datalist.get(position).get("name"));
                            intent.putExtra("address",datalist.get(position).get("address"));
                            intent.putExtra("tel",datalist.get(position).get("tel"));
                            startActivity(intent);
                        }
                    });
                }
                else{
                    searchAdapter.notifyDataSetChanged();
                }
                super.onPostExecute(o);
            }
        }.execute(0);

    }

	public void onSearchBackClick(View view){
		finish();
	}
}