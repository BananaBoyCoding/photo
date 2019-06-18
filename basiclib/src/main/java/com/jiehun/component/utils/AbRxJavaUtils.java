package com.jiehun.component.utils;

import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.jiehun.component.http.ISubscriberTag;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by zhouyao on 16-10-6.
 * 可能是东半球最全的RxJava使用场景小结
 * http://blog.csdn.net/theone10211024/article/details/50435325
 */
public class AbRxJavaUtils {

    //请求任务的Subscriber几种管理，onDestroy或者dialog.cancel时候需要移除该Subscriber
    public static List<ISubscriberTag> mISubscriberTags = new ArrayList<>();

    public static void unSubscribe(int requestTag) {
        Iterator<ISubscriberTag> iterator = AbRxJavaUtils.mISubscriberTags.iterator();
        while (iterator.hasNext()) {
            ISubscriberTag next = iterator.next();
            if (next == null) {
                continue;
            }
            if (next.getTag() == requestTag) {
                Subscriber temp = (Subscriber) next;
                temp.unsubscribe();
                iterator.remove();
            }
        }
    }

    /**
     * 搜索框操作
     *
     * @param inputEditText
     * @param mSubscriber
     */
    public static void setEditTextChangeLis(TextView inputEditText, Subscriber<TextViewTextChangeEvent> mSubscriber) {
        RxTextView.textChangeEvents(inputEditText)
                .debounce(400, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mSubscriber);
    }


    public static void setEditTextChangeLis(TextView inputEditText, int timeMills, Subscriber<TextViewTextChangeEvent> mSubscriber) {
        RxTextView.textChangeEvents(inputEditText)
                .debounce(timeMills, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mSubscriber);
    }

    /**
     * 使用timer做定时操作,当有“x秒后执行y操作” 一次操作
     * 获取rx的时钟，记得绑定生命周期
     *
     * @param milliSeconds
     * @return
     */
    public static Observable<Long> getRxTimer(int milliSeconds) {
        return Observable.timer(milliSeconds, TimeUnit.MILLISECONDS);
    }


    /**
     * interval做周期性操作,每隔xx秒后执行yy操作
     * 获取rx的时钟，记得绑定生命周期
     *
     * @param milliSeconds
     * @return
     */
    public static Observable<Long> getRxInterval(int milliSeconds) {
        return Observable.interval(milliSeconds, TimeUnit.MILLISECONDS);
    }

    /**
     * 防止重复点击
     *
     * @param view
     * @param mSubscriber
     */
    public static void setUnDoubleClick(View view, Subscriber<Object> mSubscriber) {
        RxView.clicks(view)
                .throttleFirst(400, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mSubscriber);
    }

    public static <T> Subscription toSubscribe(Observable<T> o, Subscriber<T> s) {
        return o
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    public static boolean unSubscribe(Subscription subscription) {
        if (AbPreconditions.checkNotNullRetureBoolean(subscription)) {
            if (!subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
            return subscription.isUnsubscribed();
        }
        return true;
    }

    public static <T> Subscription toSubscribe(Observable<T> o, Observable.Transformer transformer, Subscriber<T> s) {
        if (s instanceof ISubscriberTag) {
            ISubscriberTag iSubscriberTag = (ISubscriberTag) s;
            if (iSubscriberTag.getTag() > 0) {
                mISubscriberTags.add((ISubscriberTag) s);
            }
        }
        return o.compose(transformer)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

}
