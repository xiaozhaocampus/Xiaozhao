package com.campus.xiaozhao.basic.data;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class FeedbackData extends BmobObject {
	
	private String QQCode = "";
	
	private String suggestion = "";
	
	private String classify = "";//分类放到一个String中用|间隔开
	
	private BmobFile screenShot = null;

	public String getQQCode() {
		return QQCode;
	}

	public void setQQCode(String qQCode) {
		QQCode = qQCode;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

	public String getClassify() {
		return classify;
	}

	public void setClassify(String classify) {
		this.classify = classify;
	}

	public BmobFile getScreenShot() {
		return screenShot;
	}

	public void setScreenShot(BmobFile screenShot) {
		this.screenShot = screenShot;
	}
}
