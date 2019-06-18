package com.jiehun.component.widgets.recycleview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jiehun.component.config.BaseLibConfig;
import com.jiehun.component.utils.AbPreconditions;
import com.jiehun.component.widgets.recycleview.itemdecorator.DividerItemDecoration;
import com.jiehun.component.widgets.recycleview.itemdecorator.SpaceLayoutDecoration;

/**
 * Created by zhouyao on 16-11-24.
 * Recyclerview 的构造类 包含设置行间距;设置垂直、水平、表格模式;添加头部脚部;
 * 使用案例
 * <p>
 * <p>
 * RecyclerBuild mRecyclerBuild = new RecyclerBuild(xRecyclerView)
 * .setLinerLayout(true) //或者.setGridLayout(3)
 * .bindAdapter(mCustomerDetailAdapter, true)//如果有下拉刷新的，或者加载头部脚部的
 * .addHeadView(item_custom_detail).addFootView(include_demand_housesource)
 * .setItemSpace(AbScreenUtil.dip2px(10));//
 * <p>
 * <p>
 * RecyclerBuild mRecyclerBuild = new RecyclerBuild(mRecyclerView1)
 * .setGridLayoutNoScroll(2)
 * .bindAdapter(commonAdapter, true)
 * .addHeadView(header1).addFootView(footer1)
 * .setItemSpace(AbScreenUtil.dip2px(10))
 * .reLayoutGridHeaderView();
 */

public class RecyclerBuild {

    private RecyclerView xRecyclerView;//列表

    public RecyclerBuild(RecyclerView xRecyclerView) {
        this.xRecyclerView = xRecyclerView;
    }

    /**
     * 设置线性布局
     *
     * @param isVertial 垂直，水平
     */
    public RecyclerBuild setLinerLayout(boolean isVertial) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseLibConfig.getContext());
        if (isVertial) {
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        } else {
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        }
        xRecyclerView.setLayoutManager(linearLayoutManager);
        return this;
    }

    /**
     * 设置表格列数
     *
     * @param columnCount
     * @return
     */
    public RecyclerBuild setGridLayout(int columnCount) {
        xRecyclerView.setLayoutManager(new GridLayoutManager(BaseLibConfig.getContext(), columnCount));
        return this;
    }

    /**
     * 让recycleview 所有item都显示
     *
     * @param columnCount
     * @return
     */
    public RecyclerBuild setGridLayoutNoScroll(int columnCount) {
        xRecyclerView.setLayoutManager(new GridLayoutManager(BaseLibConfig.getContext(), columnCount) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        return this;
    }

    /**
     * 让recycleview 所有item都显示
     *
     * @return
     */
    public RecyclerBuild setLinearLayouNoScroll() {
        xRecyclerView.setLayoutManager(new LinearLayoutManager(BaseLibConfig.getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        return this;
    }

    public RecyclerBuild setHorizontalLinearLayouNoScroll() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseLibConfig.getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        xRecyclerView.setLayoutManager(linearLayoutManager);
        return this;
    }

    /**
     * 绑定adapter
     *
     * @param mAdapter
     * @param enableHeaderOrFoot
     * @return
     */
    public RecyclerBuild bindAdapter(RecyclerView.Adapter mAdapter, boolean enableHeaderOrFoot) {
        if (enableHeaderOrFoot) {
            RecyclerAdapterWithHF mRecyclerAdapterWithHF = new RecyclerAdapterWithHF(mAdapter);
            xRecyclerView.setAdapter(mRecyclerAdapterWithHF);
        } else {
            xRecyclerView.setAdapter(mAdapter);
        }
        return this;
    }


    public RecyclerBuild setOnItemClickLis(RecyclerAdapterWithHF.OnItemClickListener mOnItemClickListener) {
        getRecyclerAdapterWithHF().setOnItemClickListener(mOnItemClickListener);
        return this;
    }

    /**
     * 长按监听， 设置了RecyclerAdapterWithHF， RecyclerView必须用这个设置长按
     *
     * @return
     */
    public RecyclerBuild setOnItemLongClickLis(RecyclerAdapterWithHF.OnItemLongClickListener mOnItemLongClickListener) {
        getRecyclerAdapterWithHF().setOnItemLongClickListener(mOnItemLongClickListener);
        return this;
    }


    /**
     * 给recylerview 添加头部
     *
     * @param headView
     * @return
     */
    public RecyclerBuild addHeadView(View headView) {
        getRecyclerAdapterWithHF().addHeader(headView);
        return this;
    }

    public RecyclerBuild addHeadView(View headView, int size) {
        getRecyclerAdapterWithHF().addHeader(headView,size);
        return this;
    }


    public void removeHeaderView(View headerView) {
        getRecyclerAdapterWithHF().removeHeader(headerView);
    }

    public boolean containsHeader(View headerView) {
        return getRecyclerAdapterWithHF().containsHeader(headerView);
    }

    public int getHeaderSize() {
        return getRecyclerAdapterWithHF().getHeadSize();
    }

    /**
     * @param footer 添加尾部
     * @return
     */
    public RecyclerBuild addFootView(View footer) {
        getRecyclerAdapterWithHF().addFooter(footer);
        return this;
    }

    public void removeFooterView(View footer) {
        getRecyclerAdapterWithHF().removeFooter(footer);
    }


    /**
     * 特殊情况的时候设置间距
     *
     * @param ItemDecoration 参考 SpaceLayoutDecoration
     * @return
     */
    public RecyclerBuild setItemSpace(RecyclerView.ItemDecoration ItemDecoration) {
        this.xRecyclerView.addItemDecoration(ItemDecoration);
        return this;
    }


    /**
     * 一.有上下拉加载更多的情况
     * 　　下拉刷新转动条
     * itemSpace
     * 加载更多
     * <p>
     * 二.没有上下拉加载更多的情况
     * itemSpace
     * <p>
     * 设置间距
     *
     * @param itemSpace
     * @return
     */
    public RecyclerBuild setItemSpace(int itemSpace) {
        setItemSpace(itemSpace, -1, -1);
        return this;
    }

    public RecyclerBuild setItemSpaceWithMargin(int leftMargin) {
        setItemSpaceWithMargin(leftMargin, 0, -1, -1, false);
        return this;
    }

    public RecyclerBuild setItemSpaceWithMargin(int leftMargin, int headCut, int tailCut) {
        setItemSpaceWithMargin(leftMargin, 0, headCut, tailCut, false);
        return this;
    }

    public RecyclerBuild setItemSpaceWithMargin(int leftMargin, int rightMargin, int headCut, int tailCut, boolean isShowTopLine) {

        AbPreconditions.checkArgument(getLayoutManager() instanceof LinearLayoutManager);
        AbPreconditions.checkArgument(((LinearLayoutManager) getLayoutManager()).getOrientation() == LinearLayoutManager.VERTICAL);
        xRecyclerView.addItemDecoration(new DividerItemDecoration.Builder(xRecyclerView.getContext())
                .setHeadCut(headCut)
                .setTailCut(tailCut)
                .setMiddleRightMargin(rightMargin)
                .showTopLine(isShowTopLine)
                .setMiddleLeftMargin(leftMargin).build());
        return this;
    }


    /**
     * 一.有上下拉加载更多的情况
     * 　　下拉刷新转动条
     * headSpace
     * itemSpace
     * 加载更多
     * 二.没有上下拉加载更多的情况
     * headSpace
     * itemSpace
     * <p>
     * 设置间距
     *
     * @param itemSpace
     * @param headSpace 头部跟上面的间距
     * @return
     */
    public RecyclerBuild setItemSpace(int itemSpace, int headSpace) {
        setItemSpace(itemSpace, headSpace, -1);
        return this;
    }

    /**
     * 一.有上下拉加载更多的情况
     * 　　下拉刷新转动条
     * headSpace
     * itemSpace
     * 加载更多
     * tailSpace
     * 二.没有上下拉加载更多的情况
     * headSpace
     * itemSpace
     * tailSpace
     * <p>
     * 设置间距
     *
     * @param itemSpace
     * @param headSpace 头部跟上面的间距
     * @param tailSpace 脚部与下面的间距
     * @return
     */
    public RecyclerBuild setItemSpace(int itemSpace, int headSpace, int tailSpace) {
        if (getLayoutManager() instanceof GridLayoutManager) {
            int headSpaceTmp = 0;
            int tailSpaceTmp = 0;

            if(headSpace > 0){
                headSpaceTmp = headSpace;
            }
            if(tailSpace > 0){
                tailSpaceTmp = tailSpace;
            }

            xRecyclerView.addItemDecoration(new SpaceLayoutDecoration
                    .Builder(SpaceLayoutDecoration.GRID_LAYOUT)
                    .span(((GridLayoutManager) getLayoutManager())
                            .getSpanCount())
                    .rowSpace(itemSpace)
                    .columnSpace(itemSpace)
                    .headCut(headSpaceTmp)
                    .tailCut(tailSpaceTmp).leftCut(0).rightCut(0)
                    .build());
        } else if (getLayoutManager() instanceof LinearLayoutManager) {
            if (((LinearLayoutManager) getLayoutManager()).getOrientation() == LinearLayoutManager.VERTICAL) {
                xRecyclerView.addItemDecoration(new SpaceLayoutDecoration
                        .Builder(SpaceLayoutDecoration.LINEAR_LAYOUT_VERTICAL)
                        .headCut(headSpace)
                        .linearSpace(
                                itemSpace)
                        .tailCut(tailSpace).build());
            } else if (((LinearLayoutManager) getLayoutManager()).getOrientation() == LinearLayoutManager.HORIZONTAL) {
                xRecyclerView.addItemDecoration(new SpaceLayoutDecoration
                        .Builder(SpaceLayoutDecoration.LINEAR_LAYOUT_HORIZONTAL)
                        .headCut(headSpace)
                        .linearSpace(
                                itemSpace)
                        .tailCut(tailSpace).build());
            }
        }
        return this;
    }

    /**
     * 调整Grid RecyclerView header footer占用多列
     *
     * @return
     */
    public RecyclerBuild reLayoutGridHeaderView() {
        AbPreconditions.checkArgument(getLayoutManager() instanceof GridLayoutManager, "reLayoutGridHeaderView only used by grid recyclerview!");
        final RecyclerAdapterWithHF adapter = getRecyclerAdapterWithHF();
        final GridLayoutManager manager = (GridLayoutManager) getLayoutManager();
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (adapter.getItemViewType(position) == RecyclerAdapterWithHF.TYPE_HEADER
                        || adapter.getItemViewType(position) == RecyclerAdapterWithHF.TYPE_FOOTER) ? manager.getSpanCount() : 1;
            }
        });
        return this;
    }


    public RecyclerView.LayoutManager getLayoutManager() {
        return AbPreconditions.checkNotNullThrow(this.xRecyclerView.getLayoutManager(), "RecyclerView need setLayoutManager first!");
    }

    public RecyclerAdapterWithHF getRecyclerAdapterWithHF() {
        AbPreconditions.checkNotNullThrow(xRecyclerView.getAdapter());
        AbPreconditions.checkArgument(xRecyclerView.getAdapter() instanceof RecyclerAdapterWithHF, "RecyclerView With Head need wrap RecyclerAdapterWithHF");
        return (RecyclerAdapterWithHF) xRecyclerView.getAdapter();
    }

}
