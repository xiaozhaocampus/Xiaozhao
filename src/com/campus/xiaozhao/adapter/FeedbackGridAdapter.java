package com.campus.xiaozhao.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ToggleButton;

import com.campus.xiaozhao.R;

public class FeedbackGridAdapter extends BaseAdapter {
	public static class Selection {
		public String mSelTitle = null;
		public boolean mIsSelected = false;
	}
	
	private List<Selection> mSelections = new ArrayList<Selection>();
	public List<Selection> getSelections() {
		return mSelections;
	}
	private Context mContext;
	public FeedbackGridAdapter(Context context) {
		mContext = context;
		init();
	}
	
	private void init() {
		String[] array = mContext.getResources().getStringArray(R.array.feedback_options);
		for (String title:array) {
			Selection selection = new Selection();
			selection.mSelTitle = title;
			selection.mIsSelected = false;
			mSelections.add(selection);
		}
	}
	
	@Override
	public int getCount() {
		return mSelections.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mSelections.get(0);
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
			String title = mSelections.get(pos).mSelTitle;
			mSelections.get(pos).mIsSelected = button.isChecked(); 
			button.setTextOn(title);
			button.setTextOff(title);
			button.setChecked(button.isChecked());
		}
		return view;
	}

}
