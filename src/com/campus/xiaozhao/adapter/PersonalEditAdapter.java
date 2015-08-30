package com.campus.xiaozhao.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.campus.xiaozhao.R;
import com.campus.xiaozhao.XZApplication;
import com.campus.xiaozhao.basic.data.CampusUser;
import com.campus.xiaozhao.basic.db.CampusUserDBProcessor;
import com.campus.xiaozhao.basic.utils.BitmapUtils;
import com.campus.xiaozhao.basic.utils.PersonalUtils;
import com.campus.xiaozhao.basic.utils.PersonalUtils.PersonalEditBaseItem;
import com.campus.xiaozhao.basic.utils.PersonalUtils.PersonalEditPhotoItem;
import com.campus.xiaozhao.basic.utils.PersonalUtils.PersonalEditTextItem;
import com.campus.xiaozhao.basic.utils.PersonalUtils.PersonalOptionItem;

public class PersonalEditAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private ExpandableListView mExpandableListView;
	public CampusUser getUserInfo() {
		CampusUser user = CampusUserDBProcessor.fromDB(mContext, false);
		if(user == null) {
			return null;		
		}
		int size = mItemGroups.size();
		for (int i = 0;i < size;i++) {
			List<PersonalEditBaseItem> items = mItemGroups.valueAt(i);
			for(PersonalEditBaseItem item:items) {
				switch (item.mTitleResId) {
				case R.string.personal_photo:
					user.setUserPhoto(((PersonalEditPhotoItem)item).mPhotoData);
					break;
				case R.string.personal_nickname:
					user.setUserNickName(((PersonalEditTextItem)item).mContent);
					break;
				case R.string.personal_gender:
					user.setUserGender(((PersonalOptionItem)item).mSelectionIndex);
					break;
				case R.string.personal_phone:
					user.setUserPhoneNum(((PersonalEditTextItem)item).mContent);
					break;
				case R.string.personal_mail:
					user.setUserEmail(((PersonalEditTextItem)item).mContent);
					break;
				case R.string.personal_school:
					user.setUserShcool(((PersonalEditTextItem)item).mContent);
					break;
				case R.string.personal_major:
					user.setUserMajor(((PersonalEditTextItem)item).mContent);
					break;
				case R.string.personal_grade:
					user.setUserClass(((PersonalEditTextItem)item).mContent);
					break;
				default:
					break;
				}
			}
		}
		return user;
	}
	private SparseArray<List<PersonalEditBaseItem>> mItemGroups = new SparseArray<List<PersonalEditBaseItem>>();
	public SparseArray<List<PersonalEditBaseItem>> getItemGroups() {
		return mItemGroups;
	}

	public PersonalEditAdapter(Context context, ExpandableListView listview) {
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
		final TextView editText = (TextView)view.findViewById(R.id.personal_value);
		ImageView image = (ImageView)view.findViewById(R.id.personal_photo);
		switch (item.mType) {
		case PersonalUtils.TYPE_PERSONAL_PHOTO:
			editText.setVisibility(View.GONE);
			String data = ((PersonalEditPhotoItem)item).mPhotoData;
			if(data != null && !data.isEmpty()) {
				image.setImageBitmap(BitmapUtils.base64ToBitmap(data));
			}
			image.setVisibility(View.VISIBLE);
			return view;
		case PersonalUtils.TYPE_PERSONAL_TEXT:
//			editText.setInputType(InputType.TYPE_CLASS_TEXT);
			break;
		case PersonalUtils.TYPE_PERSONAL_TEXT_PHONE_NUM:
//			editText.setInputType(InputType.TYPE_CLASS_PHONE);
			break;
		case PersonalUtils.TYPE_PERSONAL_TEXT_MAIL:
//			//TODO:暂时可以输入所有
//			editText.setInputType(InputType.TYPE_CLASS_TEXT);
			break;
		case PersonalUtils.TYPE_PERSONAL_OPTION:
			PersonalOptionItem op = ((PersonalOptionItem)item);
			int index = op.mSelectionIndex;
			editText.setText(op.mOptions.get(index).toString());
			editText.setFocusable(false);
			editText.setFocusableInTouchMode(false);			
			return view;
		default:
			break;
		}
		editText.setFocusable(false);
		editText.setFocusableInTouchMode(false);
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
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

	/**
	 * 
	 */
	private void initPersonalData(){
		List<PersonalEditBaseItem> baseItems = new ArrayList<PersonalEditBaseItem>();
		//由资源文件获取URI
		Resources r = XZApplication.getInstance().getResources();
		CampusUser user = CampusUserDBProcessor.fromDB(XZApplication.getInstance(), false);
		if(user == null) {
			user = new CampusUser();
		}
		baseItems.add(new PersonalEditPhotoItem(PersonalUtils.TYPE_PERSONAL_PHOTO, R.string.personal_photo, user.getUserPhoto()));
		baseItems.add(new PersonalEditTextItem(PersonalUtils.TYPE_PERSONAL_TEXT, R.string.personal_nickname, user.getUserNickName()));
		String[] options = r.getStringArray(R.array.personal_gender_options);
		baseItems.add(new PersonalOptionItem(PersonalUtils.TYPE_PERSONAL_OPTION, R.string.personal_gender, Arrays.asList(options), user.getUserGender()));
		baseItems.add(new PersonalEditTextItem(PersonalUtils.TYPE_PERSONAL_TEXT_PHONE_NUM, R.string.personal_phone, user.getUserPhoneNum()));
		baseItems.add(new PersonalEditTextItem(PersonalUtils.TYPE_PERSONAL_TEXT_MAIL, R.string.personal_mail, user.getUserEmail()));
		
		List<PersonalEditBaseItem> moreItems = new ArrayList<PersonalEditBaseItem>();
		moreItems.add(new PersonalEditTextItem(PersonalUtils.TYPE_PERSONAL_TEXT, R.string.personal_school, user.getUserShcool()));
		moreItems.add(new PersonalEditTextItem(PersonalUtils.TYPE_PERSONAL_TEXT, R.string.personal_major, user.getUserMajor()));
		moreItems.add(new PersonalEditTextItem(PersonalUtils.TYPE_PERSONAL_TEXT, R.string.personal_grade, user.getUserClass()));
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
