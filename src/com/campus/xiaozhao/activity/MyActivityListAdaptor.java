package com.campus.xiaozhao.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.campus.xiaozhao.R;

public class MyActivityListAdaptor extends BaseAdapter {
	private String[][] mInfos = {{"中兴集团校园招聘会","6月28 4:30","华中科大活动中心","已过期"},
			{"华为集团校园招聘会","6月30 4:30","武汉大学学生活动中心","已过期"}};
	public Context mContext;
	public MyActivityListAdaptor(Context context) {
		mContext = context;
	}
	@Override
	public int getCount() {
		return mInfos.length;
	}

	@Override
	public Object getItem(int arg0) {
		return mInfos[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		if(view == null) {
			view = LayoutInflater.from(mContext).inflate(R.layout.me_page_list_view_item, null);
		}
		TextView title = ((TextView)view.findViewById(R.id.title));
		TextView time = ((TextView)view.findViewById(R.id.time));
		TextView place = ((TextView)view.findViewById(R.id.place));
		TextView over = ((TextView)view.findViewById(R.id.over));
		title.setText(mInfos[pos][0]);
		time.setText(mInfos[pos][1]);
		place.setText(mInfos[pos][2]);
		over.setText(mInfos[pos][3]);
		return view;
	}

}
