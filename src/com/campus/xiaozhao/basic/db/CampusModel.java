package com.campus.xiaozhao.basic.db;

import android.provider.BaseColumns;

/**
 * Created by frankenliu on 2015/6/2.
 */
public class CampusModel {

    public static final class CampusInfoItemColumn implements BaseColumns {

        /** 单条校招信息的唯一标识(后台下发) */
        public static final String CAMPUS_ID = "campus_id";
        /** 发布此校招信息的公司名称 */
        public static final String COMPANY_NAME = "company_name";

        /** 发布此校招信息的公司简介 */
        public static final String COMPANY_INTRODUCTION = "company_introduction";

        /** 校招城市 */
        public static final String CITY = "city";

        /** 此校招信息的所属类别 */
        public static final String TYPE = "type";

        /** 此校招信息的标题 */
        public static final String TITLE = "title";

        /** 此校招信息的内容 */
        public static final String CONTENT = "content";

        /** 此校招的举行地址 */
        public static final String ADDRESS = "address";

        /** 此校招的举行时间 */
        public static final String TIME = "time";

        /** 此校招信息在后台的唯一版本号(更新原校招信息版本号增加) */
        public static final String VERSION = "version";

        /** 用户是否已经设置此校招的提醒 */
        public static final String IS_REMIND = "is_remind";

        /** 用户设置此校招的提醒频率类型 */
        public static final String REMIND_TYPE = "remind_type";

        /** 用户设置此校招的提醒时间(只有REMIND_TYPE为自定义时有效) */
        public static final String REMIND_TIME = "remind_time";

        /** 用户是否已收藏该校招 */
        public static final String IS_SAVE = "is_save";
    }
}
