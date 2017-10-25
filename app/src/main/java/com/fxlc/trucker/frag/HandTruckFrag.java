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

public class HandTruckFrag extends Fragment {
    ImageView img1, img2, img3, manageImg;
    TextView carnoTx, typeTx, lengthTx, heightTx;
    Truck truck;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static HandTruckFrag newInstance(Truck truck) {

        Bundle args = new Bundle();
        args.putSerializable("truck",truck);
        HandTruckFrag fragment = new HandTruckFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Truck truck = (Truck) getArguments().getSerializable("truck");

        View view = inflater.inflate(R.layout.frag_handtruck, null);

        img1 = (ImageView) view.findViewById(R.id.drive1);
        img2 = (ImageView) view.findViewById(R.id.drive2);
        img3 = (ImageView) view.findViewById(R.id.drive3);
        manageImg = (ImageView) view.findViewById(R.id.manageimg);

        typeTx = (TextView) view.findViewById(R.id.type);
        lengthTx = (TextView) view.findViewById(R.id.length);
        heightTx = (TextView) view.findViewById(R.id.height);
        carnoTx = (TextView) view.findViewById(R.id.carno);
        typeTx.setText(truck.getType());
        lengthTx.setText(truck.getLength());
        heightTx.setText(truck.getHeight());
        carnoTx.setText(truck.getHandcarNo());
        List<String> imgs = truck.getHanddriveImg();
         if (imgs != null){
             Glide.with(this).load(imgs.get(0)).into(img1);
             Glide.with(this).load(imgs.get(1)).into(img2);
             Glide.with(this).load(imgs.get(2)).into(img3);
         }
        Glide.with(this).load(truck.getHandmanageImg()).into(manageImg);

        return view;
    }
}
