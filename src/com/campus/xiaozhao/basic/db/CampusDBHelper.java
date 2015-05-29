package com.campus.xiaozhao.basic.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by frankenliu on 15/5/29.
 */
public class CampusDBHelper extends SQLiteOpenHelper {

    /**
     * 升级记录:
     * VER_CURRENT = 1: 初始化DB
     */
    public static final int VER_CURRENT = 1;
    public static final String DB_NAME = "campus_info.db";

    public CampusDBHelper(Context context) {
        this(context, DB_NAME, null, VER_CURRENT);
    }

    public CampusDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
