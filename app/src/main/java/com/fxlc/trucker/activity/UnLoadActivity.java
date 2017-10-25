package com.fxlc.trucker.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bumptech.glide.Glide;
import com.fxlc.trucker.BaseActivity;
import com.fxlc.trucker.MyApplication;
import com.fxlc.trucker.R;
import com.fxlc.trucker.api.OrderService;
import com.fxlc.trucker.bean.CurrentOrder;
import com.fxlc.trucker.bean.OrderList;
import com.fxlc.trucker.net.HttpCallback;
import com.fxlc.trucker.net.HttpResult;
import com.fxlc.trucker.service.LocationService;
import com.fxlc.trucker.util.BitmapUtil;
import com.fxlc.trucker.util.DialogUtil;
import com.fxlc.trucker.util.FileUtil;
import com.fxlc.trucker.util.UriUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class UnLoadActivity extends BaseActivity implements View.OnClickListener {


    private TextView orderNoTx, trucksTx, weightTx,timeTx;
    private ImageView billImg;
    private View callView;
    private CurrentOrder current;
    private String title;
    private TextView locationTx, evaluateTx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unload);

        current = (CurrentOrder) getIntent().getSerializableExtra("current");

        locationTx = (TextView) findViewById(R.id.location);
        evaluateTx = (TextView) findViewById(R.id.evaluate);

        orderNoTx = (TextView) findViewById(R.id.order_no);
        trucksTx = (TextView) findViewById(R.id.trucks);
        timeTx = (TextView) findViewById(R.id.time);
        findViewById(R.id.call).setOnClickListener(this);
        weightTx = (TextView) findViewById(R.id.weight);
        billImg = (ImageView) findViewById(R.id.billimg);
        billImg.setOnClickListener(this);
        findViewById(R.id.action).setOnClickListener(this);
        findViewById(R.id.toinfo).setOnClickListener(this);
        choiceDialog = DialogUtil.createDialog(this, new String[]{"相册", "拍照"}, this);

        title = current.getFrom() + "-" + current.getTo();

        setValue();
        requestPermisson();
    }

    private void setValue() {

        orderNoTx.setText("订单编号：" + current.getOrderNo());
        trucksTx.setText("接单车辆：" + current.getAgreeTrucks() + " | " + "待卸车辆：" + current.getWaiUnloadTrucks());
        timeTx.setText("卸货时间：" + current.getUnloadTime());



    }

    @Override
    protected void onResume() {
        super.onResume();
        title(title);
    }

    private static final int BAIDU_LOCATION = 99;

    private void requestPermisson() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, BAIDU_LOCATION);

    }

    LocationService service;
    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            LocationService.MyBinder binder = (LocationService.MyBinder) iBinder;
            service = binder.getService();
            if (service.getLocation() != null)
                showLocation(service.getLocation());

            service.setLocLisener(new LocationService.MLocationLisener() {
                @Override
                public void onLocation(BDLocation location) {
                    if (locationTx != null)
                        showLocation(location);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
    private void  showLocation(BDLocation location){
        LatLng fromLatLng = new LatLng(location.getLatitude() ,location.getLongitude());

        LatLng toLatLng = new LatLng(current.getUnloadLat(), current.getUnloadLon());



        locationTx.setText("当前位置：" +  location.getLocationDescribe());
        int distance = (int) DistanceUtil.getDistance(fromLatLng, toLatLng) / 1000;


        evaluateTx.setText("距离卸货地：" + distance + "公里" + "约" + distance/70 + "小时" );

    }
    private void loadOrder() {
        proDialog.show();
        Retrofit retrofit = MyApplication.getRetrofit();
        OrderService service = retrofit.create(OrderService.class);

        Map<String, String> map = new HashMap<>();
        map.put("orderId", current.getOrderId());
        map.put("unloadWeight", weightTx.getText().toString());
        MediaType type = MediaType.parse("application/otcet-stream");
        RequestBody body = RequestBody.create(type, BitmapUtil.cpPicToByte(billPath, 100));

        Call<HttpResult<CurrentOrder>> call = service.unloadOrder(map, body);
        call.enqueue(new HttpCallback<CurrentOrder>() {
            @Override
            public void onSuccess(CurrentOrder currentOrder) {
                proDialog.dismiss();
                toast("上传卸货榜单成功");
                it.setClass(ctx, BillInfoActivity.class);
                it.putExtra("current",currentOrder);
                startActivity(it);
                finish();
            }
        });

    }
    private void orderInfo() {
        proDialog.show();
        Retrofit retrofit = MyApplication.getRetrofit();

        OrderService service = retrofit.create(OrderService.class);

        Call<HttpResult<OrderList.OrderBean>> call = service.orderInfo(current.getOrderId());
        call.enqueue(new HttpCallback<OrderList.OrderBean>() {
            @Override
            public void onSuccess(OrderList.OrderBean orderBean) {
                proDialog.dismiss();
                it.putExtra("order",orderBean);
                it.putExtra("hide",true);
                it.setClass(ctx,SourceInfoActivity.class);
                startActivity(it);
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.call:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + current.getUnloadPhone()));

                startActivity(intent);
                break;
            case R.id.toinfo:
                 orderInfo();
                break;

            case R.id.billimg:
                choiceDialog.show();

                break;
            case R.id.action:
                if (TextUtils.isEmpty(weightTx.getText())) {

                    toast("请填写货重");
                    return;
                }
                if (TextUtils.isEmpty(billPath)) {

                    toast("请上传卸货榜单");
                    return;
                }
                loadOrder();
                break;
            case R.id.dialog_item1:
                choiceDialog.dismiss();
                Intent album = new Intent(Intent.ACTION_PICK);
                album.setType("image/*");
                startActivityForResult(album, ALBUM_CODE);
                break;
            case R.id.dialog_item2:
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PEMISSION_CODE);
                choiceDialog.dismiss();
                break;
            case R.id.dialog_close:
                choiceDialog.dismiss();
                break;
        }


    }

    private void photo() {

        File imagePath = new File(Environment.getExternalStorageDirectory(), "images");
        if (!imagePath.exists()) imagePath.mkdirs();
        String fileName = "unloadImg" + System.currentTimeMillis();
        captureFile = new File(imagePath, fileName);


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        Uri contentUri = FileProvider.getUriForFile(ctx, "com.fxlc.trucker.fileprovider", captureFile);
        //兼容版本处理，因为 intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION) 只在5.0以上的版本有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ClipData clip =
                    ClipData.newUri(getContentResolver(), "A photo", contentUri);
            intent.setClipData(clip);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            List<ResolveInfo> resInfoList =
                    getPackageManager()
                            .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                grantUriPermission(packageName, contentUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        startActivityForResult(intent, CAPTURE_CODE);

    }

    private File captureFile;
    public static int ALBUM_CODE = 101;
    public static int CAPTURE_CODE = 102;
    public static int PEMISSION_CODE = 100;
    public static String capture_filename = "load_bill.jpg";
    private String billPath;
    Dialog choiceDialog = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == ALBUM_CODE) {
                Uri uri = data.getData();
                billPath = UriUtil.getRealFilePath(this, uri);

            }
            if (requestCode == CAPTURE_CODE) {
                billPath = captureFile.getPath();
            }

            Glide.with(ctx).load(billPath).fitCenter().into(billImg);


        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PEMISSION_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                photo();
            } else {
                // Permission Denied
                Toast.makeText(this, "请在 设置->权限 中授权拍照", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == BAIDU_LOCATION) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                it.setClass(ctx, LocationService.class);

                bindService(it, conn, Context.BIND_AUTO_CREATE);

            } else {
                // Permission Denied
                Toast.makeText(this, "请在 设置->权限 中授权位置权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }
}
