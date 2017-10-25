package com.fxlc.trucker.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.fxlc.trucker.BaseActivity;
import com.fxlc.trucker.Constant;
import com.fxlc.trucker.MyApplication;
import com.fxlc.trucker.R;
import com.fxlc.trucker.api.SettingService;
import com.fxlc.trucker.bean.Version;
import com.fxlc.trucker.net.HttpCallback;
import com.fxlc.trucker.net.HttpResult;
import com.fxlc.trucker.util.DataCleanManager;
import com.fxlc.trucker.util.Util;

import java.io.File;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    TextView cacheTxt;
    File cacheFile;
    Handler handler = new Handler();
    IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        findViewById(R.id.feedback).setOnClickListener(this);
        findViewById(R.id.action).setOnClickListener(this);
        findViewById(R.id.clear_cache).setOnClickListener(this);
        findViewById(R.id.protocal).setOnClickListener(this);
        findViewById(R.id.aboutus).setOnClickListener(this);
        findViewById(R.id.uplate).setOnClickListener(this);
        cacheTxt = (TextView) findViewById(R.id.cache);
        cacheFile = new File(Environment.getExternalStorageDirectory(), "images");
        getCache();
        checkPermission();
    }


    @Override
    protected void onResume() {
        super.onResume();
        title("设置");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.clear_cache:
                proDialog.setMessage("请稍候...");
                proDialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DataCleanManager.cleanApplicationData(ctx, cacheFile.getPath());
                        getCache();
                        proDialog.dismiss();
                    }
                }, 1000 * 2);

                break;
            case R.id.feedback:
                it.setClass(ctx, FeedbackActivity.class);
                startActivity(it);

                break;
            case R.id.aboutus:
                it.setClass(ctx, AboutusActivity.class);
                startActivity(it);

                break;
            case R.id.protocal:
                it.setClass(ctx, ProtocalActivity.class);
                startActivity(it);

                break;
            case R.id.action:

                sp.edit().remove("user").commit();
                MyApplication.getInstance().exit();
                it.setClass(ctx, LoginNewActivity.class);
                startActivity(it);
                finish();
                break;
            case R.id.uplate:
                long id = sp.getLong("update_reference", -1);
                if (id > 0) {
                    toast("后台更行中");
                } else {

                    checkUpdate();
                }


                break;


        }
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
                    showUpdateDialog(version);

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

    private void showUpdateDialog(final Version version) {
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
                download(version.getUrl());
            }
        });
        d.setMessage("更新版本?");
        d.show();
    }

    private void getCache() {
        try {

            String size = DataCleanManager.getCacheSize(cacheFile);
            cacheTxt.setText(size);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    DownloadManager manager;
    long downId;

    public void download(String url) {
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

    public void checkPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestReadWritePermission();
        }

    }

    private static int PEMISSION_RW = 100;

    private void requestReadWritePermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PEMISSION_RW);
    }
}
