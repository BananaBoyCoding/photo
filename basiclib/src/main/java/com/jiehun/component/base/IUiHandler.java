package com.jiehun.component.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.widget.TextView;

import java.util.Collection;

/**
 * dmp_hunbohui
 * describe:
 * author liulj
 * date 2017/12/14
 */

public interface IUiHandler {

    CharSequence getTextStr(TextView textView);

    /**
     * 设置TextView的文本，判断了非空
     *
     * @param textView
     * @param destination
     */
    void setText(TextView textView, CharSequence destination);

    /**
     * 设置textview的文本，判断了非空
     *
     * @param destination
     * @param defaultStr
     */
    void setText(TextView textView, CharSequence destination, CharSequence defaultStr);

    /**
     * 弹共通的toast
     *
     * @param content 提示的内容
     */
    void showToast(String content);

    /**
     * 弹共通的toast
     *
     * @param resId
     */
    void showToast(@StringRes int resId);

    void showLongToast(String content);

    /**
     * 弹共通的toast
     *
     * @param resId
     */
    void showLongToast(@StringRes int resId);


    boolean isEmpty(CharSequence text);

    boolean isEmpty(TextView textView);

    boolean isEmpty(Collection list);


    void setTextColor(TextView textView, @ColorRes int id);

    @ColorInt
    int getCompatColor(Context context, @ColorRes int id);

    @ColorInt
    int getCompatColor( @ColorRes int id);

    Drawable getCompatDrawable(Context context, @DrawableRes int id);

    Drawable getCompatDrawable( @DrawableRes int id);
}
