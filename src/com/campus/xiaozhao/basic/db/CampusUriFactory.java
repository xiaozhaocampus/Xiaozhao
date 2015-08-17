package com.campus.xiaozhao.basic.db;

import android.content.UriMatcher;
import android.net.Uri;

/**
 * Created by frankenliu on 2015/8/15.
 */
public class CampusUriFactory {

    // 若不匹配采用UriMatcher.NO_MATCH(-1)返回
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private static final String URI_AUTHORITY = "com.campus.xiaozhao.providers.CampusProvider";

    private static final String ACTION_OPERATE_CAMPUS_INFO = "campus_info";
    // 匹配码
    public static final int URI_MATCH_CAMPUS_INFO = 1;

    static
    {
        // 对等待匹配的URI进行匹配操作，必须符合cn.xyCompany.providers.personProvider/person格式
        // 匹配返回CODE_NOPARAM，不匹配返回-1
        MATCHER.addURI(URI_AUTHORITY, ACTION_OPERATE_CAMPUS_INFO, URI_MATCH_CAMPUS_INFO);
    }

    public static Uri getCampusInfoUri() {
        return Uri.withAppendedPath(Uri.parse("content://" + URI_AUTHORITY), ACTION_OPERATE_CAMPUS_INFO);
    }

    public static String getUriAuthority() {
        return "content://" + URI_AUTHORITY;
    }

    public static UriMatcher getMatcher() {
        return MATCHER;
    }
}
