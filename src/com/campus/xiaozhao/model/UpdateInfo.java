package com.campus.xiaozhao.model;

import cn.bmob.v3.BmobObject;

public class UpdateInfo extends BmobObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long lastVersionCode;
	private String lastVersionName;
	private String lastDownloadAdr;
	private String updateTips;
	public long getLastVersionCode() {
		return lastVersionCode;
	}
	public void setLastVersionCode(long lastVersionCode) {
		this.lastVersionCode = lastVersionCode;
	}
	public String getLastVersionName() {
		return lastVersionName;
	}
	public void setLastVersionName(String lastVersionName) {
		this.lastVersionName = lastVersionName;
	}
	public String getLastDownloadAdr() {
		return lastDownloadAdr;
	}
	public void setLastDownloadAdr(String lastDownloadAdr) {
		this.lastDownloadAdr = lastDownloadAdr;
	}
	public String getUpdateTips() {
		return updateTips;
	}
	public void setUpdateTips(String updateTips) {
		this.updateTips = updateTips;
	}
	
}
