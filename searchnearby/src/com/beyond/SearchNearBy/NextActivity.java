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
    private String textView = null;
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
        TextView textview = (TextView) findViewById(R.id.title_text);
        textview.setText(getIntent().getStringExtra("keyword"));
        textView = getIntent().getStringExtra("keyword");
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
                if(textView.getText().equals("文化用品店")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("休闲餐饮场所")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("茶艺馆")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("冷饮店")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("糕饼店")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("甜品店")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("旅行社")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("物流速递")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("人才市场")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("自来水营业厅")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("电力营业厅")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("美容美发店")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("维修站点")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("摄影冲印店")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("洗浴推拿场所")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("洗衣店")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("中介机构")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("搬家公司")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("彩票彩券销售点")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("诊所")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("急救中心")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("展览馆")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("会展中心")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("美术馆")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("图书馆")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("科技馆")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("天文馆")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("文化宫")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("档案馆")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("文艺团体")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("科研机构")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("培训机构")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("驾校")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("飞机场")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("火车站")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("长途汽车站")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("地铁站")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("轻轨站")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("班车站")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("过境口岸")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("财务公司")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("公司企业")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("知名企业")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("工厂")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("道路附属设施")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("城市中心")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("公共设施")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("报刊亭")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("公用电话")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("公共厕所")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("紧急避难场所")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("民主党派")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("商务住宅相关")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("产业园区")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("风景名胜相关")){
                    button.setVisibility(View.GONE);
                }
                if(textView.getText().equals("购物相关场所")){
                    button.setVisibility(View.GONE);
                }
                return view;
            }
        };
        listView.setAdapter(simpleAdapter);
    }

    private void initobjectdata() {
        if(textView.equals("餐饮服务")){
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

        if(textView.equals("购物服务")){
            objectdata.add("商场");
            objectdata.add("便民商店／便利店");
            objectdata.add("家电电子卖场");
            objectdata.add("超级市场");
            objectdata.add("花鸟鱼虫市场");
            objectdata.add("家居建材市场");
            objectdata.add("综合市场");
            objectdata.add("文化用品店");
            objectdata.add("体育用品店");
            objectdata.add("特色商业街");
            objectdata.add("服装鞋帽皮具店");
            objectdata.add("专卖店");
            objectdata.add("特殊买卖场所");
            objectdata.add("个人用品/化妆品店");
        }

        if(textView.equals("生活服务")){
            objectdata.add("旅行社");
            objectdata.add("信息咨询中心 ");
            objectdata.add("售票处");
            objectdata.add("邮局");
            objectdata.add("物流速递");
            objectdata.add("电讯营业厅");
            objectdata.add("事务所");
            objectdata.add("人才市场");
            objectdata.add("自来水营业厅");
            objectdata.add("电力营业厅");
            objectdata.add("美容美发店");
            objectdata.add("维修站点");
            objectdata.add("摄影冲印店");
            objectdata.add("洗浴推拿场所");
            objectdata.add("洗衣店");
            objectdata.add("中介机构");
            objectdata.add("搬家公司");
            objectdata.add("彩票彩券销售点");
            objectdata.add("丧葬设施");
        }

        if(textView.equals("体育休闲服务")){
            objectdata.add("运动场馆");
            objectdata.add("高尔夫相关");
            objectdata.add("娱乐场所");
            objectdata.add("度假疗养场所");
            objectdata.add("休闲场所");
            objectdata.add("影剧院");
        }

        if(textView.equals("医疗保健服务")){
            objectdata.add("综合医院");
            objectdata.add("专科医院");
            objectdata.add("诊所");
            objectdata.add("急救中心");
            objectdata.add("疾病预防机构");
            objectdata.add("医药保健相关");
            objectdata.add("动物医疗场所");
        }

        if(textView.equals("住宿服务")){
            objectdata.add("宾馆酒店");
            objectdata.add("旅馆招待所");
        }

        if(textView.equals(" 风景名胜")){
            objectdata.add("公园广场");
            objectdata.add("风景名胜");
        }

        if(textView.equals("商务住宅")){
            objectdata.add("产业园区");
            objectdata.add("楼宇");
            objectdata.add("住宅区");
        }

        if(textView.equals("政府机构及社会团体")){
            objectdata.add("政府机关");
            objectdata.add("外国机构");
            objectdata.add("民主党派");
            objectdata.add("社会团体");
            objectdata.add("公检法机构");
            objectdata.add("交通车辆管理");
            objectdata.add("工商税务机构");
        }

        if(textView.equals("科教文化服务")){
            objectdata.add("博物馆");
            objectdata.add("展览馆");
            objectdata.add("会展中心");
            objectdata.add("美术馆");
            objectdata.add("图书馆");
            objectdata.add("科技馆");
            objectdata.add("天文馆");
            objectdata.add("文化宫");
            objectdata.add("档案馆");
            objectdata.add("文艺团体");
            objectdata.add("外国餐厅");
            objectdata.add("快餐厅");
            objectdata.add("休闲餐饮场所");
            objectdata.add("传媒机构");
            objectdata.add("学校");
            objectdata.add("科研机构");
            objectdata.add("培训机构");
            objectdata.add("驾校");
        }

        if(textView.equals("交通设施服务")){
            objectdata.add("飞机场");
            objectdata.add("火车站");
            objectdata.add("港口码头");
            objectdata.add("长途汽车站");
            objectdata.add("地铁站");
            objectdata.add("公交车站");
            objectdata.add("班车站");
            objectdata.add("停车场");
            objectdata.add("过境口岸");
        }

        if(textView.equals("金融保险服务")){
            objectdata.add("银行");
            objectdata.add("银行相关");
            objectdata.add("自动提款机");
            objectdata.add("保险公司");
            objectdata.add("证券公司");
            objectdata.add("财务公司");
        }

        if(textView.equals("公司企业")){
            objectdata.add("知名企业");
            objectdata.add("公司");
            objectdata.add("工厂");
            objectdata.add("农林牧渔基地");
        }

        if(textView.equals("道路附属设施")){
            objectdata.add("收费站");
            objectdata.add("服务区");
        }

        if(textView.equals("地名地址信息")){
            objectdata.add("城市中心");
            objectdata.add("交通地名");
        }

        if(textView.equals("公共设施")){
            objectdata.add("报刊亭");
            objectdata.add("公用电话");
            objectdata.add("公共厕所");
            objectdata.add("紧急避难场所");
        }
    }
}


