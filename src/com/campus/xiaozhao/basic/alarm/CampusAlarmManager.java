package com.campus.xiaozhao.basic.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.campus.xiaozhao.basic.data.CampusInfoItemData;
import com.campus.xiaozhao.basic.db.CampusDBProcessor;
import com.campus.xiaozhao.basic.db.CampusModel;
import com.campus.xiaozhao.basic.utils.DateUtils;
import com.component.logger.Logger;

import java.util.List;

/**
 * Created by frankenliu on 2015/6/1.
 */
public class CampusAlarmManager {
    private static final String TAG = "CampusAlarmManager";
    private static volatile CampusAlarmManager mAlarmManager;

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
    public void startAlarm(Context context, long time, String campusID) {
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, CampusAlarmReceiver.class);
        intent.putExtra("campus_id", campusID);
        PendingIntent send = PendingIntent.getBroadcast(context, getIdByDB(context, campusID), intent, 0);
        manager.set(AlarmManager.RTC_WAKEUP, time, send);
        Logger.d(TAG, "set alarm success");
    }

    /**
     * 取消计时
     * @param context
     * @param campusID
     */
    public void stopAlarm(Context context, String campusID) {
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, CampusAlarmReceiver.class);
        intent.putExtra("campus_id", campusID);
        PendingIntent send = PendingIntent.getBroadcast(context, getIdByDB(context, campusID), intent, 0);
        manager.cancel(send);
    }

    /**
     * 设置一个定时提醒, 默认定时提醒在校招开始前6小时
     * @param context
     * @param time 校招信息的时间
     */
    public long setCampusAlarm(Context context, long time, String campusID) {
        long res = DateUtils.checkTime(time);
        showTips(context, res);
        if(res > 0) {
            startAlarm(context, res, campusID);
        }
        return res;
    }

    /**
     * 获取校招信息的数据库ID，作为定时提醒的ID以便能准确撤销定时提醒和设置多个定时提醒
     * @param campusId
     * @return
     */
    private int getIdByDB(Context context, String campusId) {
        CampusInfoItemData itemData = CampusDBProcessor.getInstance(context).getCampusInfoByCampsuID(campusId);
        if(itemData != null) {
            return (int)itemData.getId();
        }
        return -1;
    }



    public void showTips(Context context, long res) {
        if(res < 0) {
            Toast.makeText(context, "信息已过期!", Toast.LENGTH_LONG).show();
        } else if(res == 0) {
            Toast.makeText(context, "距离校招开始还有不到6小时的时间, 请做好准备!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "系统将于" + DateUtils.transferTimeToDate(res) + "提醒您!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 重新设定Alarm定时，主要用于用于再次启动或手机重启
     * @param context
     */
    public void resetAllAlarm(Context context) {
        CampusDBProcessor processor = CampusDBProcessor.getInstance(context);
        String whereClause = CampusModel.CampusInfoItemColumn.IS_REMIND + " =?";
        String[] whereArgs = new String[]{String.valueOf(true)};
        List<CampusInfoItemData> list = processor.getCampusInfos(whereClause, whereArgs, null);
        if(list != null && list.size() > 0) {
            for(CampusInfoItemData data : list) {
                stopAlarm(context, data.getCampusID());
                setCampusAlarm(context, data.getTime(), data.getCampusID());
            }
        }
    }
}
