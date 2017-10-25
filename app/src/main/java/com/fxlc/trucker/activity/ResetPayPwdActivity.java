package com.fxlc.trucker.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fxlc.trucker.BaseActivity;
import com.fxlc.trucker.Constant;
import com.fxlc.trucker.MyApplication;
import com.fxlc.trucker.R;
import com.fxlc.trucker.api.UserService;
import com.fxlc.trucker.net.HttpResult;
import com.fxlc.trucker.net.SimpleCallback;

import java.util.HashMap;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ResetPayPwdActivity extends BaseActivity implements View.OnClickListener {

    private TextView mPhoneView, mVerifyCodeView, mPwdView, mRePwdView;
    private String phone, pass, repass, verifyCode, title;
    private TextView getSmsTx;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_paypwd);

        user = MyApplication.getUser();
        initView();
        retrofit = MyApplication.getInstance().getRetrofit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String title = getIntent().getStringExtra("title");
        if (TextUtils.isEmpty(title))
            title("修改交易密码");
        else title(title);
    }

    private void initView() {

        mPhoneView = (TextView) findViewById(R.id.phone);
        mVerifyCodeView = (TextView) findViewById(R.id.verfiy_code);
        mPwdView = (EditText) findViewById(R.id.pwd);
        mRePwdView = (EditText) findViewById(R.id.repwd);


        mPhoneView.setText(user.getMobile());
        getSmsTx = (TextView) findViewById(R.id.getverify);
        findViewById(R.id.getverify).setOnClickListener(this);

        findViewById(R.id.action).setOnClickListener(this);

    }


    private void change() {
        proDialog.show();
        count = 60;
        retrofit = MyApplication.getRetrofit();
        UserService apiService = retrofit.create(UserService.class);

        HashMap map = new HashMap();
        map.put("id", user.getId());
        map.put("mobile", phone);
        map.put("yzm", verifyCode);
        map.put("newpass", pass);


        Log.d("req", "request" + verifyCode);
        Call<HttpResult> call = apiService.savePayPass(map);

        call.enqueue(new SimpleCallback() {
            @Override
            public void onSuccess(HttpResult result) {
                proDialog.dismiss();
                toast("设置成功");
                finish();
            }

            @Override
            public void onFailure(Call<HttpResult> call, Throwable throwable) {
                proDialog.dismiss();

            }
        });

    }

    private void getValue() {
        phone = mPhoneView.getText().toString();
        pass = mPwdView.getText().toString();
        repass = mRePwdView.getText().toString();
        verifyCode = mVerifyCodeView.getText().toString();


    }

    private boolean valid() {

        if (TextUtils.isEmpty(verifyCode)) {
            toast("请输入验证码");
            return false;
        }


        if (TextUtils.isEmpty(pass)) {

            toast("请输入密码");
            return false;
        }
        if (TextUtils.isEmpty(repass)) {
            toast("请再输一次密码");
            return false;
        }

        if (!Pattern.matches(pwPattern, pass)) {
            toast("密码必须是6位数字");
            return false;
        }
        if (!pass.equals(repass)) {
            toast("两次输入密码不一致");
            return false;
        }

        return true;
    }

    String pwPattern = "^\\d{6}$";

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.getverify:
                getsms();
                break;
            case R.id.action:
                getValue();
                if (valid())
                    change();

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
                    getSmsTx.setText(count + "s");
                    getSmsTx.setClickable(false);

                    sendEmptyMessageDelayed(0, 1000);
                } else if (count == 0) {
                    getSmsTx.setText("获取验证码");
                    getSmsTx.setClickable(true);

                }


            }

        }
    };

    public void getsms() {
        count = 60;
        handler.sendEmptyMessage(0);

        retrofit = new Retrofit.Builder().baseUrl(Constant.Host_SMS).build();

        UserService service = retrofit.create(UserService.class);
        String mobile = mPhoneView.getText().toString();
        Call<ResponseBody> call = service.getSms(mobile);
        call.enqueue(new Callback<ResponseBody>() {


            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                toast("发送成功");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


}
