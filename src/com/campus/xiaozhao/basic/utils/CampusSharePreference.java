package com.campus.xiaozhao.basic.utils;

import cn.bmob.v3.BmobUser;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by frankenliu on 15/5/29.
 */
public class CampusSharePreference {
    private static final int PRE_MODEL = Context.MODE_PRIVATE;
    private static final String PRE_NAME = "campus_pre";
    private static final String KEY_LAST_START_UP_TIME = "last_start_up_time";

    /** 终端最大版本号，用于向服务器获取最新的校招信息 */
    private static final String KEY_LAST_MAX_VERSION = "key_last_max_version";
    /** 缓存地址位置 */
    private static final String KEY_LOCATION = "key_location";
    /** 已获取服务器数据的条数(最大版本号)，用于分页查询 */
    private static final String KEY_GET_SERVER_DATA_COUNT = "key_get_server_data_count";
    /** 缓存用户设置的过滤条件 */
    private static final String KEY_CACHE_CATEGORY_FILTER = "key_cache_category_filter";

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
        getPreference(context).edit().putInt(KEY_LAST_MAX_VERSION, version).commit();
    }

    /**
     * 获取缓存的地理位置
     * @param context
     * @return
     */
    public static String getLocation(Context context) {
        return getPreference(context).getString(KEY_LOCATION, null);
    }

    /**
     * 缓存地理位置
     * @param context
     * @param location
     */
    public static void setLocation(Context context, String location) {
        getPreference(context).edit().putString(KEY_LOCATION, location).commit();
    }

    /**
     * 获取已经从服务器获取的数据条数
     * @param context
     * @return
     */
    public static long getServerDataCount(Context context) {
        return getPreference(context).getLong(KEY_GET_SERVER_DATA_COUNT, 0);
    }

    /**
     * 缓存已经从服务器获取数据的条数
     * @param context
     * @param count
     */
    public static void setServerDataCount(Context context, long count) {
        getPreference(context).edit().putLong(KEY_GET_SERVER_DATA_COUNT, count).commit();
    }
    
    /**
     * 判断是否已经登录
     * @param context
     * @return
     */
    public static boolean isLogin(Context context) {
    	return BmobUser.getCurrentUser(context) != null;
    }
    
    public static void setLastStartUpTime(Context context, long time) {
        getPreference(context).edit().putLong(KEY_LAST_START_UP_TIME, time).commit();
    }
    
    public static long getLastStartUpTime(Context context) {
        return getPreference(context).getLong(KEY_LAST_START_UP_TIME, 0);
    }

    /**
     * 缓存用户设置的过滤条件
     * @param context
     * @param filter
     */
    public static void setCacheCategoryFilter(Context context, Set<String> filter) {
        getPreference(context).edit().putStringSet(KEY_CACHE_CATEGORY_FILTER, filter).commit();
    }

    /**
     * 获取用户设置的过滤条件
     * @param context
     * @return
     */
    public static Set<String> getCacheCategoryFilter(Context context) {
       return getPreference(context).getStringSet(KEY_CACHE_CATEGORY_FILTER, null);
    }

    private static SharedPreferences getPreference(Context context) {
        if(context == null) {
            return null;
        }
        return context.getSharedPreferences(PRE_NAME, PRE_MODEL);
    }
}
