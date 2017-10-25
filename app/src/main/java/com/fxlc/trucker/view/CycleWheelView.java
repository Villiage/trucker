package com.fxlc.trucker.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by cyd on 2017/3/24.
 */

public class CycleWheelView extends ListView {
    public static final String TAG = "CycleWheelView";
    public static final int showCount = 5;
    private int freeScrollTime = 200;//ms
    private int itemHeight;
    private int selectIndex = -1;
    private List<String> mData;
    private WheelViewAdapter mAdapter;
    private boolean cycleEnable;

    public CycleWheelView(Context context) {
        super(context);
        init();
    }

    public CycleWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CycleWheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    public void init() {

        setVerticalScrollBarEnabled(false);
        setFastScrollEnabled(false);
        setDividerHeight(0);
        setOnScrollListener(osl);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() > 0) {
            View view = getChildAt(0);
            itemHeight = view.getMeasuredHeight();
            setMeasuredDimension(widthMeasureSpec, itemHeight * showCount);

        }
    }

    public int getItemHeight() {
        return itemHeight;
    }

    @Override
    public void fling(int velocityY) {

        super.fling(velocityY/10);

    }


    public void setData(List<String> data) {
        this.mData = data;
        mAdapter = new WheelViewAdapter(data, showCount, cycleEnable);
        setAdapter(mAdapter);

    }

    public void setmAdapter(WheelViewAdapter mAdapter) {
        this.mAdapter = mAdapter;

    }

    public void setCycleEnable(boolean cycleEnable) {
        this.cycleEnable = cycleEnable;
        if(cycleEnable){
            setSelection(mData.size() * (Integer.MAX_VALUE/2 / mData.size())+ selectIndex);
        }
    }

    private OnScrollListener osl = new OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView absListView, int i) {
            if (i == SCROLL_STATE_IDLE) {
                adjust();
                Log.d("scroll","onScrollStateChanged");
            }

        }

        @Override
        public void onScroll(AbsListView absListView, int firstVisiableItem, int visiableItemCount, int totalItemCount) {
            refresh(firstVisiableItem,visiableItemCount);

        }



    };

    public void adjust() {

            View itemView = getChildAt(0);
            if (itemView != null) {
                float deltaY = itemView.getY();
                if (deltaY == 0) {
                    return;
                }
                if (Math.abs(deltaY) < itemHeight / 2) {
                    smoothScrollBy((int)deltaY, 0);
                } else {
                    smoothScrollBy((int)(itemHeight + deltaY), 0);
                }
            }


    }


    public int getSelectIndex() {
        return selectIndex;
    }

    public void refresh(int firstVisiableItem, int visiableItemCount) {

        if (getChildAt(0) == null) {
            return;
        }
        int offset = showCount/2;
        int position = 0;
        if (Math.abs(getChildAt(0).getY()) <= itemHeight / 2) {
            position = firstVisiableItem + offset;
        } else {
            position = firstVisiableItem + offset + 1;
        }
        if(position == selectIndex + offset){
            return;
        }
        selectIndex = (position - offset) % mData.size();
        for(int i = 0;i<  visiableItemCount;i++){
            TextView txt = (TextView) getChildAt(i);
              if( firstVisiableItem + i == position){
                  txt.setTextColor(Color.DKGRAY);
              }else txt.setTextColor(Color.LTGRAY);
        }

    }

   public interface OnSelectChangeLisenter{

        void onSelect(int p);

   }
   private OnSelectChangeLisenter oscl;

    public void setOscl(OnSelectChangeLisenter oscl) {
        this.oscl = oscl;
    }
}
