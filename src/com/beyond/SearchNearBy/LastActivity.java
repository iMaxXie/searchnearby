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
	private final String TAG="snb.LastActivity";
    private ListView listView;
    private List<Map<String, ?>> data = new ArrayList<Map<String, ?>>();
    private ArrayList<String>objectdata = new ArrayList<String>();
    private String textView = null;

    private double mylongitude = 0.0;
    private double mylatitude = 0.0;
    private String keyword = null;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next);
        listView=(ListView)findViewById(R.id.next_list);
        listView.setDividerHeight(1);

        getIntentExtra();
        inittop();
        initlistView();
    }
    private void getIntentExtra(){
        keyword = getIntent().getStringExtra("keyword");
        mylatitude = getIntent().getDoubleExtra("mylatitude",0.0);
        mylongitude = getIntent().getDoubleExtra("mylongitude",0.0);
        Log.d(TAG, "getIntent Longitude " + mylongitude);
        Log.d(TAG, "getIntent Latitude " + mylatitude);
    }

    private void inittop() {
        TextView textview = (TextView) findViewById(R.id.title_text);
        textview.setText(keyword);
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
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,data,R.layout.main_item,
                new String[]{"nameTextView"},new int[]{R.id.text_name}){
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);
                ImageButton button = (ImageButton) view.findViewById(R.id.next_button);
                button.setVisibility(View.GONE);

				view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
						TextView textView = (TextView) view.findViewById(R.id.text_name);
                        Log.d(TAG,"Item : onClick");
                        Intent intent = new Intent(LastActivity.this,PoiCatalogueShowActivity.class);
                        Log.d(TAG, "view sendIntent Longitude " + mylongitude);
                        Log.d(TAG, "view sendIntent Latitude " + mylatitude);
                        intent.putExtra("keyword",textView.getText());
                        intent.putExtra("mylongitude",mylongitude);
                        intent.putExtra("mylatitude",mylatitude);
                        startActivity(intent);
                    }
                });
                return view;
            }
        };

        listView.setAdapter(simpleAdapter);
    }

    private void initobjectdata() {
       if(textView.equals("中餐厅")){
           objectdata.add("综合酒楼");
           objectdata.add("四川菜（川菜）");
           objectdata.add("广东菜（粤菜）");
           objectdata.add("山东菜（鲁菜）");
           objectdata.add("江苏菜");
           objectdata.add("浙江菜");
           objectdata.add("上海菜");
           objectdata.add("湖南菜（湘菜）");
           objectdata.add("安徽菜（徽菜）");
           objectdata.add("福建菜");
           objectdata.add("北京菜");
           objectdata.add("湖北菜（鄂菜）");
           objectdata.add("云贵菜");
           objectdata.add("西北菜");
           objectdata.add("老字号");
           objectdata.add("火锅店");
           objectdata.add("特色／地方风味餐厅");
           objectdata.add("海鲜酒楼");
           objectdata.add("中式素菜馆");
           objectdata.add("清真菜馆");
           objectdata.add("台湾菜");
           objectdata.add("潮州菜");
       }

        if(textView.equals("快餐厅")){
            objectdata.add("肯德基");
            objectdata.add("麦当劳");
            objectdata.add("必胜客");
            objectdata.add("永和豆浆");
            objectdata.add("茶餐厅");
            objectdata.add("大家乐");
            objectdata.add("大快活");
            objectdata.add("美心");
            objectdata.add("吉野家");
            objectdata.add("仙跡岩");
        }

        if(textView.equals("咖啡厅")){
            objectdata.add("星巴克咖啡");
            objectdata.add("上岛咖啡");
            objectdata.add("Pacifi Ccoffee Company");
            objectdata.add("巴黎咖啡店");
        }

        if(textView.equals("商场")){
            objectdata.add("商场");
            objectdata.add("购物中心");
            objectdata.add("普通商场");
            objectdata.add("免税品店");
        }

        if(textView.equals("便民商店／便利店")){
            objectdata.add("便民商店／便利店");
            objectdata.add("7-ELEVEN便利店");
            objectdata.add("OK便利店");
        }

        if(textView.equals("家电电子卖场")){
            objectdata.add("家电电子卖场");
            objectdata.add("综合家电商场");
            objectdata.add("国美");
            objectdata.add("大中");
            objectdata.add("苏宁");
            objectdata.add("手机销售");
            objectdata.add("数码电子");
            objectdata.add("丰泽");
            objectdata.add("镭射");
        }
        if(textView.equals("超级市场")){
            objectdata.add("超市");
            objectdata.add("家乐福");
            objectdata.add("沃尔玛");
            objectdata.add("华润");
            objectdata.add("北京华联");
            objectdata.add("上海华联");
            objectdata.add("麦德龙");
            objectdata.add("万客隆");
            objectdata.add("华堂");
            objectdata.add("易初莲花");
            objectdata.add("好又多");
            objectdata.add("屈臣氏");
            objectdata.add("乐购");
            objectdata.add("惠康超市");
            objectdata.add("百佳超市");
            objectdata.add("万宁超市");
        }
        if(textView.equals("花鸟鱼虫市场")){
            objectdata.add("花鸟鱼虫市场");
            objectdata.add("花卉市场");
            objectdata.add("宠物市场");
        }
        if(textView.equals("家居建材市场")){
            objectdata.add("家居建材市场");
            objectdata.add("家具建材综合市场");
            objectdata.add("家具城");
            objectdata.add("建材五金市场");
            objectdata.add("厨卫市场");
            objectdata.add("布艺市场");
            objectdata.add("灯具瓷器市场");
        }
        if(textView.equals("综合市场")){
            objectdata.add("综合市场");
            objectdata.add("小商品市场");
            objectdata.add("旧货市场");
            objectdata.add("农副产品市场");
            objectdata.add("果品市场");
            objectdata.add("蔬菜市场");
            objectdata.add("水产海鲜市场");
        }

        if(textView.equals("体育用品店")){
            objectdata.add("体育用品店");
            objectdata.add("李宁专卖店");
            objectdata.add("耐克专卖店");
            objectdata.add("阿迪达斯专卖店");
            objectdata.add("锐步专卖店");
            objectdata.add("彪马专卖店");
            objectdata.add("高尔夫用品店");
            objectdata.add("户外用品");
        }
        if(textView.equals("特色商业街")){
            objectdata.add("特色商业街");
            objectdata.add("步行街");
        }
        if(textView.equals("服装鞋帽皮具店")){
            objectdata.add("服装鞋帽皮具店");
            objectdata.add("品牌服装店");
            objectdata.add("品牌鞋店");
            objectdata.add("品牌皮具店");
        }
        if(textView.equals("专卖店")){
            objectdata.add("专营店");
            objectdata.add("古玩字画店");
            objectdata.add("珠宝首饰工艺品");
            objectdata.add("钟表店");
            objectdata.add("眼镜店");
            objectdata.add("书店");
            objectdata.add("音像店");
            objectdata.add("儿童用品店");
            objectdata.add("自行车专卖店");
            objectdata.add("礼品饰品店");
            objectdata.add("烟酒专卖店");
            objectdata.add("宠物用品店");
            objectdata.add("摄影器材店");
            objectdata.add("宝马生活方式");
        }
        if(textView.equals("特殊买卖场所")){
            objectdata.add("特殊买卖场所");
            objectdata.add("拍卖行");
            objectdata.add("典当行");
        }
        if(textView.equals("个人用品/化妆品店")){
            objectdata.add("其它个人用品店");
            objectdata.add("莎莎");
        }

        if(textView.equals("售票处")){
            objectdata.add("售票处");
            objectdata.add("飞机票代售点");
            objectdata.add("火车票代售点");
            objectdata.add("长途汽车票代售点");
            objectdata.add("船票代售点");
            objectdata.add("公交卡／月票代售点");
            objectdata.add("公园景点售票处");
        }
        if(textView.equals("邮局")){
            objectdata.add("邮局");
            objectdata.add("邮政速递");
        }

        if(textView.equals("电讯营业厅")){
            objectdata.add("电讯营业厅");
            objectdata.add("中国电信营业厅");
            objectdata.add("中国网通营业厅");
            objectdata.add("中国移动营业厅");
            objectdata.add("中国联通营业厅");
            objectdata.add("中国铁通营业厅");
            objectdata.add("中国卫通营业厅");
            objectdata.add("和记电讯");
            objectdata.add("数码通电讯");
            objectdata.add("电讯盈科");
            objectdata.add("中国移动万众/Peoples");
        }
        if(textView.equals("事务所")){
            objectdata.add("事务所");
            objectdata.add("律师事务所");
            objectdata.add("会计师事务所");
            objectdata.add("评估事务所");
            objectdata.add("审计事务所");
            objectdata.add("认证事务所");
            objectdata.add("专利事务所");
        }

        if(textView.equals("彩票彩券销售点")){
            objectdata.add("彩票彩券销售点");
            objectdata.add("马会投注站");
        }
        if(textView.equals("丧葬设施")){
            objectdata.add("公墓");
            objectdata.add("陵园");
            objectdata.add("殡仪馆");
        }
        if(textView.equals("体育休闲服务场所")){
            objectdata.add("体育休闲服务场所");
        }
        if(textView.equals("运动场馆")){
            objectdata.add("运动场所");
            objectdata.add("综合体育馆");
            objectdata.add("保龄球馆");
            objectdata.add("网球场");
            objectdata.add("篮球场馆");
            objectdata.add("足球场");
            objectdata.add("滑雪场");
            objectdata.add("溜冰场");
            objectdata.add("户外健身场所");
            objectdata.add("海滨浴场");
            objectdata.add("游泳馆");
            objectdata.add("健身中心");
            objectdata.add("乒乓球馆");
            objectdata.add("台球厅");
            objectdata.add("壁球场");
            objectdata.add("马术俱乐部");
            objectdata.add("赛马场");
            objectdata.add("橄榄球场");
            objectdata.add("羽毛球场");
            objectdata.add("跆拳道场馆");
        }
        if(textView.equals("高尔夫相关")){
            objectdata.add("高尔夫相关");
            objectdata.add("高尔夫球场");
            objectdata.add("高尔夫练习场");
        }
        if(textView.equals("娱乐场所")){
            objectdata.add("娱乐场所");
            objectdata.add("夜总会");
            objectdata.add("ＫＴＶ");
            objectdata.add("迪厅");
            objectdata.add("酒吧");
            objectdata.add("游戏厅");
            objectdata.add("棋牌室");
            objectdata.add("博采中心");
            objectdata.add("网吧");
        }
        if(textView.equals("度假疗养场所")){
            objectdata.add("度假疗养场所");
            objectdata.add("度假村");
            objectdata.add("疗养院");
        }
        if(textView.equals("休闲场所")){
            objectdata.add("休闲场所");
            objectdata.add("游乐场");
            objectdata.add("垂钓园");
            objectdata.add("采摘园");
            objectdata.add("露营地");
            objectdata.add("水上活动中心");
        }
        if(textView.equals("影剧院")){
            objectdata.add("影剧院相关");
            objectdata.add("电影院");
            objectdata.add("音乐厅");
            objectdata.add("剧场");
        }
        if(textView.equals("医疗保健服务场所")){
            objectdata.add("医疗保健服务场所");
        }
        if(textView.equals("综合医院")){
            objectdata.add("综合医院");
            objectdata.add("三级甲等医院");
            objectdata.add("卫生院");
        }
        if(textView.equals("专科医院")){
            objectdata.add("专科医院");
            objectdata.add("整形美容");
            objectdata.add("口腔医院");
            objectdata.add("眼科医院");
            objectdata.add("耳鼻喉医院");
            objectdata.add("胸科医院");
            objectdata.add("骨科医院");
            objectdata.add("肿瘤医院");
            objectdata.add("脑科医院");
            objectdata.add("妇科医院");
            objectdata.add("精神病医院");
            objectdata.add("传染病医院");
        }

        if(textView.equals("医药保健相关")){
            objectdata.add("医药保健相关");
            objectdata.add("药房");
            objectdata.add("医疗保健用品");
        }
        if(textView.equals("动物医疗场所")){
            objectdata.add("动物医疗场所");
            objectdata.add("宠物诊所");
            objectdata.add("兽医站");
        }

        if(textView.equals("宾馆酒店")){
            objectdata.add("宾馆酒店");
            objectdata.add("六星级宾馆");
            objectdata.add("五星级宾馆");
            objectdata.add("四星级宾馆");
            objectdata.add("三星级宾馆");
            objectdata.add(" 经济型连锁酒店");
        }
        if(textView.equals("旅馆招待所")){
            objectdata.add("旅馆招待所");
            objectdata.add("青年旅社");
        }

        if(textView.equals("公园广场")){
            objectdata.add("公园广场");
            objectdata.add("公园");
            objectdata.add("动物园");
            objectdata.add("植物园");
            objectdata.add("水族馆");
            objectdata.add("城市广场");
        }
        if(textView.equals("风景名胜")){
            objectdata.add("风景名胜");
            objectdata.add("世界遗产");
            objectdata.add("国家级景点");
            objectdata.add("省级景点");
            objectdata.add("纪念馆");
            objectdata.add("寺庙道观");
            objectdata.add("教堂");
            objectdata.add("回教寺");
            objectdata.add("海滩");
        }

        if(textView.equals("楼宇")){
            objectdata.add("楼宇相关");
            objectdata.add("商务写字楼");
            objectdata.add("工业大厦建筑物");
            objectdata.add("商住两用楼宇");

        }
        if(textView.equals("住宅区")){
            objectdata.add("住宅区");
            objectdata.add("别墅");
            objectdata.add("住宅小区");
            objectdata.add("宿舍");
            objectdata.add("社区中心");
        }
        if(textView.equals("政府机关")){
            objectdata.add("政府机关相关");
            objectdata.add("国家级机关及事业单位");
            objectdata.add("省直辖市级政府及事业单位");
            objectdata.add("地市级政府及事业单位");
            objectdata.add("区县级政府及事业单位");
            objectdata.add("乡镇级政府及事业单位");
            objectdata.add("乡镇以下级政府及事业单位");
            objectdata.add("外地政府办");
        }
        if(textView.equals("外国机构")){
            objectdata.add("外国机构相关");
            objectdata.add("外国使领馆");
            objectdata.add("国际组织办事处");
        }
        if(textView.equals("社会团体")){
            objectdata.add("社会团体相关");
            objectdata.add("共青团");
            objectdata.add("少先队");
            objectdata.add("妇联");
            objectdata.add("残联");
            objectdata.add("红十字会");
            objectdata.add("消费者协会");
            objectdata.add("行业协会");
            objectdata.add("慈善机构");
            objectdata.add("教会");
        }
        if(textView.equals("公检法机构")){
            objectdata.add("公检法机关");
            objectdata.add("公安警察");
            objectdata.add("检察院");
            objectdata.add("法院");
            objectdata.add("消防机关");
            objectdata.add("公证鉴定机构");
            objectdata.add("社会治安机构");
        }
        if(textView.equals("交通车辆管理")){
            objectdata.add("交通车辆管理相关");
            objectdata.add("交通车辆管理相关");
            objectdata.add("交通车辆管理机构");
            objectdata.add("验车场");
            objectdata.add("交通执法站");
        }
        if(textView.equals("工商税务机构")){
            objectdata.add("工商税务机构");
            objectdata.add("工商部门");
            objectdata.add("国税机关");
            objectdata.add("地税机关");
        }
        if(textView.equals("博物馆")){
            objectdata.add("博物馆");
            objectdata.add("奥迪博物馆");
            objectdata.add("奔驰博物馆");
        }
        if(textView.equals("传媒机构")){
            objectdata.add("传媒机构");
            objectdata.add("电视台");
            objectdata.add("电台");
            objectdata.add("报社");
            objectdata.add("杂志社");
            objectdata.add("出版社");
        }
        if(textView.equals("学校")){
            objectdata.add("学校");
            objectdata.add("高等院校");
            objectdata.add("中学");
            objectdata.add("小学");
            objectdata.add("幼儿园");
            objectdata.add("成人教育");
            objectdata.add("职业技术学校");
        }
        if(textView.equals("港口码头")){
            objectdata.add("港口码头");
            objectdata.add("客运港");
            objectdata.add("车渡口");
            objectdata.add("人渡口");
        }
        if(textView.equals("公交车站")){
            objectdata.add("公交车站相关");
            objectdata.add("旅游专线车站");
            objectdata.add("普通公交站");
        }
        if(textView.equals("班车站")){
            objectdata.add("班车站");
        }
        if(textView.equals("停车场")){
            objectdata.add("停车场相关");
            objectdata.add("室内停车场");
            objectdata.add("室外停车场");
            objectdata.add("停车换乘点");
        }
        if(textView.equals("金融保险服务机构")){
            objectdata.add("金融保险服务机构");
        }
        if(textView.equals("银行")){
            objectdata.add("银行");
            objectdata.add("中国人民银行");
            objectdata.add("国家开发银行");
            objectdata.add("中国进出口银行");
            objectdata.add("中国银行");
            objectdata.add("中国工商银行");
            objectdata.add("中国建设银行");
            objectdata.add("中国农业银行");
            objectdata.add("交通银行");
            objectdata.add("招商银行");
            objectdata.add("华夏银行");
            objectdata.add("中信银行");
            objectdata.add("中国民生银行");
            objectdata.add("中国光大银行");
            objectdata.add("上海银行");
            objectdata.add("上海浦东发展银行");
            objectdata.add("深圳发展银行");
            objectdata.add("深圳市商业银行");
            objectdata.add("兴业银行");
            objectdata.add("北京银行");
            objectdata.add("广东发展银行");
            objectdata.add("中国信合");
            objectdata.add("香港恒生银行");
            objectdata.add("东亚银行");
            objectdata.add("美国花旗银行");
            objectdata.add("渣打银行");
            objectdata.add("汇丰银行");
            objectdata.add("荷兰银行");
            objectdata.add("美国运通银行");
            objectdata.add("瑞士友邦银行");
            objectdata.add("美国银行");
            objectdata.add("蒙特利尔银行");
            objectdata.add("纽约银行");
            objectdata.add("苏格兰皇家信合");
            objectdata.add("法国兴业银行");
            objectdata.add("德意志银行");
            objectdata.add("日本三菱东京日联银行");
            objectdata.add("巴克莱银行");
            objectdata.add("摩根大通银行");
            objectdata.add("中国邮政储蓄");
            objectdata.add("香港星展银行");
            objectdata.add("南洋商业银行");
            objectdata.add("上海商业银行");
            objectdata.add("永亨银行");
            objectdata.add("香港永隆银行");
            objectdata.add("创兴信合");
            objectdata.add("大新银行");
            objectdata.add("中信嘉华银行");
            objectdata.add("大众银行(香港)");
        }
        if(textView.equals("自动提款机")){
            objectdata.add("自动提款机ATM");
            objectdata.add("中国银行ATM");
            objectdata.add("中国工商银行ATM");
            objectdata.add("中国建设银行ATM");
            objectdata.add("中国农业银行ATM");
            objectdata.add("交通银行ATM");
            objectdata.add("招商银行ATM");
            objectdata.add("华夏银行ATM");
            objectdata.add("中信银行ATM");
            objectdata.add("中国民生银行ATM");
            objectdata.add("中国光大银行ATM");
            objectdata.add("上海银行ATM");
            objectdata.add("上海浦东发展银行ATM");
            objectdata.add("深圳发展银行ATM");
            objectdata.add("深圳市商业银行ATM");
            objectdata.add("兴业银行ATM");
            objectdata.add("北京银行ATM");
            objectdata.add("广东发展银行ATM");
            objectdata.add("中国信合ATM");
            objectdata.add("香港恒生银行ATM");
            objectdata.add("东亚银行ATM");
            objectdata.add("美国花旗银行ATM");
            objectdata.add("渣打银行ATM");
            objectdata.add("汇丰银行ATM");
            objectdata.add("荷兰银行ATM");
            objectdata.add("美国运通银行ATM");
            objectdata.add("瑞士友邦银行ATM");
            objectdata.add("美国银行ATM");
            objectdata.add("蒙特利尔银行ATM");
            objectdata.add("纽约银行ATM");
            objectdata.add("苏格兰皇家信合ATM");
            objectdata.add("法国兴业银行ATM");
            objectdata.add("德意志银行ATM");
            objectdata.add("日本三菱东京日联银行ATM");
            objectdata.add("巴克莱银行ATM");
            objectdata.add("摩根大通银行ATM");
            objectdata.add("中国邮政储蓄ATM");
            objectdata.add("香港星展银行ATM");
            objectdata.add("南洋商业银行ATM");
            objectdata.add("上海商业银行ATM");
            objectdata.add("永亨银行ATM");
            objectdata.add("香港永隆银行ATM");
            objectdata.add("创兴信合ATM");
            objectdata.add("大新银行ATM");
            objectdata.add("中信嘉华银行ATM");
            objectdata.add("大众银行(香港)ATM");
        }
        if(textView.equals("保险公司")){
            objectdata.add("保险公司 160400");
            objectdata.add("中国人民保险公司");
            objectdata.add("中国人寿保险公司");
            objectdata.add("中国平安保险公司");
            objectdata.add("中国再保险公司");
            objectdata.add("中国太平洋保险");
            objectdata.add("人寿保险公司");
            objectdata.add("华泰财产保险股份有限公司");
            objectdata.add("泰康人寿保险公司");
        }
        if(textView.equals("证券公司")){
            objectdata.add("证券公司");
            objectdata.add("证券营业厅");
        }
        if(textView.equals("公司")){
            objectdata.add("公司");
            objectdata.add("广告装饰");
            objectdata.add("建筑公司");
            objectdata.add("医药公司");
            objectdata.add("机械电子");
            objectdata.add("冶金化工");
            objectdata.add("网络科技");
            objectdata.add("商业贸易");
            objectdata.add("电信公司");
            objectdata.add("矿产公司");
        }
        if(textView.equals("农林牧渔基地")){
            objectdata.add("农林牧渔基地");
            objectdata.add("渔场");
            objectdata.add("农场");
            objectdata.add("林场");
            objectdata.add("牧场");
            objectdata.add("家禽养殖基地");
            objectdata.add("蔬菜基地");
            objectdata.add("水果基地");
            objectdata.add("花卉苗圃基地");
        }
        if(textView.equals("收费站")){
            objectdata.add("收费站");
            objectdata.add("高速收费站");
            objectdata.add("国省道收费站");
            objectdata.add("桥洞收费站");
        }
        if(textView.equals("服务区")){
            objectdata.add("加油站服务区");
            objectdata.add("高速服务区");
            objectdata.add("高速停车区");
        }
        if(textView.equals("交通地名")){
            objectdata.add("交通地名");
            objectdata.add("环岛名");
            objectdata.add("高速路出口");
            objectdata.add("高速路入口");
            objectdata.add("立交桥");
            objectdata.add("桥");
        }
    }
}
