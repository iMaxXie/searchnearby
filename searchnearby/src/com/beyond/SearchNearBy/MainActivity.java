package com.beyond.SearchNearBy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity{
	private static final String TAG="MainActivity";
    private ListView listView;
    private ArrayList<HashMap<String, ?>> data = new ArrayList<HashMap<String, ?>>();
    private ArrayList<String>objectdata = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        init();
    }
    private void init() {
		inittop();
        initlistView();
    }

	private void inittop(){
		ImageButton search_btn = (ImageButton)findViewById(R.id.search_button);
		ImageButton set_btn = (ImageButton)findViewById(R.id.setting_button);
		ImageButton loc_btn = (ImageButton)findViewById(R.id.location_button);
		search_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent  intent = new Intent(MainActivity.this,SearchActivity.class);
				startActivity(intent);
			}
		});
		set_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent  intent = new Intent(MainActivity.this,SetActivity.class);
				startActivity(intent);
			}
		});
		loc_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
	}

    private void initlistView(){
        initobjectdata();
        for(String str:objectdata){
            HashMap<String,Object> hashMap = new HashMap<String, Object>();
            hashMap.put("nameTextView",str);
            data.add(hashMap);
		}

        listView = (ListView) findViewById(R.id.Main_listView);
        SimpleAdapter myAdapate = new SimpleAdapter(this,data,R.layout.main_item,new String[]{"nameTextView"},new int[]{R.id.nameTextView}){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);
                ImageButton button = (ImageButton) view.findViewById(R.id.next_button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("MainActivity"," onClick");
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this,NextActivity.class);
                        startActivity(intent);
                    }
                });
				view.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.d(TAG,"Item : onClick");
						Intent intent = new Intent(MainActivity.this,PoiCatalogueShowActivity.class);
						startActivity(intent);
					}
				});
                return view;
            }
        };

        listView.setAdapter(myAdapate);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.d(TAG,"onItemClick");
				Intent intent = new Intent(MainActivity.this,PoiCatalogueShowActivity.class);
				startActivity(intent);
			}
		});
    }

    private void initobjectdata() {
        objectdata.add("餐饮服务");
        objectdata.add("购物服务");
        objectdata.add("生活服务");
        objectdata.add("体育休闲服务");
        objectdata.add("住宿服务");
        objectdata.add("医疗保健服务");
        objectdata.add("科教文化服务");
        objectdata.add("交通设施服务");
        objectdata.add("公共服务");
        objectdata.add("住宿服务");
        objectdata.add("医疗保健服务");
        objectdata.add("科教文化服务");
        objectdata.add("交通设施服务");
        objectdata.add("公共服务");

    }
}
