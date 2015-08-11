package com.campus.xiaozhao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.campus.xiaozhao.R;
import com.campus.xiaozhao.basic.utils.CampusSharePreference;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.startFrom(SplashActivity.this.getApplicationContext());
                finish();
            }
        }, 1500);
        CampusSharePreference.setLastStartUpTime(getApplicationContext(), System.currentTimeMillis());
    }
    
    public static void startFrom(Context context) {
        context.startActivity(new Intent(context, SplashActivity.class));
    }
    
    @Override
    public void onBackPressed() {
        // Do nothing
    }
}
