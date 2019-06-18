package com.jiehun.album;
import com.jiehun.component.componentlib.router.ComponentManager;
import com.jiehun.component.config.BaseLibConfig;
import com.jiehun.componentservice.application.AppExtPlugin;
import com.jiehun.componentservice.application.BaseApplication;
import com.jiehun.componentservice.service.WebviewService;

/**
 * Created by zg on 2017/12/20.
 */

public class AlbumApplication  extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        BaseLibConfig.initRxLib(this, true, BuildConfig.BUILD_TYPE, "1.0", true,new AppExtPlugin());
        ComponentManager.registerComponent("com.jiehun.webview.applicationlike.WebAppLike");
        initTbsX5();
    }

    /**
     * 注册x5内核
     */
    private void initTbsX5() {
        ComponentManager componentManager = ComponentManager.getInstance();

        if (componentManager.getService(WebviewService.class.getSimpleName()) != null) {
            WebviewService webviewService = (WebviewService) componentManager.getService(WebviewService.class.getSimpleName());
            webviewService.initTbsX5(this);
        }
    }
}
