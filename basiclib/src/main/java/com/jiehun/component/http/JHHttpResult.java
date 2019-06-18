package com.jiehun.component.http;

/**
 * Created by zhouyao on 17-12-13.
 */
public class JHHttpResult<T> implements HttpResult<T> {

    private int    code;
    private String serverTime;
    private String message;
    private T      data;
    private T districts;

    private int    retrofitCode;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getServerTime() {
        return serverTime;
    }

    @Override
    public int getSuccessCode() {
        return 0;
    }

    @Override
    public int getResponseCode() {
        return retrofitCode;
    }

    @Override
    public void setResponseCode(int retrofitCode) {
        this.retrofitCode = retrofitCode;
    }

    @Override
    public T getDistricts() {
        return districts;
    }

}
