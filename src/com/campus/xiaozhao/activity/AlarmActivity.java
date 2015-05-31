package com.campus.xiaozhao.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.campus.xiaozhao.R;
import com.component.logger.Logger;

public class AlarmActivity extends Activity {
	private static final String TAG = "AlarmActivity";
	private ViewPager mViewPager = null;
	private List<PageViewInfo> mPageViewInfos = new ArrayList<AlarmActivity.PageViewInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);
		initData();

		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setAdapter(new AlarmPagerAdapter(this, mPageViewInfos));

	}

	private void initData() {
		String[] alarmTitles = getResources().getStringArray(R.array.alarms);
		for (String title : alarmTitles) {
			PageViewInfo info = new PageViewInfo();
			info.mTitle = title;
			info.mView = LayoutInflater.from(this).inflate(R.layout.alarm_classis_view, null);
			if(mPageViewInfos.size() == 0) {
				info.mView.setBackgroundResource(R.color.black);	
			} else {
				info.mView.setBackgroundResource(R.color.light_dark_bg);
			}
			mPageViewInfos.add(info);
		}
	}

	// 返回
	public void back(View view) {
		Logger.i(TAG, "AlarmActivity back");
		finish();
	}

	public static class PageViewInfo {
		public View mView;
		public String mTitle;
	}

}
