package com.campus.xiaozhao.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.text.InputType;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.campus.xiaozhao.R;
import com.campus.xiaozhao.XZApplication;

public class PersonalEditAdaptor extends BaseExpandableListAdapter {

	private static final int TYPE_PERSONAL_PHOTO = 0;
	private static final int TYPE_PERSONAL_TEXT = 1;
	private static final int TYPE_PERSONAL_TEXT_PHONE_NUM = 2;
	private static final int TYPE_PERSONAL_TEXT_MAIL = 3;
	private static final int TYPE_PERSONAL_OPTION = 4;
	private Context mContext;
	private ExpandableListView mExpandableListView;
	private SparseArray<List<PersonalEditBaseItem>> mItemGroups = new SparseArray<List<PersonalEditBaseItem>>();
	private class PersonalEditBaseItem {
		public int mType;
		public int mTitleResId;
		public PersonalEditBaseItem(int type, int titleResId){
			mType = type;
			mTitleResId = titleResId;
		}
	}
	
	private class PersonalEditPhotoItem extends PersonalEditBaseItem {
		public String mPhotoUrl;//可能是本地的也可能是网络的
		public PersonalEditPhotoItem(int type, int titleResId, String photoUrl){
			super(type, titleResId);
			mPhotoUrl = photoUrl;
		}
	}
	
	private class PersonalEditTextItem extends PersonalEditBaseItem {
		public String mContent;//填写的内容
		public PersonalEditTextItem(int type, int titleResId, String content){
			super(type, titleResId);
			mContent = content;
		}
	}
	
	private class PersonalOptionItem extends PersonalEditBaseItem {
		public List<? extends Object> mOptions;//提供的选项
		public int mSelectionIndex;//选择的结果
		public PersonalOptionItem(int type, int titleResId, List<? extends Object> options,int index){
			super(type, titleResId);
			mOptions = options;
			mSelectionIndex = index;
		}		
	}	

	public PersonalEditAdaptor(Context context, ExpandableListView listview) {
		mContext = context;
		mExpandableListView = listview;
		initPersonalData();
	}
	@Override
	public Object getChild(int arg0, int arg1) {
		@SuppressWarnings("unchecked")
		List<PersonalEditBaseItem> items = (List<PersonalEditBaseItem>)getGroup(arg0);
		return items.get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		return arg0*10 + arg1;
	}

	@Override
	public View getChildView(int pos0, int pos1, boolean arg2, View view,
			ViewGroup parent) {
		if(view == null) {
			view = LayoutInflater.from(mContext).inflate(R.layout.personal_child_item, null);
		}
		@SuppressWarnings("unchecked")
		List<PersonalEditBaseItem> items = (List<PersonalEditBaseItem>)getGroup(pos0);
		PersonalEditBaseItem item = items.get(pos1);
		((TextView)view.findViewById(R.id.personal_title)).setText(item.mTitleResId); 
		final EditText editText = (EditText)view.findViewById(R.id.personal_value);
		ImageView image = (ImageView)view.findViewById(R.id.personal_photo);
		switch (item.mType) {
		case TYPE_PERSONAL_PHOTO:
			editText.setVisibility(View.GONE);
			String url = ((PersonalEditPhotoItem)item).mPhotoUrl;
			if(url != null && !url.isEmpty()) {
				image.setImageURI(Uri.parse(url));
			}
			image.setVisibility(View.VISIBLE);
			return view;
		case TYPE_PERSONAL_TEXT:
			editText.setInputType(InputType.TYPE_CLASS_TEXT);
			break;
		case TYPE_PERSONAL_TEXT_PHONE_NUM:
			editText.setInputType(InputType.TYPE_CLASS_PHONE);
			break;
		case TYPE_PERSONAL_TEXT_MAIL:
			//TODO:暂时可以输入所有
			editText.setInputType(InputType.TYPE_CLASS_TEXT);
			break;
		case TYPE_PERSONAL_OPTION:
			PersonalOptionItem op = ((PersonalOptionItem)item);
			int index = op.mSelectionIndex;
			editText.setText(op.mOptions.get(index).toString());
			editText.setFocusable(false);
			editText.setFocusableInTouchMode(false);
			return view;
		default:
			break;
		}
		editText.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean focused) {
				if(focused) {
					InputMethodManager inputMethodManager = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

					// 接受软键盘输入的编辑文本或其它视图

					inputMethodManager.showSoftInput(editText,InputMethodManager.SHOW_FORCED);
				}
			}
		});
		editText.setFocusable(true);
		editText.setFocusableInTouchMode(true);
		editText.setText(((PersonalEditTextItem)item).mContent);
		editText.setVisibility(View.VISIBLE);
		image.setVisibility(View.GONE);
		return view;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getChildrenCount(int arg0) {
		return ((List<PersonalEditBaseItem>)getGroup(arg0)).size();
	}

	@Override
	public Object getGroup(int pos) {
		return mItemGroups.valueAt(pos);
	}

	@Override
	public int getGroupCount() {
		return mItemGroups.size();
	}

	@Override
	public long getGroupId(int pos) {
		return pos;
	}

	@Override
	public View getGroupView(int pos, boolean arg1, View view, ViewGroup parent) {
		if(view == null) {
			view = LayoutInflater.from(mContext).inflate(R.layout.personal_group_item, null);
		}
		((TextView)view.findViewById(R.id.personal_group_item_title)).setText(mItemGroups.keyAt(pos)); 
		return view;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return false;
	}

	/**
	 * 
	 */
	private void initPersonalData(){
		List<PersonalEditBaseItem> baseItems = new ArrayList<PersonalEditBaseItem>();
		//由资源文件获取URI
		Resources r = XZApplication.getInstance().getResources();
		baseItems.add(new PersonalEditPhotoItem(TYPE_PERSONAL_PHOTO, R.string.personal_photo, ""));
		baseItems.add(new PersonalEditTextItem(TYPE_PERSONAL_TEXT, R.string.personal_nickname, r.getString(R.string.personal_nickname_default)));
		String[] options = r.getStringArray(R.array.personal_gender_options);
		baseItems.add(new PersonalOptionItem(TYPE_PERSONAL_OPTION, R.string.personal_gender, Arrays.asList(options), 0));
		baseItems.add(new PersonalEditTextItem(TYPE_PERSONAL_TEXT_PHONE_NUM, R.string.personal_phone, ""));
		baseItems.add(new PersonalEditTextItem(TYPE_PERSONAL_TEXT_MAIL, R.string.personal_mail, ""));
		
		List<PersonalEditBaseItem> moreItems = new ArrayList<PersonalEditBaseItem>();
		moreItems.add(new PersonalEditTextItem(TYPE_PERSONAL_TEXT, R.string.personal_school, ""));
		moreItems.add(new PersonalEditTextItem(TYPE_PERSONAL_TEXT, R.string.personal_major, ""));
		moreItems.add(new PersonalEditTextItem(TYPE_PERSONAL_TEXT, R.string.personal_grade, ""));
		mItemGroups.put(R.string.personal_base_group_title, baseItems);
		mItemGroups.put(R.string.personal_more_group_title, moreItems);
	}
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		for(int i = 0; i < getGroupCount(); i++){  
			mExpandableListView.expandGroup(i);  
		} 
	}
	
	@Override
	public void notifyDataSetInvalidated() {
		super.notifyDataSetInvalidated();
		for(int i = 0; i < getGroupCount(); i++){  
			mExpandableListView.expandGroup(i);  
		} 
	}
}
