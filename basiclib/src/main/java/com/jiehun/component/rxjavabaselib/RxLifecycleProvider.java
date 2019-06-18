package com.jiehun.component.rxjavabaselib;

import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;

import rx.Observable;

/**
 * Created by zhouyao
 * on 2017/12/12.
 */

public interface RxLifecycleProvider<E> extends LifecycleProvider<E> {


    /**
     * Binds a source until the Destroy.
     * <p>
     * Intended for use with {@link Observable#compose(Observable.Transformer)}
     *
     * @return a reusable {@link Observable.Transformer} which unsubscribes at the correct time.
     */
     <T> LifecycleTransformer<T> bindToLifecycleDestroy();
}
