package com.jiehun.component.http;


import android.util.Log;
import android.widget.Toast;

import com.jiehun.component.base.RequestDialogInterface;
import com.jiehun.component.config.BaseLibConfig;
import com.jiehun.component.http.exception.ApiException;
import com.jiehun.component.http.exception.HttpRetrofitException;
import com.jiehun.component.http.exception.NetLinkException;
import com.jiehun.component.http.exception.ServerException;
import com.jiehun.component.utils.AbLazyLogger;
import com.jiehun.component.utils.AbPreconditions;
import com.jiehun.component.utils.AbStorageManager;
import com.jiehun.component.utils.AbToast;

import rx.Subscriber;

/** 请求结果处理
 * Created by zhouyao on 16-9-22.
 * <p>
 */
public abstract class NetSubscriber<T> extends Subscriber<HttpResult<T>> {

    protected RequestDialogInterface mNetWorkLoading;
    /*
    *
    * 执行顺序  成功 onNext-->onCompleted-->commonCall
    * */

    @Override
    public void onCompleted() {
        commonCall(null);
    }

    @Override
    public void onError(Throwable e) {
        dealOnError(e);
        commonCall(e);
    }

    public NetSubscriber() {

    }

    public NetSubscriber(RequestDialogInterface netWorkLoading) {
        this.mNetWorkLoading = netWorkLoading;
    }

    public void commonCall(Throwable e) {
        if (AbPreconditions.checkNotNullRetureBoolean(mNetWorkLoading)) {
            mNetWorkLoading.dismissDialog();
        }
    }

    /**
     * 错误类型
     * 1.网络连接错误类型  NetLinkException , 链接网咯不给力提示
     * 2.网络数据接收之后的处理错误类型HttpRetrofitException,  数据为空, +  Toast错误类型
     * 3.与服务器约定的错误类型ApiException, 数据为空  +  Toast服务器返回的错误信息
     * 4.onNext中的错误类型, 数据为空 + Toast错误类型
     *
     * @param e
     */
    public void dealOnError(Throwable e) {

        AbLazyLogger.d(((e instanceof NullPointerException) ? "NullPointerException" : e.getMessage()));
        if (BaseLibConfig.isLibOpenLog) {
            e.printStackTrace();
        }

        if (e instanceof NetLinkException) {//网络不给力
            AbToast.show("您的网络不顺畅哦...");
            e.printStackTrace();
        } else if (e instanceof ApiException) {//与服务器约定的错误类型ApiException
            AbToast.show(e.getMessage());
        } else if (e instanceof HttpRetrofitException) {
            if (!BaseLibConfig.buildType.equals("release")) {
                AbToast.show(
                        ((e instanceof NullPointerException) ? "NullPointerException" : e.getMessage()));
            }

            //写入sd卡
//            AbStorageManager.getInstance().saveRxLog(e);
        } else if (e instanceof ServerException) {
            AbToast.show(e.getMessage());
            //写入sd卡
//            AbStorageManager.getInstance().saveRxLog(e);
        }else {
            if (!BaseLibConfig.buildType.equals("release")) {
                AbToast.show("app:" +  //为了区分HttpRetrofitException框架内部错误与 onNext错误类型
                        ((e instanceof NullPointerException) ? "NullPointerException" : e.getMessage()));
            }
            //写入sd卡
//            AbStorageManager.getInstance().saveRxLog(e);
        }
    }


}
