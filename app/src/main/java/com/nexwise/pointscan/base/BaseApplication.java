package com.nexwise.pointscan.base;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

public class BaseApplication extends Application {

    private static BaseApplication mApplication;

    public static BaseApplication getIntance() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;

    }


    //这是一个重新方法

    @Override

    protected void attachBaseContext(Context base) {

        super.attachBaseContext(base);

        MultiDex.install(this);

    }

}
