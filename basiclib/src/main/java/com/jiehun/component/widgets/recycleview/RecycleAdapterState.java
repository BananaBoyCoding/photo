package com.jiehun.component.widgets.recycleview;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.jiehun.component.widgets.recycleview.RecycleAdapterState.State.STATE_EMPTY;
import static com.jiehun.component.widgets.recycleview.RecycleAdapterState.State.STATE_NORMAL;

/**
 * Created by zhouyao on 16-12-6.
 */
@IntDef({STATE_NORMAL, STATE_EMPTY})
@Retention(RetentionPolicy.SOURCE)
public @interface RecycleAdapterState {

    public static interface State {
        int STATE_NORMAL = 1;
        int STATE_EMPTY = 2;
    }

}
