package com.jiehun.component.http;

import android.util.Log;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jiehun.component.config.BaseLibConfig;
import com.jiehun.component.config.StoragePathConfig;
import com.jiehun.component.http.interceptor.SignInterceptor;
import com.jiehun.component.utils.AbKJLoger;
import com.jiehun.component.utils.AbReflectionUtils;

import java.io.File;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.internal.Util;
import okhttp3.internal.cache.DiskLruCache;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by zhouyao on 16-9-26.
 */
public class OkHttpUtils {
    //上传下载监听池
    protected static OkHttpClient mOkHttpClient;

    private volatile static OkHttpUtils instance;
    private final static int DEFAULT_TIMEOUT = 30;//超时时间

    public OkHttpUtils() {
        initOkHttpClient();
    }

    /**
     * Returns singleton class instance
     */
    public static OkHttpUtils getInstance() {
        if (instance == null) {
            synchronized (OkHttpUtils.class) {
                if (instance == null) {
                    instance = new OkHttpUtils();
                }
            }
        }
        return instance;
    }


    public OkHttpClient getOkHttpClient() {
        if (null == mOkHttpClient) {
            initOkHttpClient();
        }
        return mOkHttpClient;
    }

    protected void initOkHttpClient() {
        if (mOkHttpClient == null) {
            synchronized (OkHttpUtils.class) {
                if (mOkHttpClient == null) {
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                        @Override
                        public void log(String message) {
                            Log.e("lsszym","---->"+message);
                            AbKJLoger.debug("okhttp", message);
                        }
                    });

                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

//                    // 指定缓存路径,缓存大小100Mb
                    Cache cache = new Cache(new File(StoragePathConfig.getAppCacheDir(), "HttpCache"),
                            1024 * 1024 * 100);

                    mOkHttpClient = new OkHttpClient.Builder()
                            .cache(cache)//SignInterceptor
//                            .addNetworkInterceptor(new QueryParamInterceptor())
                            .addInterceptor(new SignInterceptor())//添加签名
//                            .addInterceptor(new TimeCheckInterceptor())//添加签名
                            //                            .addInterceptor(new TokenInterceptor())//添加token失效的验证
                            //                            .addInterceptor(new TimeCheckInterceptor())//添加token失效的验证
//                            .addNetworkInterceptor(new CacheInterceptor())
//                            .addInterceptor(new CacheInterceptor())//
                            .addInterceptor(interceptor)
                            .addNetworkInterceptor(new StethoInterceptor())
                            /**
                            .addNetworkInterceptor(new ResponseHeaderInterceptor())
                             **/
                            .retryOnConnectionFailure(true)
                            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .sslSocketFactory(setCertificates())
                            .hostnameVerifier(new HostnameVerifier() {
                                @Override
                                public boolean verify(String hostname, SSLSession session) {
                                    return true;
                                }
                            })
                            .build();

                }
            }
        }
    }

    private SSLSocketFactory setCertificates() {
        try {
            Certificate ca;
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            AbKJLoger.debug("okhttp", "httpsWrap == " + BaseLibConfig.httpsWrap);
            //签名
//            String key = KeyUtils.getKey(BaseLibConfig.buildTyte.equals("release") ? JniUtils.getEncrypt() : JniUtils.getTestEncrypt());
//            InputStream cert = new ByteArrayInputStream(key.getBytes());
//            ca = cf.generateCertificate(cert);
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
//            keyStore.setCertificateEntry("ca", ca);
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);
            TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(tmf.getTrustManagers());
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, BaseLibConfig.httpsWrap ? wrappedTrustManagers : tmf.getTrustManagers(), null);
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {
        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
        return new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return originalTrustManager.getAcceptedIssuers();
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        try {
                            originalTrustManager.checkClientTrusted(certs, authType);
                        } catch (CertificateException ignored) {
                        }
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        try {
                            originalTrustManager.checkServerTrusted(certs, authType);
                        } catch (CertificateException ignored) {
                        }
                    }
                }
        };
    }
}
