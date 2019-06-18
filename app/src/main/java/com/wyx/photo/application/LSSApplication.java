package com.wyx.photo.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.jiehun.component.config.BaseLibConfig;
import com.jiehun.component.crash.CrashHandler;
import com.umeng.commonsdk.UMConfigure;


/**
 * @author tunsh
 */
public class LSSApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BaseLibConfig.initRxLib(this, true, "debug", "1.0", true);
    }


}
