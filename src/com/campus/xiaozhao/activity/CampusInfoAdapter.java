package com.campus.xiaozhao.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.campus.xiaozhao.R;

/**
 * Created by frankenliu on 15/5/29.
 */
public class CampusInfoAdapter extends BaseAdapter {
    private Context mContext;

    public CampusInfoAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.campus_info_list, null);
            holder.mTitle = (TextView) convertView.findViewById(R.id.info_list_title);
            holder.mAddress = (TextView) convertView.findViewById(R.id.info_list_address);
            holder.mTime = (TextView) convertView.findViewById(R.id.info_list_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTitle.setText("title");
        holder.mAddress.setText("address");
        holder.mTime.setText("time");
        return convertView;
    }

    static class ViewHolder {
        public TextView mTitle;
        public TextView mAddress;
        public TextView mTime;
    }
}
