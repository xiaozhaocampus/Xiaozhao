package com.campus.xiaozhao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import com.campus.xiaozhao.Environment;
import com.campus.xiaozhao.R;
import com.campus.xiaozhao.basic.utils.CampusSharePreference;
import com.component.logger.Logger;

public class MainActivity extends FragmentActivity{
	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (Environment.ENABLE_SETUP_ACTIVITY) {
            if (!CampusSharePreference.isLogin(this)) {
	            SetupActivity.startFrom(this);
	            finish();
	            return;
            }
        }

		new Handler().post(new Runnable() {
			@Override
			public void run() {
				setupUI();
			}
		});
	}

	public static void startFrom(Context context) {
    	Intent intent = new Intent(context, MainActivity.class);
    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    		.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    		.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	context.startActivity(intent);
    }

    private void setupUI() {
		setContentView(R.layout.activity_main);
		Logger.d(TAG, "setup UI");
    }

	// TODO:处理各个点击事件
	public void onClick(View v) {
		Logger.i(TAG, "===Click Id ==>" + v.getId());
		int id = v.getId();
		switch (v.getId()) {
		case R.id.more_alarms:
			break;
		case R.id.more_feedback:
			break;
		}
		startActivityById(id);
	}

	private void startActivityById(int id) {
		Class<?> classes = null; 
		switch (id) {
		case R.id.more_feedback:
			classes = FeedbackActivity.class;
			break;
		case R.id.more_alarms:
			classes = AlarmActivity.class;
			break;			
		default:
			break;
		}
		
		if(classes != null) {
			Intent intent = new Intent(this, classes);
			startActivity(intent);
		}
	}

}
