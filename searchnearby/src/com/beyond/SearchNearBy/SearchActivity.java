package com.beyond.SearchNearBy;

import android.app.Activity;
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

public class SearchActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private ListView listView1;
    private ArrayList<HashMap<String, ?>> data = new ArrayList<HashMap<String, ?>>();
    BaseAdapter baseAdapter;
    private int selectedPosition = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        listView1 = (ListView) findViewById(R.id.Search_listView);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 20; i++) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("text1", "黄记煌三汁焖锅" + i);
            item.put("text2", "西安南大街52号南附楼内3层" + i + " " + i);
            item.put("value", "500m");
            data.add(item);
        }

        baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return data.size();
            }

            @Override
            public Object getItem(int position) {
                return data.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Log.d("Search_ListView", "getView " + position);

                if (convertView == null) {
                    LayoutInflater layoutInflater = getLayoutInflater();
                    convertView = layoutInflater.inflate(R.layout.search_item, parent, false);
                }

                Map<String, Object> itemData = (Map<String, Object>) getItem(position);

                TextView nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
                TextView addressTextView = (TextView) convertView.findViewById(R.id.addressTextView);
                TextView distanceTextView = (TextView) convertView.findViewById(R.id.distanceTextView);

                nameTextView.setText(itemData.get("text1").toString());
                addressTextView.setText(itemData.get("text2").toString());
                distanceTextView.setText(itemData.get("value").toString());


                if (position == selectedPosition) {
                    convertView.setBackgroundColor(Color.WHITE);
                }
                return convertView;
            }
        };


        listView1.setAdapter(baseAdapter);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position,long itemId) {
                selectedPosition = position;
                baseAdapter.notifyDataSetChanged();
            }
        });

    }
}