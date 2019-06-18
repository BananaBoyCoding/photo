package com.jiehun.album.api;

import com.jiehun.component.http.BaseApiManager;

import java.util.HashMap;

//import okhttp3.MultipartBody;
import rx.Observable;

/**
 * Created by zhouyao
 * on 2017/12/13.
 */

public class ApiManager extends BaseApiManager {

    private ApiManagerImpl mApiManagerImpl;
    /**
     * 单例模式
     *
     * @return
     */
    private static class HelperHolder {
        public static final ApiManager helper = new ApiManager();
    }

    public static ApiManager getInstance(){
        return HelperHolder.helper;
    }

    @Override
    public void ipHostChange() {
//        initRetrofit(BaseHttpUrl.BASE_URL);
        mApiManagerImpl = create(ApiManagerImpl.class);
    }


    // 上传图片
//    public Observable uploadImgList(MultipartBody multipartBody){
//        Observable observable = mApiManagerImpl.postImgList(multipartBody);
//        return wrapObservable(observable);
//    }

}
