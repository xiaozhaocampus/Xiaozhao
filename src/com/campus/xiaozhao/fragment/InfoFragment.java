package com.campus.xiaozhao.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.campus.xiaozhao.R;
import com.campus.xiaozhao.activity.CampusDetailActivity;
import com.campus.xiaozhao.activity.CampusInfoAdapter;
import com.campus.xiaozhao.basic.data.CampusInfoItemData;
import com.campus.xiaozhao.basic.location.BaiDuLocationManager;
import com.campus.xiaozhao.basic.utils.CampusSharePreference;
import com.component.logger.Logger;

/**
 * Created by frankenliu on 15/6/7.
 */
public class InfoFragment extends Fragment implements Handler.Callback{
    private static final String TAG = "InfoFragment";
    public static final int MSG_SET_LOCATION = 1;
    private ListView mCampusList;
    private CampusInfoAdapter mInfoAdapter;
    private List<CampusInfoItemData> mDatas;
    private TextView mLocation;
    private Handler mHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_info, container, false);
        mCampusList = (ListView) view.findViewById(R.id.campus_list);
        mLocation = (TextView) view.findViewById(R.id.location);
        String location = CampusSharePreference.getLocation(getActivity());
        if(TextUtils.isEmpty(location)) {
            location = "暂无定位";
        }
        mLocation.setText(location);
        initUI();
        return view;
    }

    private void initUI() {
        mHandler = new Handler(this);
        BaiDuLocationManager.getInstance().start(getActivity(), mHandler);
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
        mInfoAdapter = new CampusInfoAdapter(getActivity(), mDatas);
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
                Logger.d(TAG, "location:" + location.getCity());
        }

        return false;
    }

    class ListItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Logger.d(TAG, "position:" + position + ", id:" + id);
            Intent intent = new Intent(getActivity(), CampusDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("detail_data", mDatas.get(position));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
