package com.campus.xiaozhao.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.campus.xiaozhao.R;
import com.campus.xiaozhao.XZApplication;
import com.campus.xiaozhao.activity.HistoryListView;
import com.campus.xiaozhao.activity.MyActiviesListView;
import com.campus.xiaozhao.basic.data.CampusInfoItemData;
import com.campus.xiaozhao.basic.db.CampusDBProcessor;
import com.campus.xiaozhao.basic.db.CampusModel;
import com.campus.xiaozhao.basic.utils.ActivityUtils;

public class MePagerAdapter extends PagerAdapter implements OnItemClickListener {

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		Resources res = XZApplication.getInstance().getResources();
		if(position == 0) {
			return res.getString(R.string.me_page_title_activity);
		} else if(position == 1) {
			return res.getString(R.string.me_page_title_history);
		}
		return super.getPageTitle(position);
	}
	
	@Override
	public Object instantiateItem(android.view.ViewGroup container,
			int position) {
		Context context = container.getContext();
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = null;
		TextView emptyView = null;
		//历史记录
		if(position == 1) {
			view = inflater.inflate(R.layout.history_listview, null);
			HistoryListView list = ((HistoryListView)view.findViewById(R.id.history_list));
			emptyView = (TextView)view.findViewById(R.id.history_empty_view); 
			emptyView.setText(R.string.no_history);
			list.setEmptyView(emptyView);
			list.setOnItemClickListener(this);
			Cursor cur = CampusDBProcessor.getInstance(context).query("("+ CampusModel.CampusInfoItemColumn.IS_SAVE + " =? OR " + CampusModel.CampusInfoItemColumn.IS_REMIND + " =?) AND " + CampusModel.CampusInfoItemColumn.TIME + " <? ", new String[]{String.valueOf(1),String.valueOf(1),String.valueOf(System.currentTimeMillis()) }, CampusModel.CampusInfoItemColumn.TIME);
			HistoryListAdapter adaptor = new HistoryListAdapter(context, cur, true);
			list.setAdapter(adaptor);
		} else if(position == 0) {
			//我的活动
			view = inflater.inflate(R.layout.my_activities_listview, null);
			MyActiviesListView expListView = ((MyActiviesListView)view.findViewById(R.id.my_activies_list));
			expListView.setGroupIndicator(null);
			MyActiviesListViewAdapter adapter = new MyActiviesListViewAdapter(context,expListView);
			expListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			emptyView = (TextView)view.findViewById(R.id.my_activies_empty_view);
			emptyView.setText(R.string.no_activities);
			expListView.setEmptyView(emptyView);
			adapter.refresh();
		}
		container.addView(view);
		return view;
	}

	@Override
	public void destroyItem(android.view.ViewGroup container, int position,
			Object object) {
		((ViewPager) container)
				.removeView((View)object);
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int pos, long arg) {
		Context context = view.getContext();
		Cursor cur = (Cursor) adapter.getAdapter().getItem(pos);
        cur.moveToPosition(pos);
        ActivityUtils.showCampusDetailActivity(context, CampusInfoItemData.from(cur));
	}

	@Override  
    public int getItemPosition(Object object) {  
		return POSITION_NONE;  
    }  
}
