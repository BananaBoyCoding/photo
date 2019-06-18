package com.jiehun.component.rxpermission;

import android.app.Activity;

/**
 * Created by zhouyao on 16-10-13.
 */
public interface RxCallBack {
    void onOk();

    void onCancel();

    void onNeverAsk(final Activity aty, String permission);
}
