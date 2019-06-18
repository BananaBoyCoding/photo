package com.robin.lazy.cache.util.diskcache;

import android.content.Context;

import com.robin.lazy.cache.disk.DiskCache;
import com.robin.lazy.cache.disk.impl.ext.LruDiskCache;
import com.robin.lazy.cache.disk.naming.FileNameGenerator;
import com.robin.lazy.util.StorageUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by zhangjianlin on 16-9-26.
 * 网咯缓存配置
 */
public class DiskCacheUtils extends IDiskCacheUtils{

    private volatile static DiskCacheUtils instance;

    @Override
    public File getCacheDirectory(Context context) {
        return StorageUtils.getCacheDirectory(context);
    }


    @Override
    public DiskCache getDiskCache(File individualCacheDir, File reserveCacheDir, FileNameGenerator diskCacheFileNameGenerator, long diskCacheSize, int diskCacheFileCount) throws IOException
    {
        return new LruDiskCache(individualCacheDir, reserveCacheDir,
                diskCacheFileNameGenerator, diskCacheSize,
                diskCacheFileCount);
    }

    @Override
    protected File createReserveDiskCacheDir(Context context) {
        return getCacheDirectory(context);
    }



    /** Returns singleton class instance */
    public static DiskCacheUtils getInstance() {
        if (instance == null) {
            synchronized (DiskCacheUtils.class) {
                if (instance == null) {
                    instance = new DiskCacheUtils();
                }
            }
        }
        return instance;
    }

    @Override
    protected File getIndividualCacheDirectory(Context context) {
        return StorageUtils
                .getIndividualCacheDirectory(context);
    }


}
