package com.campus.xiaozhao.basic.data;

import cn.bmob.v3.BmobObject;

/**
 * 公司信息表
 * Created by frankenliu on 15/6/22.
 */
public class CompanyInfo extends BmobObject {

    /** 公司名称 */
    private String name;
    /** 公司介绍 */
    private String introduction;
    /** 公司地址 */
    private String address;
    /** 公司号码 */
    private String number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
