package com.fxlc.trucker.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fxlc.trucker.BaseActivity;
import com.fxlc.trucker.Constant;
import com.fxlc.trucker.MainActivity;
import com.fxlc.trucker.MyApplication;
import com.fxlc.trucker.R;
import com.fxlc.trucker.api.UserService;
import com.fxlc.trucker.bean.User;
import com.fxlc.trucker.net.HttpCallback;
import com.fxlc.trucker.net.HttpResult;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A login screen that offers login via email/password.
 */
public class LoginNewActivity extends BaseActivity implements View.OnClickListener {


    private EditText mPhoneView;
    private EditText verifyCodeTx;
    private TextView getSmsTx;
    private String phone, verifyCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        mPhoneView = (EditText) findViewById(R.id.phone);
        verifyCodeTx = (EditText) findViewById(R.id.verify_code);
        getSmsTx = (TextView) findViewById(R.id.getsms);
        getSmsTx.setOnClickListener(this);

        findViewById(R.id.action).setOnClickListener(this);

    }


    private void getValue() {
        phone = mPhoneView.getText().toString();
        verifyCode = verifyCodeTx.getText().toString();

    }


    private void login() {

        proDialog.show();

        Retrofit retrofit = MyApplication.getRetrofit();
        UserService apiService = retrofit.create(UserService.class);

        Call<HttpResult<User>> call = apiService.login(phone,verifyCode);

        call.enqueue(new HttpCallback<User>() {
            @Override
            public void onSuccess(User user) {
                proDialog.dismiss();
                toast("登录成功");
                save("user", new Gson().toJson(user));
                /*
                 关闭侧滑
                 */
                sp.edit().putBoolean("close",true).commit();
                MyApplication.setUser(user);
                it.setClass(ctx, MainActivity.class);
                startActivity(it);
                finish();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                proDialog.dismiss();
            }
        });


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.action:
                getValue();
                login();
                break;
            case R.id.getsms:
                getValue();
                if (!TextUtils.isEmpty(phone))
                    getsms();
                else  toast("请输入手机号");

                break;
        }
    }
    int count = 60;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {

                count--;
                if (count > 0) {
                    getSmsTx.setText(count + "秒后重新发生");
                    getSmsTx.setClickable(false);
                    getSmsTx.setTextColor(Color.DKGRAY);
                    sendEmptyMessageDelayed(0, 1000);
                } else if (count == 0){
                    getSmsTx.setText(R.string.getsms);
                    getSmsTx.setClickable(true);
                    getSmsTx.setTextColor(getResources().getColor(R.color.text_blue));
                }


            }

        }
    };

    public void getsms() {
        count = 60;
        handler.sendEmptyMessage(0);
        String url = Constant.Host_SMS + "sms/getSms" + "?";
        String param = "mobile=" + phone;
        url += param;
        final String finalUrl = url;
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HttpURLConnection conn = (HttpURLConnection) new URL(finalUrl).openConnection();

                            conn.setRequestMethod("GET");
                            conn.getInputStream();

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
        ).start();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
         finish();
         System.exit(0);
    }
}

