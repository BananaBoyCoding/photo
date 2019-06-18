package com.jiehun.component.widgets.recycleview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.jiehun.component.widgets.recycleview.adapter.viewholder.ItemViewDelegate;
import com.jiehun.component.widgets.recycleview.adapter.viewholder.ItemViewDelegateManager;
import com.jiehun.component.widgets.recycleview.adapter.viewholder.ViewRecycleHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyao on 16/4/9.
 */
public class MultiItemTypeRecyclerAdapter<T> extends RecyclerView.Adapter<ViewRecycleHolder> {
    protected Context mContext;
    protected List<T> mDatas = new ArrayList<>();

    protected ItemViewDelegateManager mItemViewDelegateManager;
    protected OnItemClickListener     mOnItemClickListener;
    private   OnAdapterClickListener  adapterListener;

    public MultiItemTypeRecyclerAdapter(Context context) {
        mContext = context;
        mItemViewDelegateManager = new ItemViewDelegateManager();
    }

    public MultiItemTypeRecyclerAdapter(Context context, List<T> datas) {
        mContext = context;
        mDatas = datas;
        mItemViewDelegateManager = new ItemViewDelegateManager();
    }

    @Override
    public int getItemViewType(int position) {
        if (!useItemViewDelegateManager()) return super.getItemViewType(position);
        return mItemViewDelegateManager.getItemViewType(mDatas.get(position), position);
    }


    @Override
    public ViewRecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewDelegate itemViewDelegate = mItemViewDelegateManager.getItemViewDelegate(viewType);
        int layoutId = itemViewDelegate.getItemViewLayoutId();
        ViewRecycleHolder holder = ViewRecycleHolder.createViewHolder(mContext, parent, layoutId);
        onViewHolderCreated(holder,holder.getConvertView());
        setListener(parent, holder, viewType);
        return holder;
    }

    public void onViewHolderCreated(ViewRecycleHolder holder, View itemView){

    }

    public void convert(ViewRecycleHolder holder, T t) {
        mItemViewDelegateManager.convert(holder, t, holder.getAdapterPosition());
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }


    protected void setListener(final ViewGroup parent, final ViewRecycleHolder viewHolder, int viewType) {
        if (!isEnabled(viewType)) return;
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(v, viewHolder , position);
                }
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    return mOnItemClickListener.onItemLongClick(v, viewHolder, position);
                }
                return false;
            }
        });
    }

    @Override
    public void onBindViewHolder(ViewRecycleHolder holder, int position) {
        convert(holder, mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        int itemCount = mDatas.size();
        return itemCount;
    }


    public List<T> getDatas() {
        return mDatas;
    }

    public MultiItemTypeRecyclerAdapter addItemViewDelegate(ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(itemViewDelegate);
        return this;
    }

    public MultiItemTypeRecyclerAdapter addItemViewDelegate(int viewType, ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(viewType, itemViewDelegate);
        return this;
    }

    protected boolean useItemViewDelegateManager() {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnAdapterItemListener(OnAdapterClickListener adapterListener) {
        this.adapterListener = adapterListener;
    }

    public OnAdapterClickListener getAdapterListener() {
        return adapterListener;
    }

    public void add(T item) {
        if (item == null) {
            return;
        }
        mDatas.add(item);
        notifyDataSetChanged();
    }

    /**
     * 增加第二种添加数据集的方式，来满足定位添加 - 个体
     */
    public void add(int location, T item) {
        if (item == null) {
            return;
        }
        mDatas.add(location, item);
        notifyDataSetChanged();
    }

    public void addAll(List<T> list) {
        if (list == null) {
            return;
        }
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    public List<T> getList() {
        return mDatas;
    }

    /**
     * 增加第二种添加数据集的方式，来满足定位添加 - 数组
     */
    public void addAll(int location, List<T> item) {
        if (item == null) {
            return;
        }
        mDatas.addAll(location, item);
        notifyDataSetChanged();
    }

    /**
     * 获取列表的指定索引区间
     */
    public List<T> subList(int start, int end) {
        if (start < 0) {
            start = 0;
        }
        if (end > mDatas.size()) {
            end = mDatas.size();
        }

        return mDatas.subList(start, end);
    }

    public void set(T oldElem, T newElem) {
        if (newElem == null) {
            return;
        }
        set(mDatas.indexOf(oldElem), newElem);
    }

    public void set(int index, T item) {
        if (item == null) {
            return;
        }
        mDatas.set(index, item);
        notifyDataSetChanged();
    }

    public void remove(T item) {
        if (item == null) {
            return;
        }
        mDatas.remove(item);
        notifyDataSetChanged();
    }

    public void remove(int index) {
        mDatas.remove(index);
        notifyDataSetChanged();
    }

    public void removeWithoutNotify(int index) {
        mDatas.remove(index);
    }

    public T getItem(int position){
        return getDatas().get(position);
    }

    public void replaceAll(List<T> item) {
        if (item == null) {
            clear();
            return;
        }
        mDatas.clear();
        mDatas.addAll(item);
        notifyDataSetChanged();
    }

    public void replaceAllNotClearWhenNull(List<T> item) {
        if (item == null) {
            return;
        }
        mDatas.clear();
        mDatas.addAll(item);
        notifyDataSetChanged();
    }

    public boolean contains(T item) {
        if (item == null) {
            return false;
        }
        return mDatas.contains(item);
    }

    /**
     * Clear data list
     */
    public void clear() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, RecyclerView.ViewHolder holder, int position);

        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);
    }

    public interface OnAdapterClickListener<T> {
        void onclick(int id, ViewRecycleHolder viewRecycleHolder);
    }
}
