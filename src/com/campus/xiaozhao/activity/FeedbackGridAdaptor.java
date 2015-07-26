package com.campus.xiaozhao.activity;

import com.campus.xiaozhao.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ToggleButton;

public class FeedbackGridAdaptor extends BaseAdapter {
	private String[] mSelections = {"界面","网络","定位","提醒","内容","推送"};
	private Context mContext;
	public FeedbackGridAdaptor(Context context) {
		mContext = context;
	}
	@Override
	public int getCount() {
		return mSelections.length;
	}

	@Override
	public Object getItem(int arg0) {
		return mSelections[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		if(view == null) {
			view = LayoutInflater.from(mContext).inflate(R.layout.feedback_item, null);
		}
		ToggleButton button = ((ToggleButton)view.findViewById(R.id.button));
		if(button != null) {
			button.setTextOn(mSelections[pos]);
			button.setTextOff(mSelections[pos]);
			button.setChecked(button.isChecked());
		}
		return view;
	}

}
