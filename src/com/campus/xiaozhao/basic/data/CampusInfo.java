package com.campus.xiaozhao.basic.data;

import cn.bmob.v3.BmobObject;

/**
 * 校招信息表－后台用
 * Created by frankenliu on 15/6/22.
 */
public class CampusInfo extends BmobObject {

    private String city;

    private String title;

    private String content;

    private String address;

    private long version;

    private String type;

    private CompanyInfo companyInfo;

    private long time;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CompanyInfo getCompanyInfo() {
        return companyInfo;
    }

    public void setCompanyInfo(CompanyInfo companyInfo) {
        this.companyInfo = companyInfo;
    }

    public long getDate() {
        return time;
    }

    public void setDate(long time) {
        this.time = time;
    }
}
