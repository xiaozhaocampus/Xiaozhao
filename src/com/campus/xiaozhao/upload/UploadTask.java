package com.campus.xiaozhao.upload;

import android.net.Uri;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.SaveListener;

import com.bmob.btp.callback.UploadListener;

public class UploadTask {
	public static class UploadBaseTask {
		public int mUploadType;
	}
	
	public static class UploadFileTask extends UploadBaseTask{
		public String mUploadFilePath;
		public UploadListener mListener;
	}
	
	public static class UploadBmobObjTask extends UploadBaseTask{
		public BmobObject mUploadObj;
		public SaveListener mSaveListener;
	}
	
	public static class UploadFileAndBmobObjTask extends UploadBmobObjTask{
		public BmobObject mUploadObj;
		public String mUploadFilePath;
	}

	public static class UploadDBTask extends UploadBaseTask{
		public Uri mUri;
		public String mUploadFlag;//这个数据库字段标示未是否需要上传
		public SaveListener mListener;
		public UploadDbHelper mHelper;
	}
}
