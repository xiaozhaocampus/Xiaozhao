package com.campus.xiaozhao.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import com.campus.xiaozhao.R;

/**
 * Created by frankenliu on 15/5/31.
 */
public class CampusDetailActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campus_detail);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}