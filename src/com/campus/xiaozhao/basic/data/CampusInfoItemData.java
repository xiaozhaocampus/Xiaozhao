package com.campus.xiaozhao.basic.data;

/**
 * Created by frankenliu on 15/5/31.
 */
public class CampusInfoItemData {

    /** 单条校招信息的唯一标识 */
    private long id;
    /** 校招信息发布的公司 */
    private String company;
    /** 校招信息的类型 */
    private String type;
    /** 校招信息的标题 */
    private String title;
    /** 校招地址 */
    private String address;
    /** 校招时间 */
    private long time;

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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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
}
