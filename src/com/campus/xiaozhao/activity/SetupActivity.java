package com.campus.xiaozhao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.campus.xiaozhao.R;

public class SetupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
    }
    
    public static void startFrom(Context context) {
        context.startActivity(new Intent(context, SetupActivity.class));
    }
    
    public void onLogin(View view) {
        
    }
    
    public void onRegister(View view) {
        
    }
}
