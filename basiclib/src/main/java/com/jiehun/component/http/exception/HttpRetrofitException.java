package com.jiehun.component.http.exception;

/**
 * Created by zhouyao on 17-12-13.
 * 网络框架内部错误 instanceof做区分
 *
 *
 * NetLinkException , HttpRetrofitException, ApiException
 */
public class HttpRetrofitException extends RuntimeException {
    public HttpRetrofitException(String message, Throwable cause) {
        super(message, cause);
    }
}
