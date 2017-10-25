package com.fxlc.trucker.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.fxlc.trucker.MyApplication;
import com.fxlc.trucker.api.SettingService;
import com.fxlc.trucker.net.HttpResult;
import com.fxlc.trucker.net.SimpleCallback;

import retrofit2.Call;
import retrofit2.Retrofit;

public class LocationService extends Service {

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    private MyBinder myBinder = new MyBinder();
    private Handler handler;
    private BDLocation location;
    private static int SPAN = 60 * 1000;

    @Override
    public void onCreate() {
        super.onCreate();
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        initLocation();
        handler = new Handler(getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                BDLocation loc = (BDLocation) msg.obj;
                if (locLisener != null) locLisener.onLocation(loc);

                  saveTrack(loc);
            }
        };

    }

    private void saveTrack(BDLocation lacation) {
        Retrofit retrofit = MyApplication.getRetrofit();
        SettingService service = retrofit.create(SettingService.class);

        Call call = service.saveLocation(location.getLatitude() , location.getLongitude(), lacation.getLocationDescribe());
        call.enqueue(new SimpleCallback() {
            @Override
            public void onSuccess(HttpResult result) {

            }
        });


    }

    public BDLocation getLocation() {
        return location;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("location", "start");

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.d("location", "IBinder" + "execute");
        mLocationClient.start();
        return myBinder;
    }

    public class MyBinder extends Binder {

        public LocationService getService() {

            return LocationService.this;
        }

    }

    public interface MLocationLisener {

        void onLocation(BDLocation location);
    }

    private MLocationLisener locLisener;

    public void setLocLisener(MLocationLisener locLisener) {
        this.locLisener = locLisener;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            LocationService.this.location = location;
            Message msg = Message.obtain();
            msg.obj = location;
            handler.sendMessage(msg);


        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系


        option.setScanSpan(SPAN);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
    }
}
