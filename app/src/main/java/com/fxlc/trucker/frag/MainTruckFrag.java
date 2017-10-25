package com.fxlc.trucker.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fxlc.trucker.R;
import com.fxlc.trucker.bean.Truck;


import java.util.List;



/**
 * Created by cyd on 2017/6/30.
 */

public class MainTruckFrag extends Fragment {

    ImageView img1, img2, img3, manageImg;
    TextView brandTx;
    TextView carnoTx;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static MainTruckFrag newInstance(Truck truck) {

        Bundle args = new Bundle();
        args.putSerializable("truck", truck);
        MainTruckFrag fragment = new MainTruckFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Truck truck = (Truck) getArguments().getSerializable("truck");
        View view = inflater.inflate(R.layout.frag_maintruck, null);
        img1 = (ImageView) view.findViewById(R.id.drive1);
        img2 = (ImageView) view.findViewById(R.id.drive2);
        img3 = (ImageView) view.findViewById(R.id.drive3);
        manageImg = (ImageView) view.findViewById(R.id.manageimg);

        brandTx = (TextView) view.findViewById(R.id.brand);
        carnoTx = (TextView) view.findViewById(R.id.carno);
        if (truck.getCartype() == 0) {
            brandTx.setText(truck.getBrand() + " " + truck.getStyle());
            carnoTx.setText(truck.getCarNo());

        }

        List<String> imgs = truck.getDriveImg();
        if (imgs != null) {
            Glide.with(this).load(imgs.get(0)).into(img1);
            Glide.with(this).load(imgs.get(1)).into(img2);
            Glide.with(this).load(imgs.get(2)).into(img3);
        }

        Glide.with(this).load(truck.getManageImg()).into(manageImg);

        return view;


    }
}
