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

    /** 校招信息表 */
    public static final String TABLE_CAMPUS_INFO = "campus_info";

    public static final String CREATE_TABLE_CAMPUS_INFO =
            "CREATE TABLE IF NOT EXISTS " + TABLE_CAMPUS_INFO + " ("
            + CampusModel.CampusInfoItemColumn._ID + " INTEGER PRIMARY KEY,"
            + CampusModel.CampusInfoItemColumn.CAMPUS_ID + " LONG UNIQUE,"
            + CampusModel.CampusInfoItemColumn.COMPANY_NAME + " TEXT,"
            + CampusModel.CampusInfoItemColumn.PUBLISH_TIME + " LONG DEFAULT -1,"
            + CampusModel.CampusInfoItemColumn.CITY + " TEXT,"
            + CampusModel.CampusInfoItemColumn.TYPE + " TEXT,"
            + CampusModel.CampusInfoItemColumn.TITLE + " TEXT,"
            + CampusModel.CampusInfoItemColumn.CONTENT + " TEXT,"
            + CampusModel.CampusInfoItemColumn.ADDRESS + " TEXT,"
            + CampusModel.CampusInfoItemColumn.TIME + " LONG DEFAULT -1,"
            + CampusModel.CampusInfoItemColumn.VERSION + " LONG DEFAULT -1,"
            + CampusModel.CampusInfoItemColumn.IS_REMIND + " INTEGER NOT NULL DEFAULT 0,"
            + CampusModel.CampusInfoItemColumn.REMIND_TYPE + " INTEGER NOT NULL DEFAULT 0,"
            + CampusModel.CampusInfoItemColumn.REMIND_TIME + " LONG DEFAULT -1,"
            + CampusModel.CampusInfoItemColumn.IS_SAVE + " INTEGER NOT NULL DEFAULT 0,"
            + CampusModel.CampusInfoItemColumn.SOURCE + " TEXT);";

    /** 删除 校招信息数据表 */
    public static final String DROP_TABLE_CAMPUS_INFO = "DROP TABLE IF EXISTS " + TABLE_CAMPUS_INFO;

    public CampusDBHelper(Context context) {
        super(context, DB_NAME, null, VER_CURRENT);
    }

    public CampusDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CAMPUS_INFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_CAMPUS_INFO);

        // 重新建表
        db.execSQL(CREATE_TABLE_CAMPUS_INFO);
    }
}
