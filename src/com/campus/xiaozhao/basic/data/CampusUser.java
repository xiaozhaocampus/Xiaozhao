package com.campus.xiaozhao.basic.data;

import cn.bmob.v3.BmobObject;

public class CampusUser extends BmobObject {

	private static final long serialVersionUID = 1L;

	private String userName = "";
	
	private String userPhoto = "";//头像都是转成base64编码
	
	private String userNickName = "";
	
	private int userGender = 0;
	
	private String userPhoneNum = "";
	
	private String userEmail = "";
	
	private String userSchool = "";
	
	private String userMajor = "";
	
	private String userClass = "";

	private long userLoginTime = -1;
	
	public CampusUser() {
	}
	
	public String getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}

	public String getUserNickName() {
		return userNickName;
	}

	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}

	public int getUserGender() {
		return userGender;
	}

	public void setUserGender(int userGender) {
		this.userGender = userGender;
	}

	public String getUserPhoneNum() {
		return userPhoneNum;
	}

	public void setUserPhoneNum(String userPhoneNum) {
		this.userPhoneNum = userPhoneNum;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserShcool() {
		return userSchool;
	}

	public void setUserShcool(String userShcool) {
		this.userSchool = userShcool;
	}

	public String getUserMajor() {
		return userMajor;
	}

	public void setUserMajor(String userMajor) {
		this.userMajor = userMajor;
	}

	public String getUserClass() {
		return userClass;
	}

	public void setUserClass(String userClass) {
		this.userClass = userClass;
	}
	
	public long getUserLoginTime() {
		return userLoginTime;
	}

	public void setUserLoginTime(long userLoginTime) {
		this.userLoginTime = userLoginTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
