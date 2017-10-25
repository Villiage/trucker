package com.fxlc.trucker.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.fxlc.trucker.MainActivity;
import com.fxlc.trucker.MyApplication;
import com.fxlc.trucker.bean.User;

public class SplashActivity extends Activity {
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        user = MyApplication.getUser();
        if (user == null) {
            Intent it = new Intent();
            it.setClass(this, LoginNewActivity.class);
            startActivity(it);
            finish();
        } else {


            Intent it = new Intent();
            it.setClass(this, MainActivity.class);
            startActivity(it);
            finish();
        }

    }
}
