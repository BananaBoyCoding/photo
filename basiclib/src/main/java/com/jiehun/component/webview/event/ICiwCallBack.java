package com.jiehun.component.webview.event;

import org.json.JSONObject;

/**
 * Created by zg on 2018/2/6.
 */

public interface ICiwCallBack {
    void ciwShare(JSONObject jsonObject);
    void ciwScan();
}
