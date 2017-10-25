package com.fxlc.trucker.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.MainThread;
import android.util.Log;
import android.widget.Toast;

import com.fxlc.trucker.Constant;
import com.fxlc.trucker.MyApplication;
import com.fxlc.trucker.api.CarService;
import com.fxlc.trucker.bean.MediaStoreData;
import com.fxlc.trucker.bean.Truck;
import com.fxlc.trucker.util.BitmapUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class UploadService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_ADD_CAR = "com.fxlc.trucker.service.action.addcar";
    public static final String ACTION_ADD_BILL = "com.fxlc.trucker.service.action.addbill";

    // TODO: Rename parameters
    public static final String EXTRA_TRUCK = "com.fxlc.trucker.service.extra.truck";
    public static final String EXTRA_BILL_IMG = "com.fxlc.trucker.service.extra.billimg";
    public static final String EXTRA_BILL_ID = "com.fxlc.trucker.service.extra.billid";

    @Override
    public void onCreate() {
        super.onCreate();
        handler  = new Handler(getMainLooper()){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 100){
                    Toast.makeText(UploadService.this,"提交成功", Toast.LENGTH_SHORT).show();
                    Log.d("savetruck","savetruck sucess");

                }
            }
        };
    }


    private Handler handler;
    public UploadService() {
        super("UploadService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionAddCar(Context context, Truck truck) {
        Intent intent = new Intent(context, UploadService.class);
        intent.setAction(ACTION_ADD_CAR);
        intent.putExtra(EXTRA_TRUCK, truck);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void actionAddBillImg(Context context, String id, ArrayList<MediaStoreData> imgs) {
        Intent intent = new Intent(context, UploadService.class);
        intent.setAction(ACTION_ADD_BILL);
        intent.putExtra(EXTRA_BILL_ID, id);
        intent.putParcelableArrayListExtra(EXTRA_BILL_IMG, imgs);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_ADD_CAR.equals(action)) {
                final Truck truck = (Truck) intent.getSerializableExtra(EXTRA_TRUCK);
                handleActionAddCar(truck);
            } else if (ACTION_ADD_BILL.equals(action)) {
                Log.d("uploadService", action);
                String id = intent.getStringExtra(EXTRA_BILL_ID);
                List<MediaStoreData> imgs = intent.getParcelableArrayListExtra(EXTRA_BILL_IMG);
                handleActionBillImgs(id, imgs);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionAddCar(Truck truck) {
        // TODO: Handle action Foo

//        Retrofit retrofit = MyApplication.getRetrofit();
//
//        CarService service = retrofit.create(CarService.class);
//        Map<String, String> param = new HashMap<>();
//        param.put("brand", truck.getBrand());
//        param.put("style", truck.getStyle());
//        param.put("drive", truck.getDrive());
//        param.put("soup", truck.getSoup());
//        param.put("carNo", truck.getCarNo());
//        param.put("handcarNo", truck.getHandcarNo());
//        param.put("type", truck.getType());
//        param.put("length", truck.getLength());
//        param.put("height", truck.getHeight());


//        MediaType type = MediaType.parse("application/otcet-stream");
//        Map<String, RequestBody> imgMap = new HashMap<>();
//
//
//        RequestBody body = null;
//        /*
//          主车
//         */
//        body = RequestBody.create(type, BitmapUtil.cpPicToByte(truck.getDriveImg1(), 100));
//        imgMap.put("driveImg\"; filename=\"" + "driveImg1", body);
//        body = RequestBody.create(type, BitmapUtil.cpPicToByte(truck.getDriveImg2(), 100));
//        imgMap.put("driveImg\"; filename=\"" + "driveImg2", body);
//        body = RequestBody.create(type, BitmapUtil.cpPicToByte(truck.getDriveImg3(), 100));
//        imgMap.put("driveImg\"; filename=\"" + "driveImg3", body);
//
//        body = RequestBody.create(type, BitmapUtil.cpPicToByte(truck.getManageImg(), 100));
//        imgMap.put("manageImg\"; filename=\"" + "manageImg", body);
//
//
//        body = RequestBody.create(type, BitmapUtil.cpPicToByte(truck.getHanddriveImg1(), 100));
//        imgMap.put("handdriveImg\"; filename=\"" + "handdriveImg1", body);
//        body = RequestBody.create(type, BitmapUtil.cpPicToByte(truck.getHanddriveImg2(), 100));
//        imgMap.put("handdriveImg\"; filename=\"" + "handdriveImg2", body);
//        body = RequestBody.create(type, BitmapUtil.cpPicToByte(truck.getHanddriveImg3(), 100));
//        imgMap.put("handdriveImg\"; filename=\"" + "handdriveImg3", body);
//
//        body = RequestBody.create(type, BitmapUtil.cpPicToByte(truck.getHandmanageImg(), 100));
//        imgMap.put("handmanageImg\"; filename=\"" + "handmanageImg", body);


        OkHttpClient client = new OkHttpClient();

        MultipartBody.Builder data = new MultipartBody.Builder();

        data.addFormDataPart("userId",MyApplication.getUser().getId());
        data.addFormDataPart("token",MyApplication.getUser().getToken());

        data.addFormDataPart("brand", truck.getBrand());
        data.addFormDataPart("style", truck.getStyle());
        data.addFormDataPart("drive", truck.getDrive());
        data.addFormDataPart("soup", truck.getSoup());
        data.addFormDataPart("carNo", truck.getCarNo());
        data.addFormDataPart("handcarNo", truck.getHandcarNo());
        data.addFormDataPart("type", truck.getType());
        data.addFormDataPart("length", truck.getLength());
        data.addFormDataPart("height", truck.getHeight());

        MediaType type = MediaType.parse("application/otcet-stream");
        RequestBody body1 = RequestBody.create(type, BitmapUtil.cpPicToByte(truck.getDriveImg1(), 100));
        data.addFormDataPart("driveImg", "driveImg1.img", body1);

        RequestBody body2 = RequestBody.create(type, BitmapUtil.cpPicToByte(truck.getDriveImg2(), 100));
        data.addFormDataPart("driveImg", "driveImg2.img", body2);

        RequestBody body3 = RequestBody.create(type, BitmapUtil.cpPicToByte(truck.getDriveImg3(), 100));
        data.addFormDataPart("driveImg", "driveImg3.img", body3);

        RequestBody mana = RequestBody.create(type, BitmapUtil.cpPicToByte(truck.getManageImg(), 100));
        data.addFormDataPart("manageImg", "manaImg.img", mana);
        /*
          挂车
         */

        RequestBody h1 = RequestBody.create(type, BitmapUtil.cpPicToByte(truck.getHanddriveImg1(), 100));
        data.addFormDataPart("handdriveImg", "handdriveImg1.img", h1);

        RequestBody h2 = RequestBody.create(type, BitmapUtil.cpPicToByte(truck.getHanddriveImg2(), 100));
        data.addFormDataPart("handdriveImg", "hnaddriveImg2.img", h2);

        RequestBody h3 = RequestBody.create(type, BitmapUtil.cpPicToByte(truck.getHanddriveImg3(), 100));
        data.addFormDataPart("handdriveImg", "handdriveImg3.img", h3);

        RequestBody hm = RequestBody.create(type, BitmapUtil.cpPicToByte(truck.getHandmanageImg(), 100));
        data.addFormDataPart("handmanageImg", "handmanaImg.img", hm);


        Request request = new Request.Builder().url(Constant.Host + "saveTruck").post(data.build()).build();

          client.newCall(request).enqueue(new Callback() {
             @Override
             public void onFailure(okhttp3.Call call, IOException e) {

             }

             @Override
             public void onResponse(okhttp3.Call call, Response response) throws IOException {

                 if (response.isSuccessful()){

                    handler.sendEmptyMessage(100);

                 }
             }
         });

//        Call<HttpResult> call = service.saveTruck2(param, imgMap);
//        call.enqueue(new SimpleCallback() {
//            @Override
//            public void onSuccess(HttpResult result) {
//                 Toast.makeText(UploadService.this,"提交成功",Toast.LENGTH_SHORT).show();
//                Log.d("response","upload success");
//            }
//
//            @Override
//            public void onFailure(Call<HttpResult> call, Throwable throwable) {
//                super.onFailure(call, throwable);
//                Log.d("response","upload  fail");
//            }
//        });
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBillImgs(String id, List<MediaStoreData> imgs) {
        // TODO: Handle action Baz

        MediaType type = MediaType.parse("application/otcet-stream");
        Map<String, RequestBody> imgMap = new HashMap<>();
        if (imgs.size() > 0) {
            for (int i = 0; i < imgs.size(); i++) {
                String path = imgs.get(i).path;
                String substring = path.substring(path.lastIndexOf("/") + 1, path.length());
                Log.d("uploadService", substring);
                RequestBody body = RequestBody.create(type, BitmapUtil.cpPicToByte(path, 100));

                imgMap.put("billImg\"; filename=\"" + substring, body);

            }
        }



    }
}
