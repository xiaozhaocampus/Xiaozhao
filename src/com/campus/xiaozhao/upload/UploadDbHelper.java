package com.campus.xiaozhao.upload;

import android.net.Uri;

import com.campus.xiaozhao.upload.UploadTask.UploadBaseTask;

public interface UploadDbHelper {
	public UploadBaseTask fromUri(Uri uri, String uploadFlagField);
	public boolean changeUploadStatus(Uri uri, String uploadFlagField, int status);
}
