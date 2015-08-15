package com.campus.xiaozhao.basic.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.campus.xiaozhao.basic.data.CampusInfo;
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

    private static Context mContext;

    private CampusDBProcessor(Context context) {
    }

    public static CampusDBProcessor getInstance(Context context) {
        if(mInstance == null) {
            synchronized (CampusDBProcessor.class) {
                if(mInstance == null) {
                    mInstance = new CampusDBProcessor(context);
                    mContext = context;
                }
            }
        }
        return mInstance;
    }

    public void addCampusInfo(CampusInfoItemData itemData) {
        final ContentValues values = new ContentValues();
        itemData.onAddToDatabase(values);
        mContext.getContentResolver().insert(CampusUriFactory.getCampusInfoUri(), values);
    }

    public void addCampusInfo(List<CampusInfoItemData> list) {
        if(list == null && list.size() < 1) {
            return;
        }
        try {
            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            for(CampusInfoItemData itemData : list) {
                ContentValues values = new ContentValues();
                itemData.onAddToDatabase(values);
               ops.add(ContentProviderOperation.newInsert(CampusUriFactory.getCampusInfoUri()).withValues(values).build());
            }
            mContext.getContentResolver().applyBatch(CampusUriFactory.getCampusInfoUri().getAuthority(), ops);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    public void deleteCampusInfo(String whereClause, String[] whereArgs) {
        mContext.getContentResolver().delete(CampusUriFactory.getCampusInfoUri(), whereClause, whereArgs);
    }

    public void deleteCampusIfoByCampusId(String campusId) {
        String whereClause = CampusModel.CampusInfoItemColumn.CAMPUS_ID + "= ? ";
        String[] whereArgs = new String[]{campusId};
        mContext.getContentResolver().delete(CampusUriFactory.getCampusInfoUri(), whereClause, whereArgs);
    }

    public void updateCampus(CampusInfoItemData itemData) {
        if(itemData == null) {
            return;
        }
        final ContentValues values = new ContentValues();
        itemData.onAddToDatabase(values);

        String whereClause = CampusModel.CampusInfoItemColumn.CAMPUS_ID + " =?";
        String[] whereArgs = new String[]{String.valueOf(itemData.getCampusID())};
        mContext.getContentResolver().update(CampusUriFactory.getCampusInfoUri(), values, whereClause, whereArgs);
    }

    public List<CampusInfoItemData> getCampusInfos(String whereClause, String[] whereArgs, String sortOrder) {
        List<CampusInfoItemData> list = null;
        final Cursor cursor = mContext.getContentResolver().query(CampusUriFactory.getCampusInfoUri(), CAMPUS_INFO, whereClause, whereArgs, sortOrder);
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

    public Cursor query(String whereClause, String[] whereArgs, String sortOrder){
        final Cursor cursor = mContext.getContentResolver().query(CampusUriFactory.getCampusInfoUri(), CAMPUS_INFO, whereClause, whereArgs, sortOrder);
        return cursor;
    }
    
    public CampusInfoItemData getCampusInfoByCampsuID(String campusID) {
        String whereClause = CampusModel.CampusInfoItemColumn.CAMPUS_ID + " =?";
        String[] whereArgs = new String[] {String.valueOf(campusID)};
        final Cursor cursor =  mContext.getContentResolver().query(CampusUriFactory.getCampusInfoUri(), CAMPUS_INFO, whereClause, whereArgs, null);
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
