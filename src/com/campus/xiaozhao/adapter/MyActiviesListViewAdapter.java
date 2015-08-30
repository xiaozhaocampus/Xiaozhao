package com.campus.xiaozhao.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.campus.xiaozhao.R;
import com.campus.xiaozhao.XZApplication;
import com.campus.xiaozhao.basic.data.CampusInfoItemData;
import com.campus.xiaozhao.basic.db.CampusDBProcessor;
import com.campus.xiaozhao.basic.db.CampusModel;
import com.campus.xiaozhao.basic.utils.ActivityUtils;
import com.campus.xiaozhao.basic.utils.DateUtils;

public class MyActiviesListViewAdapter extends BaseExpandableListAdapter implements OnChildClickListener{
	private ExpandableListView mExpandableListView;
	private Context mContext = null;
	private class OneDayCampusInfo{
		public String mTitle;
		public List<CampusInfoItemData> mDatas; 
	}
	private List<OneDayCampusInfo> mDatas;
	public MyActiviesListViewAdapter(Context context, ExpandableListView expandableListView) {
		mContext = context;
		mExpandableListView = expandableListView;
		mExpandableListView.setOnChildClickListener(this);
	}
	
	@Override
	public Object getChild(int pos0, int pos1) {
		if(mDatas == null) {
			return null;
		}
		return mDatas.get(pos0).mDatas.get(pos1);
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
		CampusInfoItemData data = mDatas.get(group).mDatas.get(pos);
		title.setText(data.getTitle());
		time.setText(DateUtils.transferTimeToDate(data.getTime()));
		place.setText(data.getAddress());
		if(group == 0 && pos == 0) {
			((ImageView)view.findViewById(R.id.remain)).setImageResource(R.drawable.remain);
		}
		return view;
	}

	@Override
	public int getChildrenCount(int arg0) {
		if(mDatas == null) {
			return 0;
		}
		return mDatas.get(arg0).mDatas.size();
	}

	@Override
	public Object getGroup(int arg0) {
		if(mDatas == null) {
			return null;
		}
		return mDatas;
	}

	@Override
	public int getGroupCount() {
		if(mDatas == null) {
			return 0;
		}
		return mDatas.size();
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
		title.setText(mDatas.get(arg0).mTitle);
		detail.setText("");
		return view;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
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
	
	/**
	 * 刷新数据
	 */
	public void refresh() {
		AsyncTask<Void, Void, List<OneDayCampusInfo>> task = new AsyncTask<Void, Void, List<OneDayCampusInfo>>(){

			@Override
			protected List<OneDayCampusInfo> doInBackground(Void... arg0) {
				Context context = XZApplication.getInstance();
				List<CampusInfoItemData> datas = CampusDBProcessor.getInstance(context).getCampusInfos("("+ CampusModel.CampusInfoItemColumn.IS_SAVE + " =? OR " + CampusModel.CampusInfoItemColumn.IS_REMIND + " =?) AND " + CampusModel.CampusInfoItemColumn.TIME + " >? ", new String[]{String.valueOf(1),String.valueOf(1),String.valueOf(System.currentTimeMillis())}, CampusModel.CampusInfoItemColumn.TIME);
				if(datas == null) {
					return null;
				}
				Collections.sort(datas, new Comparator<CampusInfoItemData>() {

					@Override
					public int compare(CampusInfoItemData arg0,
							CampusInfoItemData arg1) {
						if(arg0.getTime() >= arg1.getTime()) {
							return 1;
						}
						return -1;
					}
				});
				Map<String, List<CampusInfoItemData>> datasMap = new HashMap<String, List<CampusInfoItemData>>();
				for(CampusInfoItemData data:datas) {
					String day = DateUtils.transferTimeToDayByClasses(data.getTime());
					List<CampusInfoItemData> dayDatas = datasMap.get(day);
					if(dayDatas == null) {
						dayDatas = new ArrayList<CampusInfoItemData>();
						datasMap.put(day, dayDatas);
					}
					dayDatas.add(data);
				}
				List<OneDayCampusInfo> oneDayDatas = new ArrayList<MyActiviesListViewAdapter.OneDayCampusInfo>();
				for(Entry<String, List<CampusInfoItemData>> entry:datasMap.entrySet()) {
					String title = entry.getKey();
					List<CampusInfoItemData> res = entry.getValue();
					OneDayCampusInfo oneDayCampusInfo = new OneDayCampusInfo();
					oneDayCampusInfo.mTitle = title;
					oneDayCampusInfo.mDatas = res;
					oneDayDatas.add(oneDayCampusInfo);
				}
				return oneDayDatas;
			}
			
			@Override
			protected void onPostExecute(List<OneDayCampusInfo> result) {
				super.onPostExecute(result);
				mDatas = result;
				notifyDataSetChanged();
			}
		};
		task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	@Override
	public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2,
			int arg3, long arg4) {
		if(mDatas == null) {
			return true;
		}
        ActivityUtils.showCampusDetailActivity(arg0.getContext(), mDatas.get(arg2).mDatas.get(arg3));
		return true;
	}
}
