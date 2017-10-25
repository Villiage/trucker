package com.fxlc.trucker.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fxlc.trucker.R;
import com.fxlc.trucker.bean.MediaStoreData;

import java.util.List;

public class PhotoActivity extends AppCompatActivity {
    ViewPager pager;
    List<MediaStoreData> datas;
    Context context;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        context = this;
        setContentView(R.layout.activity_photo);
        datas =  getIntent().getParcelableArrayListExtra("localImg");
        position = getIntent().getIntExtra("position",0);
        Log.d("cyd","" + position);
        pager = (ViewPager) findViewById(R.id.pager);

        pager.setAdapter(new MPagerDapter());
        pager.setCurrentItem(position);
    }

    class MPagerDapter extends android.support.v4.view.PagerAdapter {


        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return datas.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            ImageView photoView = new ImageView(container.getContext());
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1,-1);

            Glide.with(context).load(datas.get(position).path).fitCenter().into(photoView);

            container.addView(photoView,layoutParams);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            // TODO Auto-generated method stub
            container.removeView((View) object);
        }

    }
}
