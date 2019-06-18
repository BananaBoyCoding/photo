package com.jiehun.component.http;

public interface HttpResult<T> {

    //错误码
    public int getCode();

    //接口返回的数据
    public T getData();

    //接口返回的信息
    public String getMessage();

    //返回服务器时间
    public String getServerTime();

    //与服务器约定的正确返回码
    public int getSuccessCode();

    public int getResponseCode();

    //404,403等等网络框架返回的错误码
    public void setResponseCode(int responseCode);
    public T getDistricts();

}
