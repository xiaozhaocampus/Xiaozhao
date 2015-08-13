package com.campus.xiaozhao.basic.data;

import cn.bmob.v3.BmobObject;

public class FeedbackData extends BmobObject {
	
	private String QQCode = "";
	
	private String suggestion = "";
	
	private String classify = "";//分类放到一个String中用|间隔开
	
	private String screenShotUrl = null;

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

	public String getScreenShot() {
		return screenShotUrl;
	}

	public void setScreenShot(String screenShot) {
		this.screenShotUrl = screenShot;
	}
}
