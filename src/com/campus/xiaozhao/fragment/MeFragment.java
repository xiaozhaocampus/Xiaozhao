package com.campus.xiaozhao.fragment;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.campus.xiaozhao.R;
import com.campus.xiaozhao.activity.MePagerAdapter;
import com.campus.xiaozhao.basic.db.CampusUriFactory;
import com.viewpagerindicator.TabPageIndicator;

public class MeFragment extends Fragment {
	private ViewPager mViewPager = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_me, container, false);
		mViewPager = (ViewPager)view.findViewById(R.id.viewpager);
		mViewPager.setAdapter(new MePagerAdapter());
		final ImageView indicatorImage = (ImageView)view.findViewById(R.id.indicator_image);
		final int width = container.getWidth()/2;
	      //实例化TabPageIndicator然后设置ViewPager与之关联  
        final TabPageIndicator indicator = (TabPageIndicator)view.findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);
        //如果我们要对ViewPager设置监听，用indicator设置就行了  
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {  
              
            @Override  
            public void onPageSelected(int arg0) {  
            	indicator.setCurrentItem(arg0);
            	indicatorImage.setX(arg0*width);
            }  
              
            @Override  
            public void onPageScrolled(int arg0, float arg1, int x) {
            	indicatorImage.setX((arg0 + arg1)*width);
            }  
              
            @Override  
            public void onPageScrollStateChanged(int arg0) {  
            }  
        });  
        
        ContentResolver cr = getActivity().getContentResolver();
        cr.registerContentObserver(CampusUriFactory.getCampusInfoUri(), true, new DBObserver(new Handler(Looper.getMainLooper())));
		return view;
	}
	
	@Override
	public void onDestroy() {

		super.onDestroy();
	}
	
	private class DBObserver extends ContentObserver {

		public DBObserver(Handler handler) {
			super(handler);
		}
		
		@Override
		public void onChange(boolean selfChange, Uri uri) {
			super.onChange(selfChange, uri);
			if(CampusUriFactory.getMatcher().match(uri) == CampusUriFactory.URI_MATCH_CAMPUS_INFO) {
				if(mViewPager != null) {
					(mViewPager.getAdapter()).notifyDataSetChanged();
				}
			}
		}
	}
}
