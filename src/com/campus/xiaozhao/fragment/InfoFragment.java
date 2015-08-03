package com.campus.xiaozhao.fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import com.baidu.location.BDLocation;
import com.campus.xiaozhao.R;
import com.campus.xiaozhao.activity.CampusDetailActivity;
import com.campus.xiaozhao.activity.CampusInfoAdapter;
import com.campus.xiaozhao.basic.data.CampusInfo;
import com.campus.xiaozhao.basic.data.CampusInfoItemData;
import com.campus.xiaozhao.basic.data.CampusType;
import com.campus.xiaozhao.basic.location.BaiDuLocationManager;
import com.campus.xiaozhao.basic.utils.CampusSharePreference;
import com.campus.xiaozhao.basic.utils.DateUtils;
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
    private Set<String> mCampusIDs = new HashSet<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_info, container, false);
        mCampusList = (PullToRefreshListView) view.findViewById(R.id.campus_list);
        mCampusList.setMode(PullToRefreshBase.Mode.BOTH);
        mLocation = (TextView) getActivity().findViewById(R.id.actionbar_location_city);
        String location = CampusSharePreference.getLocation(getActivity());
        if(TextUtils.isEmpty(location)) {
            location = "暂无定位";
        }
        mLocation.setText(location);
        mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "其他城市正在开发中", Toast.LENGTH_LONG).show();
            }
        });
        initData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mInfoAdapter != null) {
            mInfoAdapter.notifyDataSetChanged();
        }
    }

    private void initData() {
        mHandler = new Handler(this);
        BaiDuLocationManager.getInstance().start(getActivity(), mHandler);
        mDatas = new ArrayList<CampusInfoItemData>();
        mInfoAdapter = new CampusInfoAdapter(getActivity(), mDatas);
        mCampusList.setAdapter(mInfoAdapter);
        ListItemClickListener listener = new ListItemClickListener();
        mCampusList.setOnItemClickListener(listener);
        pullDataForward();

        mCampusList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                Logger.d(TAG, "pull down");
                //设置上一次刷新的提示标签
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + DateUtils.transferTimeToDate(System.currentTimeMillis(), "MM月dd日 a hh:mm"));
                new GetDataTask().execute(true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                Logger.d(TAG, "pull up");
                //设置上一次刷新的提示标签
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + DateUtils.transferTimeToDate(System.currentTimeMillis(), "MM月dd日 a hh:mm"));
                new GetDataTask().execute(false);
            }
        });
    }

    /**
     * 向前获取数据(最新的数据)
     */
    private void pullDataForward() {
        int currentCount = CampusSharePreference.getServerDataCount(getActivity());
        getDataFromBmob(currentCount);
    }

    /**
     * 向后拉取数据(较早的数据)
     */
    private void pullDataBackword() {
        int currentCount = CampusSharePreference.getServerDataCount(getActivity());
        int start = currentCount - mDatas.size() - 1;
        if(start < 0) {
            start = 0;
        }
        getDataFromBmob(start);
    }

    private void getDataFromBmob(final int startPosition) {
        BmobQuery<CampusInfo> query = new BmobQuery<CampusInfo>();
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(3));//此表示缓存三天
        //判断是否有缓存
        boolean isCache = query.hasCachedResult(getActivity(),CampusInfo.class);
        if(isCache){
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        }else{
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }
        // 设置分页查询
        query.setLimit(1);
        query.setSkip(startPosition);

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
                            if(mCampusIDs.contains(itemData.getCampusID())) {
                                continue;
                            } else {
                                mDatas.add(itemData);
                                mCampusIDs.add(itemData.getCampusID());
                            }
                        }
                    }
                    Message msg = mHandler.obtainMessage(MSG_RECEIVE_DATA_FROM_BMOB);
                    mHandler.sendMessage(msg);

                    // 缓存已经获取的条数
                    int count = CampusSharePreference.getServerDataCount(getActivity());
                    if(startPosition < count) { // 获取较早的数据不缓存条数
                        return;
                    }
                    CampusSharePreference.setServerDataCount(getActivity(), count + list.size());
                }
                Logger.d(TAG, "get success");

                if(mCampusList != null) {
                    mCampusList.onRefreshComplete();
                }
            }

            @Override
            public void onError(int i, String s) {
                Logger.d(TAG, "get fail");
                if(mCampusList != null) {
                    mCampusList.onRefreshComplete();
                }
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
                BaiDuLocationManager.getInstance().stop();
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
            CampusType campusType = new CampusType();
            campusType.setCampusId("asdf123adsfa");
            campusType.setIsTimeout(false);
            campusType.save(getActivity(), new SaveListener() {
                @Override
                public void onSuccess() {
                    Logger.d(TAG, "onSuccess");
                }

                @Override
                public void onFailure(int i, String s) {
                    Logger.d(TAG, "onFailure");
                }
            });

            Intent intent = new Intent(getActivity(), CampusDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("detail_data", mDatas.get(position - 1));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    /**
     * 获取数据成功后刷新UI(上拉下拉)
     */
    class GetDataTask extends AsyncTask<Boolean, Void, Void> {

        @Override
        protected Void doInBackground(Boolean... params) {
            if(params[0]) {
                pullDataForward();
            } else {
                pullDataBackword();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mCampusList.onRefreshComplete();
        }
    }
}
