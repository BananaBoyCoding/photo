package com.jiehun.component.config;

import android.content.Context;

import com.glidebitmappool.GlideBitmapPool;
import com.jiehun.component.http.OkHttpUtils;
import com.jiehun.component.utils.AbKJLoger;
import com.jiehun.component.utils.AbPreconditions;
import com.jiehun.component.utils.AbDisplayUtil;
import com.jiehun.component.utils.AbStorageManager;

/**
 * Created by zhouyao on 16-9-21.
 * GlideBitmapPool 见 glidebitmappool文件内容
 */
public class BaseLibConfig {

    private final static int availableStringMemoryPercent = 8;//网咯请求的缓存,为了减少读取sd卡花费的时间
    private final static int availableOtherMemoryPercent = 17;//其他缓存，为了减少apk卡顿问题;替代一些hashmap
    private final static int availableBitmapMemoryPercent = 8;//自己Bitmap.Create的缓存；替代Bitmap.recycle,想列表相关请使用glide缓存特殊除外
    public final static int availableGlideMemoryPercent = 50;//glide缓存大小


    public static Context context  = null;
    /**
     * UI设计的基准宽度.
     */
    public static int UI_WIDTH = 720;

    /**
     * UI设计的基准高度.
     */
    public static int UI_HEIGHT = 1080;


    /**
     * 默认 SharePreferences文件名.
     */
    public static String SHARED_PATH = "conf.dat";

    public static String  buildType = "debug";
    public static String  version   = "1.0.0";
    public static boolean httpsWrap = false;

    public static boolean isLibOpenLog = false;

    /**
     * 初始化缓存
     *
     * @param mContext
     */
    public static void initRxLib(Context mContext){
        context = mContext;
    }
    public static void initRxLib(Context mContext, boolean isOpenLog, String mBuildType, String mVersion, boolean isHttpsWrap) {

        //设置基本变量
        context = mContext;
        isLibOpenLog = isOpenLog;
        buildType = mBuildType;
        httpsWrap = isHttpsWrap;
        version = mVersion;
        UI_WIDTH = AbDisplayUtil.getScreenWidth();
        UI_HEIGHT = AbDisplayUtil.getScreenHeight();


        //分配内存
        long availableMemory = Runtime.getRuntime().maxMemory();
        int bitmapMemoryCacheSize = (int) ((float) availableMemory * ((float) availableBitmapMemoryPercent / 100.0F));
        GlideBitmapPool.initialize(bitmapMemoryCacheSize); // 10mb max memory size

        //Okhttp 初始化
        OkHttpUtils.getInstance();

        /**
         * 缓存
         */
        AbStorageManager.getInstance().initCache();

        //log开关
        AbKJLoger.openDebutLog(isOpenLog);
        AbKJLoger.openActivityState(isOpenLog);

    }

    public static Context getContext() {
        return context;
    }


    public static String getString(int resStr){
        return AbPreconditions.checkNotNullThrow(context).getString(resStr);
    }


}
