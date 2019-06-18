package com.jiehun.component.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by zhouyao
 * on 2017/12/20.
 */

public class AbJsonParseUtils {

    private static Gson gson = new Gson();

    /**
     * @param o 传入对象
     * @return 返回json字符串
     */
    public static String getJsonString(Object o) {
        return gson.toJson(o);
    }


    /**
     * @param result
     * @param type
     * @param <T>
     * @return 获得json对象
     */
    public static <T> T jsonToBean(String result, Type type) {
        Gson gson = new Gson();
        T t = null;
        try {
            t = gson.fromJson(result, type);
        } catch (Exception e) {
            e.printStackTrace();
            t = null;
        }
        return t;

    }

    /**
     * @param result
     * @param classOfT
     * @param <T>
     * @return 获得json对象
     */
    public static <T> T jsonToBean(String result, Class<T> classOfT) {
        Gson gson = new Gson();
        T t = null;
        try {
            t = gson.fromJson(result, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
            t = null;
        }
        return t;

    }
}
