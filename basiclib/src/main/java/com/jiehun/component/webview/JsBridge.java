package com.jiehun.component.webview;

import android.app.Activity;
import android.content.Context;
import android.webkit.JavascriptInterface;

import com.jiehun.component.base.BaseActivity;
import com.jiehun.component.utils.AbStringUtils;
import com.jiehun.component.utils.TextUtils;
import com.jiehun.component.webview.event.ILoading;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;


/**
 * Created by zg on 2018/1/13.
 */

public class JsBridge {

    private Activity mContext;
    private ILoading mILoading;

    public JsBridge(Context context) {
        mContext = (BaseActivity) context;
        if (context instanceof ILoading) {
            mILoading = (ILoading) context;
        }
    }

    @JavascriptInterface
    public void android(String handler, String args, String callback) {
        // ui_ready_share  准备分享  标题栏显示出分享按钮
        if (!AbStringUtils.isEmpty(handler)) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(args);
                jsonObject.put("handler", handler);
                jsonObject.put("callback", callback);
            } catch (Exception e) {
                e.printStackTrace();
                jsonObject = null;
            }

            if (jsonObject != null) {
//                BaseResponse<JSONObject> response = new BaseResponse<>();
//                response.setMessage("JsBridge");
//                response.setData(jsonObject);
//                EventBus.getDefault().post(response);
            }

        }


    }

    private JSONObject getJsonObject(String args) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(args);
        } catch (Exception e) {
            jsonObject = new JSONObject();
        }

        return jsonObject;
    }


    @JavascriptInterface
    public void getTitle(final String title) {

        if (mILoading != null) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mILoading.loadWapTitle(title);
                }
            });
        }
    }


}
