package com.fxlc.trucker.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fxlc.trucker.BaseActivity;
import com.fxlc.trucker.MyApplication;
import com.fxlc.trucker.R;
import com.fxlc.trucker.bean.User;

public class UInfoActivity extends BaseActivity implements View.OnClickListener {


    private User user;
    private TextView mobileTxt, psStatuTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_uinfo);
        mobileTxt = (TextView) findViewById(R.id.mobile);
        psStatuTxt = (TextView) findViewById(R.id.ps_statu);


        findViewById(R.id.idcard).setOnClickListener(this);
        findViewById(R.id.setpwd).setOnClickListener(this);
        findViewById(R.id.paypwd).setOnClickListener(this);

        setValue();

    }


    @Override
    protected void onResume() {
        super.onResume();

        title("我的资料");
        setValue();
    }

    private void setValue() {
        user = MyApplication.getUser();
        mobileTxt.setText(user.getMobile());
        if (user.getPstatus() == 0) {
            psStatuTxt.setText("未认证");
        } else if (user.getPstatus() == 2) {
            psStatuTxt.setText("已认证");
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.idcard:

                if (user.getPstatus() == 0) {
                    it.setClass(ctx, IDcardAuditActivity.class);
                } else {
                    it.setClass(ctx, IDcardStatuActivity.class);

                }
                startActivity(it);

                break;

            case R.id.setpwd:

                it.setClass(ctx, ReSetPwdActivity.class);
                startActivity(it);
                break;
            case R.id.paypwd:

                it.setClass(ctx, ResetPayPwdActivity.class);

                startActivity(it);
                break;

        }
    }


}
