package com.jiehun.component.base;

import android.content.Context;

import rx.functions.Action0;

/**
 * Created by zhouyao
 * on 2017/12/14.
 */

public interface HttpDialogInterface extends Action0 {
    void showDialog(String msg, Context context);
    void dismissDialog();
}
