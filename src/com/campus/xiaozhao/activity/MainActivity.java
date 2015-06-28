package com.campus.xiaozhao.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.RadioGroup;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;

import com.campus.xiaozhao.Configuration;
import com.campus.xiaozhao.Environment;
import com.campus.xiaozhao.R;
import com.campus.xiaozhao.basic.utils.ApplicationInfo;
import com.campus.xiaozhao.basic.utils.CampusSharePreference;
import com.campus.xiaozhao.fragment.InfoFragment;
import com.campus.xiaozhao.fragment.MeFragment;
import com.component.logger.Logger;

public class MainActivity extends FragmentActivity {
	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 初始化BmobSDK
		Bmob.initialize(this, ApplicationInfo.APP_ID);
		// 使用推送服务时的初始化操作
	    BmobInstallation.getCurrentInstallation(this).save();
	    // 启动推送服务
	    BmobPush.startWork(this, ApplicationInfo.APP_ID);
		if (Environment.ENABLE_SETUP_ACTIVITY) {
            if (!CampusSharePreference.isLogin(this)) {
	            SetupActivity.startFrom(this);
	            finish();
	            return;
            }
        }
		
		if (Environment.ENABLE_SPLASH_ACTIVITY) {
		    if (shouldShowSplash()) {
    		    SplashActivity.startFrom(this);
    		    finish();
    		    return;
		    }
		}
		
		CampusSharePreference.setLastStartUpTime(getApplicationContext(), System.currentTimeMillis());
		setupUI();
	}

	public static void startFrom(Context context) {
    	Intent intent = new Intent(context, MainActivity.class);
    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    		.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    		.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	context.startActivity(intent);
    }

    private void setupUI() {
		setContentView(R.layout.activity_main);
		Logger.d(TAG, "setup UI");
		List<Fragment> fragments = new ArrayList<Fragment>();
		Fragment meFragment = new MeFragment();
		Fragment infoFragment = new InfoFragment();
		fragments.add(infoFragment);
		fragments.add(meFragment);
		
		RadioGroup rgs = (RadioGroup) findViewById(R.id.tabs_rg);

		FragmentTabAdapter tabAdapter = new FragmentTabAdapter(this, fragments,
				R.id.tab_content, rgs);
		tabAdapter
				.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {
					@Override
					public void OnRgsExtraCheckedChanged(RadioGroup radioGroup,
							int checkedId, int index) {
						System.out.println("Extra---- " + index
								+ " checked!!! ");
					}
				});

    }

	// TODO:处理各个点击事件
	public void onClick(View v) {
		Logger.i(TAG, "===Click Id ==>" + v.getId());
		int id = v.getId();
		switch (v.getId()) {
		case R.id.more_alarms:
			break;
		case R.id.more_feedback:
			break;
		case R.id.more_check_new_ver:
			break;
		}
		startActivityById(id);
	}

	private void startActivityById(int id) {
		Class<?> classes = null; 
		switch (id) {
		case R.id.more_feedback:
			classes = FeedbackActivity.class;
			break;
		case R.id.more_alarms:
			classes = AlarmActivity.class;
			break;
		case R.id.more_check_new_ver:
			classes = UpdateActivity.class;
			break;
		default:
			break;
		}
		
		if(classes != null) {
			Intent intent = new Intent(this, classes);
			startActivity(intent);
		}
	}

	private boolean shouldShowSplash() {
	    long lastStarUpTime = CampusSharePreference.getLastStartUpTime(getApplicationContext());
	    long now = System.currentTimeMillis();
	    
	    Calendar calendar1 = Calendar.getInstance();
	    calendar1.setTimeInMillis(lastStarUpTime);
	    calendar1.set(Calendar.HOUR_OF_DAY, 24);
	    calendar1.set(Calendar.HOUR, 0);
	    calendar1.set(Calendar.MINUTE, 0);
	    calendar1.set(Calendar.SECOND, 0);
	    calendar1.set(Calendar.MILLISECOND, 0);
	    
	    Calendar calendar2 = Calendar.getInstance();
	    calendar2.setTimeInMillis(now);
	    calendar2.set(Calendar.HOUR_OF_DAY, 24);
	    calendar2.set(Calendar.HOUR, 0);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);
        calendar2.set(Calendar.MILLISECOND, 0);
	    
        long interval = calendar2.getTimeInMillis() - calendar1.getTimeInMillis();
        
	    return interval >= Configuration.SPLASH_START_UP_INTERVAL;
	}
}
