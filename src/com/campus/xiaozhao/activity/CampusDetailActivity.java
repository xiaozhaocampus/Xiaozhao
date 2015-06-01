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
import com.campus.xiaozhao.basic.utils.DateUtils;
import com.component.logger.Logger;
/**
 * Created by frankenliu on 15/5/31.
 */
public class CampusDetailActivity extends Activity {
    private TextView mIntroduce, mContent, mTime, mAddress;
    private Button mSetRemind;
    private CampusInfoItemData mItemData;
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
        //TODO 根据条件设定是否需要提醒
        mSetRemind.setText("提醒");
        mSetRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 设置定时提醒
               int result = CampusAlarmManager.getInstance().setCampusAlarm(CampusDetailActivity.this, mItemData.getTime());
                if(result > 0) {
                    mSetRemind.setText("已设提醒");
                    mSetRemind.setClickable(false);
                }
            }
        });
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
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.info_share:
                Logger.d("franken", "share");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}