package com.beyond.SearchNearBy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LastActivity extends Activity{
    private ListView listView1;
    private List<Map<String, ?>> data = new ArrayList<Map<String, ?>>();
    private ArrayList<String>objectdata = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setContentView(R.layout.next);
        listView1=(ListView)findViewById(R.id.Two_listView);
        listView1.setDivider(new ColorDrawable(Color.BLACK));
        listView1.setDividerHeight(1);
        init();
    }
    private void init() {
        inittop();
        initlistView();
    }

    private void inittop() {
        TextView textView = (TextView) findViewById(R.id.top_textView);
        textView.setText(getIntent().getCharSequenceExtra("top"));
        ImageButton bt = (ImageButton) findViewById(R.id.Return_Button);
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
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,data,R.layout.main_item,new String[]{"nameTextView"},new int[]{R.id.nameTextView}){
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);
                ImageButton button = (ImageButton) view.findViewById(R.id.next_button);
                final TextView textView = (TextView) view.findViewById(R.id.nameTextView);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("aaa", view.toString());
                        Intent intent = new Intent();
                        intent.setClass(LastActivity.this,LastActivity.class);
                        intent.putExtra("top",textView.getText());
                        startActivity(intent);
                    }
                });
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setClass(LastActivity.this,LastActivity.class);
                        intent.putExtra("top",textView.getText());
                        startActivity(intent);
                    }
                });
                return view;    //To change body of overridden methods use File | Settings | File Templates.
            }
        };
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
    }
}