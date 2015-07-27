package com.campus.xiaozhao.basic.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;

import com.campus.xiaozhao.R;
import com.campus.xiaozhao.activity.CampusDetailActivity;
import com.campus.xiaozhao.basic.data.CampusInfoItemData;
import com.campus.xiaozhao.basic.db.CampusDBProcessor;
import com.component.logger.Logger;

/**
 * Created by frankenliu on 2015/6/1.
 */
public class CampusAlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "CampusAlarmReceiver";
    private PowerManager.WakeLock mWakeLock;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Logger.d(TAG, "action:" + action);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resIntent = new Intent(context, CampusDetailActivity.class);
        resIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent send = PendingIntent.getActivity(context, 0, resIntent, 0);
        String campusId = intent.getStringExtra("campus_id");
        CampusInfoItemData itemData = CampusDBProcessor.getInstance(context).getCampusInfoByCampsuID(campusId);
        Bundle bundle = new Bundle();
        bundle.putSerializable("detail_data", itemData);
        resIntent.putExtras(bundle);
        if(itemData != null) {
            Notification notification = new Notification.Builder(context)
                    .setContentTitle(itemData.getTitle())
                    .setContentText(itemData.getContent())
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentIntent(send)
                    .build();
            notification.defaults = Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.flags |= Notification.FLAG_SHOW_LIGHTS;
            manager.notify((int)(Math.random() * 10000), notification);

            acquireWakeLock(context);
        }
    }

    /**
     *
     * @param context
     */
    public void acquireWakeLock(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, TAG);
        mWakeLock.acquire(3 * 1000);
    }

    public void releaseWakeLock() {
        Logger.i(TAG, " ---------------------------------??????");
        if (mWakeLock != null && mWakeLock.isHeld()) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }
}
