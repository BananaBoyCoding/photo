package com.jiehun.component.http.map;

import android.util.Log;

import com.jiehun.component.http.HttpResult;
import com.jiehun.component.http.exception.ApiException;
import com.jiehun.component.http.exception.ServerException;
import com.jiehun.component.utils.AbKJLoger;
import com.jiehun.component.utils.AbPreconditions;

import java.util.ArrayList;

import retrofit2.Response;
import rx.functions.Func1;


/**
 * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
 *
 * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
 */
public class HttpResultFunc<T> implements Func1<Response<HttpResult<T>>, HttpResult<T>> {

    private ArrayList<Integer> mIgnoreCodes = null;//忽略的特殊错误码

    public HttpResultFunc() {

    }

    public HttpResultFunc(ArrayList<Integer> mIgnoreCodes) {
        if (null != mIgnoreCodes) {
            this.mIgnoreCodes = new ArrayList<>();
            this.mIgnoreCodes.addAll(mIgnoreCodes);
        }
    }

    public HttpResultFunc(int mIgnoreCode) {
        this.mIgnoreCodes = new ArrayList<>();
        this.mIgnoreCodes.add(mIgnoreCode);
    }


    @Override
    public HttpResult<T> call(Response<HttpResult<T>> response) {
        if(response.raw().cacheResponse() != null && response.raw().networkResponse() == null){
            AbKJLoger.debug("okhttp","this is cache response:"+response.raw().cacheResponse().request().url()+"");
        }
        if (AbPreconditions.checkNotNullRetureBoolean(response.body()) && isSuccessful(response)) {
            if (response.body().getCode() == response.body().getSuccessCode()) {
                //返回Response引用
                response.body().setResponseCode(response.code());
                return response.body();
            }

            if (AbPreconditions.checkNotNullRetureBoolean(this.mIgnoreCodes)) {
                for (int i = 0; i < this.mIgnoreCodes.size(); i++) {
                    if (null != this.mIgnoreCodes.get(i) && (this.mIgnoreCodes.get(i) == response.body().getCode())) {
                        Log.d("ignoreCode", this.mIgnoreCodes.get(i) + "");
                        //返回Response引用
                        response.body().setResponseCode(response.code());
                        return response.body();
                    }
                }
            }

            throw new ApiException(response.body().getCode(), response.body().getMessage());
        }
        //这里是抛出服务器异常
        throw new ServerException(response.code(), "");

    }

    private boolean isSuccessful(Response httpResult) {
        if (httpResult.isSuccessful()) {
            return true;
        }
        if (httpResult.code() == 304) {
            return true;
        }
        return false;
    }
}
