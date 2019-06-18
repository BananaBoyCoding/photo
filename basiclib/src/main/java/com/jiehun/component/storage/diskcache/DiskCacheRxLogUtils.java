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
public class DiskCacheRxLogUtils extends IDiskCacheUtils{
    private volatile static DiskCacheRxLogUtils instance;

    @Override
    public File getCacheDirectory(Context context) {
        return new File(StoragePathConfig.getLogRXDir());
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
    public static DiskCacheRxLogUtils getInstance() {
        if (instance == null) {
            synchronized (DiskCacheRxLogUtils.class) {
                if (instance == null) {
                    instance = new DiskCacheRxLogUtils();
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
