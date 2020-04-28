package com.nexwise.pointscan;

import android.os.Bundle;

import com.nexwise.pointscan.base.BaseAct;

public class MainActivity extends BaseAct {


    @Override
    protected void findView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
