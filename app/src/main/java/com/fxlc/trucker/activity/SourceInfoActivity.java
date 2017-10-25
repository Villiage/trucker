package com.fxlc.trucker.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.fxlc.trucker.BaseActivity;
import com.fxlc.trucker.MyApplication;
import com.fxlc.trucker.R;
import com.fxlc.trucker.api.OrderService;
import com.fxlc.trucker.bean.CarList;
import com.fxlc.trucker.bean.CurrentOrder;
import com.fxlc.trucker.bean.OrderList;
import com.fxlc.trucker.net.HttpCallback;
import com.fxlc.trucker.net.HttpResult;
import com.fxlc.trucker.net.MyThrowable;
import com.fxlc.trucker.util.CarnoDialog;
import com.fxlc.trucker.util.DisplayUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;

public class SourceInfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView loadPlaceTx, unloadPlaceTx, transFeeTx,
            goodsTypeTx, msgFeeTx, loadFeeTx, unloadFeeTx;
    private TextView unitTx, pathLostTx, remarkTx;


    private OrderList.OrderBean order;

    //    private AlertDialog.Builder builder;
//    private AlertDialog dialog;
    private StringBuffer ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_info);

        order = (OrderList.OrderBean) getIntent().getSerializableExtra("order");

        findView();
        setValue();

    }


    @Override
    protected void onResume() {
        super.onResume();

        title("订单详情");
    }


    private void findView() {


        loadPlaceTx = (TextView) findViewById(R.id.load_place);
        unloadPlaceTx = (TextView) findViewById(R.id.unload_place);
        transFeeTx = (TextView) findViewById(R.id.trans_fee);
        goodsTypeTx = (TextView) findViewById(R.id.goodsType);

        msgFeeTx = (TextView) findViewById(R.id.msg_fee);
        loadFeeTx = (TextView) findViewById(R.id.load_fee);
        unloadFeeTx = (TextView) findViewById(R.id.unload_fee);

        unitTx = (TextView) findViewById(R.id.unit_price);
        pathLostTx = (TextView) findViewById(R.id.path_loss);

        remarkTx = (TextView) findViewById(R.id.remark);


        findViewById(R.id.confirm).setOnClickListener(this);
        findViewById(R.id.load_call).setOnClickListener(this);
        findViewById(R.id.unload_call).setOnClickListener(this);
        findViewById(R.id.source_call).setOnClickListener(this);


    }


    public void setValue() {

        loadPlaceTx.setText(order.getFrom());
        unloadPlaceTx.setText(order.getTo());

        transFeeTx.setText(order.getFreight() + "元/吨");
        goodsTypeTx.setText(order.getGoodsType());


        msgFeeTx.setText("￥" + order.getMsgFee());
        loadFeeTx.setText("￥" + order.getLoadFee());
        unloadFeeTx.setText("￥" + order.getUnlaodFee());

        unitTx.setText( order.getPerPrice() + "/吨");

        pathLostTx.setText(order.getAllowLoss() + "吨");

        remarkTx.setText(getMark(order.getRemark()));
    }

    private String getMark(List<String> arr) {
        StringBuffer sb = new StringBuffer();
        for (String s : arr) {
            sb.append(s).append("、");
        }

        sb.replace(sb.length() - 1, sb.length(), "。");

        return sb.toString();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.confirm:

                getCarlist();

                break;

            case R.id.load_call:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + order.getLoadPhone()));

                startActivity(intent);

                break;
            case R.id.unload_call:
                Intent tent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + order.getUnloadPhone()));

                startActivity(tent);
            case R.id.source_call:
                Intent in = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + order.getComPhone()));

                startActivity(in);

                break;

        }
    }

    private void getCarlist() {
        proDialog.show();
        Retrofit retrofit = MyApplication.getRetrofit();

        OrderService service = retrofit.create(OrderService.class);

        Call<HttpResult<CarList>> cal = service.listCar();

        cal.enqueue(new HttpCallback<CarList>() {
            @Override
            public void onSuccess(CarList carList) {
                proDialog.dismiss();
                if (carList.getCarlist().size() > 0){
                    createDialog(carList);
                }else {
                    toast("您未添加车辆，请联系货主报号");
                }
            }

        });

    }

    private void confirm(String carId) {
        Retrofit retrofit = MyApplication.getRetrofit();

        OrderService service = retrofit.create(OrderService.class);

        Call<HttpResult<CurrentOrder>> cal = service.receiveOrder(order.getSourceId(), carId);

        cal.enqueue(new HttpCallback<CurrentOrder>() {
            @Override
            public void onSuccess(CurrentOrder currentOrder) {

                toast("报号成功");
                Intent it = new Intent();
                it.setClass(ctx, AllbilActivity.class);
                it.putExtra("tab", 0);
                startActivity(it);
                finish();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
//                super.onFailure(call, t);
                if (t instanceof MyThrowable) {
                    MyThrowable throwable = (MyThrowable) t;
                    if (throwable.getErrorCode().equals("88")) {

//                        dialog.show();
                    }
                }


            }
        });

    }

    Dialog carDialog;

    private void createDialog(CarList carList) {
        ids = new StringBuffer();
        carDialog = new Dialog(this, R.style.dialog_alert);
        carDialog.setCanceledOnTouchOutside(true);
        carDialog.setContentView(R.layout.choose_car);
        final ListView slistView = carDialog.findViewById(R.id.list);
        final TextView carnoTx =  carDialog.findViewById(R.id.carno);
        carnoTx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarnoDialog.generate(ctx,carnoTx);
            }
        });
        carDialog.findViewById(R.id.choose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0;i < slistView.getChildCount(); i ++){
                    CheckBox box = slistView.getChildAt(i).findViewById(R.id.check);
                    if (box.isChecked()){
                        if (ids.length() > 0) ids.append(",");
                            ids.append((String)box.getTag());
                    }
                }
                Log.d("cyd",ids.toString());
                if (ids.length() > 0){
                    confirm(ids.toString());
                    carDialog.dismiss();
                }

            }
        });
        carsAdapter = new CarsAdapter(carList);
        slistView.setAdapter(carsAdapter);

        Window win = carDialog.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);

        carDialog.show();

    }

    CarsAdapter carsAdapter;

    class CarsAdapter extends BaseAdapter {
        CarList carList;

        public CarsAdapter(CarList list) {
            this.carList = list;
        }

        @Override
        public int getCount() {
            return carList == null ? 0 : carList.getCarlist().size();
        }

        @Override
        public CarList.CarlistBean getItem(int i) {
            return carList.getCarlist().get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            CarList.CarlistBean bean = carList.getCarlist().get(i);
            if (view == null)
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_checklist, viewGroup, false);

            TextView text = view.findViewById(R.id.carno);
            CheckBox check = view.findViewById(R.id.check);
            check.setTag(bean.getCarId());
//            check.setOnCheckedChangeListener(occl);
            view.setEnabled(bean.getCarStatus().equals("0"));
            check.setEnabled(bean.getCarStatus().equals("0"));

            text.setText(bean.getCarNo());


            return view;
        }


    }


}
