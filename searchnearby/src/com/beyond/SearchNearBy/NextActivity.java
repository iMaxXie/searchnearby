package com.beyond.SearchNearBy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NextActivity extends Activity{
	private final String TAG="NextActivity";
    private ListView listView;
    private List<Map<String, ?>> data = new ArrayList<Map<String, ?>>();
    private ArrayList<String>objectdata = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next);

        listView=(ListView)findViewById(R.id.next_list);
        listView.setDivider(new ColorDrawable(Color.BLACK));
        listView.setDividerHeight(1);
        init();
    }
    private void init() {
        inittop();
        initlistView();
    }

    private void inittop() {
        TextView textView = (TextView) findViewById(R.id.title_text);
        textView.setText(getIntent().getStringExtra("keyword"));
        ImageButton bt = (ImageButton) findViewById(R.id.return_btn);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,data,R.layout.main_item,new String[]{"nameTextView"},new int[]{R.id.text_name}){
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);
                ImageButton button = (ImageButton) view.findViewById(R.id.next_button);
                final TextView textView = (TextView) view.findViewById(R.id.text_name);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, view.toString());
                        Intent intent = new Intent();
                        intent.setClass(NextActivity.this,LastActivity.class);
                        intent.putExtra("keyword",textView.getText());
                        startActivity(intent);
                    }
                });
				view.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						TextView textView = (TextView) v.findViewById(R.id.text_name);
						Log.d(TAG,"Item : onClick");
						Intent intent = new Intent(NextActivity.this,PoiCatalogueShowActivity.class);
						intent.putExtra("keyword",textView.getText());
						startActivity(intent);
					}
				});

                return view;
            }
        };
        listView.setAdapter(simpleAdapter);
    }

    private void initobjectdata() {
        objectdata.add("中餐厅");
        objectdata.add("外国餐厅");
        objectdata.add("快餐厅");
        objectdata.add("休闲餐饮场所");
        objectdata.add("咖啡厅");
        objectdata.add("茶艺馆");
        objectdata.add("冷饮店");
        objectdata.add("饼糕店");
        objectdata.add("甜品店");
        objectdata.add("咖啡厅");
        objectdata.add("茶艺馆");
        objectdata.add("冷饮店");
        objectdata.add("饼糕店");
        objectdata.add("甜品店");
    }
}


