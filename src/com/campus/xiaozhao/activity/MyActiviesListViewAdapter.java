package com.campus.xiaozhao.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.campus.xiaozhao.R;
import com.campus.xiaozhao.basic.utils.DateUtils;

public class MyActiviesListViewAdapter extends BaseExpandableListAdapter {
	private ExpandableListView mExpandableListView;
	private Context mContext = null;
	private String[] mGroupTitles = {"今天","明天","8月8日"};
	private String[][][] mInfos = {{{"中兴集团校园招聘会","4:30PM","华中科大活动中心"},
		{"华为集团校园招聘会","5:30PM","武汉大学学生活动中心"}},{{"魅族集团校园招聘会","6月28 4:30","华中科大活动中心"},
			{"酷派集团校园招聘会","6月28 4:30","武汉理工大学生活动中心"}},{{"小米集团校园招聘会","8月8 4:30","华中科大活动中心"},
			{"锤子集团校园招聘会","8月8 4:30","武汉科技大学生活动中心"}}};
	public MyActiviesListViewAdapter(Context context, ExpandableListView expandableListView) {
		mContext = context;
		mExpandableListView = expandableListView;
	}
	
	@Override
	public Object getChild(int pos0, int pos1) {
		return mInfos[pos0][pos1];
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		return arg1;
	}

	@Override
	public View getChildView(int group, int pos, boolean arg2, View view,
			ViewGroup arg4) {
		if(view == null) {
			view = LayoutInflater.from(mContext).inflate(R.layout.my_activities_listview_item, null);
		}
		TextView title = ((TextView)view.findViewById(R.id.title));
		TextView time = ((TextView)view.findViewById(R.id.time));
		TextView place = ((TextView)view.findViewById(R.id.place));
		title.setText(mInfos[group][pos][0]);
		time.setText(mInfos[group][pos][1]);
		place.setText(mInfos[group][pos][2]);
		if(group == 0 && pos == 0) {
			((ImageView)view.findViewById(R.id.remain)).setImageResource(R.drawable.remain);
		}
		return view;
	}

	@Override
	public int getChildrenCount(int arg0) {
		return mInfos[arg0].length;
	}

	@Override
	public Object getGroup(int arg0) {
		return mGroupTitles[arg0];
	}

	@Override
	public int getGroupCount() {
		return mGroupTitles.length;
	}

	@Override
	public long getGroupId(int arg0) {
		return arg0;
	}

	@Override
	public View getGroupView(int arg0, boolean arg1, View view, ViewGroup arg3) {
		if(view == null) {
			view = LayoutInflater.from(mContext).inflate(R.layout.my_activities_listview_group_item, null); 
		}
		TextView title = ((TextView)view.findViewById(R.id.group_title));
		TextView detail = ((TextView)view.findViewById(R.id.group_detail));
		title.setText(mGroupTitles[arg0]);
		detail.setText(DateUtils.transferTimeToDate(System.currentTimeMillis()));
		return view;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return false;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		for(int i = 0; i < getGroupCount(); i++){  
			mExpandableListView.expandGroup(i);  
		} 
	}
	
	@Override
	public void notifyDataSetInvalidated() {
		super.notifyDataSetInvalidated();
		for(int i = 0; i < getGroupCount(); i++){  
			mExpandableListView.expandGroup(i);  
		} 
	}
	
}
