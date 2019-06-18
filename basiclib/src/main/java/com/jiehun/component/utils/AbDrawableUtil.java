package com.jiehun.component.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

/** 设置圆角背景
 * Created by zhouyao
 * on 2016/11/4.
 */

        /*
        GradientDrawable drawable = new AbDrawableUtil(this)
        .setShape(GradientDrawable.RECTANGLE)//设置形状
        .setCornerRadii(20)设置圆角，默认dp单位
        .setBackgroundColor(R.color.colorPrimary)设置背景色
        .setStroke(2, R.color.colorAccent)设置边框颜色、宽度
        .setAlpha(100)//设置透明度
        .build();
        rltest.setBackgroundDrawable(drawable);
         */


public class AbDrawableUtil {
    private GradientDrawable drawable;
    private Context          context;

    /**
     * @param context 当前页面
     */
    public AbDrawableUtil(Context context) {
        this.drawable = new GradientDrawable();
        this.context = context;
    }

    /**
     * @param shape 形状
     * @return
     */
    public AbDrawableUtil setShape(int shape) {
        drawable.setShape(shape);
        return this;
    }

    /**
     * @param color 背景色 R.color
     * @return
     */
    public AbDrawableUtil setBackgroundColor(int color) {
        drawable.setColor(context.getResources().getColor(color));
        return this;
    }

    public AbDrawableUtil setBackgroundColorInt(int color) {
        drawable.setColor(color);
        return this;
    }

    /**
     * @param radii 默认dp单位
     * @return
     */
    public AbDrawableUtil setCornerRadii(float[] radii) {
        for (int i = 0; i < radii.length; i++) {
            radii[i] = AbDisplayUtil.dip2px(radii[i]);
        }
        drawable.setCornerRadii(radii);
        return this;
    }

    /**
     * @param radius 默认dp单位
     * @return
     */
    public AbDrawableUtil setCornerRadii(float radius) {
        drawable.setCornerRadius(AbDisplayUtil.dip2px(radius));
        return this;
    }

    /**
     * @param width 边框宽度
     * @param color 边框颜色 R.color
     * @return
     */
    public AbDrawableUtil setStroke(int width, int color) {
        drawable.setStroke(width, context.getResources().getColor(color));
        return this;
    }

    public AbDrawableUtil setStrokeInt(int width, int color) {
        drawable.setStroke(width, color);
        return this;
    }


    /**
     * @param alpha 透明度
     * @return
     */
    public AbDrawableUtil setAlpha(int alpha) {
        drawable.setAlpha(alpha);
        return this;
    }

    public GradientDrawable build() {
        return drawable;
    }

    /**
     * 设置drawable宽高
     *
     * @param drawable
     * @param width
     * @param height
     */
    public static void setDrawableWidthHeight(Drawable drawable, int width, int height) {
        try {
            if (drawable != null) {
                drawable.setBounds(0, 0,
                        width >= 0 ? width : drawable.getIntrinsicWidth(),
                        height >= 0 ? height : drawable.getIntrinsicHeight());
            }
        } catch (Exception e) {
        }
    }
}
