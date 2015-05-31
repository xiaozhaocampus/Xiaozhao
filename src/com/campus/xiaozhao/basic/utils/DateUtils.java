package com.campus.xiaozhao.basic.utils;

import java.text.SimpleDateFormat;

/**
 * Created by frankenliu on 15/5/31.
 */
public class DateUtils {

    /**
     * 将时间转化成日期，默认日期格式：yyyy-MM-dd HH:mm:ss
     * @param time
     * @return
     */
    public static String transferTimeToDate(long time) {
        return transferTimeToDate(time, "yyyy-MM-dd HH:00");
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
}
