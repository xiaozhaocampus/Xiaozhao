package com.campus.xiaozhao.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.campus.xiaozhao.R;
import com.campus.xiaozhao.basic.data.SubCategory;

public class SubCategoryAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<SubCategory> mCategoryList = new ArrayList<SubCategory>();
    
    public SubCategoryAdapter(Context context, List<SubCategory> categoryList) {
        assert (context != null);
        assert (categoryList != null);
        mContext = context;
        mCategoryList.addAll(categoryList);
    }
    
    public void setChecked(int position, boolean checked) {
        assert (position >= 0 && position < mCategoryList.size());
        mCategoryList.get(position).selected = checked;
        notifyDataSetChanged();
    }
    
    public void toggle(int position) {
        mCategoryList.get(position).selected = !mCategoryList.get(position).selected;
        notifyDataSetChanged();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sub_category_item, parent, false);
        }
        SubCategory item = mCategoryList.get(position);
        TextView nameTextView = (TextView) convertView.findViewById(R.id.title_tv);
        ImageView checkImageView = (ImageView) convertView.findViewById(R.id.check_iv);
        nameTextView.setText(item.title);
        if (item.selected) {
            nameTextView.setTextColor(mContext.getResources().getColor(R.color.category_title_checked_color));
            checkImageView.setVisibility(View.VISIBLE);
        } else {
            nameTextView.setTextColor(mContext.getResources().getColor(R.color.category_title_default_color));
            checkImageView.setVisibility(View.GONE);
        }
        
        return convertView;
    }

}
