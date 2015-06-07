package com.campus.xiaozhao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.baidu.location.BDLocation;
import com.campus.xiaozhao.Environment;
import com.campus.xiaozhao.R;
import com.campus.xiaozhao.basic.data.CampusInfoItemData;
import com.campus.xiaozhao.basic.location.BaiDuLocationManager;
import com.campus.xiaozhao.basic.utils.CampusSharePreference;
import com.component.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements Handler.Callback{
	private static final String TAG = "MainActivity";
	public static final int MSG_SET_LOCATION = 1;
	private ListView mCampusList;
	private CampusInfoAdapter mInfoAdapter;
	private List<CampusInfoItemData> mDatas;
	private TextView mLocation;
	private Handler mHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (Environment.ENABLE_SETUP_ACTIVITY) {
            if (!CampusSharePreference.isLogin(this)) {
	            SetupActivity.startFrom(this);
	            finish();
	            return;
            }
        }

		new Handler().post(new Runnable() {
			@Override
			public void run() {
				setupUI();
			}
		});
	}

	public static void startFrom(Context context) {
    	Intent intent = new Intent(context, MainActivity.class);
    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    		.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    		.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	context.startActivity(intent);
    }

    private void setupUI() {
		mHandler = new Handler(this);
		BaiDuLocationManager.getInstance().start(getApplicationContext(), mHandler);

        setContentView(R.layout.activity_main);
        mCampusList = (ListView) findViewById(R.id.campus_list);
		mLocation = (TextView) findViewById(R.id.location);
        mDatas = new ArrayList<CampusInfoItemData>();
        CampusInfoItemData data = new CampusInfoItemData();
        data.setCompany("Tencent");
        data.setIntroduction("Tencent is famous Internet Company in China!");
        data.setCampusID(100000);
		data.setCity("武汉");
        data.setTitle("腾讯宣讲会");
        data.setContent("2015年腾讯校园招聘宣讲会");
        data.setTime(1436256758021L);
        data.setType(1001);
        data.setAddress("华中科技大学大学生活动中心");
		data.setVersion(345);
        mDatas.add(data);
        mInfoAdapter = new CampusInfoAdapter(getApplicationContext(), mDatas);
        mCampusList.setAdapter(mInfoAdapter);
        ListItemClickListener listener = new ListItemClickListener();
        mCampusList.setOnItemClickListener(listener);
    }

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
			case MSG_SET_LOCATION:
				BDLocation location = (BDLocation) msg.obj;
				mLocation.setText(location.getCity());
		}

		return false;
	}

	class ListItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Logger.d(TAG, "position:" + position + ", id:" + id);
            Intent intent = new Intent(MainActivity.this, CampusDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("detail_data", mDatas.get(position));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

	// TODO:处理各个点击事件
	public void onClick(View v) {
		Logger.i(TAG, "===Click Id ==>" + v.getId());
		int id = v.getId();
		switch (v.getId()) {
		case R.id.more_alarms:
			break;
		case R.id.more_feedback:
			break;
		}
		startActivityById(id);
	}

	private void startActivityById(int id) {
		Class<?> classes = null; 
		switch (id) {
		case R.id.more_feedback:
			classes = FeedbackActivity.class;
			break;
		case R.id.more_alarms:
			classes = AlarmActivity.class;
			break;			
		default:
			break;
		}
		
		if(classes != null) {
			Intent intent = new Intent(this, classes);
			startActivity(intent);
		}
	}

}
