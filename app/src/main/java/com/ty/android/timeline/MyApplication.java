package com.ty.android.timeline;

import android.app.Application;

/**
 * Created by Android on 2017/3/31.
 */

public class MyApplication extends Application {
    private static MyApplication instance;
    public static MyApplication getInstance(){
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
