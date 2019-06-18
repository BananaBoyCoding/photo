package com.jiehun.component.http;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jiehun.component.http.exception.HttpRetrofitException;
import com.jiehun.component.http.exception.NetLinkException;
import com.jiehun.component.http.gson.NullStringToEmptyAdapterFactory;
import com.jiehun.component.http.map.HttpResultFunc;
import com.jiehun.component.retrofitadapters.RxJavaCallAdapterFactory;
import com.jiehun.component.utils.AbPreconditions;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;
import java.util.ArrayList;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zhouyao
 * on 2017/12/13.
 */
public abstract class BaseRetrofitManager {

    public BaseRetrofitManager() {

    }

    public BaseRetrofitManager(String baseUrl) {
        //1.创建Retrofit对象
        initRetrofit(baseUrl);
    }

    private Retrofit retrofit;

    //将String为null时，直接转换成""
    Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create();

    protected void initRetrofit(String BASE_URL) {
        retrofit = newARetrofit(BASE_URL);
    }

    protected Retrofit newARetrofit(String BASE_URL) {
        return new Retrofit.Builder() // 定义访问的主机地址
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))  //解析方法
                .client(OkHttpUtils.getInstance().getOkHttpClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }

    /**
     * 创建忽略码
     *
     * @param args
     * @return
     */
    public ArrayList<Integer> getIngoneCodeList(Integer... args) {
        if (AbPreconditions.checkNotEmptyArray(args)) {
            ArrayList<Integer> mArrayList = new ArrayList();
            for (int i = 0; i < args.length; i++) {
                mArrayList.add(args[i]);
            }
            return mArrayList;
        }
        return null;
    }

    public <T> Observable wrapObservable(Observable observable) {
        Observable observableWrap = observable.subscribeOn(Schedulers.io())//指定io
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(this.<Response<HttpResult<T>>>dealNetWorkError())
                .onErrorResumeNext(this.<Response<HttpResult<T>>>dealNetWorkError())
                .map(new HttpResultFunc<T>());
        return observableWrap;
    }

    public <T> Observable wrapObservable(Observable observable, ArrayList<Integer> mIgnoreCodes) {
        Observable observableWrap = observable.subscribeOn(Schedulers.io())//指定io
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(this.<HttpResult<T>>dealNetWorkError())
                .onErrorResumeNext(this.<HttpResult<T>>dealNetWorkError())
                .map(new HttpResultFunc<T>(mIgnoreCodes));
        return observableWrap;
    }

    public <T> Observable wrapObservable(Observable observable, int mIgnoreCode) {
        Observable observableWrap = observable.subscribeOn(Schedulers.io())//指定io
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(this.<HttpResult<T>>dealNetWorkError())
                .onErrorResumeNext(this.<HttpResult<T>>dealNetWorkError())
                .map(new HttpResultFunc<T>(mIgnoreCode));
        return observableWrap;
    }

    protected <T> Func1<Throwable, ? extends Observable<? extends T>> dealNetWorkError() {
        return new Func1<Throwable, Observable<? extends T>>() {
            @Override
            public Observable<? extends T> call(final Throwable throwable) {
                return Observable.create(new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        if ((throwable instanceof SocketException) ||
                                (throwable instanceof UnknownHostException) ||
                                (throwable instanceof InterruptedIOException) ||
                                (throwable instanceof SocketTimeoutException) ||
                                (throwable instanceof UnknownServiceException) ||
                                (throwable instanceof NoRouteToHostException) ||
                                (throwable instanceof ConnectException)||
                                (throwable instanceof NetLinkException)
                                ) {
                            subscriber.onError(new NetLinkException(throwable.getClass().getName(), throwable));
                        } else {
                            subscriber.onError(new HttpRetrofitException(throwable.getMessage(), throwable));
                        }

                    }
                });
            }
        };
    }

}
