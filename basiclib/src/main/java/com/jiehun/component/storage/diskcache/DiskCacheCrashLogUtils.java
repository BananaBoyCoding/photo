package com.jiehun.component.storage.diskcache;

import android.content.Context;

import com.jiehun.component.config.StoragePathConfig;
import com.jiehun.component.storage.diskkrucache.LruStorageDiskCache;
import com.robin.lazy.cache.disk.DiskCache;
import com.robin.lazy.cache.disk.naming.FileNameGenerator;
import com.robin.lazy.cache.util.diskcache.IDiskCacheUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by zhouyao
 * on 16-9-26.
 */
public class DiskCacheCrashLogUtils extends IDiskCacheUtils {
    private volatile static DiskCacheCrashLogUtils instance;

    @Override
    public File getCacheDirectory(Context context) {
        return new File(StoragePathConfig.getLogCrashDir());
    }

    @Override
    public DiskCache getDiskCache(File individualCacheDir, File reserveCacheDir, FileNameGenerator diskCacheFileNameGenerator, long diskCacheSize, int diskCacheFileCount)  throws IOException
    {
        return new LruStorageDiskCache(individualCacheDir, reserveCacheDir,
                diskCacheFileNameGenerator, diskCacheSize,
                diskCacheFileCount);
    }
    @Override
    protected File createReserveDiskCacheDir(Context context) {
        return getCacheDirectory(context);
    }



    /** Returns singleton class instance */
    public static DiskCacheCrashLogUtils getInstance() {
        if (instance == null) {
            synchronized (DiskCacheCrashLogUtils.class) {
                if (instance == null) {
                    instance = new DiskCacheCrashLogUtils();
                }
            }
        }
        return instance;
    }

    @Override
    protected File getIndividualCacheDirectory(Context context) {
        return getCacheDirectory(context);
    }
}