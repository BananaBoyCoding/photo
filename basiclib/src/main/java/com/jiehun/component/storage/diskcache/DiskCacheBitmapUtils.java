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
 * Created by zhangjianlin on 16-9-26.
 */
public class DiskCacheBitmapUtils extends IDiskCacheUtils {


    /**
     * Returns singleton class instance
     */

    private static class HelperHolder {
        public static final DiskCacheBitmapUtils helper = new DiskCacheBitmapUtils();
    }

    public static DiskCacheBitmapUtils getInstance() {
        return HelperHolder.helper;
    }

    @Override
    public File getCacheDirectory(Context context) {
        File mFile = new File(StoragePathConfig.getSystemImagePath());
        mFile.mkdirs();
        return mFile;
    }

    @Override
    public DiskCache getDiskCache(File individualCacheDir, File reserveCacheDir, FileNameGenerator diskCacheFileNameGenerator, long diskCacheSize, int diskCacheFileCount) throws IOException {
        return new LruStorageDiskCache(individualCacheDir, reserveCacheDir,
                diskCacheFileNameGenerator, diskCacheSize,
                diskCacheFileCount);
    }

    @Override
    protected File createReserveDiskCacheDir(Context context) {
        return getCacheDirectory(context);
    }

    @Override
    protected File getIndividualCacheDirectory(Context context) {
        return getCacheDirectory(context);
    }
}
