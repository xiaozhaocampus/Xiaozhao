package com.campus.xiaozhao;

import android.app.Application;
import android.os.Handler;

import com.campus.xiaozhao.basic.alarm.CampusAlarmManager;
import com.campus.xiaozhao.http.HttpEngine;
import com.component.logger.Logger;

public class XZApplication extends Application {
	private static final String TAG = "XZApplication";
	private static XZApplication mApp;
	private HttpEngine mHttpEngine;
	public HttpEngine getHttpEngine() {
		if(mHttpEngine == null) {
			mHttpEngine = new HttpEngine();
		}
		return mHttpEngine;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mApp = this;

		Logger.d(TAG, "application onCreate");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //APP启动时主动重置Alarm计时
                CampusAlarmManager.getInstance().resetAllAlarm(getApplicationContext());
            }
        }, 3 * 1000);
	}
	
	public static XZApplication getInstance() {
		return mApp;
	}
}
