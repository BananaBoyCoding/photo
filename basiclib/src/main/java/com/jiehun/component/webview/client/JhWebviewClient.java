package com.jiehun.component.webview.client;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jiehun.component.webview.JsBridgeWebview;


/**
 * @author zg
 * @date 2018/1/3
 */

public class JhWebviewClient extends WebViewClient {
    private JsBridgeWebview mWebview;

    public JhWebviewClient(JsBridgeWebview webview) {
        mWebview = webview;
    }

    @Override
    public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
        super.onPageStarted(webView, s, bitmap);

    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String s) {
        if (s.startsWith("ciw")) {
            mWebview.jumpPageFromCiwBridge(s);
            return true;
        } else {
            mWebview.jumpPageSynCookie(s);
            return super.shouldOverrideUrlLoading(webView, s);
        }

    }

    @Override
    public void onPageFinished(WebView webView, String s) {
        super.onPageFinished(webView, s);
//        mWebview.onPageFinished(s);

    }


}
