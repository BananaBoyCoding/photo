package com.jiehun.component.http;


import com.jiehun.component.config.BaseLibConfig;
import com.jiehun.component.utils.AbStorageManager;
import com.jiehun.component.utils.AbToast;

import rx.Subscriber;

/**
 * Created by zhouyao
 * on 17-12-13.
 */
public abstract class AppSubscriber<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        dealOnError(e);
    }

    /**
     * 错误类型
     * 1.网咯链接错误类型  NetLinkException , 链接网咯不给力提示
     * 2.网咯数据接收之后的处理错误类型HttpRetrofitException,  数据为空, +  Toast错误类型
     * 3.与服务器约定的错误类型ApiException, 数据为空  +  Toast服务器返回的错误信息
     * 4.onNext中的错误类型, 数据为空 + Toast错误类型
     *
     * @param e
     */
    public void dealOnError(Throwable e) {
        if (BaseLibConfig.isLibOpenLog) {
            e.printStackTrace();
        }

        AbToast.show("app:" +  //为了区分HttpRetrofitException框架内部错误与 onNext错误类型
                ((e instanceof NullPointerException) ? "NullPointerException" : e.getMessage()));
        //写入sd卡
        AbStorageManager.getInstance().saveRxLog(e);
    }

}
