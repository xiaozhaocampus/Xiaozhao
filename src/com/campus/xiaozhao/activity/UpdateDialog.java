package com.campus.xiaozhao.activity;

import com.campus.xiaozhao.R;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

public class UpdateDialog extends DefaultDialog {

	public UpdateDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//下载按钮
		initButton(R.id.ok, getContext().getString(R.string.download));
		initButton(R.id.cancel, getContext().getString(R.string.cancel));
	}
	
    public void stopLoading(String contentTips) {
    	findViewById(R.id.info).setVisibility(View.VISIBLE);
    	findViewById(R.id.progress).setVisibility(View.GONE);
    	initButton(R.id.tips, contentTips);
    }
}
