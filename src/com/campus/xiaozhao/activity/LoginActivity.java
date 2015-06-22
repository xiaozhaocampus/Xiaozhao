package com.campus.xiaozhao.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

import com.campus.xiaozhao.Configuration;
import com.campus.xiaozhao.R;
import com.campus.xiaozhao.basic.data.CampusUser;
import com.campus.xiaozhao.basic.utils.BmobUtil;
import com.campus.xiaozhao.basic.utils.CampusSharePreference;
import com.campus.xiaozhao.basic.utils.NumberUtils;
import com.component.logger.Logger;

public class LoginActivity extends Activity {

	public static final String TAG = "LoginActivity";
	private static final String UITypeString = "UIType";
	public enum UIType {
		Login, Register
	}
	private UIType mType;
	private EditText mPhoneEditText;
	private EditText mPwdEditText;
	private TextView mForgotPwdTextView;
	private Button mAuthButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		Bundle data = getIntent().getExtras();
        if (data == null) {
            Logger.e(TAG, "onCreate: bundle data is null");
            return;
        }
        
        mType = UIType.values()[data.getInt(UITypeString, UIType.Register.ordinal())];
        Logger.d(TAG, "onCreate: mType=" + mType);
        
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        
        init();
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
		startFrom(context, UIType.Login);
	}
	
	public static void startFrom(Context context, UIType type) {
		Bundle data = new Bundle();
        data.putInt(UITypeString, type.ordinal());
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtras(data);
        context.startActivity(intent);
	}
	
	private void init() {
		mAuthButton = (Button) findViewById(R.id.auth_btn);
		mPhoneEditText = (EditText) findViewById(R.id.phone_number_et);
		mPwdEditText = (EditText) findViewById(R.id.password_et);
		mForgotPwdTextView = (TextView) findViewById(R.id.forgot_password_tv);
		if (mType == UIType.Login) {
			setTitle(R.string.login);
            mAuthButton.setText(R.string.login);
            mForgotPwdTextView.setVisibility(View.VISIBLE);
            mForgotPwdTextView.setPaintFlags(mForgotPwdTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		} else if (mType == UIType.Register) {
			setTitle(R.string.register);
            mAuthButton.setText(R.string.next_step);
            mForgotPwdTextView.setVisibility(View.GONE);
		}
	}
	
	public void clickOnForgotPassword(View view) {
	    if (mType == UIType.Register) {
	        Logger.d(TAG, "clickOnForgotPassword: should not come here");
	        return;
	    }
	    ResetPasswordActivity.startFrom(this);
	}
	
	public void onClickAuth(View view) {
		if (!checkInput()) {
			Logger.w(TAG, "checkInput failed");
			return;
		}
		
		String number = mPhoneEditText.getText().toString();
		String pwd = mPwdEditText.getText().toString();
		if (mType == UIType.Login) {
			login(number, pwd);
		} else if (mType == UIType.Register) {
			verifyNumber(number, pwd);
		}
	}
	
	private boolean checkInput() {
		String number = mPhoneEditText.getText().toString();
		String pwd = mPwdEditText.getText().toString();
		if (TextUtils.isEmpty(number)) {
			Toast.makeText(this, R.string.toast_input_phone_number, Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if (TextUtils.isEmpty(pwd)) {
			Toast.makeText(this, R.string.toast_input_password, Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if (!NumberUtils.isPhoneNumberValid(number)) {
            Toast.makeText(this, R.string.toast_invalid_phone_numbers, Toast.LENGTH_SHORT).show();
            return false;
        }
		
		if (pwd.length() < Configuration.PASSWORD_LENGTH_MIN_LIMIT) {
			String text = String.format(getString(R.string.toast_password_too_short),
					Configuration.PASSWORD_LENGTH_MIN_LIMIT);
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            return false;
        }
		
		return true;
	}
	
	/**
	 * 网络校验
	 * @param number
	 * @param pwd
	 */
	private void login(String number, String pwd) {
		final ProgressDialog dialog = ProgressDialog.show(this, null, getString(R.string.loading_login));
		
		BmobUser.loginByAccount(this, number, pwd, new LogInListener<CampusUser>() {
			@Override
			public void done(CampusUser user, BmobException ex) {
				dialog.dismiss();
				if (ex != null) {
					Logger.e(TAG, "loginByAccount failed: code=" + ex.getErrorCode()
							+ ", msg=" + ex.getLocalizedMessage());
					toast(getString(R.string.toast_login_failed) + ": " + ex.getLocalizedMessage());
					return;
				}
				toast(getString(R.string.toast_welcome_back));
				CampusSharePreference.setLogin(LoginActivity.this, true);
				MainActivity.startFrom(LoginActivity.this);
			}
		});
	}
	
	private void verifyNumber(final String number, final String pwd) {
		final ProgressDialog dialog = ProgressDialog.show(this, null, getString(R.string.loading_verify));
		
		BmobUtil.queryUser(this, number, new BmobUtil.QueryUserListener() {
			@Override
			public void onError(int code, String msg) {
				dialog.dismiss();
				Logger.e(TAG, "verifyNumber: find user failed: code=" + code + ", msg=" + msg);
				VerifyNumberActivity.startFrom(LoginActivity.this, number, pwd);
			}
			
			@Override
			public void findResult(boolean exist) {
				dialog.dismiss();
				if (exist) {
					toast(getString(R.string.toast_login_user_exist));
				} else {
					VerifyNumberActivity.startFrom(LoginActivity.this, number, pwd);
				}
			}
		});
	}
	
	private void toast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}
}
