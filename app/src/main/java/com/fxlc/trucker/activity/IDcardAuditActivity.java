package com.fxlc.trucker.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fxlc.trucker.BaseActivity;
import com.fxlc.trucker.Constant;
import com.fxlc.trucker.MyApplication;
import com.fxlc.trucker.R;
import com.fxlc.trucker.bean.IDcard;
import com.fxlc.trucker.bean.User;
import com.fxlc.trucker.util.BitmapUtil;
import com.fxlc.trucker.util.DialogUtil;
import com.fxlc.trucker.util.FileUtil;
import com.fxlc.trucker.util.UriUtil;
import com.fxlc.trucker.util.Util;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;

public class IDcardAuditActivity extends BaseActivity implements View.OnClickListener {
    private static String TAG = "IDcardTestActivity";
    Dialog choiceDialog;
    public static int ALBUM_CODE = 101;
    public static int CAPTURE_CODE = 102;
    public static int PEMISSION_WRITE = 100;
    private static int PEMISSION_READ = 103;
    //    public static String faceTempFile = "face_temp.jpg";
//    public static String backTempFile = "back_temp.jpg";
    public File captureFile;

    private String facePath, backPath;

    private boolean faceSuccess, backSuccess;
    private ImageView faceImg, backImg;
    private int type = 1;
    IDcard idcard = new IDcard();
    private TextView idNoTxt, realNameTxt, issueTxt;
    private View verify;
    private Retrofit retrofit;
    private User user;

    private Context context;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                toast("审核成功");
                return;
            }
            if (msg.what == -1) {
                toast("照片识别失败，请重新上传");
                return;
            }
            if (type == 1) {
                String dataValue = (String) msg.obj;
                JSONObject outputObj = null;
                try {
                    outputObj = new JSONObject(dataValue);
                    if (outputObj.getBoolean("success")) {
                        faceSuccess = true;
                        idcard.setName(outputObj.getString("name"));
                        idcard.setNum(outputObj.getString("num"));

                        idNoTxt.setText(idcard.getNum());
                        realNameTxt.setText(idcard.getName());
                    } else {
                        toast("照片识别失败，请重新上传");
                        Log.d("Card", "照片识别失败，请重新上传" + "\n");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (type == 2) {
                String dataValue = (String) msg.obj;
                JSONObject outputObj = null;
                try {
                    outputObj = new JSONObject(dataValue);
                    if (outputObj.getBoolean("success")) {
                        backSuccess = true;
                        idcard.setIssue(outputObj.getString("issue"));
                        issueTxt.setText(idcard.getIssue());

                    } else {
                        toast("照片识别失败，请重新上传");
                        Log.d("Card", "照片识别失败，请重新上传" + "\n");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_idcard_test);

        faceImg = (ImageView) findViewById(R.id.faceImg);
        backImg = (ImageView) findViewById(R.id.backImg);
        findViewById(R.id.img_face).setOnClickListener(this);
        findViewById(R.id.img_back).setOnClickListener(this);

        verify = findViewById(R.id.verify);
        verify.setOnClickListener(this);

        idNoTxt = (TextView) findViewById(R.id.id_no);
        realNameTxt = (TextView) findViewById(R.id.realname);
        issueTxt = (TextView) findViewById(R.id.issue);

        int[] ids = {R.id.dialog_album, R.id.dialog_capture};
        choiceDialog = DialogUtil.createPickDialog(this, new String[]{"相册", "拍照"}, ids, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        title("个人认证");
    }

    OkHttpClient client = new OkHttpClient();


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_face:
                type = 1;
                choiceDialog.show();
                break;
            case R.id.img_back:
                type = 2;
                choiceDialog.show();
                break;

            case R.id.verify:
                if (faceSuccess && backSuccess)
                    submit();
                else
                    Toast.makeText(context, "照片信息不完整或不清晰，请重新选取", Toast.LENGTH_SHORT).show();
                break;
            case R.id.dialog_album:
                choiceDialog.dismiss();
                checkReadPermission();
                break;
            case R.id.dialog_capture:
                checkWritePermission();
                choiceDialog.dismiss();
                break;
            case R.id.dialog_close:
                choiceDialog.dismiss();
                break;

        }
    }

    private void photo() {
//        faceTempFile += System.currentTimeMillis();
//        backTempFile += System.currentTimeMillis();

        File imagePath = new File(Environment.getExternalStorageDirectory(), "images");
        if (!imagePath.exists()) imagePath.mkdirs();
        String fileName = type == 1 ? "face.jpg" + System.currentTimeMillis() : "back.jpg" + System.currentTimeMillis();


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

    public void readCard(String photoPath) {
        proDialog.setMessage("正在验证照片,请稍侯...");
        proDialog.show();

        final String host = "http://dm-51.data.aliyun.com";
        final String path = "/rest/160601/ocr/ocr_idcard.json";
        final String appcode = "60091d0fb1a648459827272c0abf7909";

        byte[] content = BitmapUtil.cpPicToByte(photoPath, 200);

        String imgBase64 = android.util.Base64.encodeToString(content, android.util.Base64.DEFAULT);

        final JSONObject requestObj = new JSONObject();
        try {
            JSONObject configObj = new JSONObject();
            JSONObject obj = new JSONObject();
            JSONArray inputArray = new JSONArray();
            if (type == 1)
                configObj.put("side", "face");
            else
                configObj.put("side", "back");
            obj.put("image", getParam(50, imgBase64));
            obj.put("configure", getParam(50, configObj.toString()));
            inputArray.put(obj);
            requestObj.put("inputs", inputArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String body = requestObj.toString();
        MediaType json = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(json, body);
        final Request request = new Request.Builder()
                .url(host + path)
                .header("Authorization", "APPCODE " + appcode)
                .header("Content-Type", "application/json; charset=UTF-8")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                proDialog.dismiss();
                Toast.makeText(ctx, "验证失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                proDialog.dismiss();
                Log.d(TAG, "response:" + response.code());

                if (response.code() == 200) {
                    try {
                        String result = response.body().string();

                        JSONObject obj = new JSONObject(result);
                        JSONArray outputs = obj.getJSONArray("outputs");
                        String outputValue = outputs.getJSONObject(0).optJSONObject("outputValue").getString("dataValue");

                        Message msg = Message.obtain();
                        msg.what = 0;
                        msg.obj = outputValue;
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(-1);
                    }

                } else {
                    handler.sendEmptyMessage(-1);

                }
            }
        });


    }

    public void submit() {
        proDialog.setMessage("请稍候...");
        proDialog.show();
        user = MyApplication.getUser();


        RequestBody faceBody =
                RequestBody.create(MediaType.parse("application/otcet-stream"), BitmapUtil.cpPicToByte(facePath, 100));

        RequestBody backBody =
                RequestBody.create(MediaType.parse("application/otcet-stream"), BitmapUtil.cpPicToByte(backPath, 100));


        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart("userId", user.getId());
        builder.addFormDataPart("token", user.getToken());

        builder.addFormDataPart("name", idcard.getName());
        builder.addFormDataPart("cardnumber", idcard.getNum());
        builder.addFormDataPart("cardorgan", idcard.getIssue());

        builder.addFormDataPart("pcardpositive", "pcardpositive.jpg", faceBody);
        builder.addFormDataPart("pcardreverse", "pcardreverse.jpg", backBody);
        Request request = new Request.Builder().url(Constant.Host + "/saveUserCard").post(builder.build()).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                toast("验证失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                proDialog.dismiss();
                handler.sendEmptyMessage(100);

                if (response.isSuccessful()) {
                    Log.d("idcard", "验证成功-----");
                    user.setName(idcard.getName());
                    user.setPstatus(2);
                    save("user", new Gson().toJson(user));
                    MyApplication.setUser(user);
                    finish();
                }


            }
        });


    }


    /*
       * 获取参数的json对象
       */

    public static JSONObject getParam(int type, String dataValue) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("dataType", type);
            obj.put("dataValue", dataValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            String photoPath = null;
            if (requestCode == ALBUM_CODE) {
                Uri uri = data.getData();
                photoPath = UriUtil.getRealFilePath(this, uri);

            }
            if (requestCode == CAPTURE_CODE) {
                photoPath = captureFile.getPath();
            }

            if (type == 1) {
                Glide.with(context).load(facePath = photoPath).fitCenter().into(faceImg);

            } else if (type == 2) {
                Glide.with(context).load(backPath = photoPath).fitCenter().into(backImg);
            }
            readCard(photoPath);
        }

    }
    public void checkReadPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            reuestReadPermission();
        } else {
            pickImage();
        }

    }

    public void checkWritePermission() {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestWritePermission();
        } else   {
            captureFile = Util.getFile("imgs");
            Util.photo(ctx, captureFile, CAPTURE_CODE);
        }

    }

    private void reuestReadPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PEMISSION_READ);

    }


    private void requestWritePermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, PEMISSION_WRITE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PEMISSION_WRITE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                captureFile = Util.getFile("imgs");
                Log.d("cyd",captureFile.getPath());
                Util.photo(ctx, captureFile, CAPTURE_CODE);
            } else {
                // Permission Denied
                Toast.makeText(this, "请在 设置->权限 中授权拍照", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == PEMISSION_READ) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImage();
            } else {
                // Permission Denied
                Toast.makeText(this, "请在 设置->权限 中授权SD卡", Toast.LENGTH_SHORT).show();
            }
        }


    }
    private void pickImage() {

        it = new Intent(Intent.ACTION_PICK);
        it.setType("image/*");
        startActivityForResult(it, ALBUM_CODE);
    }
}
