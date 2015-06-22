package com.campus.xiaozhao;

import android.app.Application;

import com.campus.xiaozhao.http.HttpEngine;

public class XZApplication extends Application {
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
	}
	
	public static XZApplication getInstance() {
		return mApp;
	}
}
