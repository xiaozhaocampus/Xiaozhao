package com.campus.xiaozhao.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.campus.xiaozhao.R;
import com.campus.xiaozhao.activity.MePagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

public class MeFragment extends Fragment {
	private ViewPager mViewPager = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_me_v2, container, false);
		mViewPager = (ViewPager)view.findViewById(R.id.viewpager);
		mViewPager.setAdapter(new MePagerAdapter());
		
	      //实例化TabPageIndicator然后设置ViewPager与之关联  
        final TabPageIndicator indicator = (TabPageIndicator)view.findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);
        //如果我们要对ViewPager设置监听，用indicator设置就行了  
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {  
              
            @Override  
            public void onPageSelected(int arg0) {  
            }  
              
            @Override  
            public void onPageScrolled(int arg0, float arg1, int x) {  
                  indicator.scrollTo(x, (int)indicator.getY());
            }  
              
            @Override  
            public void onPageScrollStateChanged(int arg0) {  
            }  
        });  


		return view;
	}
	
}
