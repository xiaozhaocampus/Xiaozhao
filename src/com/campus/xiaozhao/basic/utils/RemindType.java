package com.campus.xiaozhao.basic.utils;

/**
 * 提醒类型
 * Created by frankenliu on 2015/7/6.
 */
public class RemindType {
    /** 不提醒 */
    public static final int REMIND_TYPE_NONE = 0;
    /** 提前15分钟提醒 */
    public static final int REMIND_TYPE_15_MINUTES = 1;
    /** 提前30分钟提醒 */
    public static final int REMIND_TYPE_30_MINUTES = 2;
    /** 提前1小时提醒 */
    public static final int REMIND_TYPE_ONE_HOUR = 3;
    /** 提前3小时提醒 */
    public static final int REMIND_TYPE_THREE_HOUR = 4;
    /** 提前6小时提醒 */
    public static final int REMIND_TYPE_SIX_HOUR = 5;
    /** 自定义提醒时间 */
    public static final int REMIND_TYPE_SELF_DEFINE = 6;

    public static final String REMIND_NAME_15_MINUTES = "提前15分钟";
    public static final String REMIND_NAME_30_MINUTES = "提前30分钟";
    public static final String REMIND_NAME_1_HOUR = "提前1小时";
    public static final String REMIND_NAME_3_HOUR = "提前3小时";
    public static final String REMIND_NAME_6_HOUR = "提前6小时";

    public static final String[] REMIND_TYPES = new String[]{
        REMIND_NAME_15_MINUTES, REMIND_NAME_30_MINUTES,
        REMIND_NAME_1_HOUR, REMIND_NAME_3_HOUR, REMIND_NAME_6_HOUR
    };
}
