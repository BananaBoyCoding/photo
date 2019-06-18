package com.jiehun.component.utils;

import android.util.Log;

/**
 * 应用程序的Log管理<br>
 * <b>创建时间</b> 2014-2-28
 *
 * @version 1.1
 */
public final class AbKJLoger {
    private static boolean IS_DEBUG = false;
    private static boolean SHOW_ACTIVITY_STATE = false;

    /**
     * @description：控制是否显示Log信息显示;现在在KJConfig配置显示；
     * @author samy
     * @date 2014年11月11日 上午10:36:13
     */
    public static final void openDebutLog(boolean enable) {
        IS_DEBUG = enable;
    }

    /**
     * @description：控制是否显示Activity和Fragment的生命周期调试；现在在KJConfig配置显示；
     * @author samy
     * @date 2014年11月11日 上午10:37:10
     */
    public static final void openActivityState(boolean enable) {
        SHOW_ACTIVITY_STATE = enable;
    }

    public static final void debug(String packName, String msg) {
        if (IS_DEBUG) {
            Log.d(packName, msg);
        }
    }

    public static final void state(String packName, String state) {
        if (SHOW_ACTIVITY_STATE) {

            if (IS_DEBUG) {
                Log.d("activity_state", packName + state);
            }
        }
    }

}
