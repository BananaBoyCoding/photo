package com.jiehun.component.widgets.titlebar;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiehun.component.basiclib.R;

/**
 * Created by zhouyao on 2017/6/29.
 * 可自定义的一个标题栏，分为左边部分（默认为一个TextView），标题部分（默认为一个TextView），右边部分（默认为两个TextView）
 * app:service_left_layout="@layout/xxx"会覆盖掉左边布局中的默认
 * app:service_title_layout="@layout/xxx"会覆盖掉标题布局中的默认
 * app:service_right_layout="@layout/xxx"会覆盖掉右边布局中的默认
 * 三者如果只设置其中之一，则其他使用默认
 */
public class TitleBar extends RelativeLayout {

    private RelativeLayout rl_title;
    /**
     * 左边布局
     */
    private LinearLayout ll_tb_left;
    /**
     * 标题布局
     */
    private LinearLayout ll_tb_title;
    /**
     * 右边布局
     */
    private LinearLayout ll_tb_right;

    /**
     * 默认左边
     */
    private TextView tv_tb_left;
    /**
     * 默认标题
     */
    private TextView tv_tb_title;
    /**
     * 默认右边第一个控件
     */
    private TextView tv_tb_right_first;
    /**
     * 默认右边第二个控件
     */
    private TextView tv_tb_right_second;

    /**
     * 自定义的左边布局资源ID
     */
    private int mLeftLayoutId;
    /**
     * 自定义的标题布局资源ID
     */
    private int mTitleLayoutId;
    /**
     * 自定义的右边布局资源ID
     */
    private int mRightLayoutId;

    private Context mContext;
    private boolean isStatusBarFollow = true;

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.service_title_bar, this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.service_TitleBar);
        mLeftLayoutId = a.getResourceId(R.styleable.service_TitleBar_service_left_layout, -1);
        mTitleLayoutId = a.getResourceId(R.styleable.service_TitleBar_service_title_layout, -1);
        mRightLayoutId = a.getResourceId(R.styleable.service_TitleBar_service_right_layout, -1);
        a.recycle();

        initView();
    }

    private void initView() {
        rl_title = (RelativeLayout)findViewById(R.id.rl_title);
        ll_tb_left = (LinearLayout) findViewById(R.id.ll_tb_left);
        ll_tb_title = (LinearLayout) findViewById(R.id.ll_tb_title);
        ll_tb_right = (LinearLayout) findViewById(R.id.ll_tb_right);

        if (mLeftLayoutId != -1) {
            ll_tb_left.removeAllViews();
            ll_tb_left.addView(LayoutInflater.from(getContext()).inflate(mLeftLayoutId, ll_tb_left, false));
        } else {
            tv_tb_left = (TextView) findViewById(R.id.tv_tb_left);
            tv_tb_left.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getContext() instanceof Activity) {
                        ((Activity) getContext()).onBackPressed();
                    }
                }
            });
        }

        if (mTitleLayoutId != -1) {
            ll_tb_title.removeAllViews();
            ll_tb_title.addView(LayoutInflater.from(getContext()).inflate(mTitleLayoutId, ll_tb_title, false));
        } else {
            tv_tb_title = (TextView) findViewById(R.id.tv_tb_title);
        }

        if (mRightLayoutId != -1) {
            ll_tb_right.removeAllViews();
            ll_tb_right.addView(LayoutInflater.from(getContext()).inflate(mRightLayoutId, ll_tb_right, false));
        } else {
            tv_tb_right_first = (TextView) findViewById(R.id.tv_tb_right_first);
            tv_tb_right_second = (TextView) findViewById(R.id.tv_tb_right_second);
        }

        ll_tb_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) mContext).onBackPressed();
            }
        });
    }

    /**
     * 获取左边布局
     *
     * @return LinearLayout
     */
    public LinearLayout getLeftLayout() {
        return ll_tb_left;
    }

    /**
     * 获取标题布局
     * @return LinearLayout
     */
    public LinearLayout getTitleLayout() {
        return ll_tb_title;
    }

    /**
     * 获取右边布局
     * @return LinearLayout
     */
    public LinearLayout getRightLayout() {
        return ll_tb_right;
    }

    /**
     * 设置左边TextView的文本
     */
    public void setLeftTxt(String txt) {
        if (tv_tb_left != null) {
            tv_tb_left.setText(txt);
        }
    }

    /**
     * 设置左边TextView的文本
     */
    public void setLeftTxt(@StringRes int txtId) {
        if (tv_tb_left != null) {
            tv_tb_left.setText(txtId);
        }
    }

    /**
     * 设置左边TextView的drawableLeft
     */
    public void setLeftImage(@DrawableRes int imgId) {
        if (tv_tb_left != null) {
            tv_tb_left.setCompoundDrawablesWithIntrinsicBounds(0, 0, imgId, 0);
        }
    }

    /**
     * 设置左边TextView的drawableLeft
     */
    public void setLeftImage(Drawable drawable) {
        if (tv_tb_left != null) {
            tv_tb_left.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        }
    }

    /**
     * 设置Title标题文本
     */
    public void setTitle(String text) {
        if (tv_tb_title != null) {
            tv_tb_title.setText(text);
        }
    }

    /**
     * 设置Title标题文本
     */
    public void setTitle(@StringRes int textId) {
        if (tv_tb_title != null) {
            tv_tb_title.setText(getResources().getString(textId));
        }
    }

    /**
     * 设置Title标题drawableLeft
     */
    public void setTitleImage(@DrawableRes int imgId) {
        if (tv_tb_title != null) {
            tv_tb_title.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(imgId), null);
        }
    }

    /**
     * 设置Title标题drawableLeft
     */
    public void setTitleImage(Drawable drawable) {
        if (tv_tb_title != null) {
            tv_tb_title.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        }
    }

    /**
     * 获取左边TextView
     */
    public TextView getLeftTextView() {
        return tv_tb_left;
    }

    /**
     * 获取标题TextView
     */
    public TextView getTitleTextView() {
        return tv_tb_title;
    }

    /**
     * 获取右边第一个TextView
     */
    public TextView getRightFirstTextView() {
        return tv_tb_right_first;
    }

    /**
     * 获取右边第二个TextView
     */
    public TextView getRightSecondTextView() {
        return tv_tb_right_second;
    }

    /**
     * 设置右边第一个TextView的文本
     */
    public void setRightFirstTxt(String txt) {
        if (tv_tb_right_first != null) {
            tv_tb_right_first.setText(txt);
        }
    }

    /**
     * 设置右边第一个TextView的文本
     */
    public void setRightFirstTxt(@StringRes int txtId) {
        if (tv_tb_right_first != null) {
            tv_tb_right_first.setText(getResources().getString(txtId));
        }
    }
    /**
     * 右一点击事件
     */
    public void setRightFirstOnClick(OnClickListener onClickListener) {
        tv_tb_right_first.setOnClickListener(onClickListener);
    }

    /**
     * 左边箭头点击事件
     */
    public void setLeftOnClick(OnClickListener onClick) {
        ll_tb_left.setOnClickListener(onClick);
    }

    /**
     * 左边箭头图标点击事件
     */
    public void setLeftTVOnClick(OnClickListener onClickListener) {
        tv_tb_left.setOnClickListener(onClickListener);
    }

    /**
     * 设置右边第一个TextView的DrawableLeft
     */
    public void setRightFirstImage(@DrawableRes int imgId) {
        if (tv_tb_right_first != null) {
            tv_tb_right_first.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(imgId), null);
        }
    }

    /**
     * 设置右边第一个TextView的DrawableLeft
     */
    public void setRightFirstImage(Drawable drawable) {
        if (tv_tb_right_first != null) {
            tv_tb_right_first.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        }
    }

    /**
     * 设置右边第二个TextView的文本
     */
    public void setRightSecondTxt(String txt) {
        if (tv_tb_right_second != null) {
            tv_tb_right_second.setText(txt);
        }
    }

    /**
     * 右二点击事件
     */
    public void setRightSecondOnClick(OnClickListener onClickListener) {
        tv_tb_right_second.setOnClickListener(onClickListener);
    }

    /**
     * 设置右边第二个TextView的文本颜色
     * @param colorId
     */
    public void setRightSecondTextColor(int colorId) {
        if (tv_tb_right_second != null) {
            tv_tb_right_second.setTextColor(colorId);
        }
    }

    /**
     * 设置右边第一个TextView的文本颜色
     */
    public void setRightFirstTextColor(int colorId) {
        if (tv_tb_right_first != null) {
            tv_tb_right_first.setTextColor(colorId);
        }
    }

    /**
     * 设置右边第二个TextView的文本
     */
    public void setRightSecondTxt(@StringRes int txtId) {
        if (tv_tb_right_second != null) {
            tv_tb_right_second.setText(getResources().getString(txtId));
        }
    }

    /**
     * 设置右边第二个TextView的DrawableLeft
     */
    public void setRightSecondImage(@DrawableRes int imgId) {
        if (tv_tb_right_second != null) {
            tv_tb_right_second.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(imgId), null);
        }
    }

    /**
     * 设置右边第二个TextView的DrawableLeft
     */
    public void setRightSecondImage(Drawable drawable) {
        if (tv_tb_right_second != null) {
            tv_tb_right_second.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        }
    }

    /**
     * 设置titlebar的背景色，默认设置状态栏的颜色跟随标题栏颜色
     * @param colorRes
     * @return
     */
    public TitleBar setBackgroundColorRes(int colorRes){
        rl_title.setBackgroundResource(colorRes);
        if (isStatusBarFollow){
            StatusBarCompat.setStatusBarColor((Activity) mContext,mContext.getResources().getColor(colorRes));
        }
        return this;
    }

    /**
     * 设置状态
     * @param resId
     * @return
     */
    public TitleBar setStatusBarResource(int resId) {
        isStatusBarFollow = true;
        StatusBarCompat.setStatusBarColor((Activity) mContext, mContext.getResources().getColor(resId));
        return this;
    }

    public void setStatusBarFollow(boolean statusBarFollow) {
        isStatusBarFollow = statusBarFollow;
    }
}
