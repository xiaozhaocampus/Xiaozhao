package com.campus.xiaozhao.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import cn.bmob.push.PushConstants;

import com.campus.xiaozhao.basic.utils.NotifycationUtils;
import com.component.logger.Logger;

public class PushMsgReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
			String content = intent
					.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
			Logger.d("bmob", "客户端收到推送内容：" + content);
			NotifycationUtils.sendNotifycation(context, content);
		}
	}

}
