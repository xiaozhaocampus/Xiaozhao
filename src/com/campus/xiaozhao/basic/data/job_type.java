package com.campus.xiaozhao.basic.data;

import cn.bmob.v3.BmobObject;

/**
 * job小类-后台用
 * Created by frankenliu on 2015/8/5.
 */
public class job_type extends BmobObject {

    private String type_id;

    private String group_id;

    private String type_name;

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }
}
