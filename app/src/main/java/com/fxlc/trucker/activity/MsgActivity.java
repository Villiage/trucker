package com.fxlc.trucker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fxlc.trucker.BaseActivity;
import com.fxlc.trucker.ListActiviity;
import com.fxlc.trucker.MyApplication;
import com.fxlc.trucker.R;
import com.fxlc.trucker.api.SettingService;
import com.fxlc.trucker.bean.MsgList;
import com.fxlc.trucker.net.HttpCallback;
import com.fxlc.trucker.net.HttpResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;

public class MsgActivity extends ListActiviity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_msg);
        super.onCreate(savedInstanceState);
         listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 MsgList.MsgListBean msg = adapter.getItem(i);
                 if (msg.getType().equals("1")) {
                     it.setClass(ctx, WithdrawInfoActivity.class);
                     it.putExtra("id", msg.getMessageId());
                 } else if (msg.getType().equals("2")) {
                     it.setClass(ctx, BillInfoActivity.class);
                     it.putExtra("orderId", msg.getMessageId());
                 }
                 startActivity(it);
             }
         });

        loadData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        title("消息");
    }

    @Override
    public void loadData() {
        Retrofit retrofit = MyApplication.getRetrofit();
        SettingService service = retrofit.create(SettingService.class);
        Call<HttpResult<MsgList>> call = service.getMsg();

        call.enqueue(new HttpCallback<MsgList>() {
            @Override
            public void onSuccess(MsgList msgList) {
                 adapter =  new MListAdapter(msgList.getMsgList());

                 listView.setAdapter(adapter);
            }
        });


    }

    MListAdapter adapter;

    class MListAdapter extends BaseAdapter {
        private List<MsgList.MsgListBean> dataList;

        public MListAdapter(List<MsgList.MsgListBean> dataList) {
            this.dataList = dataList;
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public MsgList.MsgListBean getItem(int i) {
            return dataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            TextView dateTx, contentTx; ;
            if (view == null) {

                int id = R.layout.item_msg;
                view = LayoutInflater.from(viewGroup.getContext()).inflate(id, viewGroup, false);
            }
            MsgList.MsgListBean msg = dataList.get(i);
            dateTx =  view.findViewById(R.id.date);
            contentTx =  view.findViewById(R.id.msg );

            dateTx.setText(msg.getCreateDate());
            contentTx.setText(msg.getContent());
            return view;
        }
    }
}
