package com.campus.xiaozhao.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.campus.xiaozhao.R;
import com.campus.xiaozhao.activity.LoginActivity.UIType;

public class RegisterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	}

	public static void startFrom(Context context) {
		LoginActivity.startFrom(context, UIType.Register);
	}
}
