package com.campus.xiaozhao.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.campus.xiaozhao.R;
import com.campus.xiaozhao.basic.db.CampusModel;
import com.campus.xiaozhao.basic.utils.DateUtils;

public class HistoryListAdapter extends CursorAdapter {
	public HistoryListAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
	}

	@Override
	public void bindView(View view, Context context, Cursor cur) {
		if(view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.history_listview_item, null);
		}
		init(view, cur);
	}

	@Override
	public View newView(Context context, Cursor cur, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.history_listview_item, null);
		init(view, cur);
		return view;
	}

	private void init(View view, Cursor cur) {
		String title = cur.getString(cur.getColumnIndex(CampusModel.CampusInfoItemColumn.TITLE));
		Long time = cur.getLong(cur.getColumnIndex(CampusModel.CampusInfoItemColumn.TIME));
		String place = cur.getString(cur.getColumnIndex(CampusModel.CampusInfoItemColumn.ADDRESS));
		ViewHolder holder = new ViewHolder();
		holder.mTitle = (TextView)view.findViewById(R.id.title); 
		holder.mTime= (TextView)view.findViewById(R.id.time);
		holder.mPlace = (TextView)view.findViewById(R.id.place);
		holder.mOver = (TextView)view.findViewById(R.id.over);
		holder.mTitle.setText(title);
		holder.mPlace.setText(place);
		holder.mTime.setText(DateUtils.transferTimeToDate(time));
	}
	
	private static class ViewHolder {
		TextView mTitle;
		TextView mTime;
		TextView mPlace;
		TextView mOver;
	}
}
