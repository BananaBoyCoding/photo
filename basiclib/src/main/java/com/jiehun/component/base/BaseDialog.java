package com.jiehun.component.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.jiehun.component.basiclib.R;
import com.jiehun.component.dialog.LoadingDialog;
import com.jiehun.component.utils.AbRxJavaUtils;
import com.trello.rxlifecycle.LifecycleTransformer;

import butterknife.ButterKnife;


public abstract class BaseDialog extends Dialog implements IRequestDialogHandler {
    protected static final String TAG_LOG = BaseDialog.class.getSimpleName();

    public Context mContext;

    public BaseDialog(Context context) {
        super(context, R.style.dim_dialog);
        mContext = context;
        bindViews();
        initViews();
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        bindViews();
        initViews();
    }

    private void bindViews() {
        setContentView(layoutId());
        ButterKnife.bind(this);
    }

    @LayoutRes
    public abstract int layoutId();

    public abstract void initViews();


    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 第一次show的时候会调用该方法
        // bindViews();
        // initViews();
        setWindowParam();
        if (needLoadingDialog()) {
            checkRequestDialog();
        }
        performCreate(savedInstanceState);
    }

    public boolean needLoadingDialog() {
        return true;
    }


    protected void performCreate(Bundle savedInstanceState) {
    }

    protected abstract void setWindowParam();

    protected static final int MATCH = ViewGroup.LayoutParams.MATCH_PARENT;
    protected static final int WRAP  = ViewGroup.LayoutParams.WRAP_CONTENT;

    public void setWindowParams(int gravity) {
        setWindowParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, gravity);
    }

    public void setWindowParams(int height, int gravity) {
        setWindowParams(ViewGroup.LayoutParams.MATCH_PARENT, height, gravity);
    }

    /**
     * 在设置 设置dialog的一些属性
     *
     * @param width   一般布局和代码这里都设置match,要设置边距的直接布局里调好
     * @param height  一般布局height设置为wrap，这样可以调整dialog的上中下位置，要固定(非上中下)位置的直接在布局中调整， 设置match后，软键盘不会挤压布局
     * @param gravity 设置match后，此属性无用
     */
    public void setWindowParams(int width, int height, int gravity) {
//         setCancelable(cancelable);
//         setCanceledOnTouchOutside(cancel);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        // setContentView设置布局的透明度，0为透明，1为实际颜色,该透明度会使layout里的所有空间都有透明度，不仅仅是布局最底层的view
        // params.alpha = 1f;
        // 窗口的背景，0为透明，1为全黑
        // params.dimAmount = 0f;
        params.width = width;
        params.height = height;
        params.gravity = gravity;
        window.setAttributes(params);
    }


    private RequestDialogInterface mRequestDialog;

    public void checkRequestDialog() {
        if (mRequestDialog == null) {
            mRequestDialog = initRequestDialog();
            if (mRequestDialog == null) {
                mRequestDialog = new LoadingDialog(this.mContext);
            }
        }
        getRequestDialog().getDialog().setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.i(TAG_LOG, "cancelOkHttpCall:" + getRequestDialog().getTag());
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
    public LifecycleTransformer getLifecycleDestroy() {
        return null;
    }

    @Override
    public void cancelOkHttpCall(int requestTag) {
        AbRxJavaUtils.unSubscribe(requestTag);
    }

    @Override
    public void call() {
        if (getRequestDialog() != null) {
            getRequestDialog().showDialog();
        }
    }
}
