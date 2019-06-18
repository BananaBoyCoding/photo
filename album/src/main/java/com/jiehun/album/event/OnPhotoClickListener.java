package com.jiehun.album.event;

import android.view.View;

/**
 *
 * @author zg
 * @date 2017/12/20
 */

public interface OnPhotoClickListener {
    void onClick(View v, int position, boolean showCamera);
}
