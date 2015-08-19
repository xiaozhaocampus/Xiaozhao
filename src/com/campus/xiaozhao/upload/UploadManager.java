package com.campus.xiaozhao.upload;

import java.io.File;

import android.content.Context;
import android.net.Uri;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.SaveListener;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.campus.xiaozhao.upload.UploadTask.UploadBaseTask;
import com.campus.xiaozhao.upload.UploadTask.UploadBmobObjTask;
import com.campus.xiaozhao.upload.UploadTask.UploadDBTask;
import com.campus.xiaozhao.upload.UploadTask.UploadFileAndBmobObjTask;
import com.campus.xiaozhao.upload.UploadTask.UploadFileTask;
import com.component.logger.Logger;

/**
 * 上传的接口类：支持文件,BmobObject和混合类型,以及数据库中数据的上传
 * @author antoniochen
 *
 */
public class UploadManager {
	private static final String TAG = "UploadManager";
	public UploadManager() {
	}
	
	/**
	 * 
	 * @param context
	 * @param obj
	 * @return
	 */
	private boolean doUpload(Context context, BmobObject obj, SaveListener listener) {
		if(obj == null) {
			return false;
		}
		obj.save(context, listener);
		return true;
	}
	
	/**
	 * 
	 * @param context
	 * @param filePath
	 * @return
	 */
	private boolean doUpload(Context context, String filePath, UploadListener listener){
		if(filePath == null || filePath.isEmpty() || !new File(filePath).exists()) {
			return false;
		}
		BmobProFile.getInstance(context).upload(filePath, listener);
		return true;
	}
	
	/**
	 * 
	 * @param context
	 * @param uri
	 * @param uploadFlagField
	 * @param uploadFileSrcField
	 * @return
	 */
	private boolean doUpload(Context context, Uri uri, String uploadFlagField, UploadDbHelper helper) {
		if(uri == null || helper == null || uploadFlagField == null || uploadFlagField.isEmpty()) {
			return false;
		}
		
		UploadBaseTask task = helper.fromUri(uri, uploadFlagField);
		if(task.mUploadType == UploadType.UPLOAD_FROM_DB) {
			Logger.e(TAG, "UPLOAD TYPE ERROR");
			return false;
		}
		//TODO:
		upload(context, task);
		return true;
	}
	
	/**
	 * 
	 * @param context
	 * @param task
	 */
	public void upload(final Context context, final UploadBaseTask task) {
		if(task == null || context == null) {
			Logger.e(TAG, "Upload Nothing || context is null");
			return;
		}
		
		switch (task.mUploadType) {
		case UploadType.UPLOAD_FROM_DB:
			UploadDBTask dbTask = (UploadDBTask)task;
			doUpload(context, dbTask.mUri, dbTask.mUploadFlag, dbTask.mHelper);
			break;
		case UploadType.UPLOAD_FROM_FILE:
			UploadFileTask fileTask = (UploadFileTask)task;
			doUpload(context, fileTask.mUploadFilePath, fileTask.mListener);
			break;
		case UploadType.UPLOAD_FROM_BMOBOBJ:
			UploadBmobObjTask bmobObjTask = (UploadBmobObjTask)task;
			doUpload(context, bmobObjTask.mUploadObj, bmobObjTask.mSaveListener);
			break;
		case UploadType.UPLOAD_FROM_BMOBOBJ_AND_FILE:
			//有文件的情况先上传文件
			final UploadFileAndBmobObjTask bmobObjAndFileTask = (UploadFileAndBmobObjTask)task;
			if(bmobObjAndFileTask.mUploadFilePath == null 
					&& bmobObjAndFileTask.mUploadFilePath.trim().isEmpty() 
					&& new File(bmobObjAndFileTask.mUploadFilePath).exists()) {
				doUpload(context, bmobObjAndFileTask.mUploadFilePath, new UploadListener() {
					
					@Override
					public void onError(int paramInt, String paramString) {
						Logger.e(TAG, "Upload File Fail: " + paramInt);
					}
					
					@Override
					public void onSuccess(String paramString1, String paramString2) {
						Logger.e(TAG, "Upload File Success: " + paramString1);
						doUpload(context, bmobObjAndFileTask.mUploadObj, bmobObjAndFileTask.mSaveListener);
					}
					
					@Override
					public void onProgress(int paramInt) {
						
					}
				});
			} else {
				doUpload(context, bmobObjAndFileTask.mUploadObj, bmobObjAndFileTask.mSaveListener);
			}
			break;
		default:
			break;
		}
	}
}
