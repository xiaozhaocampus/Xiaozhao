package com.campus.xiaozhao.fragment;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.campus.xiaozhao.R;
import com.campus.xiaozhao.activity.InfoPagerAdapter;
import com.campus.xiaozhao.activity.MainCategoryActivity;
import com.campus.xiaozhao.basic.location.BaiDuLocationManager;
import com.campus.xiaozhao.basic.utils.CampusSharePreference;
import com.component.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by frankenliu on 15/6/7.
 */
public class InfoFragment extends Fragment implements Handler.Callback{
    private static final String TAG = "InfoFragment";
    public static final int MSG_SET_LOCATION = 1;
    private TextView mLocation;
    private Handler mHandler;
    private ViewPager mViewPager = null; // 页卡内容
    private ImageView mImageCursor; // 动画图片
    private TextView mTabAll; // 全部活动的tab
    private RelativeLayout mTabMyFilter; // 我的订阅的tab
    private TextView mTabMyFilterTitle; // 我的订阅tab中的title
    private ImageView mTabMyFilterImage; // 我的订阅tab中的image
    private int offset = 0; // 动画图片偏移量

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_info, container, false);
        mLocation = (TextView) getActivity().findViewById(R.id.actionbar_location_city);
        String location = CampusSharePreference.getLocation(getActivity());
        if(TextUtils.isEmpty(location)) {
            location = "暂无定位";
        }
        mLocation.setText(location);
        mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "其他城市正在开发中", Toast.LENGTH_LONG).show();
            }
        });
        initData();


        // 初始化ViewPager
        mViewPager = (ViewPager)view.findViewById(R.id.info_pager);
        mViewPager.setAdapter(new InfoPagerAdapter(getActivity()));
        mViewPager.setCurrentItem(0);
        mTabMyFilterTitle = (TextView) view.findViewById(R.id.tab_my_filter_title);
        mTabMyFilterImage = (ImageView) view.findViewById(R.id.tab_my_filter_image);
        mTabMyFilterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                intent.setClass(getActivity().getApplicationContext(), MainCategoryActivity.class);
                startActivity(intent);
            }
        });
        //如果我们要对ViewPager设置监听，用indicator设置就行了
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                Animation animation = new TranslateAnimation(0, 0, 0, 0);
                if(i == 0) {
                    animation = new TranslateAnimation(offset, 0, 0, 0);
                    mTabAll.setTextColor(getResources().getColor(R.color.main));
                    mTabMyFilterTitle.setTextColor(getResources().getColor(R.color.black));
                } else if(i == 1) {
                    animation = new TranslateAnimation(0, offset, 0, 0);
                    mTabAll.setTextColor(getResources().getColor(R.color.black));
                    mTabMyFilterTitle.setTextColor(getResources().getColor(R.color.main));
                }
                animation.setFillAfter(true); // 图片停在动画结束位置
                animation.setDuration(300);
                mImageCursor.setAnimation(animation);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


        // 初始化头标
        mTabAll = (TextView) view.findViewById(R.id.tab_all_info);
        mTabMyFilter = (RelativeLayout) view.findViewById(R.id.tab_my_filter);
        mTabAll.setOnClickListener(new TabOnClickListener(0));
        mTabMyFilter.setOnClickListener(new TabOnClickListener(1));


        // 初始化动画，就是页卡滑动时，下面的横线也滑动的效果
        mImageCursor = (ImageView) view.findViewById(R.id.info_cursor);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels; // 获取屏幕宽度
        offset = screenW / 2;
        ViewGroup.LayoutParams params = mImageCursor.getLayoutParams();
        params.width = screenW / 2;
        mImageCursor.setLayoutParams(params);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initData() {
        mHandler = new Handler(this);
        BaiDuLocationManager.getInstance().start(getActivity(), mHandler);
    }


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_SET_LOCATION:
                BDLocation location = (BDLocation) msg.obj;
                mLocation.setText(location.getCity());
                Logger.d(TAG, "location:" + location.getCity());
                BaiDuLocationManager.getInstance().stop();
                break;
            default:
                break;
        }

        return false;
    }

    /**
     * 头标事件监听
     */
    class TabOnClickListener implements View.OnClickListener {
        private int index = 0;

        public TabOnClickListener(int i) {
            this.index = i;
        }

        @Override
        public void onClick(View v) {
            mViewPager.setCurrentItem(index);
        }
    }
}
