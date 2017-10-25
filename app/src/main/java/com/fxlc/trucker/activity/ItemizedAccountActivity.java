package com.fxlc.trucker.activity;

import android.app.Dialog;
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
import com.fxlc.trucker.api.AccountService;
import com.fxlc.trucker.api.OrderService;
import com.fxlc.trucker.bean.MyOrder;
import com.fxlc.trucker.bean.WalletDetail;
import com.fxlc.trucker.net.HttpCallback;
import com.fxlc.trucker.net.HttpResult;
import com.fxlc.trucker.util.DialogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Retrofit;

public class ItemizedAccountActivity extends ListActiviity implements View.OnClickListener {
    boolean income = true;

    Dialog d;
    String[] yArr = {"2015", "2016", "2017", "2018", "2019"};
    String[] mArr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};

   TextView sumTx,sumBalanceTx;
    String mYear, mMonth;

    Calendar calendar = Calendar.getInstance(Locale.CHINA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_itemized);
        super.onCreate(savedInstanceState);


        createDialog();
        mYear = calendar.get(Calendar.YEAR) + "";
        mMonth = (calendar.get(Calendar.MONTH) + 1) + "";

        sumTx = (TextView) findViewById(R.id.sum);
        sumBalanceTx = (TextView) findViewById(R.id.sum_balance);


        findViewById(R.id.income).setOnClickListener(this);
        findViewById(R.id.withdraw).setOnClickListener(this);
        findViewById(R.id.cal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.show();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (income) {
                    it.setClass(ctx, BillInfoActivity.class);
                    WalletDetail.IncomeBean bean = inListAdapter.getItem(i);
                    it.putExtra("orderId", bean.getOrderId());
                } else {
                    it.setClass(ctx, WithdrawInfoActivity.class);
                    it.putExtra("id",outListAdapter.getItem(i).getWithdrawId());
                }
                startActivity(it);
            }
        });
        loadData();
    }
    private void createDialog(){

        d = DialogUtil.createDateDialog(this, Arrays.asList(yArr), Arrays.asList(mArr), new DialogUtil.DateSelLisener() {
            @Override
            public void onSel(String year, String month) {
                d.dismiss();
                mYear = year;
                mMonth = month;
                loadData();

            }
        });
    }
    WalletDetail detail;
    public void loadData() {
        proDialog.show();
        Retrofit retrofit = MyApplication.getRetrofit();
        AccountService service = retrofit.create(AccountService.class);

        Call<HttpResult<WalletDetail>> call = service.walletDetail(mYear, mMonth);
        call.enqueue(new HttpCallback<WalletDetail>() {
            @Override
            public void onSuccess(WalletDetail walletDetail) {
                proDialog.dismiss();

                detail = walletDetail;
                inListAdapter = new InListAdapter(walletDetail.getIncome());
                outListAdapter = new OutListAdapter(walletDetail.getWithdraw());
                if (income) findViewById(R.id.income).performClick();
                else findViewById(R.id.withdraw).performClick();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        title("钱包明细");
    }

    InListAdapter inListAdapter;
    OutListAdapter outListAdapter;

    @Override
    public void onClick(View view) {
        view.setEnabled(false);
        switch (view.getId()) {
            case R.id.income:

                sumTx .setText("共" + inListAdapter.getCount() + "单");
                sumBalanceTx.setText("月收入：￥" + detail.getTotalIncome());
                income = true;
                findViewById(R.id.withdraw).setEnabled(true);

                if (inListAdapter.getCount() > 0){
                    showDataView();
                    listView.setAdapter(inListAdapter);
                }else showEmptyView();
                break;
            case R.id.withdraw:
                sumTx .setText("共" + outListAdapter.getCount() + "笔");
                sumBalanceTx.setText("月提现：￥" + detail.getTotalWithdraw());
                income = false;
                findViewById(R.id.income).setEnabled(true);
                if (outListAdapter.getCount() > 0){
                    showDataView();
                    listView.setAdapter(outListAdapter);
                }else showEmptyView();

                break;


        }
    }

    class InListAdapter extends BaseAdapter {
        public List<WalletDetail.IncomeBean> dataList;

        public InListAdapter(List<WalletDetail.IncomeBean> dataList) {
            this.dataList = dataList;
        }

        @Override
        public int getCount() {
            return dataList == null ? 0 : dataList.size();
        }

        @Override
        public WalletDetail.IncomeBean getItem(int i) {
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
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_account_itemized, null);
                holder.orderNoTx = view.findViewById(R.id.order_no);
                holder.dateTx = view.findViewById(R.id.date);
                holder.moneyTx = view.findViewById(R.id.mount);

                view.setTag(holder);
            } else holder = (ViewHolder) view.getTag();

            WalletDetail.IncomeBean bean = getItem(i);
            holder.orderNoTx.setText("订单编号：" + bean.getOrderNo());
            holder.dateTx.setText(bean.getSuccessDate());
            holder.moneyTx.setText(bean.getMoney() + "");
            return view;
        }
    }

    class OutListAdapter extends BaseAdapter {
        public List<WalletDetail.WithdrawBean> list;

        public OutListAdapter(List<WalletDetail.WithdrawBean> dataList) {
            this.list = dataList;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public WalletDetail.WithdrawBean getItem(int i) {
            return list.get(i);
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
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_withdraw, null);

                holder.dateTx = view.findViewById(R.id.date);
                holder.moneyTx = view.findViewById(R.id.mount);

                view.setTag(holder);
            } else holder = (ViewHolder) view.getTag();

            WalletDetail.WithdrawBean bean = getItem(i);
            holder.dateTx.setText(bean.getSuccessDate());
            holder.moneyTx.setText(bean.getMoney() + "");

            return view;
        }
    }

    class ViewHolder {

        TextView orderNoTx, dateTx, moneyTx;

    }
}
