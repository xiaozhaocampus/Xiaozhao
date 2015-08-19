package com.campus.xiaozhao.basic.db;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.component.logger.Logger;

import java.util.ArrayList;

/**
 * Created by frankenliu on 2015/8/15.
 */
public class CampusProvider extends ContentProvider{

    private static final String TAG = "CampusProvider";
    private CampusDBHelper mDBHelper;
    private Context mContext;

    @Override
    public boolean onCreate() {
        mContext = getContext();
        mDBHelper = new CampusDBHelper(mContext);
        initDbIfNotExist();
        return false;
    }

    private void initDbIfNotExist() {
        try {
            mDBHelper.getWritableDatabase();
        } catch (Exception e) {
            Logger.w(TAG, e);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = getReadDatabase();
        if(db == null ) {
            return null;
        }
        Cursor cursor = query(db, getTable(uri), projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = getWriteDatabase();
        if(db == null ) {
            return null;
        }

        long id = insert(db, getTable(uri), null, values);
        Uri insertUri = ContentUris.withAppendedId(uri, id);
        mContext.getContentResolver().notifyChange(uri, null);
        return insertUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = getWriteDatabase();
        if(db == null ) {
            return -1;
        }
        
        int id = delete(db, getTable(uri), selection, selectionArgs);
        mContext.getContentResolver().notifyChange(uri, null);
        return id;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = getWriteDatabase();
        if(db == null ) {
            return -1;
        }

        int id = -1;
        id = update(db, getTable(uri), values, selection, selectionArgs);
        mContext.getContentResolver().notifyChange(uri, null);
        return id;
    }

    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        final SQLiteDatabase db =getWriteDatabase();
        db.beginTransaction();//开始事务
        try{
            ContentProviderResult[] results = super.applyBatch(operations);
            db.setTransactionSuccessful();//设置事务标记为successful
            return results;
        }finally {
            db.endTransaction();//结束事务
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
    
    private String getTable(Uri uri) throws IllegalArgumentException {
        String table = "";
        switch (CampusUriFactory.getMatcher().match(uri)) {
            case CampusUriFactory.URI_MATCH_CAMPUS_INFO:
            	table = CampusDBHelper.TABLE_CAMPUS_INFO;
            	break;
            case CampusUriFactory.URI_MATCH_CAMPUS_USER_INFO:
            	table = CampusDBHelper.TABLE_CAMPUS_USER_INFO;
            	break;
            default:
                throw new IllegalArgumentException("this is unknow uri:" + uri);
        }
        return table;
    }
    
}
