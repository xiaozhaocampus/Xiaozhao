package com.campus.xiaozhao.basic.utils;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;

import com.campus.xiaozhao.R;
import com.campus.xiaozhao.basic.data.CampusUser;
import com.campus.xiaozhao.basic.db.CampusUserDBProcessor;

public class LoginHelper {
	
	public static final String TAG = "LoginHelper";
	
	public interface LoginListener {
		public void onSuccess();
		public void onError(int errCode, String errMsg);
	}
	public static void login(final Context context, String username, final String password, final LoginListener listner) {
		final ProgressDialog dialog = ProgressDialog.show(context, null, context.getString(R.string.loading_login));
		BmobUser.loginByAccount(context, username, password, new LogInListener<BmobUser>() {
			@Override
			public void done(BmobUser user, BmobException ex) {
				dialog.dismiss();
				if (ex != null) {
					listner.onError(ex.getErrorCode(), ex.getLocalizedMessage());
					return;
				}
				//这里是登录成功再请求相关信息
				final String userName = user.getUsername();
				BmobQuery<CampusUser> qr = new BmobQuery<CampusUser>();
				qr.addWhereEqualTo("userName", userName);
				qr.findObjects(context, new FindListener<CampusUser>() {
					
					@Override
					public void onSuccess(List<CampusUser> paramList) {
						CampusUser campusUser = null;
						if(paramList == null || paramList.isEmpty()) {
							campusUser = new CampusUser();
							campusUser.setUserName(userName);
						} else {
							campusUser = paramList.get(0);
						}
						campusUser.setUserLoginTime(System.currentTimeMillis());
						CampusUserDBProcessor.saveToDB(context, campusUser, false);						
					}
					
					@Override
					public void onError(int paramInt, String paramString) {
						CampusUser	campusUser = new CampusUser();
						campusUser.setUserName(userName);
						CampusUserDBProcessor.saveToDB(context, campusUser, false);		
					}
				});
				listner.onSuccess();
			}
		});
	}

}
