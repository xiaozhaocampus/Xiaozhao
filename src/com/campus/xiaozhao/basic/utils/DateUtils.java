package com.campus.xiaozhao.basic.utils;

import java.text.SimpleDateFormat;

import com.campus.xiaozhao.R;
import com.campus.xiaozhao.XZApplication;

/**
 * Created by frankenliu on 15/5/31.
 */
public class DateUtils {

    public static final long REMIND_TIME_DISTANCE_15_MINUTES = 15 * 60 * 1000;
    public static final long REMIND_TIME_DISTANCE_30_MINUTES = 30 * 60 * 1000;
    public static final long REMIND_TIME_DISTANCE_1_HOUR = 1 *  60 * 60 * 1000;
    public static final long REMIND_TIME_DISTANCE_3_HOUR = 3 *  60 * 60 * 1000;
    public static final long REMIND_TIME_DISTANCE_6_HOUR = 6 *  60 * 60 * 1000;
    public static final long ONE_DAY_TIME = 1*24*60*60*1000;
    /**
     * 将时间转化成日期，默认日期格式：yyyy-MM-dd HH:mm:ss
     * @param time
     * @return
     */
    public static String transferTimeToDate(long time) {
        return transferTimeToDate(time, "yyyy-MM-dd HH:mm");
    }

    /**
     * 按制定的格式将时间转成日期
     * @param time
     * @param pattern
     * @return
     */
    public static String transferTimeToDate(long time, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(time);
    }

    public static int checkTime(long time) {
        int res;
        long subTime = time - System.currentTimeMillis();
        if(subTime < 0) {
            res = -1;
        } else if(subTime > REMIND_TIME_DISTANCE_6_HOUR) {
            res = 1;
        } else if(subTime > REMIND_TIME_DISTANCE_3_HOUR) {
            res = 2;
        } else if(subTime > REMIND_TIME_DISTANCE_1_HOUR) {
            res = 3;
        } else if(subTime > REMIND_TIME_DISTANCE_30_MINUTES) {
            res = 4;
        } else if(subTime > REMIND_TIME_DISTANCE_15_MINUTES) {
            res = 5;
        } else {
            res = 6;
        }

        return res;
    }
    
    public static String transferTimeToDayByClasses(long time) {
    	long interval = time  - System.currentTimeMillis();
    	if(interval <= ONE_DAY_TIME) {
    		return XZApplication.getInstance().getResources().getString(R.string.today);
    	} else if(interval <= 2*ONE_DAY_TIME) {
    		return XZApplication.getInstance().getResources().getString(R.string.yesterday);
    	}
    	return transferTimeToDate(time, XZApplication.getInstance().getResources().getString(R.string.time_pattern));
    }
}
