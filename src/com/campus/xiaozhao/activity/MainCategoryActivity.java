package com.campus.xiaozhao.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.campus.xiaozhao.R;
import com.campus.xiaozhao.adapter.MainCategoryAdapter;
import com.campus.xiaozhao.basic.data.MainCategory;
import com.campus.xiaozhao.basic.data.job_group;
import com.component.logger.Logger;

public class MainCategoryActivity extends Activity {

    private static final String TAG = "MainCategoryActivity";
    private ListView mCategoryListView;
    private MainCategoryAdapter mCategoryAdapter;
    private ArrayList<MainCategory> mMainCategories;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_category);
        
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
        mCategoryListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubCategoryActivity.startFrom(MainCategoryActivity.this, (MainCategory) mCategoryAdapter.getItem(position));
            }
        });
    }

    public static void startFrom(Context context) {
        context.startActivity(new Intent(context, MainCategoryActivity.class));
    }

    public void onClickOK(View view) {
        saveData();
        finish();
    }
    
    private void loadData() {
        mMainCategories = new ArrayList<MainCategory>();
        mCategoryAdapter = new MainCategoryAdapter(this, mMainCategories);
        getDataFromBmob();
    }

    /**
     * 暂时无用
     */
    private void saveData() {
        
    }

    private void getDataFromBmob() {
        BmobQuery<job_group> query = new BmobQuery<job_group>();
        query.setLimit(50);
        query.order("createdAt");
        query.findObjects(getApplicationContext(), new FindListener<job_group>() {
            @Override
            public void onSuccess(List<job_group> list) {
                if(list != null && list.size() > 0) {
                    for(job_group jobGroup : list) {
                        if(jobGroup != null) {
                            MainCategory mainCategory = new MainCategory();
                            mainCategory.id = jobGroup.getGroup_id();
                            mainCategory.title = jobGroup.getGroup_name();
                            mMainCategories.add(mainCategory);
                        }
                    }
                }

                mCategoryAdapter.notifyDataSetChanged();
                Logger.d(TAG, "get success");
            }

            @Override
            public void onError(int i, String s) {
                Logger.d(TAG, "get fail, i:" + i + ", s:" + s);
            }
        });
    }
}
