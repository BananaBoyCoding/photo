package com.jiehun.component.utils;

import android.graphics.Paint;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.jiehun.component.http.AppSubscriber;

import java.util.concurrent.TimeUnit;

/**
 * JieHun
 * describe:
 * author zhouyao
 * date 2018/1/5
 */

public class AbViewUtils {

    /**
     * 根据指定的root view 获取指定id的View对象
     *
     * @param rootView 包含该子对象的root view
     * @param resId    id
     * @param <T>      转换后的对象类型
     * @return
     */
    public static <T extends View> T findView(@NonNull View rootView, @IdRes int resId) {
        return (T) rootView.findViewById(resId);
    }


    public static void setOnclickLis(final View view, final View.OnClickListener mOnClickListener) {
        if(AbPreconditions.checkNotNullRetureBoolean(mOnClickListener)) {
            RxView.clicks(view)
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe(new AppSubscriber<Object>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);//处理错误
                        }

                        @Override
                        public void onNext(Object o) {
                            mOnClickListener.onClick(view);
                        }
                    });
        }else {
            view.setOnClickListener(mOnClickListener);
        }

    }

    /**
     * 计算文字的长度
     * @param text
     * @param size
     * @return
     */
    public static int getCharacterWidth(String text,float size){
        if (AbStringUtils.isNull(text)){
            return 0;
        }

        Paint paint = new Paint();
        paint.setTextSize(size);
        int textWidth = (int) paint.measureText(text);
        return textWidth;
    }
}
