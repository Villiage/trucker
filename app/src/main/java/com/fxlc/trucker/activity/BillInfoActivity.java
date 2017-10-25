package com.fxlc.trucker.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fxlc.trucker.BaseActivity;
import com.fxlc.trucker.MyApplication;
import com.fxlc.trucker.R;
import com.fxlc.trucker.api.OrderService;
import com.fxlc.trucker.bean.BillInfo;
import com.fxlc.trucker.net.HttpCallback;
import com.fxlc.trucker.net.HttpResult;
import com.fxlc.trucker.net.SimpleCallback;

import retrofit2.Call;
import retrofit2.Retrofit;

public class BillInfoActivity extends BaseActivity implements View.OnClickListener {
    private TextView orderNoTx;
    private TextView priceTx, loadWeightTx, unloadWeightTx, totalWorth;
    private TextView goodsTypeTx, perPriceTx, realLostTx, allowLostTx, lostWorth;
    private TextView insuranceTx, endTotalTx, intsumTx;;
    private TextView actionTx;
    //    private mBillInfoOrder mBillInfo;
    private TextView carNoTx, msgFeeTx;
    private String orderId;
    private BillInfo mBillInfo;
    private ImageView loadImg, unloadImg;
    private Dialog imgDialog;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billinfo);

        findView();

        orderId = getIntent().getStringExtra("orderId");
        loadData();

        imgDialog = new Dialog(ctx, R.style.dialog_alert);

        imgDialog.setContentView(R.layout.dialog_img);
        img = imgDialog.findViewById(R.id.img);
        Window win = imgDialog.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        orderId = getIntent().getStringExtra("orderId");
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        title("结算单");
    }

    private void loadData() {
        proDialog.show();
        Retrofit retrofit = MyApplication.getRetrofit();
        OrderService service = retrofit.create(OrderService.class);

        Call<HttpResult<BillInfo>> call = service.billInfo(orderId);

        call.enqueue(new HttpCallback<BillInfo>() {
            @Override
            public void onSuccess(BillInfo billInfo) {

                proDialog.dismiss();
                mBillInfo = billInfo;

                setValue();
            }
        });


    }

    private void setValue() {

        title(mBillInfo.getSourceName());
        carNoTx.setText("车牌号 " + mBillInfo.getCarNo() + " " + mBillInfo.getHandcarNo());
        priceTx.setText(mBillInfo.getFreight() + "元/吨");
        loadWeightTx.setText(mBillInfo.getLoadWeight() + "吨");
        if (!TextUtils.isEmpty(mBillInfo.getUnloadWeight()))
            unloadWeightTx.setText(mBillInfo.getUnloadWeight() + "吨");
        if (!TextUtils.isEmpty(mBillInfo.getFreightSum()))
            totalWorth.setText(mBillInfo.getFreightSum());

        goodsTypeTx.setText(mBillInfo.getGoodsType());
        perPriceTx.setText(mBillInfo.getPerPrice() + "元/吨");
        if (!TextUtils.isEmpty(mBillInfo.getRealLoss()))
            realLostTx.setText(mBillInfo.getRealLoss() + "吨");
        allowLostTx.setText(("允许损耗*吨 超出按货物单价赔付").replace("*", mBillInfo.getWear()));
        if (!TextUtils.isEmpty(mBillInfo.getLossSum()))
            lostWorth.setText("￥ -" + mBillInfo.getLossSum());


        insuranceTx.setText(mBillInfo.getInsurance() + "元");
        msgFeeTx.setText(mBillInfo.getMsgFee() + "元");
        if (!TextUtils.isEmpty(mBillInfo.getEndSum()))
            endTotalTx.setText("￥ " + mBillInfo.getEndSum());
        if (!TextUtils.isEmpty(mBillInfo.getIntSum()))
            intsumTx.setText("￥ " + mBillInfo.getIntSum());
        if (mBillInfo.getFeesType() == 0){
            findViewById(R.id.intpart).setVisibility(View.GONE);
        }

//        findViewById(R.id.toinfo).setOnClickListener(this);
        findViewById(R.id.action).setOnClickListener(this);

        orderNoTx.setText("订单编号：" + mBillInfo.getOrderNo());
        findViewById(R.id.call).setOnClickListener(this);

        Glide.with(ctx).load(mBillInfo.getLoadBill()).into(loadImg);
        Glide.with(ctx).load(mBillInfo.getUnloadBill()).into(unloadImg);

        if (mBillInfo.getOrderStatus().equals("3")) {
            actionTx.setVisibility(View.VISIBLE);
        } else actionTx.setVisibility(View.GONE);


    }

    private void findView() {
        carNoTx = (TextView) findViewById(R.id.carno);
        orderNoTx = (TextView) findViewById(R.id.order_no);

        titleTxt = (TextView) findViewById(R.id.title);
        priceTx = (TextView) findViewById(R.id.price);
        loadWeightTx = (TextView) findViewById(R.id.load_weight);
        unloadWeightTx = (TextView) findViewById(R.id.unload_weight);
        totalWorth = (TextView) findViewById(R.id.totalWorth);

        goodsTypeTx = (TextView) findViewById(R.id.goodsType);
        perPriceTx = (TextView) findViewById(R.id.perPrice);

        realLostTx = (TextView) findViewById(R.id.realloss);
        allowLostTx = (TextView) findViewById(R.id.allowlost);
        lostWorth = (TextView) findViewById(R.id.lostworth);

        insuranceTx = (TextView) findViewById(R.id.insurance);
        msgFeeTx = (TextView) findViewById(R.id.msg_fee);

        endTotalTx = (TextView) findViewById(R.id.end_total);
        intsumTx = (TextView) findViewById(R.id.intsum);
        loadImg = (ImageView) findViewById(R.id.loadimg);
        unloadImg = (ImageView) findViewById(R.id.unloadimg);

        actionTx = (TextView) findViewById(R.id.action);

        loadImg.setOnClickListener(this);
        unloadImg.setOnClickListener(this);

    }

    private void goCount() {
        proDialog.show();
        Retrofit retrofit = MyApplication.getRetrofit();
        OrderService service = retrofit.create(OrderService.class);

        Call<HttpResult> call = service.goCount(mBillInfo.getOrderId());

        call.enqueue(new SimpleCallback() {
            @Override
            public void onSuccess(HttpResult result) {
                proDialog.dismiss();
                toast("结算完成");
                finish();
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.action:

                goCount();
                break;

            case R.id.call:
                String tel = ((TextView) view).getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
                startActivity(intent);
                break;
            case R.id.loadimg:
                if (!TextUtils.isEmpty(mBillInfo.getLoadBill())) {
                    Glide.with(ctx).load(mBillInfo.getLoadBill()).centerCrop().into(img);
                    imgDialog.show();

                }

                break;
            case R.id.unloadimg:
                if (!TextUtils.isEmpty(mBillInfo.getUnloadBill())) {
                    Glide.with(ctx).load(mBillInfo.getUnloadBill()).centerCrop().into(img);
                    imgDialog.show();

                }
                break;
            case R.id.img:
                 imgDialog.dismiss();
                break;
        }
    }

//    private void orderInfo() {
//        proDialog.show();
//        Retrofit retrofit = MyApplication.getRetrofit();
//
//        OrderService service = retrofit.create(OrderService.class);
//
//        Call<HttpResult<OrderList.OrderBean>> call = service.orderInfo(mBillInfo.getOrderId());
//        call.enqueue(new HttpCallback<OrderList.OrderBean>() {
//            @Override
//            public void onSuccess(OrderList.OrderBean orderBean) {
//                proDialog.dismiss();
//                it.putExtra("order", orderBean);
//                it.putExtra("hide", true);
//                it.setClass(ctx, SourceInfoActivity.class);
//                startActivity(it);
//            }
//        });
//    }
}
