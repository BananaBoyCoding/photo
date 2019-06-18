package com.jiehun.component.base;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiehun.component.basiclib.R;
import com.jiehun.component.utils.AbViewUtils;
import com.jiehun.component.widgets.titlebar.TitleBar;

public abstract class BaseTitleActivity extends BaseActivity{
    private RelativeLayout mRlTitleBottomLine;
    private TitleBar titleBar;
    private TextView titleView;


    @Override
    public View layoutView() {
        ViewGroup rootView = null;
        if (layoutId() != 0) {
            rootView = (ViewGroup) getLayoutInflater().inflate(R.layout.service_activity_base_title, null);
            mRlTitleBottomLine = AbViewUtils.findView(rootView,R.id.rl_title_bottom_line);
            titleBar = rootView.findViewById(R.id.title_bar);
            initView();
            getLayoutInflater().inflate(layoutId(), rootView, true);
        }
        return rootView;
    }


    public void hideTitleBottomLine(){
        mRlTitleBottomLine.setVisibility(View.GONE);
    }

    public void showTitleBottomLine(){
        mRlTitleBottomLine.setVisibility(View.VISIBLE);
    }
    private void initView(){
        titleView  = titleBar.getTitleTextView();
    }
    protected void setTitleText(String title){
        titleView.setText(title);
    }

    protected void setFirstButton(String str,@DrawableRes int imgId){
        titleBar.getRightFirstTextView().setText(str);
        if(imgId != 0){
            titleBar.getRightFirstTextView().setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(imgId), null);
        }
    }
    protected void setSecondButton(String str,@DrawableRes int imgId){
        titleBar.getRightSecondTextView().setText(str);
        if(imgId != 0){
            titleBar.getRightSecondTextView().setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(imgId), null);
        }
    }

    protected void setFirstListener(View.OnClickListener click){
        titleBar.getRightFirstTextView().setOnClickListener(click);
    }
    protected void setSecondListener(View.OnClickListener click){
        titleBar.getRightSecondTextView().setOnClickListener(click);
    }
}
