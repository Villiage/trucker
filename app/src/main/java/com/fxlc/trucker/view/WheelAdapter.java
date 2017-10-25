package com.fxlc.trucker.view;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fxlc.trucker.bean.City;

import java.util.List;

/**
 * Created by cyd on 2017/3/24.
 */


public class WheelAdapter extends BaseAdapter {
    private List<City> mData;
    private int showCount;


    public WheelAdapter(List<City> data, int showCount) {
        this.mData = data;
        this.showCount = showCount;

    }

    @Override
    public int getCount() {

        return mData.size() + showCount - 1;
    }

    @Override
    public City getItem(int i) {
        if (i < showCount / 2 || (i >= mData.size() + showCount / 2))
            return null;
        else {
            return mData.get((i - showCount / 2) % mData.size());
        }

    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView text = null;
        if (view == null) {
            text = new TextView(viewGroup.getContext());
        } else {
            text = (TextView) view;
        }
        text.setGravity(Gravity.CENTER);
        text.setSingleLine(true);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        text.setPadding(10, 10, 10, 10);
        if (getItem(i) == null)
            text.setText("");
        else text.setText(getItem(i).getName());
        return text;
    }
}

