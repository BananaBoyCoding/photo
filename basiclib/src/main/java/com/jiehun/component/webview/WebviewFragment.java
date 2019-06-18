package com.jiehun.component.webview;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.jiehun.component.base.BaseFragment;
import com.jiehun.component.basiclib.R;
import com.jiehun.component.utils.AbLazyLogger;
import com.jiehun.component.webview.event.ILoading;
import com.jiehun.component.webview.event.IResizeHeight;

import butterknife.BindView;

/**
 * 使用webview的地方，可直接用WebviewFragment代替，用法和webview一样
 * 如不符合需要，可直接使用JsBridgeWebview
 */

public class WebviewFragment extends BaseFragment implements ILoading {
    private JsBridgeWebview mWebview;
    private ProgressBar pb;

    private String mUrl  = "https://www.jianshu.com/p/735a49865211";
    private String mHtml = null;

    private ILoading mILoading;

    @Override
    public int layoutId() {
        return R.layout.webview_fragment_webview;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        mWebview = rootView.findViewById(R.id.jswebview);
        pb = rootView.findViewById(R.id.pb);
        Bundle bundle = getArguments();
//        if (bundle != null) {
//            if (mUrl == null && mHtml == null) {
//                mUrl = bundle.getString(WebViewConfig.EXTRA_URL);
//                mHtml = bundle.getString(WebViewConfig.EXTRA_HTML);
//            }
//
//        }


        initSetting(mWebview);

        addListener();
        mWebview.setILoading(this);
    }

    private void initSetting(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setBlockNetworkImage(false);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    }

    private void addListener() {

        mWebview.setIResizeHeight(new IResizeHeight() {
            @Override
            public void resizeHeight() {
                resetHeight();
            }
        });
    }

    private void resetHeight() {
        mWebview.post(new Runnable() {
            @Override
            public void run() {
                mWebview.measure(0, 0);
                int measuredHeight = mWebview.getMeasuredHeight();
                ViewGroup.LayoutParams layoutParams = mWebview.getLayoutParams();
                layoutParams.height = measuredHeight;
                mWebview.setLayoutParams(layoutParams);
            }
        });
    }


    @Override
    public void initData() {
        if (mUrl != null) {
            loadUrl(mUrl);
        }

        if (mHtml != null) {
            loadHtml(mHtml);
        }
    }


    @Override
    public void loadProgress(int progress) {
        Log.e("yy","loadProgress------------>"+progress);
        pb.setProgress(progress);

    }

    @Override
    public void loadTitle(String title) {
        Log.e("yy","loadTitle------------>"+title);

    }

    @Override
    public void loadWapTitle(String title) {
        Log.e("yy","loadWapTitle------------>"+title);
    }

    @Override
    public void loadCurrentUrl(String url) {
        Log.e("yy","loadCurrentUrl------------>"+url);
    }

    public void loadUrl(String url) {
        mUrl = url;
        if (mWebview != null) {
            mWebview.loadUrl(url);
        }
    }

    public void loadHtml(String html) {
        mHtml = html;
        if (mWebview != null) {
            mWebview.loadDataWithBaseURL("", html, "text/html", "UTF-8", null);
        }
    }

    // 返回
    public boolean onBackPress() {
        if (mWebview.canGoBack()) {
            mWebview.goBack();
            return true;
        }

        return false;
    }
}
