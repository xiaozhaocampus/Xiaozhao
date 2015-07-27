package com.campus.xiaozhao.basic.location;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.campus.xiaozhao.basic.utils.CampusSharePreference;
import com.campus.xiaozhao.fragment.InfoFragment;
import com.component.logger.Logger;

/**
 * Created by frankenliu on 2015/6/4.
 */
public class BaiDuLocationManager {
    private static final String TAG = "BaiDuLocationManager";
    private static volatile BaiDuLocationManager mBDuManager;
    private static LocationClient mClient;
    private static BaiDuLocationListener mListener;
    private static Context mContext;
    private Handler mHandler;

    private BaiDuLocationManager() {

    }
    public static BaiDuLocationManager getInstance() {
        if(mBDuManager == null) {
            synchronized (BaiDuLocationManager.class) {
                if(mBDuManager == null) {
                    mBDuManager = new BaiDuLocationManager();
                }
            }
        }
        return mBDuManager;
    }

    public void start(Context context, Handler handler) {
        if(context == null) {
            return;
        }
        if(mClient != null) {
            int res = mClient.requestLocation();
            Logger.d(TAG, "request location:" + res);
            return;
        }
        try {
            mContext = context;
            mHandler = handler;
            mClient = new LocationClient(context);
            mListener = new BaiDuLocationListener();
            mClient.registerLocationListener(mListener);

            LocationClientOption option = new LocationClientOption();
            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
            option.setCoorType("gcj02");
//            option.setScanSpan(3 * 1000);
            option.setIsNeedAddress(true);
            option.setOpenGps(true);
            mClient.setLocOption(option);
            mClient.start();
            int res = mClient.requestLocation();
            Logger.d(TAG, "request location:" + res);
        } catch (Exception e) {
            Logger.w(TAG, e);
        }
    }

    public void stop() {
        if(mClient != null) {
            mClient.unRegisterLocationListener(mListener);
            mClient.stop();
        }
        mListener = null;
        mClient = null;
        mContext = null;
    }

    class BaiDuLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return ;
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation){
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\n" + location.getProvince());
                sb.append("\n" + location.getCity());
                sb.append("\n" + location.getDistrict());
            }
            Logger.d(TAG, "location: " + sb.toString());

            checkAndNotifyLocationChange(mContext, location);
        }
    }

    private void checkAndNotifyLocationChange(Context context, BDLocation location) {
        if(TextUtils.isEmpty(location.getCity())) {
            return;
        }
        String preLocation = CampusSharePreference.getLocation(context);
        if(!location.getCity().equals(preLocation)) {
            Message msg = mHandler.obtainMessage(InfoFragment.MSG_SET_LOCATION);
            msg.obj = location;
            mHandler.sendMessage(msg);
            CampusSharePreference.setLocation(context, location.getCity());
        }
    }
}
