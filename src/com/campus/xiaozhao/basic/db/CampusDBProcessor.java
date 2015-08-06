package com.campus.xiaozhao.basic.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.campus.xiaozhao.basic.data.CampusInfoItemData;
import com.component.logger.Logger;

/**
 * Created by frankenliu on 2015/6/2.
 */
public class CampusDBProcessor {

    private static final String TAG = "CampusDBProcessor";

    private static final String[] CAMPUS_INFO = {
            CampusModel.CampusInfoItemColumn._ID,
            CampusModel.CampusInfoItemColumn.CAMPUS_ID,
            CampusModel.CampusInfoItemColumn.COMPANY_NAME,
            CampusModel.CampusInfoItemColumn.PUBLISH_TIME,
            CampusModel.CampusInfoItemColumn.CITY,
            CampusModel.CampusInfoItemColumn.TYPE,
            CampusModel.CampusInfoItemColumn.TITLE,
            CampusModel.CampusInfoItemColumn.CONTENT,
            CampusModel.CampusInfoItemColumn.ADDRESS,
            CampusModel.CampusInfoItemColumn.TIME,
            CampusModel.CampusInfoItemColumn.VERSION,
            CampusModel.CampusInfoItemColumn.IS_REMIND,
            CampusModel.CampusInfoItemColumn.REMIND_TYPE,
            CampusModel.CampusInfoItemColumn.REMIND_TIME,
            CampusModel.CampusInfoItemColumn.IS_SAVE,
            CampusModel.CampusInfoItemColumn.SOURCE
    };

    private static volatile CampusDBProcessor mInstance;

    private CampusDBHelper mDBHelper;

    private CampusDBProcessor(Context context) {
        mDBHelper = new CampusDBHelper(context);
        initDbIfNotExist();
    }

    public static CampusDBProcessor getInstance(Context context) {
        if(mInstance == null) {
            synchronized (CampusDBProcessor.class) {
                if(mInstance == null) {
                    mInstance = new CampusDBProcessor(context);
                }
            }
        }
        return mInstance;
    }

    private void initDbIfNotExist() {
        try {
            mDBHelper.getWritableDatabase();
        } catch (Exception e) {
            Logger.w(TAG, e);
        }
    }

    /******************************************************************  数据库基本封装开始 *****************************************************************************************/
    private SQLiteDatabase getWriteDatabase() {
        try {
            return mDBHelper.getWritableDatabase();
        } catch (Exception e) {
            Logger.w(TAG, e);
            return null;
        }
    }

    private SQLiteDatabase getReadDatabase() {
        try {
            return mDBHelper.getReadableDatabase();
        } catch (Exception e) {
            Logger.w(TAG, e);
            return null;
        }
    }

    private int delete(SQLiteDatabase db, String table, String whereClause, String[] whereArgs) {
        try {
            return db.delete(table, whereClause, whereArgs);
        } catch (Exception e) {
            Logger.w(TAG, e.getMessage());
            return -1;
        }
    }

    private int update(SQLiteDatabase db, String table, ContentValues values, String whereClause, String[] whereArgs) {
        try {
            return db.update(table, values, whereClause, whereArgs);
        } catch (Exception e) {
            Logger.w(TAG, e.getMessage());
            return -1;
        }
    }

    private long insert(SQLiteDatabase db, String table, String nullColumnHack, ContentValues values) {
        try {
            return db.insert(table, nullColumnHack, values);
        } catch (Exception e) {
            Logger.w(TAG, e.getMessage());
            return -1;
        }
    }

    private Cursor query(SQLiteDatabase db, String table, String[] columns, String selection,
                         String[] selectionArgs, String groupBy, String having,
                         String orderBy) {
        try {
            return db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        } catch (Exception e) {
            Logger.w(TAG, e.getMessage());
            return null;
        }
    }
    /******************************************************************  数据库基本封装结束 *****************************************************************************************/

    public void addCampusInfo(CampusInfoItemData itemData) {
        SQLiteDatabase db = getWriteDatabase();
        if(db == null ) {
            return;
        }

        final ContentValues values = new ContentValues();
        itemData.onAddToDatabase(values);
        insert(db, CampusDBHelper.TABLE_CAMPUS_INFO, null, values);
    }

    public void addCampusInfo(List<CampusInfoItemData> list) {
        if(list == null && list.size() < 1) {
            return;
        }
        SQLiteDatabase db = getWriteDatabase();
        if(db == null ) {
            return;
        }
        try {
            db.beginTransaction();
            for(CampusInfoItemData itemData : list) {
                ContentValues values = new ContentValues();
                itemData.onAddToDatabase(values);
                insert(db, CampusDBHelper.TABLE_CAMPUS_INFO, null, values);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Logger.e(TAG, e);
        } finally {
            try {
                db.endTransaction();
            } catch (Exception e) {
                Logger.e(TAG, e);
            }
        }
    }

    public void deleteCampusInfo(String whereClause, String[] whereArgs) {
        SQLiteDatabase db = getWriteDatabase();
        if(db == null ) {
            return;
        }
        delete(db, CampusDBHelper.TABLE_CAMPUS_INFO, whereClause, whereArgs);
    }

    public void deleteCampusIfoByCampusId(String campusId) {
        SQLiteDatabase db = getWriteDatabase();
        if(db == null ) {
            return;
        }
        String whereClause = CampusModel.CampusInfoItemColumn.CAMPUS_ID + "= ? ";
        String[] whereArgs = new String[]{campusId};
        delete(db, CampusDBHelper.TABLE_CAMPUS_INFO, whereClause, whereArgs);
    }

    public void updateCampus(CampusInfoItemData itemData) {
        if(itemData == null) {
            return;
        }
        SQLiteDatabase db = getWriteDatabase();
        if(db == null ) {
            return;
        }
        final ContentValues values = new ContentValues();
        itemData.onAddToDatabase(values);

        String whereClause = CampusModel.CampusInfoItemColumn.CAMPUS_ID + " =?";
        String[] whereArgs = new String[]{String.valueOf(itemData.getCampusID())};
        update(db, CampusDBHelper.TABLE_CAMPUS_INFO, values, whereClause, whereArgs);
    }

    public List<CampusInfoItemData> getCampusInfos(String whereClause, String[] whereArgs, String sortOrder) {
        SQLiteDatabase db = getWriteDatabase();
        if(db == null ) {
            return null;
        }
        List<CampusInfoItemData> list = null;
        final Cursor cursor = query(db, CampusDBHelper.TABLE_CAMPUS_INFO, CAMPUS_INFO, whereClause, whereArgs, null, null, sortOrder);
        if(cursor != null) {
            try {
                if(cursor.getCount() > 0) {
                    list = new ArrayList<CampusInfoItemData>();
                    while(cursor.moveToNext()) {
                        CampusInfoItemData itemData = new CampusInfoItemData();
                        itemData.setId(cursor.getLong(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn._ID)));
                        itemData.setCampusID(cursor.getString(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn.CAMPUS_ID)));
                        itemData.setCompany(cursor.getString(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn.COMPANY_NAME)));
                        itemData.setCity(cursor.getString(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn.CITY)));
                        itemData.setPtime(cursor.getLong(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn.PUBLISH_TIME)));
                        itemData.setType(cursor.getString(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn.TYPE)));
                        itemData.setTitle(cursor.getString(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn.TITLE)));
                        itemData.setContent(cursor.getString(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn.CONTENT)));
                        itemData.setAddress(cursor.getString(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn.ADDRESS)));
                        itemData.setTime(cursor.getLong(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn.TIME)));
                        itemData.setVersion(cursor.getLong(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn.VERSION)));
                        itemData.setIsRemind(cursor.getInt(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn.IS_REMIND)) > 0 ? true : false);
                        itemData.setRemindType(cursor.getInt(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn.REMIND_TYPE)));
                        itemData.setRemindTime(cursor.getLong(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn.REMIND_TIME)));
                        itemData.setIsSave(cursor.getInt(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn.IS_SAVE)) > 0 ? true : false);
                        itemData.setSource(cursor.getString(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn.SOURCE)));
                        list.add(itemData);
                    }
                }
            } catch (Exception e) {
                Logger.e(TAG, e);
                list = null;
            }  finally {
                try {
                    cursor.close();
                } catch (Exception e) {
                    Logger.e(TAG, e);
                }
            }
        }

        return list;
    }

    public CampusInfoItemData getCampusInfoByCampsuID(String campusID) {
        SQLiteDatabase db = getWriteDatabase();
        if(db == null ) {
            return null;
        }
        String whereClause = CampusModel.CampusInfoItemColumn.CAMPUS_ID + " =?";
        String[] whereArgs = new String[] {String.valueOf(campusID)};
        final Cursor cursor = query(db, CampusDBHelper.TABLE_CAMPUS_INFO, CAMPUS_INFO, whereClause, whereArgs, null, null, null);
        if(cursor != null) {
            try {
                if(cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        CampusInfoItemData itemData = new CampusInfoItemData();
                        itemData.setId(cursor.getLong(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn._ID)));
                        itemData.setCampusID(cursor.getString(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn.CAMPUS_ID)));
                        itemData.setCompany(cursor.getString(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn.COMPANY_NAME)));
                        itemData.setCity(cursor.getString(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn.CITY)));
                        itemData.setPtime(cursor.getLong(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn.PUBLISH_TIME)));
                        itemData.setType(cursor.getString(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn.TYPE)));
                        itemData.setTitle(cursor.getString(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn.TITLE)));
                        itemData.setContent(cursor.getString(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn.CONTENT)));
                        itemData.setAddress(cursor.getString(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn.ADDRESS)));
                        itemData.setTime(cursor.getLong(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn.TIME)));
                        itemData.setVersion(cursor.getLong(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn.VERSION)));
                        itemData.setIsRemind(cursor.getInt(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn.IS_REMIND)) > 0 ? true : false);
                        itemData.setRemindType(cursor.getInt(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn.REMIND_TYPE)));
                        itemData.setRemindTime(cursor.getLong(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn.REMIND_TIME)));
                        itemData.setIsSave(cursor.getInt(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn.IS_SAVE)) > 0 ? true : false);
                        itemData.setSource(cursor.getString(cursor.getColumnIndex(CampusModel.CampusInfoItemColumn.SOURCE)));
                        return itemData;
                    }
                }
            } catch (Exception e) {
                Logger.e(TAG, e);
            } finally {
                try {
                    cursor.close();
                } catch (Exception e) {
                    Logger.e(TAG, e);
                }
            }
        }
        return null;
    }
}
