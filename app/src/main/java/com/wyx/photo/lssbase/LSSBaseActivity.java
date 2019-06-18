package com.wyx.photo.lssbase;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jiehun.component.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

/*
 * 需要自定义标题栏就继承LSSBaseActivity，需要带有标题栏的页面继承LSSBaseTitleActivity
 * 友盟统计相关必须继承LSSBaseActivity,不能直接继承BaseActivity
 * */

public abstract class LSSBaseActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
