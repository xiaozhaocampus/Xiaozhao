package com.campus.xiaozhao.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.campus.xiaozhao.Environment;
import com.campus.xiaozhao.R;
import com.component.logger.Logger;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity"; 
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        int setupDelay = 0;
        if (Environment.ENABLE_SETUP_ACTIVITY) {
            // TODO: 判断是否已经login
            SetupActivity.startFrom(this);
            setupDelay = 500;
            finish();
        }
        
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setupUI();
            }
        }, setupDelay);
    }

    private void setupUI() {
        setContentView(R.layout.activity_main);
    }
    
    //TODO:处理各个点击事件
	public void onClick(View v) {
		Logger.i(TAG, "===Click Id ==>" + v.getId());
		switch(v.getId()) {
			case R.id.more_alarms:
			break;
		}
	} 
}
