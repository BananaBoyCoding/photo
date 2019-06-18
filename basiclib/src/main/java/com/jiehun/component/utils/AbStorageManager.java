package com.jiehun.component.utils;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.jiehun.component.config.BaseLibConfig;
import com.jiehun.component.config.StoragePathConfig;
import com.jiehun.component.storage.StorageFileNameGenerator;
import com.jiehun.component.storage.StorageLoaderManager;
import com.jiehun.component.storage.diskcache.DiskCacheBitmapUtils;
import com.jiehun.component.storage.diskcache.DiskCacheCrashLogUtils;
import com.jiehun.component.storage.diskcache.DiskCacheRxLogUtils;
import com.robin.lazy.util.CacheTime;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhouyao
 * on 16-9-26.
 */
public class AbStorageManager {

    private final static long ONE_MB = 1024 * 1024;//硬盘文件的缓存大小20MB
    private static int diskCacheFileCount = 100;//文件数目

    private StorageLoaderManager mBitmapManager;
    private StorageLoaderManager mCrashLogManager;
    private StorageLoaderManager mRxLogManager;


    /**
     * Returns singleton class instance
     */

    private static class HelperHolder {
        public static final AbStorageManager helper = new AbStorageManager();
    }

    public static AbStorageManager getInstance() {
        return HelperHolder.helper;
    }


    /**
     * 初始化
     */
    public void initCache() {
        getRxLogManager();
        getBitmapManager();
        getCrashLogManager();
    }



    /**
     * 默认保存10天
     *
     */
    public void saveCrashLog(Throwable ex) {
        saveCrashLog(getThrowable(ex));
    }

    /**
     * 默认保存10天
     *
     */
    public void saveRxLog(Throwable ex) {
        saveRxLog(getThrowable(ex));
    }


    /**
     * 默认保存10天
     *
     * @param value
     */
    public void saveRxLog(String value) {
        if(!TextUtils.isEmpty(value)) {
            String fileName = getFileName() + ".txt";
            getRxLogManager().saveString(fileName, value, CacheTime.getInstance().getTIME_OF_TENDAY());
        }
    }

    /**
     * 默认保存10天
     *
     * @param value
     */
    public void saveCrashLog(String value) {
        if(!TextUtils.isEmpty(value)) {
            String fileName = getFileName() + ".txt";
            getCrashLogManager().saveString(fileName, value, CacheTime.getInstance().getTIME_OF_TENDAY());
        }
    }

    /**
     * 默认保存一个月
     *
     * @param value
     */
    public void saveBitmap(Bitmap value) {
        if (AbPreconditions.checkNotNullRetureBoolean(value)) {
            String fileName = getFileName() + ".png";

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            value.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            InputStream is = new ByteArrayInputStream(baos.toByteArray());
            getBitmapManager().saveInputStream(fileName, is, null, CacheTime.getInstance().getTIME_OF_ONEMONTH());
        }
    }


    public StorageLoaderManager getBitmapManager() {
        if (null == mBitmapManager) {
            mBitmapManager = new StorageLoaderManager();
            mBitmapManager.init(BaseLibConfig.getContext(), new StorageFileNameGenerator(), ONE_MB * 40,
                    2 * diskCacheFileCount, DiskCacheBitmapUtils.getInstance());
        }
        return mBitmapManager;
    }


    /**
     * rx错误日志
     *
     * @return
     */
    public StorageLoaderManager getRxLogManager() {
        if (null == mRxLogManager) {
            mRxLogManager = new StorageLoaderManager();
            mRxLogManager.init(BaseLibConfig.getContext(), new StorageFileNameGenerator(), ONE_MB * 5,
                    2 * diskCacheFileCount, DiskCacheRxLogUtils.getInstance());
        }
        return mRxLogManager;
    }


    /**
     * 崩溃日志相关
     *
     * @return
     */
    public StorageLoaderManager getCrashLogManager() {
        if (null == mCrashLogManager) {
            mCrashLogManager = new StorageLoaderManager();
            mCrashLogManager.init(BaseLibConfig.getContext(), new StorageFileNameGenerator(), ONE_MB * 5,
                    2 * diskCacheFileCount, DiskCacheCrashLogUtils.getInstance());
        }
        return mCrashLogManager;
    }

    public String getAFullPath(String subfix){
        return StoragePathConfig.getSystemImagePath() + getFileName() + subfix;
    }

    public String getFileName() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String currentTimeStamp = format.format(new Date());
        return currentTimeStamp;
    }

    public String getThrowable(Throwable ex) {

        Writer writer = null;
        PrintWriter printWriter = null;
        String stackTrace = "";
        try {
            writer = new StringWriter();
            printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            stackTrace = writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (printWriter != null) {
                printWriter.close();
            }
        }
        return stackTrace;
    }


}
