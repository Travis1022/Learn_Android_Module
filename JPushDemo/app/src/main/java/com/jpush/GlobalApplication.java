package com.jpush;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Travis1022 on 2017/2/10.
 */
public class GlobalApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
