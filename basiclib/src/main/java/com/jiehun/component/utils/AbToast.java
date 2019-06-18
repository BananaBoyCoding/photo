package com.jiehun.component.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jiehun.component.basiclib.R;
import com.jiehun.component.config.BaseLibConfig;


/**
 * Created by zhouyao
 * on 16-9-23.
 */
public class AbToast {

    public static void show(@StringRes int resId) {
        showToast(BaseLibConfig.getContext().getResources().getText(resId), Toast.LENGTH_SHORT);
    }

    public static void show(@StringRes int resId, int showTime) {
        showToast(BaseLibConfig.getContext().getResources().getText(resId), showTime);
    }

    public static void show(String str) {
        showToast(str, Toast.LENGTH_SHORT);
    }

    public static void show(String str, int showTime) {
        showToast(String.valueOf(str), showTime);
    }


    public static void showLong(String str) {
        showToast(String.valueOf(str), Toast.LENGTH_LONG);
    }


    public static void showLong(int resId) {
        showToast(String.valueOf(BaseLibConfig.getContext().getResources().getText(resId)), Toast.LENGTH_LONG);
    }


    private static void showToast(CharSequence toastStr, int showTime) {
        Toast toast = Toast.makeText(BaseLibConfig.getContext(), "", showTime);
        LayoutInflater li = (LayoutInflater) BaseLibConfig.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.common_toastlayout, null);
        TextView context = view.findViewById(R.id.tv_tip);
        context.setText(toastStr);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(view);
        toast.show();
    }


}
