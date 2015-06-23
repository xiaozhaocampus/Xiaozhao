package com.campus.xiaozhao.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.os.AsyncTask;
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

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import com.baidu.location.BDLocation;
import com.campus.xiaozhao.R;
import com.campus.xiaozhao.activity.CampusDetailActivity;
import com.campus.xiaozhao.activity.CampusInfoAdapter;
import com.campus.xiaozhao.basic.data.CampusInfo;
import com.campus.xiaozhao.basic.data.CampusInfoItemData;
import com.campus.xiaozhao.basic.location.BaiDuLocationManager;
import com.campus.xiaozhao.basic.utils.CampusSharePreference;
import com.component.logger.Logger;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by frankenliu on 15/6/7.
 */
public class InfoFragment extends Fragment implements Handler.Callback{
    private static final String TAG = "InfoFragment";
    public static final int MSG_SET_LOCATION = 1;
    public static final int MSG_RECEIVE_DATA_FROM_BMOB = 2;
    private PullToRefreshListView mCampusList;
    private CampusInfoAdapter mInfoAdapter;
    private List<CampusInfoItemData> mDatas;
    private TextView mLocation;
    private Handler mHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_info, container, false);
        mCampusList = (PullToRefreshListView) view.findViewById(R.id.campus_list);
        mCampusList.setMode(PullToRefreshBase.Mode.BOTH);
        mLocation = (TextView) view.findViewById(R.id.location);
        String location = CampusSharePreference.getLocation(getActivity());
        if(TextUtils.isEmpty(location)) {
            location = "暂无定位";
        }
        mLocation.setText(location);
        initData();
        return view;
    }

    private void initData() {
        mHandler = new Handler(this);
        BaiDuLocationManager.getInstance().start(getActivity(), mHandler);
        mDatas = new ArrayList<CampusInfoItemData>();
        mInfoAdapter = new CampusInfoAdapter(getActivity(), mDatas);
        mCampusList.setAdapter(mInfoAdapter);
        ListItemClickListener listener = new ListItemClickListener();
        mCampusList.setOnItemClickListener(listener);
        getDataFromBmob();

        mCampusList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                Logger.d(TAG, "pull down");
                new GetDataTask().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                Logger.d(TAG, "pull up");
                new GetDataTask().execute();
            }
        });
    }

    private void getDataFromBmob() {
        BmobQuery<CampusInfo> query = new BmobQuery<CampusInfo>();
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(3));//此表示缓存三天
        //判断是否有缓存
        boolean isCache = query.hasCachedResult(getActivity(),CampusInfo.class);
        if(isCache){
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        }else{
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }
        query.include("companyInfo");
        query.findObjects(getActivity(), new FindListener<CampusInfo>() {
            @Override
            public void onSuccess(List<CampusInfo> list) {
                if(list != null && list.size() > 0) {
                    for(CampusInfo info : list) {
                        if(info != null) {
                            CampusInfoItemData itemData = new CampusInfoItemData();
                            itemData.setCampusID(info.getObjectId());
                            itemData.setCity(info.getCity());
                            itemData.setCompany(info.getCompanyInfo().getName());
                            itemData.setIntroduction(info.getCompanyInfo().getIntroduction());
                            itemData.setAddress(info.getAddress());
                            itemData.setTitle(info.getTitle());
                            itemData.setContent(info.getContent());
                            itemData.setTime(info.getDate());
                            itemData.setVersion(info.getVersion());
                            itemData.setType(info.getType());
                            mDatas.add(itemData);
                        }
                    }
                    Message msg = mHandler.obtainMessage(MSG_RECEIVE_DATA_FROM_BMOB);
                    mHandler.sendMessage(msg);
                }
                Logger.d(TAG, "get success");
            }

            @Override
            public void onError(int i, String s) {
                Logger.d(TAG, "get fail");
            }
        });
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_SET_LOCATION:
                BDLocation location = (BDLocation) msg.obj;
                mLocation.setText(location.getCity());
                Logger.d(TAG, "location:" + location.getCity());
                break;
            case MSG_RECEIVE_DATA_FROM_BMOB:
                mInfoAdapter.notifyDataSetChanged();
                break;
            default:
                break;
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

    class GetDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mCampusList.onRefreshComplete();
        }
    }
}
