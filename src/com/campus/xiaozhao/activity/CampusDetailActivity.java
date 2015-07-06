package com.campus.xiaozhao.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.campus.xiaozhao.R;
import com.campus.xiaozhao.basic.alarm.CampusAlarmManager;
import com.campus.xiaozhao.basic.data.CampusInfoItemData;
import com.campus.xiaozhao.basic.db.CampusDBProcessor;
import com.campus.xiaozhao.basic.utils.DateUtils;
import com.component.logger.Logger;
/**
 * Created by frankenliu on 15/5/31.
 */
public class CampusDetailActivity extends Activity {
    private TextView mIntroduce, mContent, mTime, mAddress;
    private Button mSetRemind;
    private CampusInfoItemData mItemData;
    private CampusDBProcessor mDBProcessor;
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
                    long checkRes = CampusAlarmManager.getInstance().checkTime(mItemData.getTime());
                    if(checkRes > 0) {
                        mItemData.setIsRemind(true);
                        mItemData.setRemindType(1);
                        mDBProcessor.addCampusInfo(mItemData); // 这个地方先入库，数据库ID在设置定时提醒时唯一标示一个定时提醒
                        CampusAlarmManager.getInstance().startAlarm(CampusDetailActivity.this, checkRes, mItemData.getCampusID());
                        mSetRemind.setText("已设提醒");
                        mSetRemind.setClickable(false);
                    }
                    CampusAlarmManager.getInstance().showTips(CampusDetailActivity.this, checkRes);
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
                Logger.d("franken", "share");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}