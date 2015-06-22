package com.campus.xiaozhao.activity;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.Header;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.campus.xiaozhao.R;
import com.campus.xiaozhao.XZApplication;
import com.campus.xiaozhao.basic.utils.ApplicationInfo;
import com.campus.xiaozhao.basic.utils.AutoInstall;
import com.campus.xiaozhao.basic.utils.FileUtils;
import com.campus.xiaozhao.model.UpdateInfo;
import com.component.logger.Logger;
import com.loopj.android.http.BinaryHttpResponseHandler;

public class UpdateActivity extends Activity {
	private static final String TAG = "UpdateActivity";
	private String mDownloadUrl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);
		Logger.i(TAG, "Start Query Update");
		
		BmobQuery<UpdateInfo> query = new BmobQuery<UpdateInfo>();

		query.addWhereGreaterThan("lastVersionCode",Integer.valueOf(ApplicationInfo.getVersionCode(this)*10000));

		query.findObjects(this, mUpdateInfoListener);
		
	}
	
	private FindListener<UpdateInfo> mUpdateInfoListener = new FindListener<UpdateInfo>() {

		@Override
		public void onError(int error, String errorString) {
			Logger.i(TAG, "Error:" + errorString);
			Toast.makeText(UpdateActivity.this, R.string.check_update_failed, Toast.LENGTH_SHORT).show();
			finish();
		}

		@Override
		public void onSuccess(List<UpdateInfo> update) {
			if(update == null || update.isEmpty()) {
				Toast.makeText(UpdateActivity.this, R.string.update_no_new_version, Toast.LENGTH_SHORT).show();
				finish();
			}
			Collections.sort(update, new Comparator<UpdateInfo>() {

				@Override
				public int compare(UpdateInfo update0, UpdateInfo update1) {
					if(update0.getLastVersionCode() < update1.getLastVersionCode()) {
						return 1;
					}
					return -1;
				}
				
			});
			mDownloadUrl = update.get(0).getLastDownloadAdr();
			Logger.i(TAG, "OK:" + update.get(0).getLastVersionCode() + " Tips:" + update.get(0).getUpdateTips());
			findViewById(R.id.progress).setVisibility(View.GONE);
			findViewById(R.id.update_info).setVisibility(View.VISIBLE);
			TextView tips = (TextView)findViewById(R.id.update_tips);
			String tipContent = "";
			for(UpdateInfo up:update) {
				tipContent += "版本" + up.getLastVersionCode()/10000.0f + " ：\n";
				tipContent += "  " + up.getUpdateTips();
				tipContent += "\n";
			}
			tips.setText(tipContent);
		}
	};
	
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.download:
			download();
			Toast.makeText(UpdateActivity.this, R.string.downloading_tips, Toast.LENGTH_SHORT).show();
			break;
		case R.id.cancel:
			break;
		default:
			break;
		}
		finish();
	}
	
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
				File dstDir = FileUtils.getExternalStorageAppFilesDirectory(UpdateActivity.this.getPackageName());
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
				AutoInstall.install(UpdateActivity.this, dst);
			}
		});
	}
	
}
