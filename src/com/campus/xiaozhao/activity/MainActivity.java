package com.campus.xiaozhao.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.campus.xiaozhao.Environment;
import com.campus.xiaozhao.R;
import com.campus.xiaozhao.basic.data.CampusInfoItemData;
import com.campus.xiaozhao.basic.utils.CampusSharePreference;
import com.component.logger.Logger;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private ListView mCampusList;
    private CampusInfoAdapter mInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        int setupDelay = 0;
        if (Environment.ENABLE_SETUP_ACTIVITY) {
            if (!CampusSharePreference.isLogin(this)) {
	            SetupActivity.startFrom(this);
	            setupDelay = 500;
	            finish();
            }
        }
        
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setupUI();
            }
        }, setupDelay);
    }
    
    public static void startFrom(Context context) {
    	Intent intent = new Intent(context, MainActivity.class);
    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    		.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    		.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	context.startActivity(intent);
    }

    private void setupUI() {
        setContentView(R.layout.activity_main);
        mCampusList = (ListView) findViewById(R.id.campus_list);
        List<CampusInfoItemData> list = new ArrayList<CampusInfoItemData>();
        CampusInfoItemData data = new CampusInfoItemData();
        data.setCompany("tencent");
        data.setId(100000);
        data.setTitle("2015 campus employment");
        data.setTime(1436256758021L);
        data.setType("IT");
        list.add(data);
        mInfoAdapter = new CampusInfoAdapter(getApplicationContext(), list);
        mCampusList.setAdapter(mInfoAdapter);
        ListItemClickListener listener = new ListItemClickListener();
        mCampusList.setOnItemClickListener(listener);
    }

    class ListItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = new Intent(MainActivity.this, CampusDetailActivity.class);
            startActivity(intent);
        }
    }
    
    //TODO:处理各个点击事件
	public void onClick(View v) {
		Logger.i(TAG, "===Click Id ==>" + v.getId());
		switch(v.getId()) {
			case R.id.more_alarms:
			break;
		}
	} 
}
