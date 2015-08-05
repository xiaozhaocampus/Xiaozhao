package com.campus.xiaozhao.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
import com.campus.xiaozhao.R;
import com.campus.xiaozhao.basic.data.SubCategory;
import com.campus.xiaozhao.basic.utils.CampusSharePreference;

public class SubCategoryAdapter extends BaseAdapter {

    private Context mContext;
    private List<SubCategory> mCategoryList;
    
    public SubCategoryAdapter(Context context, List<SubCategory> categoryList) {
        assert (context != null);
        assert (categoryList != null);
        mContext = context;
        mCategoryList = categoryList;
    }
    
    public void setChecked(int position, boolean checked) {
        assert (position >= 0 && position < mCategoryList.size());
        mCategoryList.get(position).selected = checked;
        notifyDataSetChanged();
    }
    
    public void toggle(int position) {
        mCategoryList.get(position).selected = !mCategoryList.get(position).selected;
        saveFilter(position);
        notifyDataSetChanged();
    }

    /**
     * 缓存用户设置的过滤
     * @param position
     */
    private void saveFilter(int position) {
        SubCategory category = mCategoryList.get(position);
        boolean isSelected = category.selected;
        if(isSelected) {
            Set<String> filters = CampusSharePreference.getCacheCategoryFilter(mContext);
            Set<String> temp = new HashSet<>();
            if(filters == null) {
                filters = new HashSet<>();
                filters.add(category.id);
                CampusSharePreference.setCacheCategoryFilter(mContext, filters);
            } else {
                temp.addAll(filters);
                if(temp.size() >= 5) {
                    category.selected = false;
                    Toast.makeText(mContext, "订阅数不能大于5个", Toast.LENGTH_LONG).show();
                } else {
                    temp.add(category.id);
                    CampusSharePreference.setCacheCategoryFilter(mContext, temp);
                }
            }
        } else {
            Set<String> filters = CampusSharePreference.getCacheCategoryFilter(mContext);
            Set<String> temp = new HashSet<>();
            if(filters != null) {
                temp.addAll(filters);
                if(temp.contains(category.id)) {
                    temp.remove(category.id);
                    CampusSharePreference.setCacheCategoryFilter(mContext, temp);
                }
            }
        }
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
