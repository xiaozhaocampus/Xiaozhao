package com.campus.xiaozhao.activity;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ListView;

import com.campus.xiaozhao.R;
import com.campus.xiaozhao.activity.AlarmActivity.PageViewInfo;

public class AlarmPagerAdapter extends PagerAdapter {
    
    public static final int LAST_PAGE_INDEX = 1;
    private Context mContext;
    private int mCurrentIndex;
    private List<PageViewInfo> mPageViewInfos = null;
    public AlarmPagerAdapter(Context context, List<PageViewInfo> infos) {
        mContext = context;
        mPageViewInfos = infos;
    }

	public Object instantiateItem(android.view.ViewGroup container,
			int position) {
		View showView = mPageViewInfos.get(position).mView;
		ListView view = (ListView)showView.findViewById(R.id.list);
		view.setAdapter(new AlarmListAdapter(mContext));
		((ViewPager)container).addView(mPageViewInfos.get(position).mView);
		return mPageViewInfos.get(position).mView;
	};

	public void destroyItem(android.view.ViewGroup container, int position,
			Object object) {
		((ViewPager) container)
				.removeView(mPageViewInfos.get(position).mView);
	};

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public int getCount() {
		return mPageViewInfos.size();
	}

	public CharSequence getPageTitle(int position) {
		return mPageViewInfos.get(position).mTitle;
	};
    public void setCurrentIndex(int paramInt)
    {
        mCurrentIndex = paramInt;
    }
    
}
