package com.jiehun.component.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.jiehun.component.config.BaseLibConfig;

import java.lang.reflect.Method;

public class AbDisplayUtil {

    public static Bitmap takeScreenshot(View view) {
        Bitmap screenshot = null;
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(false);
        Bitmap cache = view.getDrawingCache();
        // 获取状态栏高度
        Rect frame = new Rect();
        view.getWindowVisibleDisplayFrame(frame);
        int statusHeight = frame.top;// Activity 的顶端， 就是状态栏的高度。
        screenshot = Bitmap.createBitmap(cache, 0, statusHeight, cache.getWidth(), cache.getHeight() - statusHeight);
        view.destroyDrawingCache();
        return screenshot;
    }

    /**
     * 获取屏幕尺寸与密度.
     *
     * @return mDisplayMetrics
     */
    public static DisplayMetrics getDisplayMetrics() {
        Resources mResources = BaseLibConfig.getContext().getResources();
        //DisplayMetrics{density=1.5, width=480, height=854, scaledDensity=1.5, xdpi=160.421, ydpi=159.497}
        //DisplayMetrics{density=2.0, width=720, height=1280, scaledDensity=2.0, xdpi=160.42105, ydpi=160.15764}
        DisplayMetrics mDisplayMetrics = mResources.getDisplayMetrics();
        return mDisplayMetrics;
    }

    /**
     * 状态栏的高度
     *
     * @return
     */
    public static int getStatusBarHeight(View view) {
        // 获取状态栏高度
        Rect frame = new Rect();
        view.getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;// Activity 的顶端， 就是状态栏的高度。
        return statusBarHeight;
    }


    public static int getScreenWidth() {

        int width = 0;
        WindowManager wm = (WindowManager) BaseLibConfig.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        return width;
    }

    public static int getScreenHeight() {
        int height = 0;
        WindowManager wm = (WindowManager) BaseLibConfig.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        height = dm.heightPixels;
        return height;
    }

    public static int getScreenDensity() {
        WindowManager wm = (WindowManager) BaseLibConfig.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int density = dm.densityDpi;
        return density;
    }

    /**
     * px (pixels)像素 – 是像素，就是屏幕上实际的像素点单位。
     * dip或dp (device independent pixels)设备独立像素， 与设备屏幕有关。
     * dpi(dot per inch):屏幕像素密度，每英寸多少像素
     * <p/>
     * 换算公式：px = dip * (dpi / 160)
     * DisplayMetrics中的density = dpi / 160
     * DisplayMetrics中的densityDpi就是dpi
     */
    public static float getDensity() {
//        DisplayMetrics metrics = StarZoneApplication.context.getResources().getDisplayMetrics();
//        return metrics.density;

        WindowManager wm = (WindowManager) BaseLibConfig.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.density;
    }

    /**
     * 根据手机的分辨率将dp/dip(设备的独立像素)转化为px(像素)值
     */
    public static int dip2px(float dpValue) {
        return (int) (dpValue * getDensity() + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp/dip(设备的独立像素)
     */
    public static int px2dip(float pxValue) {
        return (int) (pxValue / getDensity() + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
     */
    public static int px2sp(float pxValue) {
        float fontScale = BaseLibConfig.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px
     */
    public static int sp2px(float spValue) {
        float fontScale = BaseLibConfig.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    // 是否有虚拟键盘
    public static boolean checkDeviceHasNavigationBar() {
        boolean hasNavigationBar = false;
        Resources rs = BaseLibConfig.getContext().getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            Log.w("TAG", e);
        }

        return hasNavigationBar;

    }

    //虚拟键盘的高度
    public static int getNavigationBarHeight() {
        int navigationBarHeight = 0;
        Resources rs = BaseLibConfig.getContext().getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && checkDeviceHasNavigationBar()) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }

    /**
     * 获取屏幕宽度
     *
     * @param cutDown 缩进的距离
     * @return
     */
    public static int getDisplayWidth(int cutDown) {
        return getScreenWidth() - dip2px(cutDown);
    }

}
