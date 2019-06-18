package com.jiehun.component.webview.client;


import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.jiehun.component.webview.JsBridgeWebview;

/**
 * Created by zg on 2018/1/3.
 */

public class JhWebChromeClient extends WebChromeClient {
    private JsBridgeWebview mWebview;

    public JhWebChromeClient(JsBridgeWebview context) {
       mWebview = context;
    }

    @Override
    public void onProgressChanged(WebView webView, int i) {
        super.onProgressChanged(webView, i);

        if(mWebview!=null) {
            mWebview.loadProgress(i);
        }
    }

    @Override
    public void onReceivedTitle(WebView webView, String s) {
        super.onReceivedTitle(webView, s);
        if(mWebview!=null) {
            mWebview.loadTitle(s);
        }
    }


}
