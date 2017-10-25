package com.fxlc.trucker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.fxlc.trucker.BaseActivity;
import com.fxlc.trucker.MyApplication;
import com.fxlc.trucker.R;
import com.fxlc.trucker.api.AccountService;
import com.fxlc.trucker.bean.Withdraw;
import com.fxlc.trucker.bean.WithdrawInfo;
import com.fxlc.trucker.net.HttpCallback;
import com.fxlc.trucker.net.HttpResult;

import retrofit2.Call;

public class WithdrawInfoActivity extends BaseActivity {

    Withdraw withdraw;
    ImageView proImg;
    TextView cashTx, reqDateTx, sucDateTx, bankNameTx, amountTx, resTx;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_info);
        withdraw = (Withdraw) getIntent().getSerializableExtra("withdraw");

        if (withdraw != null) {
            id = withdraw.getCashId();
        } else {
            id = getIntent().getStringExtra("id");
        }

        cashTx = (TextView) findViewById(R.id.cash);
        proImg = (ImageView) findViewById(R.id.pro);

        reqDateTx = (TextView) findViewById(R.id.req_date);
        sucDateTx = (TextView) findViewById(R.id.suc_date);
        bankNameTx = (TextView) findViewById(R.id.bank);
        amountTx = (TextView) findViewById(R.id.amount);
        resTx = (TextView) findViewById(R.id.withdraw_res);


        getInfo();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        id = getIntent().getStringExtra("id");
         getInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();

        title("提现详情");
    }


    private void getInfo() {

        AccountService service = MyApplication.getRetrofit().create(AccountService.class);

        Call<HttpResult<WithdrawInfo>> call = service.withdrawInfo(id);

        call.enqueue(new HttpCallback<WithdrawInfo>() {
            @Override
            public void onSuccess(WithdrawInfo withdrawInfo) {

                cashTx.setText("￥" + withdrawInfo.getMoney());
                reqDateTx.setText(withdrawInfo.getCreateDate());
                if (!TextUtils.isEmpty(withdrawInfo.getSuccessDate())) {
                    sucDateTx.setText(withdrawInfo.getSuccessDate());
                    proImg.setImageResource(R.drawable.tixian);
                }
                if (withdrawInfo.getStatus().equals("1")) {

                    resTx.setText("处理成功");
                }else if (withdrawInfo.getStatus().equals("2")){
                    resTx.setText("处理失败");
                    resTx.setTextColor(getResources().getColor(R.color.text_tip));
                }else if (withdrawInfo.getStatus().equals("0")){
                    resTx.setText("处理中");
                    resTx.setTextColor(getResources().getColor(R.color.text_tip));
                }
                String wh = withdrawInfo.getBankNo().substring(withdrawInfo.getBankNo().length() - 4);
                bankNameTx.setText(withdrawInfo.getBankName() + "(" + wh + ")");
                amountTx.setText("￥" + withdrawInfo.getMoney());
            }
        });

    }
}
