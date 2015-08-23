package com.campus.xiaozhao.basic.widget;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.campus.xiaozhao.R;
import com.campus.xiaozhao.XZApplication;
import com.campus.xiaozhao.activity.UpdateDialog;
import com.campus.xiaozhao.basic.utils.ApplicationInfo;
import com.campus.xiaozhao.basic.utils.AutoInstall;
import com.campus.xiaozhao.basic.utils.CampusSharePreference;
import com.campus.xiaozhao.basic.utils.FileUtils;
import com.campus.xiaozhao.eventbus.EventBus;
import com.campus.xiaozhao.model.UpdateInfo;
import com.component.logger.Logger;
import com.loopj.android.http.BinaryHttpResponseHandler;

/**
 * 
 * @author antoniochen
 *
 */
public class Updater {
	private static final String TAG = "Updater";
	private static final long ONE_DAY_MILLIS = 24*60*60*1000;
	public WeakReference<Activity> mContext;
	public String mDownloadUrl = "";
	private UpdateDialog mUpdateDialog;
	private boolean mIsChecking;
	private Timer mTimer;
	public Updater(Activity context) {
		mContext = new WeakReference<Activity>(context);
	}
	
	public void checkUpdate(boolean auto) {
		
		mIsChecking = true;
		final Activity ac = mContext.get();
		if(ac == null) {
			Logger.e(TAG, "Context is null");
			return;
		}
		
		if(auto) {
			long interval = System.currentTimeMillis() - CampusSharePreference.getLastAutoCheckUpdateTime(ac);
			if(interval < ONE_DAY_MILLIS) {
				Logger.i(TAG, "AUTO CHECK TIME IS TOO SHORT");
				return;
			}
			CampusSharePreference.setLastAutoCheckUpdateTime(ac, System.currentTimeMillis());
		}
		
		
		if(mUpdateDialog == null || !mUpdateDialog.isShowing()) {
			mUpdateDialog = new UpdateDialog(ac);
			mUpdateDialog.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int id) {
					if(id == R.id.ok || id == R.id.cancel) {
						dialog.dismiss();
						if(id == R.id.ok) {
							Toast.makeText(ac, R.string.downloading_tips, Toast.LENGTH_SHORT).show();
							download();
						}
					}
				}
			});
		}
		mUpdateDialog.show();
		
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				EventBus.runOnUIThread(new Runnable() {
					
					@Override
					public void run() {
						timeIsUp();
					}
				});
			}
		}, 10000);
		
		BmobQuery<UpdateInfo> query = new BmobQuery<UpdateInfo>();

		query.addWhereGreaterThan("lastVersionCode",Integer.valueOf(ApplicationInfo.getVersionCode(ac)*10000));

		query.findObjects(ac, mUpdateInfoListener);
	}
	
	/**
	 * 检查更新的回调
	 */
	private FindListener<UpdateInfo> mUpdateInfoListener = new FindListener<UpdateInfo>() {

		@Override
		public void onError(int error, String errorString) {
			Logger.i(TAG, "Error:" + errorString);
			Activity ac = mContext.get();
			if(ac == null) {
				Logger.e(TAG, "Context is null");
				return;
			}
			Toast.makeText(ac, R.string.check_update_failed, Toast.LENGTH_SHORT).show();
			mIsChecking = false;
			stopTimer(true);
		}

		@Override
		public void onSuccess(List<UpdateInfo> update) {
			stopTimer(false);
			if(mIsChecking == false) {
				Logger.e(TAG, "Is Not Checking Now");
				return;
			}
			
			Activity ac = mContext.get();
			if(ac == null) {
				Logger.e(TAG, "Context is null");
				return;
			}
			
			if(update == null || update.isEmpty()) {
				Toast.makeText(ac, R.string.update_no_new_version, Toast.LENGTH_SHORT).show();
				return;
			}
			//更新排序
			Collections.sort(update, new Comparator<UpdateInfo>() {

				@Override
				public int compare(UpdateInfo update0, UpdateInfo update1) {
					if(update0.getLastVersionCode() < update1.getLastVersionCode()) {
						return 1;
					}
					return -1;
				}
				
			});
			UpdateInfo info = update.get(0); 
			mDownloadUrl = info.getLastDownloadAdr();
			String newVersion = ac.getString(R.string.update_new_version);
			StringBuilder tipContent = new StringBuilder(newVersion);
			tipContent.append(info.getLastVersionCode()/10000.0f).append("：\n");
			int i = 0;
			for(UpdateInfo up:update) {
				String[] tips = up.getUpdateTips().split("\\|");
				for(String tip:tips) {
					tipContent.append(++i).append(".").append(tip).append("\n");
				}
			}
			if(mUpdateDialog != null && mUpdateDialog.isShowing()) {
				mUpdateDialog.stopLoading(tipContent.toString());
			}
			mIsChecking = false;
		}
	};
	
	/**
	 * 下载
	 */
	private void download() {
		XZApplication.getInstance().getHttpEngine().httpGet(mDownloadUrl, null, null, new BinaryHttpResponseHandler(
				){
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] binaryData, Throwable error) {
				super.onFailure(statusCode, headers, binaryData, error);
			}
			
			@Override
			public void onSuccess(byte[] binaryData) {
				super.onSuccess(binaryData);
				ByteArrayInputStream input = new ByteArrayInputStream(binaryData);
				File dstDir = FileUtils.getExternalStorageAppFilesDirectory(XZApplication.getInstance().getPackageName());
				if(dstDir == null) {
					return;
				}
				
				if(!dstDir.exists()) {
					dstDir.mkdirs();
				}
				
				String dst = dstDir.getAbsolutePath() + "/temp.apk";
				FileOutputStream output;
				try {
					output = new FileOutputStream(dst);
					FileUtils.copyStream(input, output, true, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				Activity ac = mContext.get();
				if(ac == null) {
					Logger.e(TAG, "Context is null");
					return;
				}
				AutoInstall.install(ac, dst);
			}
		});
	}
	
	private void stopTimer(boolean close) {
		if(mTimer == null) {
			return;
		}
		
		mTimer.cancel();
		mTimer = null;
		if(close && mUpdateDialog != null && mUpdateDialog.isShowing()) {
			mUpdateDialog.dismiss();
		}
	}
	
	private void timeIsUp() {
		final Activity ac = mContext.get();
		if(ac == null) {
			Logger.e(TAG, "Context is null");
			return;
		}
		
		if(mIsChecking) {
			mIsChecking = false;
			Toast.makeText(ac, R.string.check_update_failed, Toast.LENGTH_SHORT).show();
			if(mUpdateDialog != null && mUpdateDialog.isShowing()) {
				mUpdateDialog.dismiss();
				mUpdateDialog = null;
			}
		}
	}
}
