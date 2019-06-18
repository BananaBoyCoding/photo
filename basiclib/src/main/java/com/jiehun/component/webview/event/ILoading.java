package com.jiehun.component.webview.event;

/**
 * Created by zg on 2018/1/3.
 */

public interface ILoading {
    void loadProgress(int progress);

    /**
     *
     * @param title
     */
    void loadTitle(String title);

    /**
     * 自定义title,根据js拿
     * @param title
     */
    void loadWapTitle(String title);

    /**
     * 当前加载的url
     * 内部跳转时触发
     * @param url
     */
    void loadCurrentUrl(String url);
}
