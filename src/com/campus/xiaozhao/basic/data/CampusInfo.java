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

    private long time;

    private String company;

    private long ptime;

    private String source;

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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public long getDate() {
        return time;
    }

    public void setDate(long time) {
        this.time = time;
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
}
