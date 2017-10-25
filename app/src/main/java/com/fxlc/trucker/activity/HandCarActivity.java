package com.fxlc.trucker.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fxlc.trucker.BaseActivity;
import com.fxlc.trucker.R;
import com.fxlc.trucker.bean.MediaStoreData;
import com.fxlc.trucker.bean.Truck;
import com.fxlc.trucker.service.UploadService;
import com.fxlc.trucker.util.DialogUtil;
import com.fxlc.trucker.util.UriUtil;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class HandCarActivity extends BaseActivity implements View.OnClickListener {

    TextView carnoTx,typeTx,lengthTx,heightTx;
    Dialog carNoDialog;

    Truck truck;
    StringBuffer sb = new StringBuffer();
    private static String[] Provinces = {"京", "津", "沪", "渝", "冀", "豫", "云", "辽", "黑", "湘", "皖", "鲁", "新", "苏", "浙", "赣", "鄂", "桂",
            "甘", "晋", "蒙", "陕", "吉", "闽", "贵", "粤", "青", "藏", "川", "宁", "琼", "台"};
    private static String[] Nums = new String[10];
    private static String[] Chars = new String[24];
    private static List<String> characters = new ArrayList<>();
    GridView gridView;
    GridAdapter gridAdapter;
    int statu ;
    ImageView img1, img2, img3, manageImg;
    List<MediaStoreData> driveImgList;
    private String manageImgPath;
    static {
        for (int i = 0; i < 10; i++) {
            Nums[i] = String.valueOf(i);
        }
        int index = 0;
        for (char i = 'A'; i <= 'Z'; i++) {

            if (i != 'I' && i != 'O') {
                Chars[index] = String.valueOf(i);
                index++;
            }
        }
        Iterator<String> iterator1 = Arrays.asList(Nums).iterator();
        Iterator<String> iterator2 = Arrays.asList(Chars).iterator();
        for (int i = 0; i < Nums.length + Chars.length; i++) {
            if (i % 7 < 2) {
                characters.add(iterator1.next());
            } else {
                characters.add(iterator2.next());
            }

        }
        characters.add("✖");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        truck = (Truck) getIntent().getSerializableExtra("truck");
        setContentView(R.layout.activity_handcar);

        img1 = (ImageView) findViewById(R.id.drive1);
        img2 = (ImageView) findViewById(R.id.drive2);
        img3 = (ImageView) findViewById(R.id.drive3);
        manageImg = (ImageView) findViewById(R.id.manageimg);

        typeTx = (TextView) findViewById(R.id.type);
        lengthTx = (TextView) findViewById(R.id.length);
        heightTx = (TextView) findViewById(R.id.height);
        carnoTx = (TextView) findViewById(R.id.carno);

        findViewById(R.id.get_type).setOnClickListener(this);
        findViewById(R.id.get_length).setOnClickListener(this);
        findViewById(R.id.get_height).setOnClickListener(this);
        findViewById(R.id.get_no).setOnClickListener(this);
        findViewById(R.id.get_drive_license).setOnClickListener(this);
        findViewById(R.id.get_manage_license).setOnClickListener(this);
        initCarNoDialog();
        typeD= DialogUtil.createSelDialog(this, typeArr, typeOcl);

        findViewById(R.id.next).setOnClickListener(this);
    }

    Dialog d,typeD;
    String[] typeArr = {"仓栅式半挂车", "栏板式半挂车", "平板式半挂车"};
    String[] longArr = {"5", "6", "7", "8", "9","10","11","12","13","14","15","16","17","18","19","20"};
    String[] heightArr = {"1", "2", "3", "4"};
    String[] pointArr = {"0" ,"1", "2", "3", "4","5","6","7","8","9"};


    @Override
    protected void onResume() {
        super.onResume();
        title("添加挂车");
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_close:

                d.dismiss();
                break;
            case R.id.dialog_ok:
                d.dismiss();

                break;
            case R.id.get_type:


                typeD.show();
                break;
            case R.id.get_length:

                d = DialogUtil.createWheelDialog(this, Arrays.asList(longArr), Arrays.asList(pointArr), new DialogUtil.ValueLisener() {
                    @Override
                    public void onValue(String value) {
                           lengthTx.setText(value);
                          truck.setLength(value);
                    }
                });
                d.show();
                break;
            case R.id.get_height:

                d = DialogUtil.createWheelDialog(this, Arrays.asList(heightArr), Arrays.asList(pointArr), new DialogUtil.ValueLisener() {
                    @Override
                    public void onValue(String value) {
                          heightTx.setText(value);
                          truck.setHeight(value);
                    }
                });
                d.show();
                break;
            case R.id.get_drive_license:

                it.setClass(ctx, PickImgActivity.class);
                it.putExtra("min", 3);
                it.putExtra("max", 3);
                startActivityForResult(it, 102);

                break;
            case R.id.get_manage_license:

                it = new Intent(Intent.ACTION_PICK);
                it.setType("image/*");
                startActivityForResult(it, 103);

                break;
            case R.id.get_no:
                carNoDialog.show();

                break;
            case R.id.next:
                if (notEmpty()){
                    UploadService.startActionAddCar(this,truck);
                    toast("车辆已提交审核");
                    setResult(RESULT_OK);
                    it.setClass(ctx,MycarActivity.class);
                    startActivity(it);
                    finish();
                }
                break;

        }
    }
    private boolean notEmpty(){
        if (TextUtils.isEmpty(truck.getType()) || TextUtils.isEmpty(truck.getLength())|| TextUtils.isEmpty(truck.getHeight())|| TextUtils.isEmpty(truck.getHandcarNo())){
            toast("信息不完整");
            return false;
        } else if (TextUtils.isEmpty(truck.getHanddriveImg1()) || TextUtils.isEmpty(truck.getHandmanageImg())){
            toast("请添加相应的图片");
            return false;
        }
        return  true;
    }
    private View.OnClickListener typeOcl = new View.OnClickListener(){


        @Override
        public void onClick(View view) {
             int tag = (int) view.getTag();
             typeTx.setText(typeArr[tag]);
             truck.setType(typeArr[tag]);
             typeD.dismiss();

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 102) {
                int width = img1.getMeasuredWidth();
                int height = width / 3 * 2;
                driveImgList = data.getParcelableArrayListExtra("imgs");
                Glide.with(this).load(driveImgList.get(0).path).override(width, height).into(img1);
                Glide.with(this).load(driveImgList.get(1).path).override(width, height).into(img2);
                Glide.with(this).load(driveImgList.get(2).path).override(width, height).into(img3);
                truck.setHanddriveImg1(driveImgList.get(0).path);
                truck.setHanddriveImg2(driveImgList.get(1).path);
                truck.setHanddriveImg3(driveImgList.get(2).path);

            }
            if (requestCode == 103) {
                int width = manageImg.getMeasuredWidth();
                int height = width / 3 * 2;
                manageImgPath = UriUtil.getRealFilePath(this, data.getData());
                Glide.with(this).load(manageImgPath).override(width, height).into(manageImg);
                truck.setHandmanageImg(manageImgPath);
            }
        }
    }

    private void initCarNoDialog() {
        carNoDialog = new Dialog(this, R.style.dialog_alert);
        gridView = new GridView(this);
        gridView.setNumColumns(8);
        gridView.setBackgroundColor(Color.GRAY);
        gridView.setPadding(1, 1, 1, 1);
        gridView.setVerticalSpacing(1);
        gridView.setHorizontalSpacing(1);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(-1, -2);
        carNoDialog.setContentView(gridView, params);
        gridAdapter = new GridAdapter();
        gridAdapter.setValues(Arrays.asList(Provinces));
        gridView.setAdapter(gridAdapter);
        Window win = carNoDialog.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == adapterView.getCount() - 1) {
                    if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
                    if (sb.length() == 0) statu = 0;
                } else {
                    if (sb.length() < 7) sb.append(gridAdapter.getItem(i));
                }
                if (sb.length() == 0) {
                    gridView.setNumColumns(8);
                    gridAdapter.setValues(Arrays.asList(Provinces));
                    gridAdapter.notifyDataSetChanged();
                } else if (statu == 0) {
                    statu = 1;
                    gridView.setNumColumns(7);
                    gridAdapter.setValues(characters);
                    gridAdapter.notifyDataSetChanged();
                }else if (sb.length() == 7){
                    carNoDialog.dismiss();
                }
                carnoTx.setText(sb.toString());
                truck.setHandcarNo(sb.toString() + "挂");
            }
        });
    }
    class GridAdapter extends BaseAdapter {
        List<String> values;

        @Override
        public int getCount() {
            return values.size();
        }

        @Override
        public String getItem(int i) {
            return values.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        public void setValues(List<String> values) {
            this.values = values;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if (view == null)
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.simple_list_item, viewGroup, false);
            TextView txt = (TextView) view;
            txt.setBackgroundColor(getResources().getColor(R.color.white));
            txt.setText(values.get(i));
            return view;
        }
    }
}
