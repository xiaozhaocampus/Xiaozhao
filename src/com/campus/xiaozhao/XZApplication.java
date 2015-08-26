package com.campus.xiaozhao;

import android.app.Application;
import android.os.Handler;

import com.campus.xiaozhao.basic.alarm.CampusAlarmManager;
import com.campus.xiaozhao.http.HttpEngine;
import com.component.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;

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

		// 初始化腾讯Bugly监控应用程序的crash
		CrashReport.initCrashReport(getApplicationContext(), "900007583", false);
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
