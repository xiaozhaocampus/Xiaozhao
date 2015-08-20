package com.campus.xiaozhao.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.campus.xiaozhao.R;

public class DefaultDialog extends Dialog implements OnCancelListener, OnClickListener {
    private int mGravity;
    private Bundle mArguments;
    private CharSequence mContentText;
    private CharSequence mCancelText;
    private CharSequence mConfirmText;
    private DialogInterface.OnClickListener mClickListener;

    public DefaultDialog(Context context) {
        super(context, R.style.default_dialog_style);
        mGravity = Gravity.CENTER;
        setOnCancelListener(this);
    }

    public DefaultDialog(Context context, int gravity) {
        super(context, R.style.default_dialog_style);
        mGravity = gravity;
        setOnCancelListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_dialog);

        final TextView content = (TextView)findViewById(R.id.tips);
        content.setGravity(mGravity);
        content.setText(mContentText);

        if (!TextUtils.isEmpty(mConfirmText)) {
            initButton(R.id.ok,  mConfirmText);
        }

        if (!TextUtils.isEmpty(mCancelText)) {
            initButton(R.id.cancel,  mCancelText);
        }
    }

    public final void setContentText(CharSequence text) {
        mContentText = text;
    }

    public final void setConfirmText(CharSequence text) {
        mConfirmText = text;
    }

    public final void setCancelText(CharSequence text) {
        mCancelText = text;
    }

    public final void setOnClickListener(DialogInterface.OnClickListener listener) {
        mClickListener = listener;
    }

    public final Bundle getArguments() {
        return mArguments;
    }

    public final void setArguments(Bundle args) {
        mArguments = args;
    }

    @Override
    public void onClick(View view) {
        if (mClickListener != null) {
            mClickListener.onClick(this, view.getId());
        } else {
            dismiss();
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (mClickListener != null) {
            mClickListener.onClick(dialog, R.id.cancel);
        }
    }

    protected void initButton(int textId, CharSequence text) {
        final TextView view = (TextView)findViewById(textId);
        view.setText(text);
        view.setOnClickListener(this);
        view.setVisibility(View.VISIBLE);
    }

}
