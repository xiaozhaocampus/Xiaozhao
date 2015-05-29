package com.campus.xiaozhao.basic.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by frankenliu on 15/5/29.
 */
public class CampusSharePreference {
    private static final String PRE_NAME = "campus_pre";
    private static final int PRE_MODEL = Context.MODE_PRIVATE;

    /** 终端最大版本号，用于向服务器获取最新的校招信息 */
    private static final String KEY_LAST_MAX_VERSION = "key_last_max_version";

    /**
     * 获取终端最大版本号
     * @param context
     * @return
     */
    public static int getLastMaxVersion(Context context) {
        return getPreference(context).getInt(KEY_LAST_MAX_VERSION, 0);
    }

    /**
     * 设置终端最大版本号
     * @param context
     * @param version
     */
    public static void setLastMaxVersion(Context context, int version) {
        getPreference(context).edit().putInt(KEY_LAST_MAX_VERSION, version);
    }

    private static SharedPreferences getPreference(Context context) {
        if(context == null) {
            return null;
        }
        return context.getSharedPreferences(PRE_NAME, PRE_MODEL);
    }
}
