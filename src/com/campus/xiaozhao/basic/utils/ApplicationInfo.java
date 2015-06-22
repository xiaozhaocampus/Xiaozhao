package com.campus.xiaozhao.basic.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.campus.xiaozhao.R;

/**
 * Created by frankenliu on 15/6/16.
 */
public class ApplicationInfo {
    /** bmob上应用的ID */
    public static final String APP_ID = "3e59d13fcb79ad2903744f1d495e416f";
    
	/**
	 * 获取版本号
	 * @return 当前应用的版本号
	 */
	public static String getVersion(Context context) {
	    try {
	        PackageManager manager = context.getPackageManager();
	        PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
	        String version = info.versionName;
	        return version;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return context.getString(R.string.can_not_find_version_name);
	    }
	}

	/**
	 * 获取版本号
	 * @return 当前应用的版本号
	 */
	public static int getVersionCode(Context context) {
	    try {
	        PackageManager manager = context.getPackageManager();
	        PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
	        int version = info.versionCode;
	        return version;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return -1;
	    }
	}
	
}
