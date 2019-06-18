package com.jiehun.component.widgets.recycleview.itemdecorator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.jiehun.component.basiclib.R;


/**
 * Created by zhouyao on 2016/12/1.
 * recycler长短线控制
 * 1、支持中间线的边距
 * 2、支持bottom和top的显示与否
 * 3、支持线的颜色变化
 * 4、支持线的高度控制
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private int middleLeftMargin;
    private int middleRightMargin;

    private int headCut = -1;//头部
    private int tailCut = -1;//脚部

    private int      color;
    private Drawable drawable;
    private boolean  topShow;
    private boolean  bottomShow;

    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };
    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    private Drawable mDivider;//线条
    private Drawable mDividerItem;//item背景线条
    private int      itemColor;//颜色


    private int mOrientation = VERTICAL_LIST;

    public DividerItemDecoration(Context context, int orientation) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = context.getResources().getDrawable(R.drawable.divider);
        mDividerItem = context.getResources().getDrawable(R.drawable.divider);
        a.recycle();
        setOrientation(orientation);
    }

    /**
     *
     * @param context
     * @param middleLeftMargin      分割线左间距
     * @param middleRightMargin     分割线右间距
     * @param drawable              drawable类型的分割线
     * @param topShow               第一个item上边是否显示分割线
     * @param bottomShow            最后一个item下是否显示分割线
     * @param color                 color类型的分割线
     * @param itemColor             item的背景色，默认为白色，其他色需要进行设置
     * @param headCut               recycleview向上空隔得距离，这个空格会随着recycleview的滑动而滑动
     * @param tailCut               recycleview向下空隔得距离，这个空格会随着recycleview的滑动而滑动
     */
    public DividerItemDecoration(Context context, int middleLeftMargin, int middleRightMargin, Drawable drawable, Boolean topShow, Boolean bottomShow, int color, int itemColor, int headCut, int tailCut) {
        this.middleLeftMargin = middleLeftMargin;
        this.middleRightMargin = middleRightMargin;
        this.drawable = drawable;
        this.topShow = topShow;
        this.bottomShow = bottomShow;
        this.color = color;
        this.headCut = headCut;
        this.tailCut = tailCut;
        this.itemColor = itemColor;


        if (drawable != null) {
            mDivider = drawable;
        } else {
            mDivider = context.getResources().getDrawable(R.drawable.divider);
        }

        mDividerItem = context.getResources().getDrawable(R.drawable.divider_item);

        if (this.itemColor > 0) {
            ((GradientDrawable) mDividerItem).setColor(context.getResources().getColor(color));
        } else {
            ((GradientDrawable) mDividerItem).setColor(context.getResources().getColor(R.color.white));
        }


//        mDivider.setColorFilter(, PorterDuff.Mode.DST_OVER);
        if (color > 0) {
            ((GradientDrawable) mDivider).setColor(context.getResources().getColor(color));
        }

    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent) {
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        int left = 0;
        int right = 0;
        final int childCount = parent.getChildCount();
        int allCount = parent.getAdapter().getItemCount();
        if(allCount == 1){//只有一条的时候
            final View child = parent.getChildAt(0);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();

            //画最后一根长线
            if (isLastPosition(child, parent) && bottomShow) {
                left = parent.getPaddingLeft();
                right = parent.getWidth() - parent.getPaddingRight();
                final int top = child.getBottom() + params.bottomMargin +
                        Math.round(ViewCompat.getTranslationY(child));
                final int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }


            // 画第一个项目头上的线
            if (isFirstPosition(child, parent) && topShow) {
                //left 减去边距
                left = parent.getPaddingLeft();
                right = parent.getWidth() - parent.getPaddingRight();

                int bottom = child.getTop() - params.topMargin +
                        Math.round(ViewCompat.getTranslationY(child));

                int top = bottom - mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }

        }else {
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                int tmpRightMargin = parent.getPaddingRight() + middleRightMargin;

                //画最后一根长线
                if (isLastPosition(child, parent)) {
                    left = parent.getPaddingLeft();
                    right = parent.getWidth() - parent.getPaddingRight();
                    final int top = child.getBottom() + params.bottomMargin +
                            Math.round(ViewCompat.getTranslationY(child));
                    if (!bottomShow) {
                        continue;
                    }
                    final int bottom = top + mDivider.getIntrinsicHeight();
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);

                    return; //会直接跳出循环。
                }

                //left 减去边距
                left = parent.getPaddingLeft() + middleLeftMargin;
                right = parent.getWidth() - tmpRightMargin;


                //画了中间线
                int top = child.getBottom() + params.bottomMargin +
                        Math.round(ViewCompat.getTranslationY(child));
                int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);


                if (left > 0) {//左边画一条　跟 itemview 一样的颜色填充,长度为 左边距(比如UI规定的15dp)
                    mDividerItem.setBounds(0, top, left, bottom);
                    mDividerItem.draw(c);
                }

                if (tmpRightMargin > 0) {//右边边画一条　跟 itemview 一样的颜色填充,长度为 右边距(比如UI规定的15dp)
                    mDividerItem.setBounds(right, top, right + tmpRightMargin, bottom);
                    mDividerItem.draw(c);
                }


                // 画第一个项目头上的线
                if (isFirstPosition(child, parent)) {
                    //left 减去边距
                    left = parent.getPaddingLeft();
                    right = parent.getWidth() - parent.getPaddingRight();

                    bottom = child.getTop() - params.topMargin +
                            Math.round(ViewCompat.getTranslationY(child));
                    if (!topShow) {
                        continue;
                    }

                    top = bottom - mDivider.getIntrinsicHeight();
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }
            }
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin +
                    Math.round(ViewCompat.getTranslationX(child));
            final int right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if (mOrientation == VERTICAL_LIST) {//
            int allCount = parent.getAdapter().getItemCount();
            if (allCount == 1) {

                if (isLastPosition(view, parent)) {

                    if (tailCut > 0) {//recycleview 跟底部的　间距
                        outRect.bottom = tailCut;
                    } else if (mDivider != null) {
                        outRect.bottom = bottomShow ? mDivider.getIntrinsicHeight() : 0;

                        Log.e("bottom", outRect.bottom + "");
                    }
                }

                if (isFirstPosition(view, parent)) {
                    if (headCut > 0) {//recycleview 跟顶部的　间距
                        outRect.top = headCut;
                    } else {
                        if (mDivider != null) {
                            outRect.top = topShow ? mDivider.getIntrinsicHeight() : 0;
                        }
                    }

                }

            } else {
                if (isLastPosition(view, parent)) {

                    if (tailCut > 0) {//recycleview 跟底部的　间距
                        outRect.bottom = tailCut;
                    } else if (mDivider != null) {
                        outRect.bottom = bottomShow ? mDivider.getIntrinsicHeight() : 0;

                        Log.e("bottom", outRect.bottom + "");
                    }
                    return;
                }

                outRect.bottom = mDivider.getIntrinsicHeight();

                if (isFirstPosition(view, parent)) {
                    if (headCut > 0) {//recycleview 跟顶部的　间距
                        outRect.top = headCut;
                    } else {
                        if (mDivider != null) {
                            outRect.top = topShow ? mDivider.getIntrinsicHeight() : 0;
                        }
                    }

                }
            }
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }

        Log.e("test", mDivider.getIntrinsicHeight() + "");
    }

    private boolean isFirstPosition(View view, RecyclerView parent) {
        return parent.getChildAdapterPosition(view) == 0;
    }

    private boolean isLastPosition(View view, RecyclerView parent) {
        return parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1;
    }

    public static class Builder {
        private int middleLeftMargin;
        private int middleRightMargin;

        private int headCut = -1;//头部
        private int tailCut = -1;//脚部

        private int itemColor;//颜色

        private int      color;
        private Drawable drawable;
        private boolean  topShow;
        private boolean  bottomShow;
        Context context;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMiddleLeftMargin(int leftMargin) {
            this.middleLeftMargin = leftMargin;
            return this;
        }

        public Builder setMiddleRightMargin(int rightMargin) {
            this.middleRightMargin = rightMargin;
            return this;
        }

        public Builder showTopLine(boolean showTop) {
            this.topShow = showTop;
            return this;
        }

        public Builder showBottomLine(boolean showBottom) {
            this.bottomShow = showBottom;
            return this;
        }

        public Builder setLineBackGround(int color) {
            this.color = color;
            return this;
        }


        public Builder setHeadCut(int headCut) {
            this.headCut = headCut;
            return this;
        }

        public Builder setTailCut(int tailCut) {
            this.tailCut = tailCut;
            return this;
        }


        public Builder setItemColor(int itemColor) {
            this.itemColor = itemColor;
            return this;
        }


        public Builder setBackGroundDrawble(Drawable drawable) {
            this.drawable = drawable;
            return this;
        }

        public DividerItemDecoration build() {
            return new DividerItemDecoration(context, middleLeftMargin, middleRightMargin, drawable, topShow, bottomShow, color, itemColor, headCut, tailCut);
        }

    }

}