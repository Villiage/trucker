package com.fxlc.trucker;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.bumptech.glide.Glide;
import com.fxlc.trucker.activity.AllbilActivity;
import com.fxlc.trucker.activity.BankActivity;
import com.fxlc.trucker.activity.BannerInfoActivity;

import com.fxlc.trucker.activity.LoginActivity;
import com.fxlc.trucker.activity.LoginNewActivity;
import com.fxlc.trucker.activity.MsgActivity;
import com.fxlc.trucker.activity.MyWalletActivity;
import com.fxlc.trucker.activity.MybillActivity;
import com.fxlc.trucker.activity.MycarActivity;
import com.fxlc.trucker.activity.SettingActivity;
import com.fxlc.trucker.activity.SourceInfoActivity;
import com.fxlc.trucker.activity.UInfoActivity;
import com.fxlc.trucker.activity.UsualPlaceActivity;
import com.fxlc.trucker.api.OrderService;
import com.fxlc.trucker.api.SettingService;
import com.fxlc.trucker.api.UserService;
import com.fxlc.trucker.bean.Carousel;
import com.fxlc.trucker.bean.CurrentOrder;
import com.fxlc.trucker.bean.OrderList;
import com.fxlc.trucker.bean.User;
import com.fxlc.trucker.bean.UserInfo;
import com.fxlc.trucker.bean.Version;
import com.fxlc.trucker.net.HttpCallback;
import com.fxlc.trucker.net.HttpResult;
import com.fxlc.trucker.net.MyThrowable;
import com.fxlc.trucker.service.LocationService;
import com.fxlc.trucker.util.ListUtil;
import com.fxlc.trucker.util.Util;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Context context;
    public ListView listView;
    public View dataEmptyView;

    private TextView locationTx;
    public CurrentOrder currentOrder;
    NavigationView navView;
    DrawerLayout drawer;
    private User user;
    private TextView sumWeight, nameTx, mobileTx, creditTx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        listView = (ListView) findViewById(R.id.list);
        dataEmptyView = findViewById(R.id.empty);
        locationTx = (TextView) findViewById(R.id.location);
        findViewById(R.id.toggle).setOnClickListener(this);
        findViewById(R.id.news).setOnClickListener(this);
        initDrawer();

        initPager();
        getCarsousel();
        findViewById(R.id.current).setOnClickListener(this);

        latestVersion();
        SDKInitializer.initialize(getApplication());
        checkPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume", "onResume");

        loadData();

        user = MyApplication.getUser();
        if (user != null) {
            mobileTx.setText(user.getMobile());
            if (!TextUtils.isEmpty(user.getName())) {
                nameTx.setText(user.getName());
            } else {
                nameTx.setText("未认证");
            }
        }
//        else {
//            it.setClass(ctx, LoginActivity.class);
//            startActivity(it);
//        }
        if (sp.getBoolean("close", false)) {

            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }
            sp.edit().remove("close").commit();
        }
    }

    private void initDrawer() {

        navView = (NavigationView) findViewById(R.id.nav_view);
        int statubarHeight = getStatubarHeight();
        navView.setPadding(navView.getPaddingLeft(), statubarHeight, navView.getPaddingRight(), navView.getPaddingBottom());
        navView.findViewById(R.id.mywallet).setOnClickListener(this);
        navView.findViewById(R.id.mycar).setOnClickListener(this);
        navView.findViewById(R.id.setting).setOnClickListener(this);
        navView.findViewById(R.id.usual_place).setOnClickListener(this);

        navView.findViewById(R.id.name).setOnClickListener(this);

        navView.findViewById(R.id.bankcard).setOnClickListener(this);


        sumWeight = (TextView) findViewById(R.id.sumweight);
        nameTx = (TextView) findViewById(R.id.name);
        mobileTx = (TextView) findViewById(R.id.mobile);
        creditTx = (TextView) findViewById(R.id.credit);


    }

    Timer timer;

    private void playBanner() {
        if (timer != null) timer.cancel();
        timer = null;
        timer = new Timer("banner");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int p = pager.getCurrentItem() + 1;
                if (p == pager.getAdapter().getCount()) p = 0;
                pager.setCurrentItem(p, true);
            }
        }, 0, 5000);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }

    public int getStatubarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        return statusBarHeight;
    }

    Intent it = new Intent();

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.current:
//
//                if (currentOrder.getOrderStatus() == 1) {
//                    it.setClass(ctx, LoadActivity.class);
//                } else if (currentOrder.getOrderStatus() == 2) {
//                    it.setClass(ctx, UnLoadActivity.class);
//                } else if (currentOrder.getOrderStatus() == 3) {
//                    it.setClass(ctx, ClearingActivity.class);
//                }
                it.setClass(ctx, AllbilActivity.class);

                startActivity(it);

                break;

            case R.id.mywallet:

                it.setClass(context, MyWalletActivity.class);
                startActivity(it);
                break;


            case R.id.mycar:

                it.setClass(context, MycarActivity.class);
                startActivity(it);
                break;
            case R.id.usual_place:

                it.setClass(context, UsualPlaceActivity.class);
                startActivity(it);

                break;
            case R.id.bankcard:
                it.setClass(context, BankActivity.class);
                startActivity(it);

                break;
            case R.id.setting:

                it.setClass(context, SettingActivity.class);
                startActivity(it);

                break;

            case R.id.name:
                if (MyApplication.getUser() == null) {
                    it.setClass(context, LoginActivity.class);

                } else {
                    it.setClass(context, UInfoActivity.class);
                }
                startActivity(it);
                break;


            case R.id.toggle:

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }

                break;
            case R.id.news:
                it.setClass(ctx, MsgActivity.class);
                startActivity(it);

                break;
        }
    }

    public void checkRDPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestReadWritePermission();
        } else {
            download();
        }

    }

    private static int PEMISSION_RW = 100;

    private void requestReadWritePermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PEMISSION_RW);
    }

    private static final int BAIDU_LOCATION = 99;

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermisson();
        } else {
            it.setClass(ctx, LocationService.class);
            bindService(it, conn, Context.BIND_AUTO_CREATE);
        }

    }

    private void requestPermisson() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, BAIDU_LOCATION);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == BAIDU_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                it.setClass(ctx, LocationService.class);

                bindService(it, conn, Context.BIND_AUTO_CREATE);

            } else {
                // Permission Denied
                Toast.makeText(this, "请在 设置->权限 中授权位置权限", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == PEMISSION_RW) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                download();
            } else {
                // Permission Denied
                Toast.makeText(this, "无法访问SD卡存储", Toast.LENGTH_SHORT).show();
            }
        }

    }

    LocationService service;
    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            LocationService.MyBinder binder = (LocationService.MyBinder) iBinder;
            service = binder.getService();
            if (service.getLocation() != null)
                locationTx.setText(service.getLocation().getLocationDescribe());

            service.setLocLisener(new LocationService.MLocationLisener() {
                @Override
                public void onLocation(BDLocation location) {


                    if (locationTx != null)
                        locationTx.setText(location.getLocationDescribe());
                }
            });

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


    public void loadData() {


        Retrofit retrofit = MyApplication.getRetrofit();

        OrderService service = retrofit.create(OrderService.class);

        Call<HttpResult<OrderList>> call = service.listOrder();
        call.enqueue(new HttpCallback<OrderList>() {
            @Override
            public void onSuccess(OrderList orderList) {
                getUserInfo();
                dataList = orderList.getSource();
                if (dataList.size() > 0) {

                    adapter = new MListAdapter(dataList);
                    listView.setAdapter(adapter);
                }


            }

            @Override
            public void onFailure(Call call, Throwable t) {

                if (t instanceof MyThrowable){
                    MyThrowable mt = (MyThrowable) t;
                    if (mt.getErrorCode() .equals("01")){
                        it.setClass(ctx, LoginNewActivity.class);
                        startActivity(it);
                        MyApplication.getInstance().exit();

                    }
                }
            }
        });
    }

    public void getUserInfo() {
        Retrofit retrofit = MyApplication.getRetrofit();

        UserService service = retrofit.create(UserService.class);

        Call<HttpResult<UserInfo>> call = service.getUserInfo();
        call.enqueue(new HttpCallback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo userInfo) {

                sumWeight.setText(userInfo.getSumWeight() + "");

                creditTx.setText("信用积分" + userInfo.getCredit());
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });


    }

    public void getCarsousel() {


        Retrofit retrofit = MyApplication.getRetrofit();

        SettingService service = retrofit.create(SettingService.class);

        Call<HttpResult<Carousel>> call = service.getCarousel();
        call.enqueue(new HttpCallback<Carousel>() {
            @Override
            public void onSuccess(Carousel carousel) {
                final List<Carousel.CarouselListBean> carouselList = carousel.getCarouselList();
                pager.setAdapter(new PagerAdapter() {
                    @Override
                    public int getCount() {
                        return carouselList.size();
                    }

                    @Override
                    public boolean isViewFromObject(View view, Object object) {
                        return view == object;
                    }

                    @Override
                    public Object instantiateItem(ViewGroup container, final int position) {
                        ImageView img = new ImageView(ctx);
                        img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                it.setClass(ctx, BannerInfoActivity.class);
                                it.putExtra("p", position);
                                startActivity(it);
                            }
                        });
                        container.addView(img);
                        Glide.with(ctx).load(carouselList.get(position).getPhotoPath()).centerCrop().into(img);
                        return img;
                    }

                    @Override
                    public void destroyItem(ViewGroup container, int position, Object object) {
                        container.removeView((View) object);
                    }
                });
                playBanner();

            }
        });


    }

    private List<OrderList.OrderBean> dataList;
    private MListAdapter adapter;

    class MListAdapter extends BaseAdapter {
        public List<OrderList.OrderBean> list;

        public MListAdapter(List<OrderList.OrderBean> dataList) {
            this.list = dataList;
        }

        @Override
        public int getCount() {
            return dataList == null ? 0 : dataList.size();
        }

        @Override
        public OrderList.OrderBean getItem(int i) {
            return dataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order, null);
                holder.infoTxt = view.findViewById(R.id.info);
                holder.frome_toTx = view.findViewById(R.id.from_to);
                holder.goodsTypeTx = view.findViewById(R.id.goodsType);
                holder.feeTx = view.findViewById(R.id.fee);
                holder.markTx = view.findViewById(R.id.mark);
                holder.infoTxt.setOnClickListener(infoOcl);

                view.setTag(holder);
            } else holder = (ViewHolder) view.getTag();

            OrderList.OrderBean bean = getItem(i);
            holder.infoTxt.setTag(bean);
            holder.frome_toTx.setText(bean.getFrom() + "-" + bean.getTo());
            holder.goodsTypeTx.setText("货物类型：" + bean.getGoodsType());
            holder.feeTx.setText("运费：" + bean.getFreight() + "元/吨");


            if (bean.getRemark() != null && bean.getRemark().size() > 0) {
                StringBuffer sb = new StringBuffer();
                for (String s : bean.getRemark()) {
                    sb.append(" ");
                    sb.append("#");
                    sb.append(s);
                    sb.append("   ");
                }

                SpannableStringBuilder style = new SpannableStringBuilder(sb.toString());
                int y = 0, z = 0;

                for (String s : bean.getRemark()) {
                    z = y + s.length() + 3;
                    BackgroundColorSpan bgSpan = new BackgroundColorSpan(Color.parseColor("#C3D3E3"));
                    style.setSpan(bgSpan, y, z, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    y += s.length() + 5;

                }
                holder.markTx.setText(style);

            } else holder.markTx.setVisibility(View.GONE);


//            LatLng fromLatLng = new LatLng(bean.getLoadLat(), bean.getLoadLon());
//            LatLng toLatLng = new LatLng(bean.getUnloadLat(), bean.getUnloadLon());
//
//
//            int distance = (int) DistanceUtil.getDistance(fromLatLng, toLatLng) / 1000;
//            holder.distanceTx.setText("运距： " + distance + "公里");
//            holder.timeTx.setText("用时：" + "约" + (distance / 70) + "小时");


            return view;
        }
    }

    private View.OnClickListener infoOcl = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            it.setClass(ctx, SourceInfoActivity.class);
            it.putExtra("order", (OrderList.OrderBean) view.getTag());
            startActivity(it);
        }
    };

    class ViewHolder {

        TextView infoTxt, markTx;
        TextView feeTx, goodsTypeTx, frome_toTx, distanceTx, timeTx;


    }

    private ViewPager pager;

    private void initPager() {

        pager = (ViewPager) findViewById(R.id.banner);

    }


    private void latestVersion() {

        checkUpdate();

    }

    Version mVersion;

    private void checkUpdate() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.Host_SMS)
                .addConverterFactory(GsonConverterFactory.create()).build();

        SettingService service = retrofit.create(SettingService.class);

        Call<HttpResult<Version>> call = service.getVersion(1, 1);
        call.enqueue(new HttpCallback<Version>() {
            @Override

            public void onSuccess(Version version) {
                mVersion = version;
                if (version.getVersionNumber() > getVersionCode()) {
                    showUpdateDialog();
                } else {
                    toast("已是最新版本");
                }

            }
        });


    }

    private int getVersionCode() {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return packInfo.versionCode;
    }

    private void showUpdateDialog() {
        AlertDialog d = new AlertDialog.Builder(this).create();
        d.setButton(AlertDialog.BUTTON_NEGATIVE, "稍后", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        d.setButton(AlertDialog.BUTTON_POSITIVE, "更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
//                download();
                checkRDPermission();
            }
        });
        d.setMessage("发现新版本");
        d.show();
    }

    DownloadManager manager;
    long downId;

    public void download() {
        toast("下载中...");
        manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mVersion.getUrl()));

        request.setTitle("卡盟货运");

        //设置状态栏中显示Notification

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        //设置可用的网络类型
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "app.apk");

//        File f = Util.getFile("trucker");
//
//        request.setDestinationUri(Uri.fromFile(f));
        //设置文件类型
        request.setMimeType("application/vnd.android.package-archive");
//        //将请求加入请求队列会 downLoadManager会自动调用对应的服务执行者个请求
        downId = manager.enqueue(request);
        sp.edit().putLong("update_reference", downId).commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long secondTime = System.currentTimeMillis();
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
                return true;
            } else {
                if (secondTime - firstTime < 2000) {
                    System.exit(0);
                } else {
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = System.currentTimeMillis();
                }
                return true;
            }


        }
        return super.onKeyDown(keyCode, event);
    }


}
