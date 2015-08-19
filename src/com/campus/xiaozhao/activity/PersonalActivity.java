package com.campus.xiaozhao.activity;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.campus.xiaozhao.R;
import com.campus.xiaozhao.basic.data.CampusUser;
import com.campus.xiaozhao.basic.db.CampusUserDBProcessor;
import com.campus.xiaozhao.basic.utils.BitmapUtils;
import com.campus.xiaozhao.basic.utils.PersonalUtils;
import com.campus.xiaozhao.basic.utils.PersonalUtils.PersonalEditBaseItem;
import com.campus.xiaozhao.basic.utils.PersonalUtils.PersonalEditPhotoItem;
import com.campus.xiaozhao.basic.utils.PersonalUtils.PersonalEditTextItem;
import com.campus.xiaozhao.basic.utils.PersonalUtils.PersonalOptionItem;
import com.campus.xiaozhao.basic.utils.PicChooseUtils;

public class PersonalActivity extends Activity {
	private PersonalEditAdaptor mAdaptor = null;
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
		listView.setClickable(true);
		mAdaptor = new PersonalEditAdaptor(this, listView);
		listView.setAdapter(mAdaptor);
		listView.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2,
					long arg3) {
				return true;
			}
		});
		
		listView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1, int pos0,
					int pos1, long arg4) {
				PersonalEditBaseItem item = (PersonalEditBaseItem)mAdaptor.getItemGroups().valueAt(pos0).get(pos1);
				switch (item.mType) {
				case PersonalUtils.TYPE_PERSONAL_PHOTO:
					PicChooseUtils.choosePic(PersonalActivity.this);
					break;
				case PersonalUtils.TYPE_PERSONAL_TEXT:
				case PersonalUtils.TYPE_PERSONAL_TEXT_MAIL:
				case PersonalUtils.TYPE_PERSONAL_TEXT_PHONE_NUM:
					editDialog(item.mTitleResId, item.mType, pos0, pos1);
					break;
				case PersonalUtils.TYPE_PERSONAL_OPTION:
					selectDialog(item.mTitleResId,pos0,pos1);
					break;

				default:
					break;
				}
				return true;
			}
		});

		mAdaptor.notifyDataSetInvalidated();
		refreshProgress();
	}
	
	private void editDialog(int title, int type, final int pos0, final int pos1) {
		final EditText editText = new EditText(this);
		switch (type) {
		case PersonalUtils.TYPE_PERSONAL_TEXT:
			editText.setInputType(InputType.TYPE_CLASS_TEXT);
			break;
		case PersonalUtils.TYPE_PERSONAL_TEXT_PHONE_NUM:
			editText.setInputType(InputType.TYPE_CLASS_PHONE);
			break;
		case PersonalUtils.TYPE_PERSONAL_TEXT_MAIL:
//			//TODO:暂时可以输入所有
			editText.setInputType(InputType.TYPE_CLASS_TEXT);
			break;
		default:
			break;
		}
	 	new AlertDialog.Builder(this)
	 	.setTitle(title)
	 	.setIcon(R.drawable.ic_launcher)
	 	.setView(editText)
	 	.setPositiveButton(R.string.commit, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int pos) {
				dialog.dismiss();
				PersonalEditBaseItem item = mAdaptor.getItemGroups().valueAt(pos0).get(pos1);
				((PersonalEditTextItem)item).mContent = editText.getText().toString();
				mAdaptor.notifyDataSetChanged();
				refreshProgress();
			}
		})
	 	.setNegativeButton(R.string.cancel, null)
	 	.show();
	}
	
	
	private void selectDialog(int title, final int pos0, final int pos1){
		new AlertDialog.Builder(this)
	 	.setTitle(title)
	 	.setIcon(android.R.drawable.ic_dialog_info)                
	 	.setSingleChoiceItems(R.array.personal_gender_options, 0, 
	 	  new DialogInterface.OnClickListener() {
	 	                              
	 	     public void onClick(DialogInterface dialog, int which) {
	 	        dialog.dismiss();
	 	        
				PersonalEditBaseItem item = mAdaptor.getItemGroups().valueAt(pos0).get(pos1);
				((PersonalOptionItem)item).mSelectionIndex = which;
				mAdaptor.notifyDataSetChanged();
				refreshProgress();
	 	     }
	 	  }
	 	)
	 	.setNegativeButton(R.string.cancel, null)
	 	.show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Uri uri = data.getData();
			if(uri != null) {
				int width = getResources().getDimensionPixelSize(R.dimen.campus_edit_photo_size);
				PersonalEditBaseItem item = mAdaptor.getItemGroups().valueAt(0).get(0);
				((PersonalEditPhotoItem)item).mPhotoData = BitmapUtils.getBase64FromUri(this, uri,width,width);
				mAdaptor.notifyDataSetChanged();
			} else {
				Log.e("uri", "URI IS NULL");
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void finish() {
		CampusUser user = mAdaptor.getUserInfo();
		if(user != null) {
			CampusUserDBProcessor.saveToServer(this, user, null);
		}
		super.finish();
	}
	
	/**
	 * 刷新完成进度
	 */
	private void refreshProgress() {
		SparseArray<List<PersonalEditBaseItem>> itemGroups = mAdaptor.getItemGroups();
		int all = 0;
		int done = 0;
		int size = itemGroups.size();
		for(int i = 0; i < size;i++) {
			List<PersonalEditBaseItem> bases = itemGroups.valueAt(i);
			if(bases == null) {
				return;
			}
			all += bases.size();
			for(PersonalEditBaseItem item : bases) {
				boolean isDone = false;
				switch (item.mType) {
				case PersonalUtils.TYPE_PERSONAL_PHOTO:
					String photoData = ((PersonalEditPhotoItem)item).mPhotoData;
					isDone = photoData != null && !photoData.isEmpty();
					break;
				case PersonalUtils.TYPE_PERSONAL_OPTION:
					int selIndex = ((PersonalOptionItem)item).mSelectionIndex;
					isDone = selIndex != -1;
					break;
				case PersonalUtils.TYPE_PERSONAL_TEXT:
				case PersonalUtils.TYPE_PERSONAL_TEXT_MAIL:
				case PersonalUtils.TYPE_PERSONAL_TEXT_PHONE_NUM:
					String content = ((PersonalEditTextItem)item).mContent;
					isDone = content != null && !content.isEmpty();
					break;

				default:
					break;
				}
				if(isDone) {
					done++;
				}
			}
		}
		
		int progress = (int)(done*100/all*1.0f);
		((PersonalEditProgressBar)findViewById(R.id.edit_progress)).setProgress(progress);
	}
}
