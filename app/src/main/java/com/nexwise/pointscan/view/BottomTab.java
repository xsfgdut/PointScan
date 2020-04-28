package com.nexwise.pointscan.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.nexwise.pointscan.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 底部tab集合
 * Created by xsf on 2019/7/18.
 */
public class BottomTab extends LinearLayout implements View.OnClickListener {
    public static final int TAB_ONE = 0;
    public static final int TAB_TWO = 1;
    private static final String TAG = "BottomTab";
    private List<Tab> tabs = new ArrayList<>();
    private BoottomTabLsn mBoottomTabLsn;
    private int[] drawableSrcs;

    public BottomTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.bootom_tab, (ViewGroup) getRootView());

    }


    /**
     * 获取图片和字符串资源
     *
     * @param drawableSrcs 图片资源数组
     */
    @SuppressWarnings("deprecation")
    public void setParameter(int[] drawableSrcs) {
        Tab tab1 = findViewById(R.id.tab_one);
        tab1.setOnClickListener(this);
        Tab tab2 = findViewById(R.id.tab_two);
        tab2.setOnClickListener(this);
        tabs.add(tab1);
        tabs.add(tab2);

        this.drawableSrcs = drawableSrcs;
        for (int i = 0; i < tabs.size(); i++) {
            Drawable drawable = getResources().getDrawable(drawableSrcs[i]);
            tabs.get(i).setParameter(drawable);
        }
    }


    /**
     * 改变当前的显示内容
     *
     * @param position {@link #TAB_ONE}{@link #TAB_TWO}
     */
    @SuppressWarnings("deprecation")
    public void changeShowView(int position) {
        Drawable drawable;
        int halfSize = tabs.size();
        for (int i = 0; i < tabs.size(); i++) {
            Tab tab = tabs.get(i);
            drawable = getResources().getDrawable(position == i ? drawableSrcs[i + halfSize] : drawableSrcs[i]);
            tab.setDrawable(drawable);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_one:
                changeShowView(TAB_ONE);
                break;
            case R.id.tab_two:
                changeShowView(TAB_TWO);
                break;

        }
        if (mBoottomTabLsn == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.tab_one:
                mBoottomTabLsn.onTabClkLsn(TAB_ONE);
                break;
            case R.id.tab_two:
                mBoottomTabLsn.onTabClkLsn(TAB_TWO);
                break;

        }
    }

    /**
     * 设置底部点击事件
     */
    public void setBoottomTabLsn(BoottomTabLsn mBoottomTabLsn) {
        this.mBoottomTabLsn = mBoottomTabLsn;
    }

    /**
     * 底部tab点击接口
     */
    public interface BoottomTabLsn {
        /**
         * @param position 被点击的tab位置
         */
        void onTabClkLsn(int position);
    }
}
