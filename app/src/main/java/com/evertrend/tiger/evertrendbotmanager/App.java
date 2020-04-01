package com.evertrend.tiger.evertrendbotmanager;

import android.app.Application;

import com.evertrend.tiger.common.EvertrendCommon;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EvertrendCommon.initOption(this);
    }
}
