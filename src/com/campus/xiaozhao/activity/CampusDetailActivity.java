package com.campus.xiaozhao.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.campus.xiaozhao.R;
import com.campus.xiaozhao.basic.alarm.CampusAlarmManager;
import com.campus.xiaozhao.basic.data.CampusInfoItemData;
import com.campus.xiaozhao.basic.db.CampusDBProcessor;
import com.campus.xiaozhao.basic.utils.DateUtils;
import com.campus.xiaozhao.basic.utils.RemindType;
import com.component.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by frankenliu on 15/5/31.
 */
public class CampusDetailActivity extends Activity {
    private static final String TAG = "CampusDetailActivity";
    private TextView mCompanyName, mPublishTime, mInformationCome, mJobTime, mAddress, mProvince, mCompanyIntrodction;
    private CampusInfoItemData mItemData;
    private CampusDBProcessor mDBProcessor;
    private String[] mTypes = null; // 提醒类型, 用于用户点击确定后可选的范围
    private String mType;
    private View mContentView;

    private ImageView mSave; // actionBar中得收藏按钮

    private RelativeLayout mRemindTime, mLocation;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campus_detail);

        Intent intent = getIntent();
        mItemData = (CampusInfoItemData)intent.getSerializableExtra("detail_data");
        mDBProcessor = CampusDBProcessor.getInstance(getApplicationContext());
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setCustomView(R.layout.campus_detail_action_layout);
        initActionBar(actionBar);

        mContentView = findViewById(R.id.campus_detail_view);
        mRemindTime = (RelativeLayout) findViewById(R.id.campus_detail_time);
        mLocation = (RelativeLayout) findViewById(R.id.campus_detail_location);
        mCompanyName = (TextView) findViewById(R.id.company_name);
        mPublishTime = (TextView) findViewById(R.id.publish_time);
        String time = getResources().getString(R.string.publish_time);
        time = String.format(time, DateUtils.transferTimeToDate(mItemData.getPtime(), "yyyy-MM-dd"));
        mPublishTime.setText(time);
        mInformationCome = (TextView) findViewById(R.id.information_come);
        String source = getResources().getString(R.string.campus_source);
        source = String.format(source, mItemData.getSource());
        mInformationCome.setText(source);
        mJobTime = (TextView) findViewById(R.id.campus_time);
        mAddress = (TextView) findViewById(R.id.campus_detail_school);
        mProvince = (TextView) findViewById(R.id.campus_detail_province);
        mProvince.setText(mItemData.getCity());
        mCompanyIntrodction = (TextView) findViewById(R.id.company_introduction);
        mCompanyName.setText(mItemData.getCompany());
        mJobTime.setText(DateUtils.transferTimeToDate(mItemData.getTime()));
        mAddress.setText(mItemData.getAddress());
        mCompanyIntrodction.setText(mItemData.getContent());
        setButtonState();
    }

    private void initActionBar(ActionBar actionBar) {
        mSave = (ImageView) actionBar.getCustomView().findViewById(R.id.campus_detail_action_image_save);
        actionBar.getCustomView().findViewById(R.id.campus_detail_action_image_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CampusDetailActivity.this.finish();
            }
        });
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 点击收藏按钮
                if (mDBProcessor != null) {
                    CampusInfoItemData itemData = mDBProcessor.getCampusInfoByCampsuID(mItemData.getCampusID());
                    if(itemData == null) {
                        mItemData.setIsSave(true);
                        mDBProcessor.addCampusInfo(mItemData);
                        mSave.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.campus_detail_image_save_on));
                        Toast.makeText(getApplicationContext(), "收藏成功", Toast.LENGTH_LONG).show();
                    } else {
                        if(!itemData.isSave()) {
                            mItemData.setIsSave(true);
                            mDBProcessor.updateCampus(mItemData);
                            mSave.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.campus_detail_image_save_on));
                            Toast.makeText(getApplicationContext(), "收藏成功", Toast.LENGTH_LONG).show();
                        } else {
                            mItemData.setIsSave(false);
                            mDBProcessor.updateCampus(mItemData);
                            mSave.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.campus_detail_image_save_off));
                            Toast.makeText(getApplicationContext(), "取消收藏", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
        actionBar.getCustomView().findViewById(R.id.campus_detail_action_image_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = getMagicDrawingCache(mContentView);
                        String folder = android.os.Environment.getExternalStorageDirectory() + File.separator + ".campus";
                        String filePath = folder + File.separator + "viewTempFile.png";
                        try {
                            File destDir = new File(folder);
                            if(!destDir.exists()) {
                                destDir.mkdirs();
                            }
                            File file = new File(filePath);
                            if(file.exists()) {
                                file.delete();
                            }
                            file.createNewFile();
                            FileOutputStream stream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 85, stream);
                            stream.flush();
                            stream.close();

                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            Uri uri = Uri.fromFile(file);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                            shareIntent.setType("image/png");
                            startActivity(Intent.createChooser(shareIntent, "share"));
                        } catch (Exception e) {
                            Logger.crash(TAG, e);
                        }
                    }
                });
                thread.start();
            }
        });
    }

    public static Bitmap getMagicDrawingCache(View view) {
        int viewWidth = view.getWidth();
        int viewHeight = view.getHeight();
        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createBitmap(viewWidth, viewHeight,
                    Bitmap.Config.ARGB_8888);
        } catch (Exception e) {
            Logger.i(TAG, "createBitmap ===bitmap width==>" + viewWidth
                    + " bitmap height==>" + viewHeight);
            e.printStackTrace();
        }

        if (bitmap == null) {
            return null;
        }

        Logger.i(
                TAG,
                "before ===bitmap bytes==>"
                        + (bitmap != null ? bitmap.getByteCount() : 0));
        bitmap.eraseColor(Color.TRANSPARENT);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        Logger.i(
                TAG,
                "after ===bitmap bytes==>"
                        + (bitmap != null ? bitmap.getByteCount() : 0));
        return bitmap;
    }

    /**
     * 设置Button的状态
     */
    private void setButtonState() {
        final CampusInfoItemData itemData = mDBProcessor.getCampusInfoByCampsuID(mItemData.getCampusID());
        if(itemData != null) {
            mItemData.setIsRemind(itemData.isRemind());
            mItemData.setRemindType(itemData.getRemindType());
            mItemData.setIsSave(itemData.isSave());

            if(mItemData.isSave()) {
                mSave.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.campus_detail_image_save_on));
            }
        }

        if(itemData != null && itemData.isRemind()) { // 该校招信息已经设置过定时提醒
            if(itemData.getTime() != mItemData.getTime()) {
                // 重新设置定时
                CampusAlarmManager.getInstance().stopAlarm(CampusDetailActivity.this, mItemData.getCampusID());
                CampusAlarmManager.getInstance().setCampusAlarm(CampusDetailActivity.this, mItemData.getTime(), mItemData.getCampusID());

                // 更新本地校招信息
                mDBProcessor.updateCampus(mItemData);
            }
        } else { // 该校招信息没有设置过定时提醒
            mRemindTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 检查校招时间跟当前时间的差值
                    int checkRes = DateUtils.checkTime(mItemData.getTime());

                    switch (checkRes) {
                        case -1:
                            Toast.makeText(CampusDetailActivity.this, "信息已过期!", Toast.LENGTH_LONG).show();
                            break;
                        case 1:
                            mTypes = new String[]{RemindType.REMIND_NAME_15_MINUTES, RemindType.REMIND_NAME_30_MINUTES,
                                    RemindType.REMIND_NAME_1_HOUR, RemindType.REMIND_NAME_3_HOUR, RemindType.REMIND_NAME_6_HOUR};
                            break;
                        case 2:
                            mTypes = new String[]{RemindType.REMIND_NAME_15_MINUTES, RemindType.REMIND_NAME_30_MINUTES,
                                    RemindType.REMIND_NAME_1_HOUR, RemindType.REMIND_NAME_3_HOUR};
                            break;
                        case 3:
                            mTypes = new String[]{RemindType.REMIND_NAME_15_MINUTES, RemindType.REMIND_NAME_30_MINUTES,
                                    RemindType.REMIND_NAME_1_HOUR};
                            break;
                        case 4:
                            mTypes = new String[]{RemindType.REMIND_NAME_15_MINUTES, RemindType.REMIND_NAME_30_MINUTES};
                            break;
                        case 5:
                            mTypes = new String[]{RemindType.REMIND_NAME_15_MINUTES};
                            break;
                        case 6:
                            Toast.makeText(CampusDetailActivity.this, "距离校招开始还有不到6小时的时间, 请做好准备!", Toast.LENGTH_LONG).show();
                            break;

                    }
                    if (mTypes != null) {
                        mType = mTypes[0];
                        new AlertDialog.Builder(CampusDetailActivity.this).setTitle("请选择提醒时间")
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setSingleChoiceItems(mTypes, 0, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Logger.d(TAG, "which:" + which);
                                        mType = mTypes[which];
                                    }
                                })
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        long remindTime = 0;
                                        if (RemindType.REMIND_NAME_6_HOUR.equals(mType)) {
                                            mItemData.setRemindType(5);
                                            remindTime = mItemData.getTime() - DateUtils.REMIND_TIME_DISTANCE_6_HOUR;
                                        } else if (RemindType.REMIND_NAME_3_HOUR.equals(mType)) {
                                            mItemData.setRemindType(4);
                                            remindTime = mItemData.getTime() - DateUtils.REMIND_TIME_DISTANCE_3_HOUR;
                                        } else if (RemindType.REMIND_NAME_1_HOUR.equals(mType)) {
                                            mItemData.setRemindType(3);
                                            remindTime = mItemData.getTime() - DateUtils.REMIND_TIME_DISTANCE_1_HOUR;
                                        } else if (RemindType.REMIND_NAME_30_MINUTES.equals(mType)) {
                                            mItemData.setRemindType(2);
                                            remindTime = mItemData.getTime() - DateUtils.REMIND_TIME_DISTANCE_30_MINUTES;
                                        } else if (RemindType.REMIND_NAME_15_MINUTES.equals(mType)) {
                                            mItemData.setRemindType(1);
                                            remindTime = mItemData.getTime() - DateUtils.REMIND_TIME_DISTANCE_15_MINUTES;
                                        }

                                        CampusInfoItemData infoItemData = mDBProcessor.getCampusInfoByCampsuID(mItemData.getCampusID());
                                        if (infoItemData != null) {
                                            mItemData.setRemindTime(remindTime);
                                            mItemData.setIsRemind(true);
                                            mDBProcessor.updateCampus(mItemData);
                                            CampusAlarmManager.getInstance().stopAlarm(getApplicationContext(), infoItemData.getCampusID());
                                        } else {
                                            mItemData.setRemindTime(remindTime);
                                            mItemData.setIsRemind(true);
                                            mDBProcessor.addCampusInfo(mItemData); // 这个地方先入库，数据库ID在设置定时提醒时唯一标示一个定时提醒
                                            mSave.setClickable(false);
                                            mSave.setBackground(getResources().getDrawable(R.drawable.campus_detail_image_save_on));
                                        }
                                        CampusAlarmManager.getInstance().startAlarm(CampusDetailActivity.this, remindTime, mItemData.getCampusID());
                                        Toast.makeText(CampusDetailActivity.this, "系统将于" + DateUtils.transferTimeToDate(remindTime) + "提醒您!", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .show();
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.campus_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.info_share:
                Logger.d(TAG, "share");
                break;
            case R.id.info_save:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}