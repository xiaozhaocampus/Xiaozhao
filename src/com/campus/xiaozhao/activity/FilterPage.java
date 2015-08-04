package com.campus.xiaozhao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import com.campus.xiaozhao.R;
import com.campus.xiaozhao.basic.data.CampusInfo;
import com.campus.xiaozhao.basic.data.CampusInfoItemData;
import com.campus.xiaozhao.basic.data.CampusType;
import com.campus.xiaozhao.basic.db.CampusDBProcessor;
import com.campus.xiaozhao.basic.utils.CampusSharePreference;
import com.campus.xiaozhao.basic.utils.DateUtils;
import com.component.logger.Logger;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by frankenliu on 2015/8/4.
 */
public class FilterPage {
    private static final String TAG = "FilterPage";
    private Context mContext;
    private View mView;
    private PullToRefreshListView mCampusList;
    private List<CampusInfoItemData> mDatas;
    private CampusInfoAdapter mInfoAdapter;
    private Set<String> mCampusIDs = new HashSet<>();
    private long cacheMaxVersion;
    private long cacheMinVersion;

    public FilterPage(Context context, View view) {
        this.mContext = context;
        this.mView = view;
        doInit();
    }

    private void doInit() {
        mCampusList = (PullToRefreshListView) mView.findViewById(R.id.info_filter_list);
        mDatas = new ArrayList<>();
        mInfoAdapter = new CampusInfoAdapter(mContext, mDatas);
        mCampusList.setAdapter(mInfoAdapter);
        mCampusList.setOnItemClickListener(new ListItemClickListener());
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
        getDataFromBmob(true);
    }

    /**
     * 向后拉取数据(较早的数据)
     */
    private void pullDataBackword() {
        getDataFromBmob(false);
    }

    private void getDataFromBmob(boolean isUp) {
        BmobQuery<CampusInfo> query = new BmobQuery<CampusInfo>();
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(3));//此表示缓存三天
        //判断是否有缓存
        final boolean isBmobCache = query.hasCachedResult(mContext,CampusInfo.class);
        if(isBmobCache){
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        }else{
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }

        query.setLimit(10);
        query.order("version");
        if(isUp) {
            if(cacheMaxVersion != 0) {
                query.addWhereGreaterThan("version",cacheMaxVersion);
            }
        } else {
            if(cacheMinVersion != 0) {
                query.addWhereLessThan("version", cacheMinVersion);
            }
        }
        query.include("companyInfo");
        query.findObjects(mContext, new FindListener<CampusInfo>() {
            @Override
            public void onSuccess(List<CampusInfo> list) {
                if (list != null && list.size() > 0) {
                    for (CampusInfo info : list) {
                        if (info != null) {
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

                            if (mCampusIDs.contains(itemData.getCampusID())) {
                                continue;
                            } else {
                                //TODO 待增加过滤条件，第一期过滤在终端做
                                mDatas.add(0, itemData);
                                mCampusIDs.add(itemData.getCampusID());
                            }

                            if(cacheMaxVersion < itemData.getVersion()) {
                                cacheMaxVersion = itemData.getVersion();
                            }
                            if(cacheMinVersion > itemData.getVersion()) {
                                cacheMinVersion = itemData.getVersion();
                            }
                        }
                    }

                    mInfoAdapter.notifyDataSetChanged();
                }
                Logger.d(TAG, "get success");

                if (mCampusList != null) {
                    mCampusList.onRefreshComplete();
                }
            }

            @Override
            public void onError(int i, String s) {
                Logger.d(TAG, "get fail");
                if (mCampusList != null) {
                    mCampusList.onRefreshComplete();
                }
            }
        });
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

    class ListItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Logger.d(TAG, "position:" + position + ", id:" + id);
            CampusType campusType = new CampusType();
            campusType.setCampusId("asdf123adsfa");
            campusType.setIsTimeout(false);
            campusType.save(mContext, new SaveListener() {
                @Override
                public void onSuccess() {
                    Logger.d(TAG, "onSuccess");
                }

                @Override
                public void onFailure(int i, String s) {
                    Logger.d(TAG, "onFailure");
                }
            });

            Intent intent = new Intent(mContext, CampusDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("detail_data", mDatas.get(position - 1));
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        }
    }
}
