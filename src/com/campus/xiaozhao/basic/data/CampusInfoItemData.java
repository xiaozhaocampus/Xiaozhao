package com.campus.xiaozhao.basic.data;

import android.content.ContentValues;
import com.campus.xiaozhao.basic.db.CampusDBHelper;
import com.campus.xiaozhao.basic.db.CampusModel;

import java.io.Serializable;

/**
 * Created by frankenliu on 15/5/31.
 */
public class CampusInfoItemData implements Serializable{

    /** 0 数据库ID */
    private long id;
    /** 1 单条校招信息的唯一标识(后台下发) */
    private long campusID;
    /** 2 校招信息发布的公司 */
    private String company;
    /** 3 公司简介 */
    private String introduction;
    /** 4 校招信息的类型 */
    private int type;
    /** 5 校招信息的标题 */
    private String title;
    /** 6 校招信息的内容 */
    private String content;
    /** 7 校招地址 */
    private String address;
    /** 8 校招时间 */
    private long time;
    /** 9 校招信息的版本号 */
    private long version;
    /** 10 用户是否已经设置此校招的提醒 */
    private boolean isRemind;
    /** 11 用户设置校招提醒的频率类型 */
    private int remindType;

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

    public long getCampusID() {
        return campusID;
    }

    public void setCampusID(long campusID) {
        this.campusID = campusID;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
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

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
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

    public void onAddToDatabase(ContentValues values) {
        values.put(CampusModel.CampusInfoItemColumn.CAMPUS_ID, campusID);
        values.put(CampusModel.CampusInfoItemColumn.COMPANY_NAME, company);
        values.put(CampusModel.CampusInfoItemColumn.COMPANY_INTRODUCTION, introduction);
        values.put(CampusModel.CampusInfoItemColumn.TYPE, type);
        values.put(CampusModel.CampusInfoItemColumn.TITLE, title);
        values.put(CampusModel.CampusInfoItemColumn.CONTENT, content);
        values.put(CampusModel.CampusInfoItemColumn.ADDRESS, address);
        values.put(CampusModel.CampusInfoItemColumn.TIME, time);
        values.put(CampusModel.CampusInfoItemColumn.VERSION, version);
        values.put(CampusModel.CampusInfoItemColumn.IS_REMIND, isRemind);
        values.put(CampusModel.CampusInfoItemColumn.REMIND_TYPE, remindType);
    }
}
