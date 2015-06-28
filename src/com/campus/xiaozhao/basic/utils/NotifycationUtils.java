package com.campus.xiaozhao.basic.utils;

import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.campus.xiaozhao.R;
import com.campus.xiaozhao.activity.MainActivity;

public class NotifycationUtils {
	private static AtomicInteger NOTIFYCATION_ID = new AtomicInteger(0);
	/**
	 * 后台设置的推送格式如下
	 * {"activity":{"className":"com.campus.xiaozhao.activity.UpdateActivity"，"arg0":"01","arg1":"02"},"info":{"title":"宣讲会","msg":"2015腾讯华中科大秋季宣讲会"}}
	 * @param context
	 * @param content
	 */
	public static void sendNotifycation(Context context, String content) {
		try {
			JSONObject object = new JSONObject(content);
			JSONObject info = object.getJSONObject("info");
			String title = info.getString("title");
			String msg = info.getString("msg");
			String activity = object.getJSONObject("activity").getString("className");
			NotificationManager nm = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			// 新建状态栏通知
			Notification baseNF = new Notification();

			// 设置通知在状态栏显示的图标
			baseNF.icon = R.drawable.ic_launcher;

			// 通知时在状态栏显示的内容
			baseNF.tickerText = "";

			// 通知的默认参数 DEFAULT_SOUND, DEFAULT_VIBRATE, DEFAULT_LIGHTS.
			// 如果要全部采用默认值, 用 DEFAULT_ALL.
			// 此处采用默认声音
			baseNF.defaults = Notification.DEFAULT_SOUND;
			baseNF.flags = Notification.FLAG_AUTO_CANCEL;
			Class<?> act = MainActivity.class;
			try {
				act = Class.forName(activity);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			Intent to = new Intent(context, act);
			PendingIntent launchIntent = PendingIntent.getActivity(context, 0,
					to, 0);
			// 第二个参数 ：下拉状态栏时显示的消息标题 expanded message title
			// 第三个参数：下拉状态栏时显示的消息内容 expanded message text
			// 第四个参数：点击该通知时执行页面跳转
			baseNF.setLatestEventInfo(context, title, msg, launchIntent);

			// 发出状态栏通知
			// The first parameter is the unique ID for the Notification
			// and the second is the Notification object.
			nm.cancel(NOTIFYCATION_ID.getAndIncrement());
			nm.notify(NOTIFYCATION_ID.get(), baseNF);
		} catch (JSONException e) {
			e.printStackTrace();
			return;
		}
	}
}
