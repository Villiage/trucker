package com.fxlc.trucker.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fxlc.trucker.BaseActivity;
import com.fxlc.trucker.ListActiviity;
import com.fxlc.trucker.MyApplication;
import com.fxlc.trucker.R;
import com.fxlc.trucker.api.AccountService;
import com.fxlc.trucker.bean.BankCards;
import com.fxlc.trucker.net.HttpCallback;
import com.fxlc.trucker.net.HttpResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;

public class BankActivity extends ListActiviity implements View.OnClickListener {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bank);
        super.onCreate(savedInstanceState);
        listView = (ListView) findViewById(R.id.list);
        findViewById(R.id.add).setOnClickListener(this);
        adapter = new MListAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                it.setClass(ctx, BankCardInfoActivity.class);
                it.putExtra("card", adapter.getItem(i));
                startActivity(it);
            }
        });

    }


    public void loadData() {

        proDialog.show();
        Retrofit retrofit = MyApplication.getRetrofit();
        AccountService service = retrofit.create(AccountService.class);
        Call<HttpResult<BankCards>> call = service.listBankCard();
        call.enqueue(new HttpCallback<BankCards>() {
            @Override
            public void onSuccess(BankCards bankCards) {
                proDialog.dismiss();


                dataList = bankCards.getBankcard();
                if (dataList.size() > 0) {
                    showDataView();
                    adapter.notifyDataSetChanged();

                } else showEmptyView();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        title("银行卡");
        loadData();
    }

    private List<BankCards.BankCard> dataList;
    private MListAdapter adapter;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add:
                it.setClass(ctx, AddBankcardActivity.class);
                startActivity(it);
                break;


        }
    }

    class MListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return dataList == null ? 0 : dataList.size();
        }

        @Override
        public BankCards.BankCard getItem(int i) {
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
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_bankcard, viewGroup,false);
                holder.bankNameTx = view.findViewById(R.id.bankname);
                holder.cardNoTx = view.findViewById(R.id.cardno);

                view.setTag(holder);
            } else holder = (ViewHolder) view.getTag();

            BankCards.BankCard card = getItem(i);
            holder.bankNameTx.setText(card.getBankType());
            holder.cardNoTx.setText(card.getBankNo());
            return view;
        }
    }

    class ViewHolder {

        TextView bankNameTx, cardNoTx;

    }
}
