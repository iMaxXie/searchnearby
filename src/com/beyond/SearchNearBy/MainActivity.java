package com.beyond.SearchNearBy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.*;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.beyond.SearchNearBy.model.LocationManager;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity{
    private static final String TAG="snb.MainActivity";

//	private CurrentLocation currentLoc;
//	private ServiceConnection serviceCon = new ServiceConnection() {
//		@Override
//		public void onServiceConnected(ComponentName name, IBinder service) {
//			Log.d(TAG,"onServiceConnected: ComponentName"+name.getClassName());
//			CurrentLocation.ServiceBindler bindler = (CurrentLocation.ServiceBindler) service;
//			currentLoc = bindler.getService();
//		}
//
//		@Override
//		public void onServiceDisconnected(ComponentName name) {
//		}
//	};

    private ListView listView;
    private ArrayList<HashMap<String, ?>> data = new ArrayList<HashMap<String, ?>>();
    private ArrayList<String>objectdata = new ArrayList<String>();

    private ImageButton search_btn;
    private ImageButton set_btn;
    private ImageButton loc_btn;
    private TextView place_text;

    private ProgressDialog progressDialog = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"MainActivity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

//		Intent intent = new Intent(this,CurrentLocation.class);
//		//startService(intent);
//		bindService(intent,serviceCon,BIND_AUTO_CREATE);
        initLocation();

        init();
    }

//	@Override
//	protected void onResume() {
//		super.onResume();
//		Log.d(TAG,"MainActivity onResume");
////		Intent intent = new Intent(this,CurrentLocation.class);
////		bindService(intent,serviceCon,BIND_AUTO_CREATE);
//	}

    private void init() {
        initComponent();
        initlistView();
    }

    private void initLocation(){
        LocationManager.getCurrentLocation(this,new LocationManager.GetCurrentLocListener() {
            @Override
            public void getLoctioning(BDLocation bdLocation) {
                place_text.setText(bdLocation.getAddrStr());
            }
        });
    }

    private void initComponent(){

        place_text = (TextView) findViewById(R.id.place_text);
        search_btn = (ImageButton)findViewById(R.id.search_button);
        set_btn = (ImageButton)findViewById(R.id.setting_button);
        loc_btn = (ImageButton)findViewById(R.id.location_button);

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
				place_text.setText("定位中,请稍等...");
                initLocation();
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

        listView = (ListView) findViewById(R.id.main_list);
        listView.setDividerHeight(1);
        SimpleAdapter myAdapate = new SimpleAdapter(this,data,R.layout.main_item,new String[]{"nameTextView"},new int[]{R.id.text_name}){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);
                ImageButton button = (ImageButton) view.findViewById(R.id.next_button);
                final TextView textView = (TextView) view.findViewById(R.id.text_name);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG," onClick");
                        Intent intent = new Intent(MainActivity.this,NextActivity.class);
                        intent.putExtra("keyword",textView.getText());
                        startActivity(intent);
                    }
                });
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG,"Item : onClick");
                        Intent intent = new Intent(MainActivity.this,PoiCatalogueShowActivity.class);
                        intent.putExtra("keyword",textView.getText());
                        startActivity(intent);
                    }
                });
                return view;
            }
        };

        listView.setAdapter(myAdapate);

    }

    @Override
    protected void onPause() {
        Log.d(TAG, "MainActivity onPause");
//		unbindService(serviceCon);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG,"MainActivity onDestroy");
//		Intent intent = new Intent(this,CurrentLocation.class);
//		stopService(intent);
        super.onDestroy();
    }
    private void initobjectdata(){
        objectdata.add("餐饮服务");
        objectdata.add("购物服务");
        objectdata.add("生活服务");
        objectdata.add("体育休闲服务");
        objectdata.add("住宿服务");
        objectdata.add("医疗保健服务");
        objectdata.add("科教文化服务");
        objectdata.add("交通设施服务");
        objectdata.add("公共设施");
    }
}
