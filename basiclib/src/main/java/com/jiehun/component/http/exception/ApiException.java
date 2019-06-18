package com.jiehun.component.http.exception;

import android.text.TextUtils;

/**
 * Created by liukun on 16/3/10.
 * 一些服务器给的错误码
 */
public class ApiException extends RuntimeException {

    public static final int MAINTAIN = 5;           //系统维护
    public static final int USER_NOT_EXIST = 100;   //用户不存在
    public static final int WRONG_PASSWORD = 101;   //密码错误
    public static final int ACCOUNT_EXCEPIRATION = 4002; //账号过期

    public ApiException(int resultCode, String msg) {
        this(getApiExceptionMessage(msg, resultCode));
    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     *
     * @param code
     * @return
     */
    private static String getApiExceptionMessage(String msg, int code) {

        String message = "";
        switch (code) {
            case USER_NOT_EXIST:
                message = "该用户不存在";
                break;
            case WRONG_PASSWORD:
                message = "密码错误";
                break;

            case ACCOUNT_EXCEPIRATION:
                //这里需要退出重新跳转到登录界面
                //todo eventbus 通知跳转到登录页面
                message = "帐号过期，请重新登录";
                return message;
            case MAINTAIN:
                //系统维护
                message = "系统维护中，请稍后重试";
                break;

            default:
                message = "未知错误";

        }

        if (!TextUtils.isEmpty(msg)) {
            return msg;
        }

        return message;
    }
}

