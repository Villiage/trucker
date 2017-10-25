package com.fxlc.trucker.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.fxlc.trucker.BaseActivity;
import com.fxlc.trucker.MyApplication;
import com.fxlc.trucker.R;
import com.fxlc.trucker.api.UserService;
import com.fxlc.trucker.bean.CardInfo;
import com.fxlc.trucker.bean.UInfo;
import com.fxlc.trucker.net.HttpCallback;
import com.fxlc.trucker.net.HttpResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class IDcardStatuActivity extends BaseActivity {

    TextView nameTx, idnoTx, issueTx;
    CardInfo info;
    UserService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard_statu);

        nameTx = (TextView) findViewById(R.id.name);
        idnoTx = (TextView) findViewById(R.id.id_no);
        issueTx = (TextView) findViewById(R.id.issue);

        load();
    }

    @Override
    protected void onResume() {
        super.onResume();
        title("个人信息");
    }

    private void load() {
        proDialog.show();
        Retrofit retrofit = MyApplication.getRetrofit();

        UserService service = retrofit.create(UserService.class);
        Call<HttpResult<CardInfo>> call = service.cardInfo();

        call.enqueue(new HttpCallback<CardInfo>() {
            @Override
            public void onSuccess(CardInfo cardInfo) {
                proDialog.dismiss();
                nameTx.setText(cardInfo.getName());
                idnoTx.setText(cardInfo.getCardnumber());
                issueTx.setText(cardInfo.getCardorgan());
            }
        });
    }
}
