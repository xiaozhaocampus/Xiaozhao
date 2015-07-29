package com.campus.xiaozhao.activity;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.campus.xiaozhao.R;
import com.campus.xiaozhao.basic.data.MainCategory;

public class MainFilterActivity extends Activity {

    private ListView mCategoryListView;
    private MainCategoryAdapter mCategoryAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_filter);
        
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.actionbar_login);
        actionBar.getCustomView().findViewById(R.id.actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView) actionBar.getCustomView().findViewById(R.id.title_tv)).setText(R.string.subscribe_settings);
        
        mCategoryListView = (ListView) findViewById(R.id.main_category_lv);
        loadData();
        mCategoryListView.setAdapter(mCategoryAdapter);
    }

    public static void startFrom(Context context) {
        context.startActivity(new Intent(context, MainFilterActivity.class));
    }

    public void onClickOK(View view) {
        
    }
    
    private void loadData() {
        ArrayList<MainCategory> categories = new ArrayList<MainCategory>();
        for (int i=0; i<10; i++) {
            MainCategory category = new MainCategory();
            category.id = String.valueOf(i);
            category.title = getString(R.string.sample_main_category_title_1);
            categories.add(category);
        }
        mCategoryAdapter = new MainCategoryAdapter(this, categories);
    }
}
