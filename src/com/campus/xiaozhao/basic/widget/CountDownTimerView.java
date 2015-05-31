package com.campus.xiaozhao.basic.widget;

import com.campus.xiaozhao.R;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.Button;


public class CountDownTimerView extends Button {
    private CountDownTimer mCountDownTimer;
    private OnCountDownListener mListener;
    
    public static abstract interface OnCountDownListener {
        public abstract boolean onCountDownFinishState();
    }

    public CountDownTimerView(Context paramContext) {
        super(paramContext);
    }

    public CountDownTimerView(Context paramContext,
            AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public CountDownTimerView(Context paramContext,
            AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    public void startCountDown(long millisInFuture, long countDownInterval) {
        setEnabled(false);
        if (this.mCountDownTimer == null) {
            this.mCountDownTimer = new CountDownTimer(millisInFuture, countDownInterval) {
                @Override
                public void onFinish() {
                    if (mListener != null) {
                        setEnabled(mListener.onCountDownFinishState());
                    } else {
                        setEnabled(true);
                    }
                    CountDownTimerView.this.setText(R.string.get_verification_code);
                }

                @Override
                public void onTick(long millisUntilFinished) {
                    int leftSeconds = (int) (millisUntilFinished / 1000);
                    Object[] arrayOfObject = new Object[1];
                    arrayOfObject[0] = Integer.valueOf(leftSeconds);
                    setText(getResources().getString(R.string.get_verification_code_again, arrayOfObject));
                }
            };
        }
        this.mCountDownTimer.start();
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    public void setOnCountDownListener(OnCountDownListener onCountDownListener) {
        mListener = onCountDownListener;
    }
}
