package com.campus.xiaozhao.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;

import com.campus.xiaozhao.R;
import com.component.logger.Logger;
import com.viewpagerindicator.TabPageIndicator;

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
		
	      //实例化TabPageIndicator然后设置ViewPager与之关联  
        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);  
        indicator.setViewPager(mViewPager);  
          
        //如果我们要对ViewPager设置监听，用indicator设置就行了  
        indicator.setOnPageChangeListener(new OnPageChangeListener() {  
              
            @Override  
            public void onPageSelected(int arg0) {  
            }  
              
            @Override  
            public void onPageScrolled(int arg0, float arg1, int arg2) {  
                  
            }  
              
            @Override  
            public void onPageScrollStateChanged(int arg0) {  
                  
            }  
        });  
          
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
