package com.jiehun.component.down.v2.callback;

import android.app.Dialog;
import android.content.Context;

import com.jiehun.component.down.v2.builder.UIData;


/**
 * Created by allenliu on 2018/1/18.
 */

public interface CustomDownloadFailedListener {
    Dialog getCustomDownloadFailed(Context context, UIData versionBundle);
}
