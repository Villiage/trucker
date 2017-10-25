package com.fxlc.trucker.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.fxlc.trucker.BaseActivity;
import com.fxlc.trucker.MyApplication;
import com.fxlc.trucker.R;
import com.fxlc.trucker.api.AccountService;
import com.fxlc.trucker.bean.Balance;
import com.fxlc.trucker.bean.BankCards;
import com.fxlc.trucker.bean.User;
import com.fxlc.trucker.net.HttpCallback;
import com.fxlc.trucker.net.HttpResult;
import com.fxlc.trucker.net.SimpleCallback;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;

public class MyWalletActivity extends BaseActivity implements View.OnClickListener {
    TextView moneyTx;
    User user;
    private AlertDialog.Builder builder;
    private AlertDialog dialog1, dialog2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        findViewById(R.id.withdraw).setOnClickListener(this);
        findViewById(R.id.detail).setOnClickListener(this);
        moneyTx = (TextView) findViewById(R.id.money);
        user = MyApplication.getUser();

        moneyTx.setText(user.getMoney() + "");



        builder = new AlertDialog.Builder(ctx);
        builder.setMessage("您还没有添加银行卡");
        builder.setNegativeButton("等会儿", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("去添加", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                it.setClass(ctx, AddBankcardActivity.class);
                startActivity(it);
            }
        });
        dialog1 = builder.create();
        builder.setMessage("请先实名认证！");
        builder.setNegativeButton("等会儿", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("去认证", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                it.setClass(ctx, IDcardAuditActivity.class);
                startActivity(it);
            }
        });
        dialog2 = builder.create();

    }

    @Override
    protected void onResume() {
        super.onResume();
        title("我的钱包");
        getMoney();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.withdraw:
                if (user.getPstatus() == 0) {

                    dialog2.show();
                } else {
                    getMyCards();
                }

                break;

            case R.id.detail:

                it.setClass(ctx, ItemizedAccountActivity.class);
                startActivity(it);
                break;


        }
    }

    public void getMyCards() {

        proDialog.show();
        Retrofit retrofit = MyApplication.getRetrofit();
        AccountService service = retrofit.create(AccountService.class);
        final Call<HttpResult<BankCards>> call = service.listBankCard();
        call.enqueue(new HttpCallback<BankCards>() {
            @Override
            public void onSuccess(BankCards bankCards) {
                proDialog.dismiss();


                if (bankCards.getBankcard().size() > 0) {
                    it.setClass(ctx, WithdrawActivity.class);
                    it.putExtra("bankcards", bankCards);
                    it.putExtra("yue",moneyTx.getText().toString());
                    startActivity(it);
                } else {

//                     toast("请先绑定银行卡");
                    dialog1.show();
                }


            }
        });
    }

    private void getMoney() {

        AccountService service = MyApplication.getRetrofit().create(AccountService.class);

        Map<String, String> map = new HashMap<>();

        Call<HttpResult<Balance>> call = service.getMoney(map);
        call.enqueue(new HttpCallback<Balance>() {
            @Override
            public void onSuccess(Balance balance) {

                moneyTx.setText(balance.getMoney() + "");
            }
        });

    }
}
