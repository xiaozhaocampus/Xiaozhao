package com.campus.xiaozhao.basic.utils;

import java.util.List;

public class PersonalUtils {
	private PersonalUtils() {
		
	}
	public static final int TYPE_PERSONAL_PHOTO = 0;
	public static final int TYPE_PERSONAL_TEXT = 1;
	public static final int TYPE_PERSONAL_TEXT_PHONE_NUM = 2;
	public static final int TYPE_PERSONAL_TEXT_MAIL = 3;
	public static final int TYPE_PERSONAL_OPTION = 4;
	
	public static class PersonalEditBaseItem {
		public int mType;
		public int mTitleResId;
		public PersonalEditBaseItem(int type, int titleResId){
			mType = type;
			mTitleResId = titleResId;
		}
	}
	
	public static class PersonalEditPhotoItem extends PersonalEditBaseItem {
		public String mPhotoData;//可能是本地的也可能是网络的
		public PersonalEditPhotoItem(int type, int titleResId, String photoData){
			super(type, titleResId);
			mPhotoData = photoData;
		}
	}
	
	public static class PersonalEditTextItem extends PersonalEditBaseItem {
		public String mContent;//填写的内容
		public PersonalEditTextItem(int type, int titleResId, String content){
			super(type, titleResId);
			mContent = content;
		}
	}
	
	public static class PersonalOptionItem extends PersonalEditBaseItem {
		public List<? extends Object> mOptions;//提供的选项
		public int mSelectionIndex;//选择的结果
		public PersonalOptionItem(int type, int titleResId, List<? extends Object> options,int index){
			super(type, titleResId);
			mOptions = options;
			mSelectionIndex = index;
		}		
	}	
}
