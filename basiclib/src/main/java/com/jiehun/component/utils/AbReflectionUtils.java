package com.jiehun.component.utils;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**反射工具库
 * Created by zhouyao on 16-8-1.
 */
public class AbReflectionUtils {

    private static final String TAG = AbReflectionUtils.class.getSimpleName();
    public static Field getField(Class clazz, String fieldName) {
        try {
            final Field f = clazz.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f;
        } catch (NoSuchFieldException ignored) {
        }
        return null;
    }
    public static Object getValue(Field field, Object obj) {
        try {
            return field.get(obj);
        } catch (IllegalAccessException ignored) {
        }
        return null;
    }
    public static void setValue(Field field, Object obj, Object value) {
        try {
            field.set(obj, value);
        } catch (IllegalAccessException ignored) {
        }
    }
    public static Method getMethod(Class clazz, String methodName) {
        final Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                method.setAccessible(true);
                return method;
            }
        }
        return null;
    }
    public static void invokeMethod(Object object, Method method, Object... args) {
        try {
            if (method == null) return;
            method.invoke(object, args);
        } catch (Exception e) {
            Log.d(TAG, "Can't invoke method using reflection", e);
        }
    }
}
