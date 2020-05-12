package com.nexwise.pointscan.view;

/**
 * 一般的输入框
 * Created by shifan_xiao on 2016/11/2.
 */

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.nexwise.pointscan.R;
import com.nexwise.pointscan.bean.Point;


public class LocationDialog extends Dialog {

    public LocationDialog(Context context, Point point) {
        super(context, R.style.noTitleDialog);

        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.location_pop, null);
        super.setContentView(view);

    }


}
