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
import com.fxlc.trucker.api.CarService;
import com.fxlc.trucker.bean.MyCars;
import com.fxlc.trucker.bean.Truck;
import com.fxlc.trucker.net.HttpCallback;
import com.fxlc.trucker.net.HttpResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;


public class MycarActivity extends ListActiviity implements View.OnClickListener{


    List<Truck> trucks = new ArrayList<>();
    TruckAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_mycar);
        super.onCreate(savedInstanceState);

        listView.setAdapter(adapter =  new TruckAdapter());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                it.setClass(ctx, TruckInfoActivity.class);
                it.putExtra("truck", trucks.get(i));
                startActivity(it);
            }
        });
        findViewById(R.id.add).setOnClickListener(this);
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        title("我的车辆");
    }

    public void loadData() {
        proDialog.show();
        Retrofit retrofit = MyApplication.getRetrofit();
        CarService service = retrofit.create(CarService.class);
        Call<HttpResult<MyCars>> call = service.getTrucks();
        call.enqueue(new HttpCallback<MyCars>() {
            @Override
            public void onSuccess(MyCars myCars) {
                 proDialog.dismiss();
                 trucks = myCars.getCarlist();
                 if (trucks != null && !trucks.isEmpty()){

                     showDataView();
                     adapter.notifyDataSetChanged();

                 }else {
                     showEmptyView();
                 }
            }
        });


    }

    @Override
    public void onClick(View view) {
         switch (view.getId()){
             case R.id.add:

                 it.setClass(ctx,AddCarActivity.class);
                 startActivity(it);

                 break;


         }
    }


    class TruckAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return trucks == null ? 0 : trucks.size();
        }

        @Override
        public Truck getItem(int i) {
            return trucks.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            Truck truck = trucks.get(i);
            if (view == null)
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mycar, null);

            TextView carNo = (TextView) view.findViewById(R.id.car_no);
            TextView carStatu = (TextView) view.findViewById(R.id.car_statu);

            if (truck.getStatus() == 0){
              carStatu.setText("未通过");
            }else if (truck.getStatus() == 1){
                carStatu.setText("审核中");
                 carStatu.setTextColor(getResources().getColor(R.color.text_blue));
            }else if (truck.getStatus() == 2){
                carStatu.setTextColor(getResources().getColor(R.color.text_blue));
                carStatu.setText("通过");
            }
            if (truck.getCartype() == 0){
                carNo.setText(truck.getCarNo());

            } else if (truck.getCartype() == 1){
                carNo.setText(truck.getHandcarNo());

            }
            return view;
        }
    }


}
