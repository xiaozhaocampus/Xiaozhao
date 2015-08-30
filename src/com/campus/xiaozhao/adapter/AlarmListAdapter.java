package com.campus.xiaozhao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AlarmListAdapter extends BaseAdapter {
	private static final String[] mCompNames = {"腾讯科技股份有限公司","阿里巴巴股份有限公司","百度股份有限公司"};
	private Context mContext;
	public AlarmListAdapter(Context context) {
		mContext = context;
	}
	@Override
	public int getCount() {
		return mCompNames.length;
	}

	@Override
	public Object getItem(int arg0) {
		return mCompNames[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		if(view == null) {
			view = new TextView(mContext);
		}
		((TextView)view).setText(mCompNames[arg0]);
		return view;
	}

}
