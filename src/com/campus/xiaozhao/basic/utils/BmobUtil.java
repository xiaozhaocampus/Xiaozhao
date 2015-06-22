package com.campus.xiaozhao.basic.utils;

import java.util.List;

import com.campus.xiaozhao.Configuration;
import com.campus.xiaozhao.R;

import android.content.Context;
import android.text.TextUtils;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

public class BmobUtil {
	
	public static final String TAG = "BmobUtil";

	public interface QueryUserListener {
		public void findResult(boolean exist);
		public void onError(int code, String msg);
	}
	public static void queryUser(Context context, final String username, final QueryUserListener listener) {
		assert (username != null);
		assert (listener != null);
		// 检测用户是否已存在
		BmobQuery<BmobUser> query = new BmobQuery<BmobUser>();
		query.addWhereEqualTo("username", username);
		query.findObjects(context, new FindListener<BmobUser>() {
			@Override
			public void onSuccess(List<BmobUser> userList) {
				for (BmobUser user : userList) {
					if (TextUtils.equals(user.getUsername(), username)) {
						listener.findResult(true);
						return;
					}
				}
				listener.findResult(false);
			}

			@Override
			public void onError(int code, String msg) {
				listener.onError(code, msg);
			}
		});
	}
	
	public static String getSmsCode(Context context, String message) {
		assert (context != null);
		if (TextUtils.isEmpty(message)) {
			return null;
		}
		String smsSnippetTemplate = context.getString(R.string.sms_snippet_template_1);
		if (message.contains(smsSnippetTemplate)) {
			int start = message.indexOf(smsSnippetTemplate) + smsSnippetTemplate.length();
			String smsCode = message.substring(start, start + Configuration.SMS_CODE_LENGTH);
			return smsCode;
		}
		return null;
	}
}
