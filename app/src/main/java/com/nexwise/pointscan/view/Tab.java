package com.nexwise.pointscan.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nexwise.pointscan.R;


/**
 * 底部单个tab
 */
public class Tab extends LinearLayout {
    private ImageView mImageView;

    public Tab(Context context, AttributeSet attrs) {
        super(context, attrs);
        View rootView = LayoutInflater.from(context).inflate(R.layout.tab, (ViewGroup) getRootView());
        mImageView = rootView.findViewById(R.id.tab_img);
    }

    /**
     * 设置图片和显示字符参数
     *
     * @param tabDrawable 图片资源
     */
    public void setParameter(Drawable tabDrawable) {
        if (mImageView != null) {
            mImageView.setImageDrawable(tabDrawable);
        }

    }

    public void setDrawable(Drawable tabDrawable) {
        if (mImageView != null) {
            mImageView.setImageDrawable(tabDrawable);
        }
    }


}