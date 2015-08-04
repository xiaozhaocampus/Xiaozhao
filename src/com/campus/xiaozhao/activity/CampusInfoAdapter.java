package com.campus.xiaozhao.activity;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.campus.xiaozhao.R;
import com.campus.xiaozhao.basic.data.CampusInfoItemData;
import com.campus.xiaozhao.basic.db.CampusDBProcessor;
import com.campus.xiaozhao.basic.utils.DateUtils;

/**
 * Created by frankenliu on 15/5/29.
 */
public class CampusInfoAdapter extends BaseAdapter {
    private Context mContext;
    private List<CampusInfoItemData> mDatas;
    public CampusInfoAdapter(Context context, List<CampusInfoItemData> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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
            holder.mLocation = (ImageView) convertView.findViewById(R.id.info_list_location);
            holder.mIsSave = (ImageView) convertView.findViewById(R.id.info_list_is_save);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CampusInfoItemData itemData = mDatas.get(position);
        CampusInfoItemData itemData1 = CampusDBProcessor.getInstance(mContext).getCampusInfoByCampsuID(itemData.getCampusID());
        if(itemData1 != null) {
            itemData.setIsRemind(itemData1.isRemind());
            itemData.setRemindType(itemData1.getRemindType());
        }
        holder.mTitle.setText(itemData.getTitle());
        holder.mAddress.setText(itemData.getAddress());
        holder.mTime.setText(DateUtils.transferTimeToDate(itemData.getTime()));
        if(itemData.isSave()) {
            holder.mLocation.setImageResource(R.drawable.fragment_info_location_off);
            holder.mIsSave.setVisibility(View.VISIBLE);
            holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.campus_info_list_gray_color));
            holder.mTime.setTextColor(mContext.getResources().getColor(R.color.campus_info_list_gray_color));
        } else {
            holder.mLocation.setImageResource(R.drawable.fragment_info_location_on);
            holder.mIsSave.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    static class ViewHolder {
        public TextView mTitle;
        public TextView mAddress;
        public TextView mTime;
        public ImageView mLocation;
        public ImageView mIsSave;
    }
}
