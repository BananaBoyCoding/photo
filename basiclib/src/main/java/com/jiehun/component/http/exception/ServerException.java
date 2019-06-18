package com.jiehun.component.http.exception;

/**
 * Created by zhouyao on 17-12-13.
 * 服务器异常 例如 500 502 等
 */

public class ServerException extends RuntimeException {
    public static final int EORROR_500 = 500;
    public static final int EORROR_502 = 502;

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }


    public ServerException(int resultCode, String msg) {
//        this(getApiExceptionMessage(msg,resultCode));

        super(getMessage(resultCode));
    }

    public static String getMessage(int code) {
        String msg = "服务器生病了";
        if (code == EORROR_500) {

        }

        return msg;

    }
}