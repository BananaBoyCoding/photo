package com.wyx.photo.lssbase;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jiehun.component.base.BaseTitleActivity;
import com.umeng.analytics.MobclickAgent;

//import com.ke.gson.sdk.ReaderTools;
/*
* 需要带有标题栏的页面继承这个 需要自定义标题栏就继承LSSBaseActivity
* */
public abstract class LSSBaseTitleActivity extends BaseTitleActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ReaderTools.setListener(new ReaderTools.JsonSyntaxErrorListener() {
//            @Override
//            public void onJsonSyntaxError(String s, String s1) {
//                //s  错误类型
//                //s1 具体错误
//                Log.e("yy","s-------------------------------->"+s+"-----------s");
//                Log.e("yy","s1-------------------------------->"+s1+"-----------s1");
//            }
//        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
