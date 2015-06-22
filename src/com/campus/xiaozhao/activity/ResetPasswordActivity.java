package com.campus.xiaozhao.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.ResetPasswordByCodeListener;

import com.campus.xiaozhao.Configuration;
import com.campus.xiaozhao.R;
import com.campus.xiaozhao.basic.data.CampusUser;
import com.campus.xiaozhao.basic.utils.BmobUtil;
import com.campus.xiaozhao.basic.utils.BmobUtil.QueryUserListener;
import com.campus.xiaozhao.basic.utils.CampusSharePreference;
import com.campus.xiaozhao.basic.utils.NumberUtils;
import com.campus.xiaozhao.basic.widget.CountDownTimerView;
import com.campus.xiaozhao.sms.CloudSms;
import com.campus.xiaozhao.sms.CloudSmsManager;
import com.campus.xiaozhao.sms.CloudSmsThread;
import com.campus.xiaozhao.sms.CloudSmsManager.SmsObserver;
import com.component.logger.Logger;

public class ResetPasswordActivity extends Activity {

    public static final String TAG = "ResetPasswordActivity";
    private static final int SMS_USER_CODE = 1001;
    private CountDownTimerView mCountDownTimerView;
    private EditText mPhoneNumberEditText;
    private EditText mVerifyCodeEditText;
    private EditText mNewPwdEditText;
    private EditText mRepeatPwdEditText;
    private CloudSmsManager mSmsManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        
        setTitle(R.string.label_forgot_password);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        
        mVerifyCodeEditText = (EditText) findViewById(R.id.verification_code_et);
        mCountDownTimerView = (CountDownTimerView) findViewById(R.id.request_verification_code);
        mCountDownTimerView.setText(R.string.get_verification_code);
        mPhoneNumberEditText = (EditText) findViewById(R.id.phone_number_et);
        mPhoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String number = mPhoneNumberEditText.getText().toString();
                if (number.length() == 0) {
                    mCountDownTimerView.setEnabled(false);
                } else if (!mCountDownTimerView.isEnabled()) {
                    mCountDownTimerView.setEnabled(true);
                }
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }
            
            @Override
            public void afterTextChanged(Editable s) {
                
            }
        });
        mNewPwdEditText = (EditText) findViewById(R.id.new_password_et);
        mRepeatPwdEditText = (EditText) findViewById(R.id.repeat_new_password_et);
        mSmsManager = new CloudSmsManager(getApplicationContext());
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    public static void startFrom(Context context) {
        Intent intent = new Intent(context, ResetPasswordActivity.class);
        context.startActivity(intent);
    }
    
    public void clickOnGetVerificationCode(View view) {
        final String number = mPhoneNumberEditText.getText().toString();
        if (number.length() == 0) {
            Toast.makeText(this, R.string.reset_password_edit_hint_phone_number, Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (!NumberUtils.isPhoneNumberValid(number)) {
            Toast.makeText(this, R.string.toast_invalid_phone_numbers, Toast.LENGTH_SHORT).show();
            return;
        }
        
        final ProgressDialog dialog = ProgressDialog.show(this, null, getString(R.string.loading_verify));
        BmobUtil.queryUser(this, number, new QueryUserListener() {
			@Override
			public void onError(int code, String msg) {
				dialog.dismiss();
				Logger.e(TAG, "queryUser failed: code=" + code + ", msg=" + msg);
			}
			
			@Override
			public void findResult(boolean exist) {
				dialog.dismiss();
				if (!exist) {
					Logger.w(TAG, "queryUser: user( " + number + ") not exist");
					toast(getString(R.string.toast_reset_password_user_not_exist));
					return;
				}
				mCountDownTimerView.startCountDown(
		                Configuration.VERIFICATION_WAIT_TIME,
		                Configuration.COUNT_INTERVAL);
		        mVerifyCodeEditText.requestFocus();
				requestVerifyCode();
			}
		});
    }
    
    public void clickOnCommit(View view) {
        String number = mPhoneNumberEditText.getText().toString();
        String verifyCode = mVerifyCodeEditText.getText().toString();
        String newPassword = mNewPwdEditText.getText().toString();
        String repeatPassword = mRepeatPwdEditText.getText().toString();
        
        if (number.length() == 0) {
            Toast.makeText(this, R.string.toast_input_phone_number, Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (verifyCode.length() == 0) {
            Toast.makeText(this, R.string.toast_input_verification_code, Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (newPassword.length() == 0) {
            Toast.makeText(this, R.string.toast_input_password, Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (newPassword.length() < Configuration.PASSWORD_LENGTH_MIN_LIMIT) {
            String text = String.format(getString(R.string.toast_password_too_short),
                    Configuration.PASSWORD_LENGTH_MIN_LIMIT);
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (repeatPassword.length() == 0) {
            Toast.makeText(this, R.string.toast_repeat_input_password, Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (!TextUtils.equals(newPassword, repeatPassword)) {
            Toast.makeText(this, R.string.toast_passwords_not_match, Toast.LENGTH_SHORT).show();
            return;
        }
        
        commitResetPassword();
    }
    
    /**
	 * 向后台发送手机号验证请求
	 */
    private void requestVerifyCode() {
    	final String number = mPhoneNumberEditText.getText().toString();
        final String template = Configuration.SMS_VERIFY_TEMPLATE;
        BmobSMS.requestSMSCode(this, number, template, new RequestSMSCodeListener() {
            @Override
            public void done(Integer smsId, BmobException ex) {
                if (ex != null) {
                	Logger.e(TAG, "requestSMSCode failed: code=" + ex.getErrorCode() + ", msg=" + ex.getLocalizedMessage());
                	toast(getString(R.string.toast_send_verification_error));
                	return;
                }
                startSmsObserver();
            }
        });
    }
    
    private void startSmsObserver() {
		mSmsManager.addSmsObserver(SMS_USER_CODE, new SmsObserver() {
			@Override
			public void onNewThread(int userCode, CloudSmsThread thread) {
				Logger.d(TAG, "onNewThread: date= " + thread.getDate()
						+ ", address=" + thread.getNumberList() + ", snippet=" + thread.getSnippet());
				String smsCode = BmobUtil.getSmsCode(getApplicationContext(), thread.getSnippet());
				if (!TextUtils.isEmpty(smsCode)) {
					if (!isDestroyed()) {
						mVerifyCodeEditText.setText(smsCode);
					}
				}
			}
			
			@Override
			public void onNewSms(int userCode, CloudSms sms) {
				Logger.d(TAG, "onNewSms: date=" + sms.getDate() + ", address="
						+ sms.getAddress() + ", body=" + sms.getBody());
				String smsCode = BmobUtil.getSmsCode(getApplicationContext(), sms.getBody());
				if (!TextUtils.isEmpty(smsCode)) {
					if (!isDestroyed()) {
						mVerifyCodeEditText.setText(smsCode);
					}
				}
			}
		});
	}
    
    private void commitResetPassword() {
    	final ProgressDialog dialog = ProgressDialog.show(this, null, getString(R.string.loading_reset));
    	
    	final String number = mPhoneNumberEditText.getText().toString();
    	final String smsCode = mVerifyCodeEditText.getText().toString();
    	final String newPassword = mNewPwdEditText.getText().toString();
    	BmobUser.resetPasswordBySMSCode(this, smsCode, newPassword, new ResetPasswordByCodeListener() {
    	    @Override
    	    public void done(BmobException ex) {
    	    	dialog.dismiss();
    	        if (ex != null) {
    	        	Logger.e(TAG, "resetPasswordBySMSCode faield: code=" + ex.getErrorCode()
    	        			+ ", msg=" + ex.getLocalizedMessage());
    	        	toast(getString(R.string.toast_verification_code_not_match));
    	        	return;
    	        }
    	        login(number, newPassword);
    	    }
    	});
    }
    
    private void login(String number, String pwd) {
		BmobUser.loginByAccount(this, number, pwd, new LogInListener<CampusUser>() {
			@Override
			public void done(CampusUser user, BmobException ex) {
				if (ex != null) {
					Logger.e(TAG, "loginByAccount failed: code=" + ex.getErrorCode()
							+ ", msg=" + ex.getLocalizedMessage());
					toast(getString(R.string.toast_login_failed) + ": " + ex.getLocalizedMessage());
					return;
				}
				toast(getString(R.string.toast_reset_password_success));
				CampusSharePreference.setLogin(ResetPasswordActivity.this, true);
				MainActivity.startFrom(ResetPasswordActivity.this);
			}
		});
	}
    
    private void toast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}
}
