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
import com.campus.xiaozhao.basic.data.MainCategory;
import com.campus.xiaozhao.basic.data.SubCategory;
import com.campus.xiaozhao.basic.data.job_type;
import com.campus.xiaozhao.basic.utils.StringUtils;
import com.component.logger.Logger;

public class SubCategoryActivity extends Activity {

    public static final String TAG = "SubCategoryActivity";
    private static final String KEY_DATA = "data";
    private static final String KEY_MAIN_CATEGORY = "main_category";
    
    private ListView mCategoryListView;
    private SubCategoryAdapter mCategoryAdapter;
    private MainCategory mMainCategory;
    private ArrayList<SubCategory> mSubCategories;
    
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
        mSubCategories = new ArrayList<SubCategory>();
        mCategoryAdapter = new SubCategoryAdapter(this, mSubCategories);
        getDataFromBmob();
    }

    /**
     * 暂时无用
     */
    private void saveData() {

    }

    private void getDataFromBmob() {
        BmobQuery<job_type> query = new BmobQuery<job_type>();
        query.setLimit(50);
        query.addWhereEqualTo("group_id", mMainCategory.id);
        query.order("createdAt");
        query.findObjects(getApplicationContext(), new FindListener<job_type>() {
            @Override
            public void onSuccess(List<job_type> list) {
                if(list != null && list.size() > 0) {
                    for(job_type jobType : list) {
                        if(jobType != null) {
                            SubCategory subCategory = new SubCategory();
                            subCategory.id = jobType.getType_id();
                            subCategory.title = jobType.getType_name();
                            if(StringUtils.isInFilter(getApplicationContext(), subCategory.id)) {
                                subCategory.selected = true;
                            }
                            mSubCategories.add(subCategory);
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
