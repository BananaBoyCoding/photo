package com.jiehun.component.base;

import android.app.Dialog;

/**
 * dmp_hunbohui
 * describe:
 * author liulj
 * date 2017/12/14
 */

public interface RequestDialogInterface {

    Dialog getDialog();
    void showDialog();
    void dismissDialog();
//    void customShow(String str);
    int getTag();
    void setTag(int tag);
}
