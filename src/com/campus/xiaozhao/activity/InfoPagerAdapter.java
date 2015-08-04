package com.campus.xiaozhao.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import com.campus.xiaozhao.R;
import com.campus.xiaozhao.XZApplication;
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
 * Created by frankenliu on 15/8/3.
 */
public class InfoPagerAdapter extends PagerAdapter {

    private static final String TAG = "InfoPagerAdapter";
    private Context mContext;
    private PullToRefreshListView mCampusList;
    private CampusInfoAdapter mInfoAdapter;
    private List<CampusInfoItemData> mDatas;
    private Set<String> mCampusIDs = new HashSet<>();

    private List<View>  mListVIews;

    public InfoPagerAdapter(Context context, List<View> views) {
        this.mContext = context;
        this.mListVIews = views;
    }
    @Override
    public int getCount() {
        return mListVIews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(android.view.ViewGroup container,
                                  int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View view = null;
        if(position == 0) {
            view = inflater.inflate(R.layout.info_all, null);
            mCampusList = (PullToRefreshListView)view.findViewById(R.id.campus_list);
            mDatas = new ArrayList<>();
            mCampusList.setMode(PullToRefreshBase.Mode.BOTH);
            mInfoAdapter = new CampusInfoAdapter(container.getContext(), mDatas);
            mCampusList.setAdapter(mInfoAdapter);
            ListItemClickListener listener = new ListItemClickListener();
            mCampusList.setOnItemClickListener(listener);

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
        if(position == 1) {
            view = inflater.inflate(R.layout.info_filter, null);
            PullToRefreshListView listView = (PullToRefreshListView) view.findViewById(R.id.info_filter_list);
            List<CampusInfoItemData> datas = CampusDBProcessor.getInstance(mContext).getCampusInfos(null, null, null);
            listView.setAdapter(new CampusInfoAdapter(mContext, datas));
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(android.view.ViewGroup container, int position,
                            Object object) {
        ((ViewPager) container)
                .removeView((View) object);
    }

    /**
     * 向前获取数据(最新的数据)
     */
    private void pullDataForward() {
        int currentCount = CampusSharePreference.getServerDataCount(mContext);
        getDataFromBmob(currentCount);
    }

    /**
     * 向后拉取数据(较早的数据)
     */
    private void pullDataBackword() {
        int currentCount = CampusSharePreference.getServerDataCount(mContext);
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
        boolean isCache = query.hasCachedResult(mContext,CampusInfo.class);
        if(isCache){
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        }else{
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }
        // 设置分页查询
        query.setLimit(1);
        query.setSkip(startPosition);

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
                                mDatas.add(itemData);
                                mCampusIDs.add(itemData.getCampusID());
                            }
                        }
                    }

                    mInfoAdapter.notifyDataSetChanged();

                    // 缓存已经获取的条数
                    int count = CampusSharePreference.getServerDataCount(mContext);
                    if (startPosition < count) { // 获取较早的数据不缓存条数
                        return;
                    }
                    CampusSharePreference.setServerDataCount(mContext, count + list.size());
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
