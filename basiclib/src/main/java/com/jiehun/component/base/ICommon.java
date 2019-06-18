package com.jiehun.component.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;

/**
 * dmp_hunbohui
 * describe:
 * author liulj
 * date 2017/12/14
 */

public interface ICommon {

    @LayoutRes
    int layoutId();

    View layoutView();

    void initViews(Bundle savedInstanceState);

    void initData();
}
