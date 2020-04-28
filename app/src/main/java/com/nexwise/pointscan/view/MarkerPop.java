package com.nexwise.pointscan.view;

/**
 * 一般的输入框
 * Created by shifan_xiao on 2016/11/2.
 */

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.nexwise.pointscan.R;
import com.nexwise.pointscan.bean.Point;


public class MarkerPop extends Dialog {

    private TextView titleTextView;
    private TextView lngTextView;
    private TextView latTextView;
    private TextView statusTextView;
    private TextView btn_commit;
    private TextView btn_cancel;
    private int state = 0;
    private Point point;

    public MarkerPop(Context context, Point point) {
        super(context, R.style.noTitleDialog);

        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.marker_pop, null);
        titleTextView = view.findViewById(R.id.point_title);
        lngTextView = view.findViewById(R.id.lng_value);
        latTextView = view.findViewById(R.id.lat_value);
        statusTextView = view.findViewById(R.id.status_value);

        btn_commit = view.findViewById(R.id.btn_commit);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        this.point = point;
        super.setContentView(view);

    }

    public void setvalue() {
        titleTextView.setText(point.getName());
        lngTextView.setText(String.valueOf(point.getLng()));
        latTextView.setText(String.valueOf(point.getLat()));
        switch (point.getState()) {
            case -1:
                statusTextView.setText("已拆除");
                break;
            case 1:
                statusTextView.setText("未勘");
                break;
            case 2:
                statusTextView.setText("已勘");
                break;
            case 3:
                statusTextView.setText("已安装");
                break;
        }
    }

    public void setOnClickCommitListener(View.OnClickListener listener) {
        btn_commit.setOnClickListener(listener);
    }

    public void setOnClickCancelListener(View.OnClickListener listener) {
        btn_cancel.setOnClickListener(listener);
    }

    public void setMarkPopValue(String title,double lng,double lat,int state) {
        titleTextView.setText(title);
        lngTextView.setText(String.valueOf(lng));
        latTextView.setText(String.valueOf(lat));
        switch (state) {
            case -1:
                statusTextView.setText("已拆除");
                break;
            case 1:
                statusTextView.setText("未勘");
                break;
            case 2:
                statusTextView.setText("已勘");
                break;
            case 3:
                statusTextView.setText("已安装");
                break;
        }
    }

}
