package com.nexwise.pointscan.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class MyViewPager extends ViewPager {

    private boolean willIntercept = true;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (willIntercept) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    public void setTouchIntercept(boolean value) {
        willIntercept = value;
    }
}