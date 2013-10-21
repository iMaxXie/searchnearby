package com.beyond.SearchNearBy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-17
 * Time: 上午11:56
 * To change this template use File | Settings | File Templates.
 */
public class SetActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
    }

	public void onSetBackClick(View view){
		finish();
	}

}
