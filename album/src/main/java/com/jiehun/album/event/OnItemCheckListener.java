package com.jiehun.album.event;

import com.jiehun.album.entity.Photo;

/**
 * Created by zg on 2017/12/20.
 */

public interface OnItemCheckListener {

    /***
     *
     * @param position 所选图片的位置
     * @param photo     所选的图片
     * @param selectedItemCount  已选数量
     * @return enable check
     */
    boolean onItemCheck(int position, Photo photo, int selectedItemCount);
}
