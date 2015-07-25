package com.campus.xiaozhao.activity;

import android.content.res.Resources;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.campus.xiaozhao.R;
import com.campus.xiaozhao.XZApplication;

public class MePagerAdapter extends PagerAdapter{

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
		LayoutInflater inflater = LayoutInflater.from(container.getContext());
		View view = null;
		if(position == 1) {
			view = inflater.inflate(R.layout.history_listview, null);
			((HistoryListView)view).setAdapter(new HistoryListAdaptor(container.getContext()));
		}
		if(position == 0) {
			view = inflater.inflate(R.layout.my_activities_listview, null);
			MyActiviesListView expListView = ((MyActiviesListView)view);
			expListView.setGroupIndicator(null);
			MyActiviesListViewAdapter adapter = new MyActiviesListViewAdapter(container.getContext(),expListView);
			expListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
		container.addView(view);
		return view;
	};

	@Override
	public void destroyItem(android.view.ViewGroup container, int position,
			Object object) {
		((ViewPager) container)
				.removeView((View)object);
	};

}
