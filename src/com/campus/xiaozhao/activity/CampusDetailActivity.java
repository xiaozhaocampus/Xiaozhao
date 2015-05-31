package com.campus.xiaozhao.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
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
import com.campus.xiaozhao.basic.data.CampusInfoItemData;
import com.campus.xiaozhao.basic.utils.DateUtils;
import com.component.logger.Logger;

import java.text.SimpleDateFormat;

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
                long remindTime = mItemData.getTime() - 4 * 60 * 60 * 1000;
                //TODO 设置定时提醒

                Toast.makeText(CampusDetailActivity.this, "系统将于" +DateUtils.transferTimeToDate(remindTime) + "提醒您!", Toast.LENGTH_LONG).show();
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

    class AlarmReceive extends BroadcastReceiver {
        private NotificationManager manager;
        @Override
        public void onReceive(Context context, Intent intent) {
            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Intent playIntent = new Intent(context, CampusDetailActivity.class);
            playIntent.putExtra("", "");
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//            builder.setContentTitle("title").setContentText("提醒内容").setSmallIcon(R.drawable.app_icon).setDefaults(Notification.DEFAULT_ALL).setContentIntent(pendingIntent).setAutoCancel(true).setSubText("二级text");
//            manager.notify(1, builder.build());

        }
    }
}