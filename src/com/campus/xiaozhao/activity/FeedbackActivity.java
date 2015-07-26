package com.campus.xiaozhao.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.campus.xiaozhao.R;
import com.component.logger.Logger;

public class FeedbackActivity extends Activity implements OnItemClickListener ,OnClickListener{
	private static final String TAG = "FeedbackActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		//设置actionbar
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.actionbar_feedback);
		actionBar.getCustomView().findViewById(R.id.submit).setOnClickListener(this);
		
		//选项
		GridView items = ((GridView)findViewById(R.id.feedback_items));
		items.setAdapter(new FeedbackGridAdaptor(this));
		items.setOnItemClickListener(this);
	}
	
	//返回
	public void back(View view){
		Logger.i(TAG, "Feedback back");
		finish();
	}
	
	//提交反馈
	public void submit(View view){
		Logger.i(TAG, "Feedback submit");
		Toast.makeText(this, "请实现提交功能", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ToggleButton button = (ToggleButton)arg1.findViewById(R.id.button);
		button.setChecked(!button.isChecked());
		((FeedbackGridAdaptor)((GridView)findViewById(R.id.feedback_items)).getAdapter()).notifyDataSetChanged();
	}

	/**
	 * 处理提交事件
	 */
	@Override
	public void onClick(View arg0) {
		
	}
}
