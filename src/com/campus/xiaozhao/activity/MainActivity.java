package com.campus.xiaozhao.activity;

import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;

import com.campus.xiaozhao.Configuration;
import com.campus.xiaozhao.Environment;
import com.campus.xiaozhao.R;
import com.campus.xiaozhao.basic.utils.ApplicationInfo;
import com.campus.xiaozhao.basic.utils.CampusSharePreference;
import com.campus.xiaozhao.basic.widget.Updater;
import com.campus.xiaozhao.fragment.InfoFragment;
import com.campus.xiaozhao.fragment.MeFragment;
import com.component.logger.Logger;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {
	private static final String TAG = "MainActivity";
    private ImageView infoFragmentView;
    private ImageView selfFragmentView;
    private Fragment mInfoFragment;
    private Fragment mSelfFragment;
    private Updater mUpdater;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 初始化BmobSDK
		Bmob.initialize(this, ApplicationInfo.APP_ID);
		// 使用推送服务时的初始化操作
		BmobInstallation.getCurrentInstallation(this).save();
		// 启动推送服务
		BmobPush.startWork(this, ApplicationInfo.APP_ID);
		
		if (Environment.DEBUG_LOGIN_ACTIVITY) {
		    LoginActivity.startFrom(this);
		    finish();
		    return;
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
		//更新器
		mUpdater = new Updater(this);
		mUpdater.checkUpdate(true);
	}

	public static void startFrom(Context context) {
    	Intent intent = new Intent(context, MainActivity.class);
    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    		.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    		.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	context.startActivity(intent);
    }

    private void setupUI() {
    	setTheme(R.style.StyledIndicators);
		setContentView(R.layout.activity_main);
		
		Logger.d(TAG, "setup UI");
		mSelfFragment = new MeFragment();
		mInfoFragment = new InfoFragment();

        infoFragmentView = (ImageView) findViewById(R.id.fragment_info);
        selfFragmentView = (ImageView) findViewById(R.id.fragment_self);
        getSupportFragmentManager().beginTransaction().add(R.id.tab_content, mInfoFragment).commit();
        setBehindContentView(R.layout.settings);
        SlidingMenu menu = getSlidingMenu();
        menu.setMode(SlidingMenu.RIGHT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		menu.setAboveOffsetRes(R.dimen.slidingmenu_offset);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeEnabled(false);
		menu.setMenu(R.layout.settings);
		menu.setBackgroundResource(R.drawable.main_bg);
		menu.setAboveCanvasTransformer(new SlidingMenu.CanvasTransformer() {
            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float scale = (float) (1 - percentOpen * 0.08);
                float sx = scale;
                float sy = scale;
                float px = 0;
                float py = canvas.getHeight() / 2;
                canvas.scale(sx, sy, px, py);
            }
        });
    }
    

	// TODO:处理各个点击事件
	public void onClick(View v) {
		Logger.i(TAG, "===Click Id ==>" + v.getId());
		int id = v.getId();
		switch (v.getId()) {
		case R.id.feedback:
			break;

        case R.id.fragment_info:
            infoFragmentView.setImageResource(R.drawable.fragment_info_on);
            selfFragmentView.setImageResource(R.drawable.fragment_self_off);
            if(mSelfFragment.isAdded()) {
                mSelfFragment.onPause();
                getSupportFragmentManager().beginTransaction().hide(mSelfFragment).commit();
                mInfoFragment.onResume();

            }
            getSupportFragmentManager().beginTransaction().show(mInfoFragment).commit();
            break;
        case R.id.fragment_self:
            infoFragmentView.setImageResource(R.drawable.fragment_info_off);
            selfFragmentView.setImageResource(R.drawable.fragment_self_on);
            getSupportFragmentManager().beginTransaction().hide(mInfoFragment).commit();
            if(mSelfFragment.isAdded()) {
                mInfoFragment.onPause();
                mSelfFragment.onResume();
                getSupportFragmentManager().beginTransaction().show(mSelfFragment).commit();
            } else {
                getSupportFragmentManager().beginTransaction().add(R.id.tab_content, mSelfFragment).commit();
            }
            break;
        case R.id.me_header_photo:
        	id = R.id.personal; 
        	break;
		}
		startActivityById(id);
	}
	
	public void onMenuItemClicked(View view) {
	    switch (view.getId()) {
    	    case R.id.filter:
    	    	if (!CampusSharePreference.isLogin(getApplicationContext())) {
    	    		new AlertDialog.Builder(this)
		            .setTitle(R.string.dialog_title_login_hint)
		            .setMessage(R.string.dialog_body_login_for_filter)
		            .setPositiveButton(R.string.be_sure, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LoginActivity.startFrom(MainActivity.this);
                        }
                    }).create().show();
    	    		return;
    	    	}
    	    	MainCategoryActivity.startFrom(this);
    	        break;
    	    case R.id.feedback:
//    	        Toast.makeText(this, "Feedback clicked", Toast.LENGTH_SHORT).show();
    	        startActivityById(R.id.feedback);
    	        break;
    	    case R.id.update:
//    	        Toast.makeText(this, "Update clicked", Toast.LENGTH_SHORT).show();
//    	        startActivityById(R.id.update);
    	        mUpdater.checkUpdate(false);
    	        break;
    	    case R.id.about:
    	        showAbout();
    	        break;
	    }
	    new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getSlidingMenu().toggle();
            }
        }, 300);
	}

	private void startActivityById(int id) {
		Class<?> classes = null; 
		switch (id) {
		case R.id.feedback:
			classes = FeedbackActivity.class;
			break;
		case R.id.personal:
			classes = PersonalActivity.class;
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
	
	private void showAbout() {
		AlertDialog dialog = new AlertDialog.Builder(this)
				.setIcon(R.drawable.ic_launcher)
				.setTitle(getResources().getString(R.string.app_name))
				.setMessage("作者：校招达人工作室\n邮箱：xiaozhaocampus@qq.com\n版权：©2015-2016")
				.setPositiveButton(getResources().getString(R.string.be_sure),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).create();
		dialog.show();
	}
}
