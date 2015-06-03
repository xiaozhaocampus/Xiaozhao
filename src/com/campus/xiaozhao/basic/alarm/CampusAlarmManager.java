package com.campus.xiaozhao.basic.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.campus.xiaozhao.basic.utils.DateUtils;

/**
 * Created by frankenliu on 2015/6/1.
 */
public class CampusAlarmManager {
    private static CampusAlarmManager mAlarmManager;
    private static final long REMIND_TIME_DISTANCE = 6 * 60 * 60 * 1000;
    private CampusAlarmManager() {

    }

    public static CampusAlarmManager getInstance() {
        if(mAlarmManager == null) {
            synchronized (CampusAlarmManager.class) {
                if(mAlarmManager == null) {
                    mAlarmManager = new CampusAlarmManager();
                }
            }
        }
        return mAlarmManager;
    }

    /**
     * 开始计时并指定计时结束的时间
     * @param context
     * @param time 计时结束的时间(也就是定时提醒的时间)
     */
    public void startAlarm(Context context, long time, long campusID) {
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, CampusAlarmReceiver.class);
        intent.putExtra("campus_id", campusID);
        PendingIntent send = PendingIntent.getBroadcast(context, 0, intent, 0);
        manager.set(AlarmManager.RTC_WAKEUP, time, send);
    }

    /**
     * 设置一个定时提醒, 默认定时提醒在校招开始前6小时
     * @param context
     * @param time 校招信息的时间
     */
    public int setCampusAlarm(Context context, long time, long campusID) {
        int res;
        long subTime = time - System.currentTimeMillis();
        if(subTime < 0) {
            Toast.makeText(context, "信息已过期!", Toast.LENGTH_LONG).show();
            res = -1;
        } else if(subTime < REMIND_TIME_DISTANCE) {
            Toast.makeText(context, "距离校招开始还有不到6小时的时间, 请做好准备!", Toast.LENGTH_LONG).show();
            res = 0;
        } else {
            long remindTime = time - REMIND_TIME_DISTANCE;
            startAlarm(context, remindTime, campusID);
            Toast.makeText(context, "系统将于" + DateUtils.transferTimeToDate(remindTime) + "提醒您!", Toast.LENGTH_LONG).show();
            res = 1;
        }
        return res;
    }
}
