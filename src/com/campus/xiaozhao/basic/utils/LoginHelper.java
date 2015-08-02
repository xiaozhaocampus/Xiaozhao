package com.campus.xiaozhao.basic.utils;

import android.app.ProgressDialog;
import android.content.Context;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

import com.campus.xiaozhao.R;
import com.campus.xiaozhao.basic.data.CampusUser;

public class LoginHelper {
	
	public static final String TAG = "LoginHelper";
	
	public interface LoginListener {
		public void onSuccess();
		public void onError(int errCode, String errMsg);
	}
	public static void login(Context context, String username, String password, final LoginListener listner) {
		final ProgressDialog dialog = ProgressDialog.show(context, null, context.getString(R.string.loading_login));
		BmobUser.loginByAccount(context, username, password, new LogInListener<CampusUser>() {
			@Override
			public void done(CampusUser user, BmobException ex) {
				dialog.dismiss();
				if (ex != null) {
					listner.onError(ex.getErrorCode(), ex.getLocalizedMessage());
					return;
				}
				listner.onSuccess();
			}
		});
	}

}
