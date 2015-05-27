package com.campus.xiaozhao.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.campus.xiaozhao.CampusEnvironment;
import com.campus.xiaozhao.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        int setupDelay = 0;
        if (CampusEnvironment.ENABLE_SETUP_ACTIVITY) {
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
}
