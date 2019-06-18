package com.jiehun.component.webview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.jiehun.component.base.BaseActivity;
import com.jiehun.component.utils.AbLazyLogger;
import com.jiehun.component.webview.client.JhWebChromeClient;
import com.jiehun.component.webview.client.JhWebviewClient;
import com.jiehun.component.webview.event.ICiwCallBack;
import com.jiehun.component.webview.event.ILoading;
import com.jiehun.component.webview.event.IResizeHeight;

import org.json.JSONObject;

import java.net.URLDecoder;


/**
 * 基于x5内核自定义webview
 *
 * @author zg
 * @date 2018/1/3
 */

public class JsBridgeWebview extends WebView {
    private Context      mContext;
    private ILoading mILoading;
    private ICiwCallBack mICiwCallBack;

    private JsBridge jsBridge;

    private final String SHARE_URL = "ciw://common/content/share"; // 分享按钮显示
    private final String UI_BACK   = "ciw://ui_back"; // 控制返回按钮跳转
    private final String UI_SCAN   = "ciw://common/scan"; // 控制返回按钮跳转

    public JsBridgeWebview(Context context) {
        super(context);
        init(context);
    }

    public JsBridgeWebview(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public JsBridgeWebview(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;

        initSetting();

        initWebViewClient();
        initWebChromeClient();

        intJsBridge();

    }

    private void intJsBridge() {
        jsBridge = new JsBridge(mContext);
        //第二个参数：该java对象被映射为js对象后在js里面的对象名，
        // 在js中要调用该对象的方法就是通过这个来调用
        //需要和前段定义 Android iOS 前段保持一致
        this.addJavascriptInterface(jsBridge, "hapj_hybrid");
    }

    private void initWebChromeClient() {
        this.setWebChromeClient(new JhWebChromeClient(this));
    }

    private void initWebViewClient() {
        this.setWebViewClient(new JhWebviewClient(this));
    }

    private void initSetting() {
        WebSettings settings = this.getSettings();
        settings.setJavaScriptEnabled(true);
        // 去除wap页标题栏
        String ua = settings.getUserAgentString();
//        if (!ua.contains("a=hunbohui&p=ciw&m=1")) /
    }


    public void setILoading(ILoading mILoading) {
        this.mILoading = mILoading;

    }

    public void setICiwCallBack(ICiwCallBack callBack) {
        this.mICiwCallBack = callBack;
    }

    public void loadProgress(int i) {
        if (mILoading != null) {
            mILoading.loadProgress(i);
        }
    }

    public void loadTitle(String s) {
        if (mILoading != null) {
            mILoading.loadTitle(s);
        }
    }

//    public void onPageFinished(String url) {
//        if (mIResizeHeight != null) {
//            mIResizeHeight.resizeHeight();
//        }
//
//        getCookie(url);
//        getWebTitle();
//    }

    private void getWebTitle() {
        loadUrl("javascript:if(document.getElementById('hapj-wap-title'))" +
                "{" +
                "window.hapj_hybrid.getTitle(document.getElementById('hapj-wap-title').content);" +
                "} else " +
                "{" +
                "window.hapj_hybrid.getTitle(document.title);" +
                "}");
    }

    private IResizeHeight mIResizeHeight;

    public void setIResizeHeight(IResizeHeight iResizeHeight) {
        mIResizeHeight = iResizeHeight;
    }


    public void doLoadUrl(String url) {
        setCookie(url);
        this.loadUrl(url);
    }

    private void setCookie(String url) {
        if (url == null) {
            return;
        }
        CookieSyncManager.createInstance(mContext);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);





        if (true) {
            if (!TextUtils.isEmpty("lsstoken")) {
                String token = (String.format("Authorization" + "=%s", "lsstoken121") + String.format(";path=%s", "/"));
                Log.e("cookie", token);
                cookieManager.setCookie(url, token);
            } else {
                cookieManager.removeAllCookie();
            }
        } else {
            cookieManager.removeAllCookie();
        }

        StringBuilder sbCookie = new StringBuilder();
        sbCookie.append(String.format("app-key" + "=%s", "hunbasha_android"));
        sbCookie.append(String.format(";path=%s", "/"));
        cookieManager.setCookie(url, sbCookie.toString());
//        Log.e("cookie", "write - appkey - " + sbCookie.toString());

        StringBuilder cityCookie = new StringBuilder();
        cityCookie.append(String.format("city-id"+"=%s","100"));
        cityCookie.append(String.format(";path=%s", "/"));
        cookieManager.setCookie(url,cityCookie.toString());

        CookieSyncManager.getInstance().sync();
    }

    private void getCookie(String url) {
        CookieSyncManager.createInstance(mContext);
        CookieManager cookieManager = CookieManager.getInstance();
        String CookieStr = cookieManager.getCookie(url);
        Log.e("cookie", "cookie -- " + CookieStr);
    }

    private void removeCookie(Context context) {

        CookieSyncManager.createInstance(context);

        CookieManager cookieManager = CookieManager.getInstance();

        cookieManager.removeAllCookie();

        CookieSyncManager.getInstance().sync();

    }

    /**
     * 监听 ciw 跳转
     *
     * @param url
     */
    public void jumpPageFromCiwBridge(String url) {

        if (url.contains(SHARE_URL)) { // 分享
            performCiwShare(url);
        } else if (url.contains(UI_SCAN)) { // 扫描
            performCiwScan(url);
        }

//        CiwHelper.startActivity((BaseActivity) mContext, url);
    }

    /**
     * 去分享
     * @param url
     */
    private void performCiwShare(String url) {
        if (url.contains("{")) {
            String subStr = url.substring(url.indexOf("{"));
            String decodeStr = null;
            try {
                subStr = subStr.replaceAll("%(?![0-9a-fA-F]{2})", "%25").replaceAll("\\+", "%2B");
                decodeStr = URLDecoder.decode(subStr, "UTF-8");
                JSONObject jsonObject = new JSONObject(decodeStr);
                if (mICiwCallBack != null) {
                    mICiwCallBack.ciwShare(jsonObject);
                }
            } catch (Exception e) {
                AbLazyLogger.e(e.getMessage());
            }

        }
    }

    /**
     * 去扫描
     * @param url
     */
    private void performCiwScan(String url) {
        String[] item = url.split("[?]");
        if (item.length > 1) {
            String[] map = item[1].split("=");
            if (map != null && map.length > 1) {
                String back_url = map[1];

//                Intent intent = new Intent(mContext, MipcaActivityCapture.class);
//                intent.putExtra("back_url", back_url);
//                mContext.startActivityForResult(intent, 55);

            }
        }
    }

    /**
     * 跳转时同步cookie
     *
     * @param url
     */
    public void jumpPageSynCookie(String url) {
//        setCookie(url);
        if (mILoading != null) {
            mILoading.loadCurrentUrl(url);
        }
    }

    public void onDestroy() {
        removeCookie(mContext);
    }

    /**
     * 刷新当前url
     */
    public void refreshUrl() {
        String url = getUrl();
        doLoadUrl(url);
    }
}
