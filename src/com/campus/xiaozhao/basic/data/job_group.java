package com.campus.xiaozhao.basic.data;

import cn.bmob.v3.BmobObject;

/**
 * job大类表-后台用
 * Created by frankenliu on 2015/8/5.
 */
public class job_group extends BmobObject {

    /** 大类ID */
    private String group_id;

    /** 大类ID */
    private String group_name;

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }
}
