package com.jiehun.component.base;

import com.trello.rxlifecycle.LifecycleTransformer;

import rx.functions.Action0;

/**
 * dmp_hunbohui
 * describe:
 * author liulj
 * date 2017/12/14
 */

public interface IRequestDialogHandler extends Action0 {

    RequestDialogInterface getRequestDialog();

    RequestDialogInterface initRequestDialog();

    void showRequestDialog();

    void dismissRequestDialog();

    LifecycleTransformer getLifecycleDestroy();

    void cancelOkHttpCall(int requestTag);
}
