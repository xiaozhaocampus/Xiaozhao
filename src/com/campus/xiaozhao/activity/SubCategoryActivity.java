package com.campus.xiaozhao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.campus.xiaozhao.R;
import com.campus.xiaozhao.basic.data.MainCategory;
import com.component.logger.Logger;

public class SubCategoryActivity extends Activity {

    public static final String TAG = "SubCategoryActivity";
    private static final String KEY_DATA = "data";
    private static final String KEY_MAIN_CATEGORY = "main_category";
    private MainCategory mMainCategory;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        
        mMainCategory = (MainCategory) getIntent().getExtras().get(KEY_DATA);
        Logger.d(TAG, "onCreate: main category title is " + mMainCategory.title);
    }

    public static void startFrom(Context context, MainCategory mainCategory) {
        Bundle data = new Bundle();
        data.putSerializable(KEY_MAIN_CATEGORY, mainCategory);
        Intent intent = new Intent(context, SubCategoryActivity.class);
        intent.putExtra(KEY_DATA, data);
        context.startActivity(intent, data);
    }
}
