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

        /** 发布此校招信息的时间 */
        public static final String PUBLISH_TIME = "publish_time";

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

        /** 此校招信息的来源 */
        public static final String SOURCE = "source";

        /** 此校招信息是否被后台标记为删除 */
        public static final String IS_DELETE = "is_delete";
    }
    
    public static final class CampusUserColumn implements BaseColumns {
    	/**用户唯一标识*/
        public static final String USER_ID = "user_id";
        /**用户名*/
        public static final String USER_NAME = "user_name";
        /** 用户头像*/
        public static final String USER_PHOTO = "user_photo";
        /** 用户昵称*/
        public static final String USER_NICKNAME = "user_nickname";
        /** 用户性别*/
        public static final String USER_GENDER = "user_gender";
        /** 用户手机*/
        public static final String USER_PHONE_NUM = "user_phone_num";
        /** 用户邮箱*/
        public static final String USER_EMAIL = "user_email";
        /** 用户学校*/
        public static final String USER_SCHOOL = "user_school";
        /** 用户专业*/
        public static final String USER_MAJOR = "user_major";
        /** 用户年级*/
        public static final String USER_CLASS = "user_class";
        /** 用户 数据状态*/
        public static final String USER_LOGIN_TIME = "user_login_time";
        /** 用户 数据状态*/
        public static final String USER_DATA_STATUS = "user_data_status";
    }
    
}
