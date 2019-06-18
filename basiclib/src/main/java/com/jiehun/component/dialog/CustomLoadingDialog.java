package com.jiehun.component.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;

import com.jiehun.component.base.BaseDialog;
import com.jiehun.component.base.RequestDialogInterface;
import com.jiehun.component.basiclib.R;
import com.jiehun.component.utils.AbDisplayUtil;
import com.jiehun.component.widgets.view.TwoBallRotationProgressBar;


public class CustomLoadingDialog extends BaseDialog implements RequestDialogInterface {
    private TwoBallRotationProgressBar mIvLoading;

    private int mTag;

    private Runnable mDismissRunnable = new Runnable() {
        @Override
        public void run() {
            dismiss();
        }
    };

    public CustomLoadingDialog(Context context) {
        super(context, R.style.no_dim_dialog);
    }


    @Override
    public int layoutId() {
        return R.layout.custom_layout_loading_dialog;
    }

    @Override
    public void initViews() {
        mIvLoading = findViewById(R.id.iv_loading);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void setWindowParam() {
        int width = AbDisplayUtil.dip2px(120);
        setWindowParams(width, width, Gravity.CENTER);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        try {
            Activity activity = (Activity) mContext;
            if (!activity.isFinishing()) {
                super.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showDialog() {
        try {
            mIvLoading.removeCallbacks(mDismissRunnable);
            mIvLoading.startAnimator();
            show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismissDialog() {
        try {
            if (isShowing()) {
                mIvLoading.post(mDismissRunnable);
                mIvLoading.stopAnimator();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public Dialog getDialog() {
        return this;
    }

    @Override
    public int getTag() {
        return mTag;
    }

    @Override
    public void setTag(int tag) {
        mTag = tag;
    }

    @Override
    public void call() {
        showDialog();
    }

}
