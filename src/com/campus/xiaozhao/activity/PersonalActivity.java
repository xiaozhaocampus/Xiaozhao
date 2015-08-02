package com.campus.xiaozhao.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.campus.xiaozhao.R;

public class PersonalActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.personal_edit_view);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.actionbar_personal);
		actionBar.getCustomView().findViewById(R.id.actionbar_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		PersonalEditListView listView = (PersonalEditListView)findViewById(R.id.personal_info);
		listView.setGroupIndicator(null);
		PersonalEditAdaptor adaptor = new PersonalEditAdaptor(this, listView);
		listView.setAdapter(adaptor);
		listView.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2,
					long arg3) {
				return true;
			}
		});
		adaptor.notifyDataSetInvalidated();
		
	}
}
