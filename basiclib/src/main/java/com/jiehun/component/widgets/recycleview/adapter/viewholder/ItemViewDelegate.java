package com.jiehun.component.widgets.recycleview.adapter.viewholder;



/**
 * Created by zhouyao on 16/6/22.
 */
public interface ItemViewDelegate<T> {

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(ViewRecycleHolder holder, T t, int position);

}
