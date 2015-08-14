package com.campus.xiaozhao.upload;

public class UploadType {
	private UploadType() {
		
	}
	public static final int UPLOAD_FROM_FILE = 0;
	public static final int UPLOAD_FROM_BMOBOBJ = 1;
	public static final int UPLOAD_FROM_BMOBOBJ_AND_FILE = 2;
	public static final int UPLOAD_FROM_DB = 3;
}
