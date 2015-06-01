package com.campus.xiaozhao.activity;

import android.app.ActionBar;
import android.app.Activity;
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

import com.campus.xiaozhao.Configuration;
import com.campus.xiaozhao.R;
import com.campus.xiaozhao.basic.utils.NumberUtils;
import com.campus.xiaozhao.basic.widget.CountDownTimerView;

public class ResetPasswordActivity extends Activity {

    public static final String TAG = "ResetPasswordActivity";
    private CountDownTimerView mCountDownTimerView;
    private EditText mPhoneNumberEditText;
    private EditText mVerifyCodeEditText;
    private EditText mNewPwdEditText;
    private EditText mRepeatPwdEditText;
    
    private String mVerifyCode;
    
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
        String number = mPhoneNumberEditText.getText().toString();
        if (number.length() == 0) {
            Toast.makeText(this, R.string.reset_password_edit_hint_phone_number, Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (!NumberUtils.isPhoneNumberValid(number)) {
            Toast.makeText(this, R.string.toast_invalid_phone_numbers, Toast.LENGTH_SHORT).show();
            return;
        }
        
        mCountDownTimerView.startCountDown(
                Configuration.VERIFICATION_WAIT_TIME,
                Configuration.COUNT_INTERVAL);
        mVerifyCodeEditText.requestFocus();
        requestVerifyCode();
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
        
        if (!TextUtils.equals(mVerifyCode, verifyCode)) {
            Toast.makeText(this, R.string.toast_verification_code_not_match, Toast.LENGTH_SHORT).show();
            return;
        }
        
        commitResetPassword();
    }
    
    private void requestVerifyCode() {
        
    }
    
    private void commitResetPassword() {
        
    }
}
