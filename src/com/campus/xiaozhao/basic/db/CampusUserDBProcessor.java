package com.campus.xiaozhao.basic.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.campus.xiaozhao.basic.data.CampusUser;
import com.component.logger.Logger;

/**
 * 
 * @author antoniochen
 *
 */
public class CampusUserDBProcessor {
	private static final String TAG = "CampusUserDBProcessor";
	private CampusUserDBProcessor() {
		
	}
	public static final String[] CAMPUS_USER_INFO = {
			CampusModel.CampusUserColumn.USER_ID,
			CampusModel.CampusUserColumn.USER_NAME,
			CampusModel.CampusUserColumn.USER_PHOTO,
			CampusModel.CampusUserColumn.USER_NICKNAME,
			CampusModel.CampusUserColumn.USER_GENDER,
			CampusModel.CampusUserColumn.USER_PHONE_NUM,
			CampusModel.CampusUserColumn.USER_EMAIL,
			CampusModel.CampusUserColumn.USER_SCHOOL,
			CampusModel.CampusUserColumn.USER_MAJOR,
			CampusModel.CampusUserColumn.USER_CLASS,
			CampusModel.CampusUserColumn.USER_DATA_STATUS,
			CampusModel.CampusUserColumn.USER_LOGIN_TIME
	};
	
	/**
	 * 取user信息
	 * @param context
	 * @param needUpload
	 * @return
	 */
	public static CampusUser fromDB(Context context, boolean needUpload) {
		if(context == null) {
			return null;
		}
		
		ContentResolver cr = context.getContentResolver();
		Cursor cur = cr.query(CampusUriFactory.getCampusUserInfoUri(), CAMPUS_USER_INFO, needUpload ? CampusModel.CampusUserColumn.USER_DATA_STATUS + " =? " : null, needUpload ? new String[]{String.valueOf(1)} : null, null);//CampusModel.CampusUserColumn.USER_LOGIN_TIME);
		if(cur == null || !cur.moveToFirst()) {
			return null;
		}
		CampusUser user = new CampusUser();
		user.setObjectId(cur.getString(cur.getColumnIndex(CampusModel.CampusUserColumn.USER_ID)));
		user.setUserName(cur.getString(cur.getColumnIndex(CampusModel.CampusUserColumn.USER_NAME)));
		user.setUserPhoto(cur.getString(cur.getColumnIndex(CampusModel.CampusUserColumn.USER_PHOTO)));
		user.setUserNickName(cur.getString(cur.getColumnIndex(CampusModel.CampusUserColumn.USER_NICKNAME)));
		user.setUserGender(cur.getInt(cur.getColumnIndex(CampusModel.CampusUserColumn.USER_GENDER)));
		//
		user.setUserPhoneNum(cur.getString(cur.getColumnIndex(CampusModel.CampusUserColumn.USER_PHONE_NUM)));
		user.setUserEmail(cur.getString(cur.getColumnIndex(CampusModel.CampusUserColumn.USER_EMAIL)));
		
		user.setUserShcool(cur.getString(cur.getColumnIndex(CampusModel.CampusUserColumn.USER_SCHOOL)));
		user.setUserMajor(cur.getString(cur.getColumnIndex(CampusModel.CampusUserColumn.USER_MAJOR)));
		user.setUserClass(cur.getString(cur.getColumnIndex(CampusModel.CampusUserColumn.USER_CLASS)));
		user.setUserLoginTime(cur.getLong(cur.getColumnIndex(CampusModel.CampusUserColumn.USER_LOGIN_TIME)));
		return user;
	}
	
	/**
	 * 保存到本地数据库
	 * @param context
	 * @param needUpload 标识是否需要上传后台
	 */
	public static void saveToDB(Context context, CampusUser user,boolean needUpload) {
		ContentResolver cr = context.getContentResolver();
		ContentValues cv = new ContentValues();
		cv.put(CampusModel.CampusUserColumn.USER_ID, user.getObjectId());
		cv.put(CampusModel.CampusUserColumn.USER_NAME, user.getUserName());
		cv.put(CampusModel.CampusUserColumn.USER_PHOTO, user.getUserPhoto());
		cv.put(CampusModel.CampusUserColumn.USER_NICKNAME, user.getUserNickName());
		cv.put(CampusModel.CampusUserColumn.USER_GENDER, user.getUserGender());
		cv.put(CampusModel.CampusUserColumn.USER_PHONE_NUM, user.getUserPhoneNum());
		cv.put(CampusModel.CampusUserColumn.USER_EMAIL, user.getUserEmail());
		cv.put(CampusModel.CampusUserColumn.USER_SCHOOL, user.getUserShcool());
		cv.put(CampusModel.CampusUserColumn.USER_MAJOR, user.getUserMajor());
		cv.put(CampusModel.CampusUserColumn.USER_CLASS, user.getUserClass());
		cv.put(CampusModel.CampusUserColumn.USER_DATA_STATUS, needUpload ? 1 : 0);
		int res = cr.update(CampusUriFactory.getCampusUserInfoUri(), cv, CampusModel.CampusUserColumn.USER_ID + " =? ", new String[]{user.getObjectId()});
		if(res == 0) {
			 cr.insert(CampusUriFactory.getCampusUserInfoUri(), cv);
		}
		Logger.i(TAG, "update res = " + res);
	}
	
	/**
	 * 保存到服务端
	 * @param context
	 * @param insertListener
	 */
	public static void saveToServer(final Context context, final CampusUser user, final SaveListener insertListener) {
		if(user == null) {
			return;
		}
		//先保存到本地
		saveToDB(context,user, true);
		String objId = user.getObjectId();
		if(objId == null || objId.isEmpty()) {
			user.save(context, new SaveListener() {
				
				@Override
				public void onSuccess() {
					//上传成功了就修改数据库
					saveToDB(context, user, false);
					if(insertListener != null) {
						insertListener.onSuccess();
					}
				}
				
				@Override
				public void onFailure(int paramInt, String paramString) {
					Logger.e(TAG, "Error : " + paramString);
					if(insertListener != null) {
						insertListener.onFailure(paramInt, paramString);
					}
				}
			});
		} else {
			user.update(context, new UpdateListener() {
				
				@Override
				public void onSuccess() {
					//上传成功了就修改数据库
					saveToDB(context, user, false);
					if(insertListener != null) {
						insertListener.onSuccess();
					}
				}
				
				@Override
				public void onFailure(int paramInt, String paramString) {
					Logger.e(TAG, "Error : " + paramString);
					if(insertListener != null) {
						insertListener.onFailure(paramInt, paramString);
					}
				}
			});
		}
	}
}
