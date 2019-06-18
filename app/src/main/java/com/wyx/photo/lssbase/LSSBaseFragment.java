package com.wyx.photo.lssbase;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jiehun.component.base.BaseFragment;

public abstract class LSSBaseFragment extends BaseFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ReaderTools.setListener(new ReaderTools.JsonSyntaxErrorListener() {
//            @Override
//            public void onJsonSyntaxError(String s, String s1) {
//                //s  错误类型
//                //s1 具体错误
//                Log.e("yy","s-------------------------------->"+s+"-----------s");
//                Log.e("yy","s1-------------------------------->"+s1+"-----------s1");
//            }
//        });
    }
}
