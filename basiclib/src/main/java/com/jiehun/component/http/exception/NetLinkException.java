package com.jiehun.component.http.exception;

/**
 * Created by zhouyao on 17-12-13.
 * 网络连接错误  SocketTimeoutException,InterruptedIOException, UnknownHostException, SocketException
 */

public class NetLinkException extends RuntimeException {
    public NetLinkException(String message, Throwable cause) {
        super(message, cause);
    }


    public NetLinkException(int resultCode, String msg) {
//        this(getApiExceptionMessage(msg,resultCode));

        super(msg);
    }
}