package com.jiehun.component.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.stetho.common.LogUtil;
import com.jakewharton.rxbinding.view.RxView;
import com.jiehun.component.dialog.CustomLoadingDialog;
import com.jiehun.component.http.AppSubscriber;
import com.jiehun.component.rxjavabaselib.RxFragment;
import com.jiehun.component.utils.AbPreconditions;
import com.jiehun.component.utils.AbRxJavaUtils;
import com.jiehun.component.utils.AbToast;
import com.jiehun.component.utils.TextUtils;
import com.trello.rxlifecycle.LifecycleTransformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;

/**
 * Created by zhouyao
 * on 2017/12/13.
 */

public abstract class BaseFragment extends RxFragment implements ICommon, IRequestDialogHandler, IUiHandler {

    protected View           rootView;
    protected LayoutInflater inflater;

    public static String  TAG_LOG;
    public        Context mContext;

    public RequestDialogInterface mRequestDialog;
    //懒加载
    private boolean isLazyLoaded;       //是否已加载过
    private boolean isPrepared;         //onCreateView是否加载完毕

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        int resId = layoutId();
        if (rootView == null) {
            rootView = inflater.inflate(resId, container, false);
            ButterKnife.bind(this, rootView);
            checkRequestDialog();
            initViews(savedInstanceState);
            initData();
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isPrepared = true;
        lazyLoad();
    }

    @Override
    public View layoutView() {
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化enventBus
        TAG_LOG = getClass().getSimpleName();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        lazyLoad();
    }

    private void lazyLoad(){
        if (getUserVisibleHint() && isPrepared && !isLazyLoaded){
            onLazyLoad();
            isLazyLoaded = true;
        }
    }

    public void onLazyLoad(){

    }

    /**
     * 防重复点击
     *
     * @param view
     * @param mOnClickListener
     */
    public void setOnclickLis(final View view, final View.OnClickListener mOnClickListener) {
        RxView.clicks(view)
                .compose(this.<Void>bindToLifecycleDestroy())//不绑定也可以的,跟view相关的绑定下安全点
                .throttleFirst(600, TimeUnit.MILLISECONDS)
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
    }

    @Override
    public void onDestroyView() {
        if (rootView != null && rootView.getParent() != null) {//影响bindToLifecycleDestroy
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
        Glide.get(mContext).clearMemory();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rootView = null;//影响bindToLifecycleDestroy
        AbRxJavaUtils.unSubscribe(hashCode());
    }



    ///////////////////////////////////////////////////////////////////////////
    // ui相关操作
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public CharSequence getTextStr(TextView textView) {
        return TextUtils.getText(textView);
    }

    @Override
    public void setText(TextView textView, CharSequence destination) {
        TextUtils.setText(textView, destination);
    }

    @Override
    public void setText(TextView textView, CharSequence destination, CharSequence defaultStr) {
        TextUtils.setText(textView, destination, defaultStr);
    }

    @Override
    public void showToast(String content) {
        AbToast.show(content);
    }

    @Override
    public void showToast(int resId) {
        AbToast.show(resId);
    }

    @Override
    public void showLongToast(String content) {
        AbToast.showLong(content);
    }

    @Override
    public void showLongToast(int resId) {
        AbToast.showLong(resId);
    }

    @Override
    public boolean isEmpty(CharSequence text) {
        return android.text.TextUtils.isEmpty(text);
    }

    @Override
    public boolean isEmpty(TextView textView) {
        return android.text.TextUtils.isEmpty(getTextStr(textView));
    }

    @Override
    public boolean isEmpty(Collection list) {
        return AbPreconditions.checkNotEmptyArray(list);
    }

    @Override
    public void setTextColor(TextView textView, int id) {
        textView.setTextColor(ContextCompat.getColor(mContext, id));
    }

    @Override
    public int getCompatColor(Context context, int id) {
        return ContextCompat.getColor(context, id);
    }

    @Override
    public int getCompatColor(int id) {
        return ContextCompat.getColor(mContext, id);
    }

    @Override
    public Drawable getCompatDrawable(Context context, int id) {
        return ContextCompat.getDrawable(context, id);
    }

    @Override
    public Drawable getCompatDrawable(int id) {
        return ContextCompat.getDrawable(mContext, id);
    }


    ///////////////////////////////////////////////////////////////////////////
    // 请求Dialog
    ///////////////////////////////////////////////////////////////////////////
    //提供自定义的dialog
    @Override
    public RequestDialogInterface initRequestDialog() {
        return null;
    }

    public void checkRequestDialog() {
        if (mRequestDialog == null) {
            mRequestDialog = initRequestDialog();
            if (mRequestDialog == null) {
//                mRequestDialog = new LoadingDialog(mContext);
                mRequestDialog = new CustomLoadingDialog(mContext);
            }
        }
        getRequestDialog().getDialog().setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                LogUtil.i(TAG_LOG, "cancelOkHttpCall:" + getRequestDialog().getTag());
                cancelOkHttpCall(getRequestDialog().getTag());
            }
        });
    }

    @Override
    public RequestDialogInterface getRequestDialog() {
        return mRequestDialog;
    }


    @Override
    public void showRequestDialog() {
        if (getRequestDialog() != null) {
            getRequestDialog().showDialog();
        }
    }

    @Override
    public void dismissRequestDialog() {
        if (getRequestDialog() != null) {
            getRequestDialog().dismissDialog();
        }
    }

    @Override
    public void call() {
        if (getRequestDialog() != null) {
            getRequestDialog().showDialog();
        }
    }

    @Override
    public LifecycleTransformer getLifecycleDestroy() {
        return bindToLifecycleDestroy();
    }

    @Override
    public void cancelOkHttpCall(int requestTag) {
        // 清除对应的请求
        AbRxJavaUtils.unSubscribe(requestTag);
    }
}
