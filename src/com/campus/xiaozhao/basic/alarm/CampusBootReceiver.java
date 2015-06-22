package com.campus.xiaozhao.basic.alarm;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import com.campus.xiaozhao.basic.data.CampusInfoItemData;
import com.campus.xiaozhao.basic.db.CampusDBProcessor;
import com.campus.xiaozhao.basic.db.CampusModel;

import java.util.List;

/**
 * Created by frankenliu on 2015/6/1.
 */
public class CampusBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            //TODO Set the alarm here.
            CampusDBProcessor processor = CampusDBProcessor.getInstance(context);
            String whereClause = CampusModel.CampusInfoItemColumn.IS_REMIND + " =?";
            String[] whereArgs = new String[]{String.valueOf(true)};
            List<CampusInfoItemData> list = processor.getCampusInfos(whereClause, whereArgs, null);
            if(list != null && list.size() > 0) {
                for(CampusInfoItemData data : list) {
                    CampusAlarmManager.getInstance().setCampusAlarm(context, data.getTime(), data.getCampusID());
                }
            }
        }
    }

    public void enableBootReceiver(Context context) {
        ComponentName receiver = new ComponentName(context, CampusBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void disableBootReceiver(Context context) {
        ComponentName receiver = new ComponentName(context, CampusBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
