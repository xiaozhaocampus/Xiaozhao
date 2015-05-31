package com.campus.xiaozhao.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.campus.xiaozhao.Configuration;
import com.campus.xiaozhao.R;
import com.campus.xiaozhao.basic.utils.CampusSharePreference;
import com.campus.xiaozhao.basic.widget.CountDownTimerView;
import com.campus.xiaozhao.basic.widget.CountDownTimerView.OnCountDownListener;
import com.component.logger.Logger;

public class VerifyNumberActivity extends Activity implements OnCountDownListener {
	
	public static final String TAG = "VerifyNumberActivity";
	private static final String KEY_PHONE_NUMBER = "phone_number";
	
	private TextView mNumberTextView;
	private EditText mVerifyCodeEditText;
	private CountDownTimerView mCountDownTimerView;
	
	private String mPhoneNumber;
	private String mVerifyCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verify_number);
		
		mPhoneNumber = getIntent().getStringExtra(KEY_PHONE_NUMBER);
		if (TextUtils.isEmpty(mPhoneNumber)) {
			Logger.e(TAG, "onCreate: invalid phone number: " + mPhoneNumber);
			return;
		}
		Logger.d(TAG, "onCreate: phone number: " + mPhoneNumber);
		
		ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        
        setTitle(R.string.label_verification_title);
        
        mNumberTextView = (TextView) findViewById(R.id.phone_number_tv);
        mNumberTextView.setText(mPhoneNumber);
        
        mCountDownTimerView = (CountDownTimerView) findViewById(R.id.request_verification_code);
        mCountDownTimerView.setOnCountDownListener(this);
        mCountDownTimerView.startCountDown(
                Configuration.VERIFICATION_WAIT_TIME,
                Configuration.COUNT_INTERVAL);
        
        mVerifyCodeEditText = (EditText) findViewById(R.id.verification_code_et);
        sendVerifyRequest();
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

	public static void startFrom(Context context, String phoneNumber) {
		Intent intent = new Intent(context, VerifyNumberActivity.class);
		intent.putExtra(KEY_PHONE_NUMBER, phoneNumber);
		context.startActivity(intent);
	}

	@Override
	public boolean onCountDownFinishState() {
		return true;
	}
	
	@Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
	        .setMessage(R.string.verification_quit_dialog_message)
	        .setNegativeButton(R.string.verification_quit_dialog_cancel, null)
	        .setPositiveButton(R.string.verification_quit_dialog_confirm, new OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                finish();
	            }
	        }).create().show();
    }
	
	public void clickOnGetVerificationCode(View view) {
        mCountDownTimerView.startCountDown(
                Configuration.VERIFICATION_WAIT_TIME,
                Configuration.COUNT_INTERVAL);
        
        sendVerifyRequest();
    }
	
	public void clickOnCommit(View view) {
		String input = mVerifyCodeEditText.getText().toString();
		if (TextUtils.isEmpty(input)) {
			Toast.makeText(this, R.string.toast_input_verification_code, Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (!TextUtils.equals(mVerifyCode, input)) {
            Toast.makeText(this, R.string.toast_verification_code_not_match, Toast.LENGTH_LONG).show();
            return;
        }
		
		sendRegisterRequest();
	}
	
	/**
	 * 向后台发送手机号验证请求
	 */
	private void sendVerifyRequest() {
		// TDDO: 发送手机号验证请求
	}
	
	/**
	 * 验证成功后，向后台发送注册请求
	 */
	private void sendRegisterRequest() {
		// TODO: 向后台发送新用户注册请求
		CampusSharePreference.setLogin(this, true);
		MainActivity.startFrom(this);
	}
}
