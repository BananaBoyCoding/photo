package com.jiehun.component.down.utils;

import android.util.Log;

import com.jiehun.component.down.core.AllenChecker;


/**
 * Created by allenliu on 2017/8/16.
 */

public class ALog {
    public static void e(String msg) {
        if (AllenChecker.isDebug()) {
            if (msg != null && !msg.isEmpty())
                Log.e("Allen Checker", msg);
        }
    }
}
