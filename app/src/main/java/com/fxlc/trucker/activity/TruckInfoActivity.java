package com.fxlc.trucker.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.fxlc.trucker.BaseActivity;
import com.fxlc.trucker.R;
import com.fxlc.trucker.bean.Truck;
import com.fxlc.trucker.frag.HandTruckFrag;
import com.fxlc.trucker.frag.MainTruckFrag;


public class TruckInfoActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager mPager;
    private TabLayout tabLayout;
    private FragmentPagerAdapter pagerAdapter;
    private MainTruckFrag mainTruckFrag;
    private HandTruckFrag handTruckFrag;
    private Truck truck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        truck = (Truck) getIntent().getSerializableExtra("truck");
        setContentView(R.layout.activity_truck_info);
        tabLayout = (TabLayout) findViewById(R.id.tabcontainer);

        mPager = (ViewPager) findViewById(R.id.pager);
        tabLayout.setupWithViewPager(mPager);
        initData();
        pagerAdapter = new TruckFragAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);

//        if (truck.getCartype() == 1)
//            mPager.setCurrentItem(1);

    }

    @Override
    protected void onResume() {
        super.onResume();
        title("车辆详情");
    }

    private void initData() {

        mainTruckFrag =  MainTruckFrag.newInstance(truck);
        handTruckFrag =  HandTruckFrag.newInstance(truck);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {


        }
    }

    class TruckFragAdapter extends FragmentPagerAdapter {
        String[] titles = new String[]{"主车", "挂车"};
        public TruckFragAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
             if (truck.getCartype() == 1)
                return handTruckFrag;
            else return mainTruckFrag ;
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

}
