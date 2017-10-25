package com.fxlc.trucker.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.fxlc.trucker.R;
import com.fxlc.trucker.util.DisplayUtil;

public class BannerInfoActivity extends AppCompatActivity {
    ImageView img ;

    int  drawables [] = {R.drawable.banner11,R.drawable.banner22,R.drawable.banner33};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_info);

        int p = getIntent().getIntExtra("p",0);
        int id = drawables[p];
        DisplayUtil.translucentStatubar(this);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(),id,options);

        int w = options.outWidth;
        int h = options.outHeight;


        int sw = DisplayUtil.getScreenWidth(this);

        int rh =  (int)((h * 1.0f)/w * sw);

        Log.d("banner","w:" + w  + " h:" + h +  " sw:" + sw + " rh" + rh);


        img  = new ImageView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1,rh);
        img.setLayoutParams(params);
        img.setImageResource(id);
        ViewGroup group = (ViewGroup) findViewById(R.id.container);
        group.addView(img);
    }
}
