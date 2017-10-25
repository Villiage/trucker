package com.fxlc.trucker.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.fxlc.trucker.ListActiviity;
import com.fxlc.trucker.MyApplication;
import com.fxlc.trucker.R;
import com.fxlc.trucker.api.OrderService;
import com.fxlc.trucker.bean.AllBill;
import com.fxlc.trucker.net.HttpCallback;
import com.fxlc.trucker.net.HttpResult;
import com.fxlc.trucker.net.SimpleCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;


public class AllbilActivity extends ListActiviity implements View.OnClickListener {
    List<AllBill.BillBean> finishList;
    List<AllBill.BillBean> unloadList;
    List<AllBill.BillBean> loadList;
    List<AllBill.ReportBean> reportList;

    ReportListAdapter reportAdapter;
    LoadBillAdapter loadAdapter;
    BillAdapter unloadAdapter;
    BillAdapter finishAdapter;

    int tab = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_allbil);
        super.onCreate(savedInstanceState);
        tab = getIntent().getIntExtra("tab",1);
        findViewById(R.id.finish).setOnClickListener(this);
        findViewById(R.id.load).setOnClickListener(this);
        findViewById(R.id.unload).setOnClickListener(this);
        findViewById(R.id.report).setOnClickListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (tab > 0) {
                    it.setClass(ctx, BillInfoActivity.class);
                    it.putExtra("orderId", ((AllBill.BillBean) listView.getItemAtPosition(i)).getOrderId());
                    startActivity(it);

                }

            }
        });
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        title("运单管理");
        loadData();
    }


    @Override
    public void loadData() {
        proDialog.show();
        Retrofit retrofit = MyApplication.getRetrofit();

        OrderService service = retrofit.create(OrderService.class);

        Call<HttpResult<AllBill>> call = service.listAllbill();


        call.enqueue(new HttpCallback<AllBill>() {
            @Override
            public void onSuccess(AllBill allBill) {
                proDialog.dismiss();
                reportList = allBill.getReportList();
                loadList = allBill.getLoadList();
                unloadList = allBill.getUnloadList();
                finishList = allBill.getFinishList();

                reportAdapter = new ReportListAdapter(reportList);
                loadAdapter = new LoadBillAdapter(loadList);
                unloadAdapter = new BillAdapter(unloadList);
                finishAdapter = new BillAdapter(finishList);

                findViewById(R.id.report).setEnabled(true);
                findViewById(R.id.load).setEnabled(true);
                findViewById(R.id.unload).setEnabled(true);
                findViewById(R.id.finish).setEnabled(true);
               if (tab == 0){
                   findViewById(R.id.report).performClick();
               }else if (tab == 1){
                   findViewById(R.id.load).performClick();
               }else if (tab == 2){
                   findViewById(R.id.unload).performClick();
               }else {
                   findViewById(R.id.finish).performClick();
               }

            }

        });
    }

    public void delReport(String reportId) {

        Retrofit retrofit = MyApplication.getRetrofit();
        OrderService service = retrofit.create(OrderService.class);

        Call call = service.delReportNo(reportId);
        call.enqueue(new SimpleCallback() {
            @Override
            public void onSuccess(HttpResult result) {
                toast("取消成功");
                loadData();
            }
        });


    }

    @Override
    public void onClick(View view) {
        view.setEnabled(false);
        findViewById(R.id.report).setEnabled(!(view.getId() == R.id.report));
        findViewById(R.id.load).setEnabled(!(view.getId() == R.id.load));
        findViewById(R.id.unload).setEnabled(!(view.getId() == R.id.unload));
        findViewById(R.id.finish).setEnabled(!(view.getId() == R.id.finish));

        switch (view.getId()) {
            case R.id.report:
                tab = 0;
                if (reportList.size() > 0) {
                    showDataView();
                    listView.setAdapter(reportAdapter);
                } else showEmptyView();


                break;
            case R.id.load:
                tab = 1;
                if (loadList.size() > 0) {
                    showDataView();
                    listView.setAdapter(loadAdapter);
                } else showEmptyView();


                break;
            case R.id.unload:
                tab = 2;
                if (unloadList.size() > 0) {
                    showDataView();
                    listView.setAdapter(unloadAdapter);
                } else showEmptyView();

                break;
            case R.id.finish:
                tab = 3;
                if (finishList.size() > 0) {
                    showDataView();
                    listView.setAdapter(finishAdapter);
                } else showEmptyView();

                break;
            case R.id.cancel:
                String id = (String) view.getTag();
                delReport(id);
                break;

        }
    }

    class ReportListAdapter extends BaseAdapter {

        public List<AllBill.ReportBean> dataList;

        public ReportListAdapter(List<AllBill.ReportBean> dataList) {
            this.dataList = dataList;
        }

        @Override
        public int getCount() {
            return dataList == null ? 0 : dataList.size();
        }

        @Override
        public AllBill.ReportBean getItem(int i) {
            return dataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ReportViewHolder holder;
            if (view == null) {
                holder = new ReportViewHolder();
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_report, null);
                holder.carNoTx = view.findViewById(R.id.carno);
                holder.sourceNameTx = view.findViewById(R.id.source_name);
                holder.reportTimeTx = view.findViewById(R.id.report_time);
                holder.cancelTx = view.findViewById(R.id.cancel);
                view.setTag(holder);
            } else holder = (ReportViewHolder) view.getTag();
            final AllBill.ReportBean report = getItem(i);
            holder.carNoTx.setText("车牌号：" + report.getCarNo());
            holder.sourceNameTx.setText(report.getSourceName());
            holder.reportTimeTx.setText("报号时间：" + report.getReportNoDate());
            holder.cancelTx.setTag(report.getReportNoId());
            holder.cancelTx.setOnClickListener(AllbilActivity.this);
            return view;
        }
    }

    class LoadBillAdapter extends BaseAdapter {

        public List<AllBill.BillBean> dataList;

        public LoadBillAdapter(List<AllBill.BillBean> dataList) {
            this.dataList = dataList;
        }

        @Override
        public int getCount() {
            return dataList == null ? 0 : dataList.size();
        }

        @Override
        public AllBill.BillBean getItem(int i) {
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
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_loadbill, null);
                holder.carNoTx = view.findViewById(R.id.carno);
                holder.loadTimeTx = view.findViewById(R.id.load_time);
                holder.loadWeightTx = view.findViewById(R.id.load_weight);
                view.setTag(holder);
            } else holder = (ViewHolder) view.getTag();
            AllBill.BillBean bill = getItem(i);
            holder.carNoTx.setText("车牌号：" + bill.getCarNo());


            holder.loadTimeTx.setText("装车时间：" + bill.getLoadDate());
            holder.loadWeightTx.setText("装车净重：" + bill.getLoadWeight());


            return view;
        }


    }

    class BillAdapter extends BaseAdapter {

        public List<AllBill.BillBean> dataList;

        public BillAdapter(List<AllBill.BillBean> dataList) {
            this.dataList = dataList;
        }

        @Override
        public int getCount() {
            return dataList == null ? 0 : dataList.size();
        }

        @Override
        public AllBill.BillBean getItem(int i) {
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
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_bill, null);
                holder.carNoTx = view.findViewById(R.id.carno);
                holder.loadTimeTx = view.findViewById(R.id.load_time);
                holder.unloadTimeTx = view.findViewById(R.id.unload_time);
                holder.loadWeightTx = view.findViewById(R.id.load_weight);
                holder.unloadWeightTx = view.findViewById(R.id.unload_weight);
                holder.freightTx = view.findViewById(R.id.freight);
                view.setTag(holder);
            } else holder = (ViewHolder) view.getTag();
            AllBill.BillBean bill = getItem(i);
            holder.carNoTx.setText("车牌号：" + bill.getCarNo());

            holder.unloadTimeTx.setText("卸车时间：" + bill.getUnloadDate());
            holder.unloadWeightTx.setText("卸车净重：" + bill.getUnloadWeight());
            holder.freightTx.setText("￥" + bill.getEndSum() );

            holder.loadTimeTx.setText("装车时间：" + bill.getLoadDate());
            holder.loadWeightTx.setText("装车净重：" + bill.getLoadWeight());


            return view;
        }


    }

    class ReportViewHolder {

        TextView sourceNameTx, carNoTx, reportTimeTx, cancelTx;


    }

    class ViewHolder {

        TextView carNoTx, loadTimeTx, unloadTimeTx, loadWeightTx, unloadWeightTx, freightTx;


    }
}
