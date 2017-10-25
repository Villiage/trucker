package com.fxlc.trucker.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.fxlc.trucker.ListActiviity;
import com.fxlc.trucker.MyApplication;
import com.fxlc.trucker.R;
import com.fxlc.trucker.api.OrderService;
import com.fxlc.trucker.bean.MyOrder;
import com.fxlc.trucker.net.HttpCallback;
import com.fxlc.trucker.net.HttpResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;

public class MybillActivity extends ListActiviity implements View.OnClickListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_mybill);
        super.onCreate(savedInstanceState);

        findViewById(R.id.finish).setOnClickListener(this);
        findViewById(R.id.unfinish).setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 it.setClass(ctx,BillInfoActivity.class);
                 MyOrder.MyOrderBean order = (MyOrder.MyOrderBean) adapterView.getItemAtPosition(i);
                 it.putExtra("orderId",order.getOrderId());
                 startActivity(it);
            }
        });
        loadData();


    }
    List<MyOrder.MyOrderBean> finishList;
    List<MyOrder.MyOrderBean> unfinishList;
    MListAdapter finishAdapter;
    MListAdapter unfinishAdapter;
    boolean finish;
    public void loadData() {
        proDialog.show();
        Retrofit retrofit = MyApplication.getRetrofit();

        OrderService service = retrofit.create(OrderService.class);

        Call<HttpResult<MyOrder>> call = service.myOrder();
        call.enqueue(new HttpCallback<MyOrder>() {
            @Override
            public void onSuccess(MyOrder myOrder) {
                 proDialog.dismiss();
                 finishList = myOrder.getFinishList();
                 unfinishList = myOrder.getUnfinishList();


                 finishAdapter =  new MListAdapter(finishList);
                 unfinishAdapter =  new MListAdapter(unfinishList);
                findViewById(R.id.finish).setEnabled(true);
                findViewById(R.id.unfinish).setEnabled(true);
                 findViewById(R.id.finish).performClick();

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        title("我的订单");
    }



    @Override
    public void onClick(View view) {
        view.setEnabled(false);
        switch (view.getId()){
            case R.id.finish:
                  if (finishList.size() > 0){
                      showDataView();
                      listView.setAdapter(finishAdapter);
                  }else showEmptyView();
                findViewById(R.id.unfinish).setEnabled(true);
                 finish = true;
                break;
            case R.id.unfinish:
                if ( unfinishList.size() > 0){
                    showDataView();
                    listView.setAdapter(unfinishAdapter);
                }else showEmptyView();

                findViewById(R.id.finish).setEnabled(true);
                finish = false;
                break;

        }
    }

    class MListAdapter extends BaseAdapter {

        public List<MyOrder.MyOrderBean> dataList;

        public MListAdapter(List<MyOrder.MyOrderBean> dataList) {
            this.dataList = dataList;
        }

        @Override
        public int getCount() {
            return dataList == null ? 0 : dataList.size();
        }

        @Override
        public MyOrder.MyOrderBean getItem(int i) {
            return dataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mybill, null);
                holder.orderNoTx = view.findViewById(R.id.order_no);
                holder.fromTx = view.findViewById(R.id.from);
                holder.toTx = view.findViewById(R.id.dest);
                holder.moneyTx = view.findViewById(R.id.money);
                view.setTag(holder);
            } else holder = (ViewHolder) view.getTag();
            MyOrder.MyOrderBean  order = getItem(i);
            holder.orderNoTx.setText("订单编号：" + order.getOrderNo());
            holder.fromTx.setText(order.getFrom());
            holder.toTx.setText(order.getTo());

            holder.moneyTx.setText((finish? "已付款：": "待付款：" ) +  order.getMoney());

            return view;
        }
    }

    class ViewHolder {

        TextView orderNoTx,fromTx,toTx,moneyTx;


    }
}
