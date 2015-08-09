package com.campus.xiaozhao.basic.utils;

import android.app.Activity;
import android.content.Intent;

public class PicChooseUtils {
	private PicChooseUtils() {
		
	}
	
	public static void choosePic(Activity context) {
		Intent intent = new Intent();
		/* 开启Pictures画面Type设定为image */
		intent.setType("image/*");
		/* 使用Intent.ACTION_GET_CONTENT这个Action */
		intent.setAction(Intent.ACTION_GET_CONTENT);
		/* 取得相片后返回本画面 */
		context.startActivityForResult(intent, 1);
	}
}
