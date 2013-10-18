package com.beyond.SearchNearBy;


import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;


public class MapManagerApplication extends Application {

	private static MapManagerApplication mInstance = null;
	public boolean m_bKeyRight = true;
	BMapManager mBMapManager = null;


	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		initEngineManager(this);
	}

	public void initEngineManager(Context context) {
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(context);
		}

		if (!mBMapManager.init(Content.strkey,new MyGeneralListener())) {
			Toast.makeText(MapManagerApplication.getInstance().getApplicationContext(),
					"BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
		}
	}

	public static MapManagerApplication getInstance() {
		return mInstance;
	}


	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	static class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(MapManagerApplication.getInstance().getApplicationContext(), "您的网络出错啦！",
						Toast.LENGTH_LONG).show();
			}
			else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				Toast.makeText(MapManagerApplication.getInstance().getApplicationContext(), "输入正确的检索条件！",
						Toast.LENGTH_LONG).show();
			}
			// ...
		}

		@Override
		public void onGetPermissionState(int iError) {
			if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
				//授权Key错误：
				Toast.makeText(MapManagerApplication.getInstance().getApplicationContext(),
						"输入正确的授权Key！", Toast.LENGTH_LONG).show();
				MapManagerApplication.getInstance().m_bKeyRight = false;
			}
		}
	}
}