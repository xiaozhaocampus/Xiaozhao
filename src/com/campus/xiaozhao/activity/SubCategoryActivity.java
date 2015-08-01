package com.campus.xiaozhao.activity;

import java.util.ArrayList;

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

import com.campus.xiaozhao.R;
import com.campus.xiaozhao.basic.data.MainCategory;
import com.campus.xiaozhao.basic.data.SubCategory;
import com.component.logger.Logger;

public class SubCategoryActivity extends Activity {

    public static final String TAG = "SubCategoryActivity";
    private static final String KEY_DATA = "data";
    private static final String KEY_MAIN_CATEGORY = "main_category";
    
    private ListView mCategoryListView;
    private SubCategoryAdapter mCategoryAdapter;
    private MainCategory mMainCategory;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        
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
        
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            Logger.e(TAG, "bundle is null");
            return;
        }
        
        Bundle data = (Bundle) bundle.get(KEY_DATA);
        if (data == null) {
            Logger.e(TAG, "data is null");
            return;
        }
        
        mMainCategory = (MainCategory) data.get(KEY_MAIN_CATEGORY);
        Logger.d(TAG, "onCreate: main category title is " + mMainCategory.title);
        ((TextView) actionBar.getCustomView().findViewById(R.id.title_tv)).setText(mMainCategory.title);
        mCategoryListView = (ListView) findViewById(R.id.sub_category_lv);
        loadData();
        mCategoryListView.setAdapter(mCategoryAdapter);
        mCategoryListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCategoryAdapter.toggle(position);
            }
        });
    }

    public static void startFrom(Context context, MainCategory mainCategory) {
        Bundle data = new Bundle();
        data.putSerializable(KEY_MAIN_CATEGORY, mainCategory);
        Intent intent = new Intent(context, SubCategoryActivity.class);
        intent.putExtra(KEY_DATA, data);
        context.startActivity(intent);
    }
    
    public void onClickOK(View view) {
        saveData();
        finish();
    }
    
    private void loadData() {
        ArrayList<SubCategory> categories = new ArrayList<SubCategory>();
        for (int i=0; i<10; i++) {
            SubCategory category = new SubCategory();
            category.id = String.valueOf(i);
            category.title = getString(R.string.sample_sub_category_title_1);
            categories.add(category);
        }
        mCategoryAdapter = new SubCategoryAdapter(this, categories);
    }
    
    private void saveData() {
        
    }
}
