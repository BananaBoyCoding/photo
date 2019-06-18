package com.jiehun.component.storage;


import android.content.Context;

import com.robin.lazy.cache.disk.DiskCache;
import com.robin.lazy.cache.disk.impl.LimitedAgeDiskCache;
import com.robin.lazy.cache.disk.naming.FileNameGenerator;
import com.robin.lazy.cache.util.diskcache.IDiskCacheUtils;

/**
 * 缓存加载配置
 *
 * @author jiangyufeng
 * @version [版本号, 2015年12月15日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class StorageLoaderConfiguration {

    /**
     * 默认的过期时间(单位分钟)
     */
    private final static long MAX_LIMIT_TIEM_DEFAULT = 5;

    /**
     * 磁盘缓存类
     */
    private DiskCache diskCache;

    /**
     * 缓存默认有效期(单位分钟)
     */
    private long maxAge = MAX_LIMIT_TIEM_DEFAULT;

    /**
     * @param diskCacheFileNameGenerator
     * @param diskCacheSize              磁盘缓存大小 (单位字节byte)
     * @param diskCacheFileCount         磁盘缓存文件的最大限度
     * @param maxAge                     有效期(单位分钟)
     */
    public StorageLoaderConfiguration(Context context,
                                      FileNameGenerator diskCacheFileNameGenerator, long diskCacheSize,
                                      int diskCacheFileCount, long maxAge, IDiskCacheUtils mIDiskCacheUtils) {
        this(context, diskCacheFileNameGenerator, diskCacheSize,
                diskCacheFileCount, mIDiskCacheUtils);
        this.maxAge = maxAge;
    }

    /**
     * <默认构造函数>
     *
     * @param diskCacheFileNameGenerator
     * @param diskCacheSize              磁盘缓存大小(单位字节byte)
     * @param diskCacheFileCount         磁盘缓存文件的最大限度
     */
    public StorageLoaderConfiguration(Context context,
                                      FileNameGenerator diskCacheFileNameGenerator, long diskCacheSize,
                                      int diskCacheFileCount, IDiskCacheUtils mIDiskCacheUtils) {
        diskCache = mIDiskCacheUtils.createDiskCache(context,
                diskCacheFileNameGenerator, diskCacheSize, diskCacheFileCount);
    }

    /***
     * 获取基本类型的磁盘缓存
     *
     * @return DiskCache
     * @throws
     * @see [类、类#方法、类#成员]
     */
    public DiskCache getDiskCache() {
        return diskCache;
    }

    /**
     * 获取有缓存有有限期的磁盘缓存
     *
     * @return DiskCache
     * @throws
     * @see [类、类#方法、类#成员]
     */
    public DiskCache getLimitAgeDiskCache() {
        if (maxAge > 0) {
            return new LimitedAgeDiskCache(diskCache, maxAge * 60);
        }
        return new LimitedAgeDiskCache(diskCache, MAX_LIMIT_TIEM_DEFAULT * 60);
    }

    /***
     * 设置磁盘缓存
     *
     * @param diskCache
     * @throws
     * @see [类、类#方法、类#成员]
     */
    public void setDiskCache(DiskCache diskCache) {
        this.diskCache = diskCache;
    }

    /**
     * 清理 void
     *
     * @throws
     * @see [类、类#方法、类#成员]
     */
    public void close() {
        if (diskCache != null) {
            diskCache.close();
            diskCache = null;
        }
    }


}