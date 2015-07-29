package com.campus.xiaozhao.activity;

import java.util.ArrayList;
import java.util.List;

import com.campus.xiaozhao.R;
import com.campus.xiaozhao.basic.data.MainCategory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MainCategoryAdapter extends BaseAdapter {
    
    private Context mContext;
    private ArrayList<MainCategory> mCategoryList = new ArrayList<MainCategory>();
    
    public MainCategoryAdapter(Context context, List<MainCategory> categoryList) {
        assert (context != null);
        assert (categoryList != null);
        mContext = context.getApplicationContext();
        mCategoryList.addAll(categoryList);
    }

    @Override
    public int getCount() {
        return mCategoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCategoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.main_category_item, parent, false);
        }
        MainCategory item = mCategoryList.get(position);
        TextView name = (TextView) convertView.findViewById(R.id.title_tv);
        name.setText(item.title);
        
        return convertView;
    }

}
