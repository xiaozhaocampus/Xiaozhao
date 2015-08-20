package com.campus.xiaozhao.activity;

import com.campus.xiaozhao.R;
import com.campus.xiaozhao.eventbus.EventBus;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PersonalEditProgressBar extends LinearLayout {
	private TextView mHowMuchDone;
	private ProgressBar mProgressBar;
	public PersonalEditProgressBar(Context context) {
		super(context);
	}
	
	public PersonalEditProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public PersonalEditProgressBar(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	
	
	
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mHowMuchDone = (TextView)findViewById(R.id.how_much_done);
		mProgressBar = (ProgressBar)findViewById(R.id.progressBar1);
	}
	
	/**
	 * 设置完成度
	 * @param progress
	 */
	public void setProgress(final int progress) {
		EventBus.runOnUIThread(new Runnable() {
			@Override
			public void run() {
				String done = getResources().getString(R.string.personal_data_done);
				mHowMuchDone.setText(String.format(done, progress)); 
				mProgressBar.setProgress(progress);
			}
		});
	}
}
