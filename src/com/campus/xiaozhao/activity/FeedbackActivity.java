package com.campus.xiaozhao.activity;

import java.io.File;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import cn.bmob.v3.listener.SaveListener;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.campus.xiaozhao.R;
import com.campus.xiaozhao.adapter.FeedbackGridAdapter;
import com.campus.xiaozhao.adapter.FeedbackGridAdapter.Selection;
import com.campus.xiaozhao.basic.data.FeedbackData;
import com.campus.xiaozhao.basic.utils.PicChooseUtils;
import com.component.logger.Logger;

public class FeedbackActivity extends Activity implements OnItemClickListener,
		OnClickListener {
	private static final String TAG = "FeedbackActivity";
	private File mScreenShot;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		// 设置actionbar
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.actionbar_feedback);
		actionBar.getCustomView().findViewById(R.id.submit)
				.setOnClickListener(this);
		actionBar.getCustomView().findViewById(R.id.actionbar_back)
				.setOnClickListener(this);
		actionBar.getCustomView().findViewById(R.id.submit)
		.setOnClickListener(this);
		findViewById(R.id.feedback_choose_pic)
		.setOnClickListener(this);
		
		// 选项
		GridView items = ((GridView) findViewById(R.id.feedback_items));
		items.setAdapter(new FeedbackGridAdapter(this));
		items.setOnItemClickListener(this);
	}

	// 提交反馈
	public void submit(View view) {
		final String suggestion = ((TextView)findViewById(R.id.feedback_suggestion)).getText().toString();
		if(suggestion == null || suggestion.trim().isEmpty()) {
			Toast.makeText(this, R.string.toast_no_feedback_content, Toast.LENGTH_SHORT).show();
			return;
		}
		if(mScreenShot != null && mScreenShot.exists()) {
			BmobProFile.getInstance(this).upload(mScreenShot.getAbsolutePath(),new UploadListener() {
				
				@Override
				public void onError(int paramInt, String paramString) {
					Logger.e(TAG, "Error:" + paramString);
					Toast.makeText(FeedbackActivity.this, R.string.toast_feedback_fail, Toast.LENGTH_SHORT).show();
					finish();
				}
				
				@Override
				public void onSuccess(String paramString1, String mScreenShotUrl) {
					Logger.i(TAG, "Upload File Success Url:  " + mScreenShotUrl);
					upload(suggestion, mScreenShotUrl);
				}
				
				@Override
				public void onProgress(int paramInt) {
				}
			});
		} else {
			upload(suggestion, null);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ToggleButton button = (ToggleButton) arg1.findViewById(R.id.button);
		button.setChecked(!button.isChecked());
		((FeedbackGridAdapter) ((GridView) findViewById(R.id.feedback_items))
				.getAdapter()).notifyDataSetChanged();
	}

	/**
	 * 处理提交事件
	 */
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.submit:
			submit(view);
			break;
		case R.id.actionbar_back:
			finish();
			break;
		case R.id.feedback_choose_pic:
			PicChooseUtils.choosePic(this);
			break;
		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Uri uri = data.getData();
			Log.e("uri", uri.toString());
			ImageView imageView = (ImageView) findViewById(R.id.feedback_choose_pic);
				/* 将Bitmap设定到ImageView */
			imageView.setImageURI(uri);
			mScreenShot = new File(getRealPathFromURI(uri));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private String getRealPathFromURI(Uri contentUri) {
	    String res = null;
	    String[] proj = { MediaStore.Images.Media.DATA };
	    Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
	    if(cursor.moveToFirst()){;
	       int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	       res = cursor.getString(column_index);
	    }
	    cursor.close();
	    return res;
	}
	
	private void upload(String suggestion,String mScreenShotUrl) {
		FeedbackData data = new FeedbackData();
		data.setQQCode(((TextView)findViewById(R.id.feedback_qq)).getText().toString());
		data.setSuggestion(suggestion);
		String selection = "";
		List<Selection> selections = ((FeedbackGridAdapter)((GridView)findViewById(R.id.feedback_items)).getAdapter()).getSelections();
		int size = selections.size();
		for (int i = 0; i < size; i++) {
			if(i != 0 && selections.get(i).mIsSelected) {
				selection += "|";
				selection += selections.get(i).mSelTitle;
			}
		}
		data.setClassify(selection);
		data.setScreenShot(mScreenShotUrl);
		data.save(this, new SaveListener() {
			
			@Override
			public void onSuccess() {
				Toast.makeText(FeedbackActivity.this, R.string.toast_feedback_success, Toast.LENGTH_SHORT).show();
				finish();
			}
			
			@Override
			public void onFailure(int paramInt, String paramString) {
				Toast.makeText(FeedbackActivity.this, R.string.toast_feedback_fail, Toast.LENGTH_SHORT).show();
				finish();
			}
		});
	}
}
