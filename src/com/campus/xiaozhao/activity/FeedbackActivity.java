package com.campus.xiaozhao.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.campus.xiaozhao.R;
import com.component.logger.Logger;

public class FeedbackActivity extends Activity {
	private static final String TAG = "FeedbackActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
	}
	
	//返回
	public void back(View view){
		Logger.i(TAG, "Feedback back");
		finish();
	}
	
	//提交反馈
	public void submit(View view){
		Logger.i(TAG, "Feedback submit");
		Toast.makeText(this, "请实现提交功能", Toast.LENGTH_SHORT).show();
	}
}
