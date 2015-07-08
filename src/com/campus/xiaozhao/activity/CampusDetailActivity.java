package com.campus.xiaozhao.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.campus.xiaozhao.R;
import com.campus.xiaozhao.basic.alarm.CampusAlarmManager;
import com.campus.xiaozhao.basic.data.CampusInfoItemData;
import com.campus.xiaozhao.basic.db.CampusDBProcessor;
import com.campus.xiaozhao.basic.utils.DateUtils;
import com.campus.xiaozhao.basic.utils.RemindType;
import com.component.logger.Logger;
/**
 * Created by frankenliu on 15/5/31.
 */
public class CampusDetailActivity extends Activity {
    private static final String TAG = "CampusDetailActivity";
    private TextView mIntroduce, mContent, mTime, mAddress;
    private Button mSetRemind;
    private CampusInfoItemData mItemData;
    private CampusDBProcessor mDBProcessor;
    private String[] mTypes = null; // 提醒类型, 用于用户点击确定后可选的范围
    private String mType;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campus_detail);

        Intent intent = getIntent();
        mItemData = (CampusInfoItemData)intent.getSerializableExtra("detail_data");
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(mItemData.getCompany());

        mIntroduce = (TextView) findViewById(R.id.company_introduction);
        mContent = (TextView) findViewById(R.id.content);
        mAddress = (TextView) findViewById(R.id.address);
        mTime = (TextView) findViewById(R.id.time);
        mSetRemind = (Button) findViewById(R.id.set_remind);
        mIntroduce.setText(mItemData.getIntroduction());
        mContent.setText(mItemData.getContent());
        mAddress.setText(mItemData.getAddress());
        mTime.setText(DateUtils.transferTimeToDate(mItemData.getTime()));

        mDBProcessor = CampusDBProcessor.getInstance(getApplicationContext());
        setButtonState();
    }

    /**
     * 设置Button的状态
     */
    private void setButtonState() {
        // 校招时间小于或等于当前时间
        if(mItemData.getTime() <= System.currentTimeMillis()) {
            mSetRemind.setText("信息过期");
            mSetRemind.setClickable(false);
            return;
        }
        final CampusInfoItemData itemData = mDBProcessor.getCampusInfoByCampsuID(mItemData.getCampusID());
        if(itemData != null) {
            mItemData.setIsRemind(itemData.isRemind());
            mItemData.setRemindType(itemData.getRemindType());
        }

        if(itemData != null && itemData.isRemind()) { // 该校招信息已经设置过定时提醒
            mSetRemind.setText("已设提醒");
            mSetRemind.setClickable(false);
            if(itemData.getTime() != mItemData.getTime()) {
                // 重新设置定时
                CampusAlarmManager.getInstance().stopAlarm(CampusDetailActivity.this, mItemData.getCampusID());
                CampusAlarmManager.getInstance().setCampusAlarm(CampusDetailActivity.this, mItemData.getTime(), mItemData.getCampusID());

                // 更新本地校招信息
                mDBProcessor.updateCampus(mItemData);
            }
        } else { // 该校招信息没有设置过定时提醒
            mSetRemind.setText("提醒");
            mSetRemind.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 检查校招时间跟当前时间的差值
                    int checkRes =DateUtils.checkTime(mItemData.getTime());

                    switch (checkRes) {
                        case -1:
                            Toast.makeText(CampusDetailActivity.this, "信息已过期!", Toast.LENGTH_LONG).show();
                            break;
                        case 1:
                            mTypes = new String[] {RemindType.REMIND_NAME_15_MINUTES, RemindType.REMIND_NAME_30_MINUTES,
                                    RemindType.REMIND_NAME_1_HOUR, RemindType.REMIND_NAME_3_HOUR, RemindType.REMIND_NAME_6_HOUR};
                            break;
                        case 2:
                            mTypes = new String[] {RemindType.REMIND_NAME_15_MINUTES, RemindType.REMIND_NAME_30_MINUTES,
                                    RemindType.REMIND_NAME_1_HOUR, RemindType.REMIND_NAME_3_HOUR};
                            break;
                        case 3:
                            mTypes = new String[] {RemindType.REMIND_NAME_15_MINUTES, RemindType.REMIND_NAME_30_MINUTES,
                                    RemindType.REMIND_NAME_1_HOUR};
                            break;
                        case 4:
                            mTypes = new String[] {RemindType.REMIND_NAME_15_MINUTES, RemindType.REMIND_NAME_30_MINUTES};
                            break;
                        case 5:
                            mTypes = new String[] {RemindType.REMIND_NAME_15_MINUTES};
                            break;
                        case 6:
                            Toast.makeText(CampusDetailActivity.this, "距离校招开始还有不到6小时的时间, 请做好准备!", Toast.LENGTH_LONG).show();
                            break;

                    }
                    if(mTypes != null) {
                        mType =  mTypes[0];
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

                                        mItemData.setRemindTime(remindTime);
                                        mItemData.setIsRemind(true);
                                        mDBProcessor.addCampusInfo(mItemData); // 这个地方先入库，数据库ID在设置定时提醒时唯一标示一个定时提醒
                                        CampusAlarmManager.getInstance().startAlarm(CampusDetailActivity.this, remindTime, mItemData.getCampusID());
                                        mSetRemind.setText("已设提醒");
                                        mSetRemind.setClickable(false);
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.campus_detail, menu);
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
        }
        return super.onOptionsItemSelected(item);
    }
}