package com.campus.xiaozhao.basic.data;

import java.io.Serializable;

import android.content.ContentValues;
import android.database.Cursor;

import com.campus.xiaozhao.basic.db.CampusModel;

/**
 * 客户端使用
 * Created by frankenliu on 15/5/31.
 */
public class CampusInfoItemData implements Serializable{

    /** 0 数据库ID */
    private long id;
    /** 1 单条校招信息的唯一标识(后台下发) */
    private String campusID;
    /** 2 校招信息发布的公司 */
    private String company;
    /** 2 校招信息发布的时间 */
    private long ptime;
    /** 4 校招城市 */
    private String city;
    /** 5 校招信息的类型 */
    private String type;
    /** 6 校招信息的标题 */
    private String title;
    /** 7 校招信息的内容(公司简介合入此项中) */
    private String content;
    /** 8 校招地址 */
    private String address;
    /** 9 校招时间 */
    private long time;
    /** 10 校招信息的版本号 */
    private long version;
    /** 11 用户是否已经设置此校招的提醒 */
    private boolean isRemind;
    /** 12 用户设置校招提醒的频率类型 */
    private int remindType;
    /** 13 用户设置校招提醒的提醒时间(只有remindType为自定义时有效) */
    private long remindTime;
    /** 14 用户是否设置收藏 */
    private boolean isSave;
    /** 15 校招信息的来源 */
    private String source;
    /** 15 校招信息是否被后台标记为删除 */
    private boolean isDelete;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCampusID() {
        return campusID;
    }

    public void setCampusID(String campusID) {
        this.campusID = campusID;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public boolean isRemind() {
        return isRemind;
    }

    public void setIsRemind(boolean isRemind) {
        this.isRemind = isRemind;
    }

    public int getRemindType() {
        return remindType;
    }

    public void setRemindType(int remindType) {
        this.remindType = remindType;
    }

    public long getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(long remindTime) {
        this.remindTime = remindTime;
    }

    public boolean isSave() {
        return isSave;
    }

    public void setIsSave(boolean isSave) {
        this.isSave = isSave;
    }

    public long getPtime() {
        return ptime;
    }

    public void setPtime(long ptime) {
        this.ptime = ptime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public void onAddToDatabase(ContentValues values) {
        values.put(CampusModel.CampusInfoItemColumn.CAMPUS_ID, campusID);
        values.put(CampusModel.CampusInfoItemColumn.COMPANY_NAME, company);
        values.put(CampusModel.CampusInfoItemColumn.CITY, city);
        values.put(CampusModel.CampusInfoItemColumn.PUBLISH_TIME, ptime);
        values.put(CampusModel.CampusInfoItemColumn.TYPE, type);
        values.put(CampusModel.CampusInfoItemColumn.TITLE, title);
        values.put(CampusModel.CampusInfoItemColumn.CONTENT, content);
        values.put(CampusModel.CampusInfoItemColumn.ADDRESS, address);
        values.put(CampusModel.CampusInfoItemColumn.TIME, time);
        values.put(CampusModel.CampusInfoItemColumn.VERSION, version);
        values.put(CampusModel.CampusInfoItemColumn.IS_REMIND, isRemind);
        values.put(CampusModel.CampusInfoItemColumn.REMIND_TYPE, remindType);
        values.put(CampusModel.CampusInfoItemColumn.REMIND_TIME, remindTime);
        values.put(CampusModel.CampusInfoItemColumn.IS_SAVE, isSave);
        values.put(CampusModel.CampusInfoItemColumn.SOURCE, source);
        values.put(CampusModel.CampusInfoItemColumn.IS_DELETE, isDelete);
    }
    
    public static final CampusInfoItemData from(Cursor cur) {
    	CampusInfoItemData campusInfoItemData = new CampusInfoItemData();
    	campusInfoItemData.setAddress(cur.getString(cur.getColumnIndex(CampusModel.CampusInfoItemColumn.ADDRESS)));
    	campusInfoItemData.setCampusID(cur.getString(cur.getColumnIndex(CampusModel.CampusInfoItemColumn.CAMPUS_ID)));
    	campusInfoItemData.setCity(cur.getString(cur.getColumnIndex(CampusModel.CampusInfoItemColumn.CITY)));
    	campusInfoItemData.setCompany(cur.getString(cur.getColumnIndex(CampusModel.CampusInfoItemColumn.COMPANY_NAME)));
    	campusInfoItemData.setContent(cur.getString(cur.getColumnIndex(CampusModel.CampusInfoItemColumn.CONTENT)));
    	campusInfoItemData.setId(0);
    	campusInfoItemData.setIsRemind(cur.getInt(cur.getColumnIndex(CampusModel.CampusInfoItemColumn.IS_REMIND)) == 1);
    	campusInfoItemData.setIsSave(cur.getInt(cur.getColumnIndex(CampusModel.CampusInfoItemColumn.IS_REMIND)) == 1);
    	campusInfoItemData.setPtime(cur.getLong(cur.getColumnIndex(CampusModel.CampusInfoItemColumn.PUBLISH_TIME)));
    	campusInfoItemData.setRemindTime(cur.getLong(cur.getColumnIndex(CampusModel.CampusInfoItemColumn.REMIND_TIME)));
    	campusInfoItemData.setRemindType(cur.getInt(cur.getColumnIndex(CampusModel.CampusInfoItemColumn.REMIND_TIME)));
    	campusInfoItemData.setSource(cur.getString(cur.getColumnIndex(CampusModel.CampusInfoItemColumn.SOURCE)));
    	campusInfoItemData.setTitle(cur.getString(cur.getColumnIndex(CampusModel.CampusInfoItemColumn.TITLE)));
    	campusInfoItemData.setType(cur.getString(cur.getColumnIndex(CampusModel.CampusInfoItemColumn.TYPE)));
    	campusInfoItemData.setVersion(cur.getInt(cur.getColumnIndex(CampusModel.CampusInfoItemColumn.VERSION)));
        campusInfoItemData.setIsDelete(cur.getInt(cur.getColumnIndex(CampusModel.CampusInfoItemColumn.IS_DELETE)) == 1);
    	return campusInfoItemData;
    }
}
