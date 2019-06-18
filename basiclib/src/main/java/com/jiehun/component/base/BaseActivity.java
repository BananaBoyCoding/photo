package com.jiehun.component.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.stetho.common.LogUtil;
import com.jakewharton.rxbinding.view.RxView;
import com.jiehun.component.basiclib.R;
import com.jiehun.component.dialog.CustomLoadingDialog;
import com.jiehun.component.dialog.LoadingDialog;
import com.jiehun.component.helper.ActivityManager;
import com.jiehun.component.http.AppSubscriber;
import com.jiehun.component.rxjavabaselib.RxAppCompatActivity;
import com.jiehun.component.utils.AbPreconditions;
import com.jiehun.component.utils.AbRxJavaUtils;
import com.jiehun.component.utils.AbToast;
import com.jiehun.component.utils.InputMethodManagerUtils;
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

public abstract class BaseActivity extends RxAppCompatActivity implements ICommon, IRequestDialogHandler, IUiHandler {
    public static String TAG_LOG;

    public Context      mContext;
    public BaseActivity mBaseActivity;

    public RequestDialogInterface mRequestDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        mBaseActivity = this;

        TAG_LOG = getClass().getSimpleName();
        getIntentData(getIntent());

        View layoutView = layoutView();
        if (layoutView == null) {
            setContentView(layoutId());
        } else {
            setContentView(layoutView);
        }

        ActivityManager.create().addActivity(this);

//        //初始化eventBus
//        EventBus.getDefault().register(this);

        ButterKnife.bind(this);

        checkRequestDialog();

        initViews(savedInstanceState);
        initData();
        Log.e("classname", getClass().getSimpleName());
    }

    public void getIntentData(Intent intent) {

    }

    @Override
    public View layoutView() {
        return null;
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
    protected void onPause() {
        super.onPause();
        Glide.get(mContext).clearMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AbRxJavaUtils.unSubscribe(hashCode());
        EventBus.getDefault().unregister(this);
        ActivityManager.create().finishActivity(this);


    }


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
    public void showToast(@StringRes int resId) {
        AbToast.show(resId);
    }

    @Override
    public void showLongToast(String content) {
        AbToast.showLong(content);
    }

    @Override
    public void showLongToast(@StringRes int resId) {
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

    public void checkRequestDialog() {
        if (mRequestDialog == null) {
            mRequestDialog = initRequestDialog();
            if (mRequestDialog == null) {
//                mRequestDialog = new LoadingDialog(this);
                mRequestDialog = new CustomLoadingDialog(this);
            }
        }
        getRequestDialog().getDialog().setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.e("yy", "cancelOkHttpCall:" + getRequestDialog().getTag());
                cancelOkHttpCall(getRequestDialog().getTag());
            }
        });
    }

    @Override
    public RequestDialogInterface initRequestDialog() {
        return null;
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


    ///////////////////////////////////////////////////////////////////////////
    // 处理点击外部影藏输入法
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            InputMethodManagerUtils.hideSoftInputFromWindow(this);
        }
        return super.onTouchEvent(event);

    }


    public void superOnBackPressed() {
        super.onBackPressed();
    }
}
