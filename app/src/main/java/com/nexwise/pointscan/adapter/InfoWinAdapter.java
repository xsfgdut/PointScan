package com.nexwise.pointscan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.nexwise.pointscan.R;
import com.nexwise.pointscan.base.BaseApplication;


/**
 * Created by Teprinciple on 2016/8/23.
 * 地图上自定义的infowindow的适配器
 */
public class InfoWinAdapter implements AMap.InfoWindowAdapter, View.OnClickListener {
    OnDetailClickListener onDetailClickListener;
    private Context mContext = BaseApplication.getIntance().getBaseContext();
    private LatLng latLng;
    private LinearLayout call;
    private LinearLayout navigation;
    private TextView nameTV;
    private String agentName;
    private TextView addrTV;
    private TextView addrTV2;
    private String snippet;
    private int position;

    @Override
    public View getInfoWindow(Marker marker) {
        initData(marker);
        View view = initView();
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    private void initData(Marker marker) {
        latLng = marker.getPosition();
        snippet = marker.getSnippet();
        agentName = marker.getTitle();
    }

    @NonNull
    private View initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_infowindow, null);
        navigation = (LinearLayout) view.findViewById(R.id.navigation_LL);
        call = (LinearLayout) view.findViewById(R.id.call_LL);
        nameTV = (TextView) view.findViewById(R.id.name);
        addrTV = (TextView) view.findViewById(R.id.addr1);
        addrTV2 = (TextView) view.findViewById(R.id.addr2);

        nameTV.setText(agentName);
        addrTV.setText(String.format(mContext.getString(R.string.agent_addr), snippet));

        navigation.setOnClickListener(this);
        call.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.navigation_LL:  //点击导航
//                NavigationUtils.Navigation(latLng);
                break;

            case R.id.call_LL:  //点击打电话
//                PhoneCallUtils.call("028-"); //TODO 处理电话号码
                onDetailClickListener.onDetailClick(position);
                break;
        }
    }

    public void setOnDetailClickListener(OnDetailClickListener onDetailClickListener) {
        this.onDetailClickListener = onDetailClickListener;
    }

    public static interface OnDetailClickListener {
        public void onDetailClick(int position);
    }

}
