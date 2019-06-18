package com.jiehun.component.storage;

import android.content.Context;

import com.jiehun.component.utils.AbLazyLogger;
import com.robin.lazy.cache.disk.naming.FileNameGenerator;
import com.robin.lazy.cache.disk.read.BytesReadFromDisk;
import com.robin.lazy.cache.disk.read.InputStreamReadFormDisk;
import com.robin.lazy.cache.disk.read.SerializableReadFromDisk;
import com.robin.lazy.cache.disk.read.StringReadFromDisk;
import com.robin.lazy.cache.disk.write.BytesWriteInDisk;
import com.robin.lazy.cache.disk.write.InputStreamWriteInDisk;
import com.robin.lazy.cache.disk.write.SerializableWriteInDisk;
import com.robin.lazy.cache.disk.write.StringWriteInDisk;
import com.robin.lazy.cache.entity.CacheGetEntity;
import com.robin.lazy.cache.entity.CachePutEntity;
import com.robin.lazy.cache.util.diskcache.IDiskCacheUtils;
import com.robin.lazy.util.IoUtils;

import java.io.InputStream;
import java.io.Serializable;

/**
 * Created by zhouyao
 * on 16-9-26.
 */
public class StorageLoaderManager {

    private StorageLoaderConfiguration storageLoaderConfiguration;

    /** 基础的缓存加载任务 */
    private LoadStorageTask cacheTask;


    /***
     * 初始化缓存的一些配置
     *
     * @param diskCacheFileNameGenerator
     * @param diskCacheSize 磁盘缓存大小
     * @param diskCacheFileCount 磁盘缓存文件的最大限度
     * @return StorageLoaderConfiguration
     * @throws
     * @see [类、类#方法、类#成员]
     */
    public void init(Context context, FileNameGenerator diskCacheFileNameGenerator, long diskCacheSize,
                     int diskCacheFileCount, IDiskCacheUtils mIDiskCacheUtils) {
        if (this.storageLoaderConfiguration != null) {
            this.storageLoaderConfiguration.close();
            this.storageLoaderConfiguration = null;
        }
        storageLoaderConfiguration = new StorageLoaderConfiguration(context,
                diskCacheFileNameGenerator, diskCacheSize, diskCacheFileCount, mIDiskCacheUtils);
        cacheTask = new LoadStorageTask(
                storageLoaderConfiguration.getLimitAgeDiskCache());
    }

    /**
     * 初始化缓存配置
     *
     * @param storageLoaderConfiguration
     * @return StorageLoaderConfiguration
     * @throws
     * @see [类、类#方法、类#成员]
     */
    public void init(StorageLoaderConfiguration storageLoaderConfiguration) {
        if (this.storageLoaderConfiguration != null) {
            this.storageLoaderConfiguration.close();
            this.storageLoaderConfiguration = null;
        }
        this.storageLoaderConfiguration = storageLoaderConfiguration;
        cacheTask = new LoadStorageTask(
                storageLoaderConfiguration.getLimitAgeDiskCache());
    }

    /***
     * 加载缓存中对应的字节数组
     * @param key
     * @return
     * byte[]
     * @throws
     * @see [类、类#方法、类#成员]
     */
    public byte[] loadBytes(String key){
        if(!isInitialize())
            return null;
        CacheGetEntity<byte[]> cacheGetEntity=new CacheGetEntity<byte[]>(new BytesReadFromDisk());
        return cacheTask.query(key, cacheGetEntity);
    }


    /**
     * 加载String
     * @param key
     * @return 等到缓存数据
     * String
     * @throws
     * @see [类、类#方法、类#成员]
     */
    public String loadString(String key){
        if(!isInitialize())
            return null;
        CacheGetEntity<String> cacheGetEntity=new CacheGetEntity<String>(new StringReadFromDisk());
        return cacheTask.query(key, cacheGetEntity);
    }

    /**
     * 获取Serializable
     * @param key
     * @return
     * Serializable
     * @throws
     * @see [类、类#方法、类#成员]
     */
    public <V extends Serializable> V loadSerializable(String key){
        if(!isInitialize())
            return null;
        CacheGetEntity<V> cacheGetEntity=new CacheGetEntity<V>(
                new SerializableReadFromDisk<V>());
        return cacheTask.query(key, cacheGetEntity);
    }

    /**
     * 加载InputStream
     * @param key
     * @return
     * InputStream
     * @throws
     * @see [类、类#方法、类#成员]
     */
    public InputStream loadInputStream(String key){
        if(!isInitialize())
            return null;
        CacheGetEntity<InputStream> cacheGetEntity=new CacheGetEntity<InputStream>(
                new InputStreamReadFormDisk());
        return cacheTask.query(key, cacheGetEntity);
    }

    /**
     * save bytes到缓存
     * @param key
     * @param value
     * @param maxLimitTime 缓存期限(单位分钟)
     * @return
     * boolean
     * @throws
     * @see [类、类#方法、类#成员]
     */
    public boolean saveBytes(String key, byte[] value, long maxLimitTime){
        if(!isInitialize())
            return false;
        CachePutEntity<byte[]> cachePutEntity=new CachePutEntity<byte[]>(new BytesWriteInDisk());
        return cacheTask.insert(key, cachePutEntity, value, maxLimitTime * 60);
    }

    /**
     * save String到缓存
     * @param key
     * @param value 要缓存的值
     * @param maxLimitTime 缓存期限(单位分钟)
     * @return 是否保存成功
     * boolean
     * @throws
     * @see [类、类#方法、类#成员]
     */
    public boolean saveString(String key, String value, long maxLimitTime){
        if(!isInitialize())
            return false;
        CachePutEntity<String> cachePutEntity=new CachePutEntity<String>(new StringWriteInDisk());
        return cacheTask.insert(key, cachePutEntity, value, maxLimitTime * 60);
    }

    /**
     * save Serializable到缓存
     * @param <V>
     * @param key
     * @param values
     * @param maxLimitTime 缓存期限(单位分钟)
     * @return
     * boolean
     * @throws
     * @see [类、类#方法、类#成员]
     */
    public <V extends Serializable> boolean saveSerializable(String key, V values, long maxLimitTime){
        if(!isInitialize())
            return false;
        CachePutEntity<V> cachePutEntity=new CachePutEntity<V>(new SerializableWriteInDisk<V>());
        return cacheTask.insert(key, cachePutEntity, values, maxLimitTime * 60);
    }

    /**
     * save inputStream到缓存
     * @param key
     * @param values
     * @param listener 进度监听器
     * @param maxLimitTime 缓存期限(单位分钟)
     * @return
     */
    public boolean saveInputStream(String key, InputStream values, IoUtils.CopyListener listener, long maxLimitTime){
        if(!isInitialize())
            return false;
        CachePutEntity<InputStream> cachePutEntity=new CachePutEntity<InputStream>(
                new InputStreamWriteInDisk(listener));
        return cacheTask.insert(key, cachePutEntity, values, maxLimitTime * 60);
    }

    /***
     * 删除一条缓存数据
     *
     * @param key 数据标识
     * @return 是否删除成功 boolean
     * @throws
     * @see [类、类#方法、类#成员]
     */
    public boolean delete(String key) {
        if(!isInitialize())
            return false;
        return cacheTask.delete(key);
    }

    /**
     * 获取缓存大小
     * @return
     * long
     * @throws
     * @see [类、类#方法、类#成员]
     */
    public long size(){
        if(!isInitialize())
            return 0;
        return cacheTask.size();
    }

    /**
     * 是否初始化
     * @return
     * boolean
     * @throws
     * @see [类、类#方法、类#成员]
     */
    private boolean isInitialize(){
        if(cacheTask==null){
            AbLazyLogger.e("缓存任务没有初始化");
            return false;
        }
        return true;
    }

    /***
     * 清理掉当前缓存,可以继续使用
     *
     * @throws
     * @see [类、类#方法、类#成员]
     */
    public void clear() {
        if(cacheTask!=null){
            cacheTask.clear();
        }
    }

    /**
     * 关闭缓存,关闭后将不能再使用缓存了
     * void
     * @throws
     * @see [类、类#方法、类#成员]
     */
    public void close(){
        if(cacheTask!=null){
            cacheTask.close();
            cacheTask = null;
        }
        if (storageLoaderConfiguration != null) {
            storageLoaderConfiguration.close();
            storageLoaderConfiguration = null;
        }
    }


}
