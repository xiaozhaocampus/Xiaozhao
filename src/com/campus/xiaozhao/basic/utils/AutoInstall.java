package com.campus.xiaozhao.basic.utils;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class AutoInstall {

	/**
	 * 安装
	 * 
	 * @param context
	 *            接收外部传进来的context
	 */
	public static void install(Context context, String url) {
		// 核心是下面几句代码
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(url)),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}
}