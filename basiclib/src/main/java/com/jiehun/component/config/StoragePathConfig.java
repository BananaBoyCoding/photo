package com.jiehun.component.config;

import android.os.Build;
import android.os.Environment;

import com.jiehun.component.utils.AbFileUtils;
import com.robin.lazy.util.StorageUtils;

import java.io.File;

/**
 * Created by zhouyao
 * on 16-9-26.
 */
public class StoragePathConfig {


    public final static  String DEFAULT_GLIDEIMG_PATH  = "imagecache";//rxjava 错误日志
    private final static String APP_ROOT_PATH          = "topsaas";
    private final static String DEFAULT_STORAGE_PATH   = "logcrash";//崩溃日志
    private final static String DEFAULT_RXSTORAGE_PATH = "logrx";//rxjava 错误日志
    private final static String DEFAULT_TMPIMG_PATH    = "tmpimg";//图片路径
    private final static String DEFAULT_APK_PATH       = "apk";//apk缓存路径


    /**
     * 网咯数据缓存地址
     * @return
     */
    public static String getAppCacheDir() {
        return StorageUtils.getCacheDirectory(BaseLibConfig.getContext()).getAbsolutePath();
    }


    /**
     * 崩溃日志
     * @return
     */
    public static String getLogCrashDir() {
        return AbFileUtils.getSaveFolder(APP_ROOT_PATH + File.separator + DEFAULT_STORAGE_PATH).getAbsolutePath();
    }

    /**
     * rxjava 错误日志
     * @return
     */
    public static String getLogRXDir() {
        return AbFileUtils.getSaveFolder(APP_ROOT_PATH + File.separator + DEFAULT_RXSTORAGE_PATH).getAbsolutePath();
    }


    /**
     * rxjava 错误日志
     * @return
     */
    public static String getApkDir() {
        return AbFileUtils.getSaveFolder(APP_ROOT_PATH + File.separator + DEFAULT_APK_PATH).getAbsolutePath();
    }

    /**
     * 图片存储 永久存储  如浏览的图片
     * @return
     */
    public static String getSystemImagePath() {
        if (Build.VERSION.SDK_INT > 7) {
            String picturePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
            return picturePath + "/xiaoguan/";
        } else {
            String picturePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
            return picturePath + "/xiaoguan/";
        }
    }


    /**
     * 图片存储 永久存储  如浏览的图片
     * @return
     */
    public static String getTmpPicDir() {
        return AbFileUtils.getSaveFolder(APP_ROOT_PATH + File.separator + DEFAULT_TMPIMG_PATH).getAbsolutePath();
    }






}
