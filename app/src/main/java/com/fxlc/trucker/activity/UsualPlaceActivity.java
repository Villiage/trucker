package com.fxlc.trucker.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.fxlc.trucker.BaseActivity;
import com.fxlc.trucker.MyApplication;
import com.fxlc.trucker.R;
import com.fxlc.trucker.api.SettingService;
import com.fxlc.trucker.bean.City;
import com.fxlc.trucker.bean.UsualPlace;
import com.fxlc.trucker.bean.UsualPlaceData;
import com.fxlc.trucker.db.CityDao;
import com.fxlc.trucker.net.HttpCallback;
import com.fxlc.trucker.net.HttpResult;
import com.fxlc.trucker.net.SimpleCallback;
import com.fxlc.trucker.util.DisplayUtil;
import com.fxlc.trucker.view.FlowLayout;
import com.fxlc.trucker.view.WheelAdapter;
import com.fxlc.trucker.view.WheelView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import retrofit2.Call;

public class UsualPlaceActivity extends BaseActivity implements View.OnClickListener {

    private List<UsualPlaceData.UsualPlace> usualPlaceList;
    private FlowLayout flowLayout;
    private CityDao dao;
    private List<City> fullList;
    private List<City> proList;
    private List<City> cityList;
    private List<City> disList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_place);
        flowLayout = (FlowLayout) findViewById(R.id.flowlayout);

        findViewById(R.id.rightopt).setOnClickListener(this);

        findViewById(R.id.add_place).setOnClickListener(this);
        dao = new CityDao(ctx);
        fullList = dao.getAllCity();
        proList = fiterList(0 + "");
        cityList = fiterList(proList.get(0).getId());
        disList = fiterList(cityList.get(0).getId());


        createDialog();
        listUsualPlace();

    }

    @Override
    protected void onResume() {
        super.onResume();
        title("常跑地点");
    }

    private void listUsualPlace() {
        SettingService service = MyApplication.getRetrofit().create(SettingService.class);

        Call<HttpResult<UsualPlaceData>> call = service.listUsuallyPlace();

        call.enqueue(new HttpCallback<UsualPlaceData>() {
            @Override
            public void onSuccess(UsualPlaceData usualPlaceData) {
                usualPlaceList = usualPlaceData.getUsuallyPlace();
                for (UsualPlaceData.UsualPlace place : usualPlaceList) {
                    addData(place);
                }
            }
        });
    }


    private void addData(UsualPlaceData.UsualPlace place) {
        int margin = DisplayUtil.dp2px(ctx, 10);
        FrameLayout.LayoutParams fParam = new FrameLayout.LayoutParams(-2, -2);
        FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(-2, -2);
        param.leftMargin = param.rightMargin = param.topMargin = param.bottomMargin = margin;
        FrameLayout.LayoutParams iParam = new FrameLayout.LayoutParams(margin * 2, margin * 2);
        iParam.gravity = Gravity.RIGHT;

        FrameLayout frame = new FrameLayout(ctx);
        TextView text = new TextView(ctx);
        text.setText(place.getCountyName());
        text.setTextColor(getResources().getColor(R.color.text_mark));
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        text.setBackgroundResource(R.drawable.place_bg);
        frame.addView(text, param);

        ImageView img = new ImageView(ctx);
        img.setImageResource(R.drawable.deletex);
        img.setId(R.id.del);
        img.setVisibility(View.GONE);
        img.setOnClickListener(delLisener);
        frame.addView(img, iParam);
        img.setTag(place);

        flowLayout.addView(frame, fParam);


    }
    private void saveUsualPlace(String pid, String cid, String did) {
        proDialog.show();
        SettingService service = MyApplication.getRetrofit().create(SettingService.class);

        Call<HttpResult<UsualPlace>> call = service.saveUsualPlace(pid, cid, did);

        call.enqueue(new HttpCallback<UsualPlace>() {
            @Override
            public void onSuccess(UsualPlace usualPlace) {
                proDialog.dismiss();
                addData(new UsualPlaceData.UsualPlace(usualPlace.getUsuallyPlaceId(),usualPlace.getUsuallyPlaceName()));
                toast("添加成功");
            }
        });


    }
    private void del(String id){

        SettingService service = MyApplication.getRetrofit().create(SettingService.class);

         Call<HttpResult> call = service.delUsuallyPlace(id);

         call.enqueue(new SimpleCallback() {
             @Override
             public void onSuccess(HttpResult result) {
                 toast("已删除");
             }
         });




    }

    View.OnClickListener delLisener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            ((View) view.getParent()).setVisibility(View.GONE);

            flowLayout.removeView((View)view.getParent());
            UsualPlaceData.UsualPlace place = (UsualPlaceData.UsualPlace) view.getTag();

            del(place.getId());

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rightopt:
                editable();

                break;

            case R.id.add_place:

                cityDialog.show();
                break;
        }

    }

    private void editable() {

        int cout = flowLayout.getChildCount();

        for (int i = 0; i < cout; i++) {
            View del = flowLayout.getChildAt(i).findViewById(R.id.del);
            del.setVisibility(View.VISIBLE);
        }
    }

    Dialog cityDialog;
    WheelView wheelViewP, wheelViewC, wheelViewD;
    WheelAdapter proAdapter, cityAdapter, disAdapter;
    private View.OnClickListener onChooseLisener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            cityDialog.dismiss();
            String pid = proAdapter.getItem(wheelViewP.getSelectIndex() + 2).getId();
            String cid = cityAdapter.getItem(wheelViewC.getSelectIndex() + 2).getId();
            String did = disAdapter.getItem(wheelViewD.getSelectIndex() + 2).getId();

            saveUsualPlace(pid, cid, did);

        }
    };



    public void createDialog() {
        cityDialog = new Dialog(ctx, R.style.dialog_alert);
        cityDialog.setContentView(R.layout.dialog_city);

        wheelViewP = cityDialog.findViewById(R.id.p);
        wheelViewC = cityDialog.findViewById(R.id.c);
        wheelViewD = cityDialog.findViewById(R.id.d);
        cityDialog.findViewById(R.id.choose).setOnClickListener(onChooseLisener);

        wheelViewP.setOscl(new WheelView.OnSelectChangeLisenter() {
            @Override
            public void onSelect(int p) {

                cityList.clear();
                cityList.addAll(fiterList(proList.get(p).getId()));
                cityAdapter.notifyDataSetChanged();

                disList.clear();
                disList.addAll(fiterList(cityList.get(0).getId()));
                disAdapter.notifyDataSetChanged();
            }
        });
        wheelViewC.setOscl(new WheelView.OnSelectChangeLisenter() {
            @Override
            public void onSelect(int p) {

                disList.clear();
                disList.addAll(fiterList(cityList.get(p).getId()));
                disAdapter.notifyDataSetChanged();

            }
        });


        Window win = cityDialog.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.gravity = Gravity.BOTTOM;

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        wheelViewP.setAdapter(proAdapter = new WheelAdapter(proList, 5));
        wheelViewC.setAdapter(cityAdapter = new WheelAdapter(cityList, 5));
        wheelViewD.setAdapter(disAdapter = new WheelAdapter(disList, 5));


    }


    private List<City> fiterList(String pid) {
        List<City> list = new ArrayList<>();
        for (City city : fullList) {
            if (city.getParentId().equals(pid)) {
                list.add(city);
            }
        }

        return list;
    }
}
