package com.wyx.photo.view;

import android.content.Context;
import android.support.annotation.NonNull;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.jiehun.album.adapter.PhotoSelectedAdapter;
import com.jiehun.component.base.BaseActivity;
import com.jiehun.component.widgets.recycleview.adapter.CommonRecyclerViewAdapter;
import com.jiehun.component.widgets.recycleview.adapter.viewholder.ViewRecycleHolder;
import com.wyx.photo.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PhotoGridView extends RelativeLayout implements CommonRecyclerViewAdapter.DelImage {
    private List<String> allPath = new ArrayList<>();
    private Context context;
    private RecyclerView rv;
    private List<String> paths;
    private int count = 0;
    private PhotoSelectedAdapter photoSelectedAdapter;


    public PhotoGridView(Context context) {
        super(context);
        init(context);
    }

    public PhotoGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PhotoGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.photo_grid_layout, this);
        rv = findViewById(R.id.rv);
        allPath.clear();
        if (context instanceof BaseActivity) {
            photoSelectedAdapter = new PhotoSelectedAdapter((BaseActivity) context, 9, false);
//            RecyclerBuild recyclerBuild = new RecyclerBuild(rv);
//            recyclerBuild
//                    .setGridLayout(4)
//                    .setItemSpace(AbDisplayUtil.dip2px(5))
//                    .bindAdapter(photoSelectedAdapter, false);
            initDrag();
            photoSelectedAdapter.setDelImageInterface(this);

        }
    }

    public void setPaths(List<String> paths) {
        count++;
        if (context instanceof BaseActivity && paths != null) {
            allPath.addAll(paths);
            photoSelectedAdapter.setPhotoSelectedData(allPath, count);
        }

    }

    @Override
    public void delImage(int positon) {
        if (allPath != null && allPath.size() > 0) {
            allPath.remove(positon);
        }
    }

    private void initDrag() {
        rv.setLayoutManager(new GridLayoutManager(context, 4));
        rv.setAdapter(photoSelectedAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags;
                int swipeFlags;
                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                    swipeFlags = 0;
                } else {
                    dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    swipeFlags = 0;
                }
                return makeMovementFlags(dragFlags, swipeFlags);

            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                //得到拖动ViewHolder的position
                int fromPosition = viewHolder.getAdapterPosition();
                //得到目标ViewHolder的position
                int toPosition = target.getAdapterPosition();
                if (fromPosition < toPosition) {
                    if (toPosition != allPath.size()) {
                        for (int i = fromPosition; i < toPosition; i++) {
                            Collections.swap(allPath, i, i + 1);
                        }
                        photoSelectedAdapter.notifyItemMoved(fromPosition, toPosition);
                    }

                } else {
                    if (toPosition != allPath.size()) {
                        for (int i = fromPosition; i > toPosition; i--) {
                            Collections.swap(allPath, i, i - 1);
                        }
                        photoSelectedAdapter.notifyItemMoved(fromPosition, toPosition);
                    }

                }


                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                Log.e("yy", "----------onSwiped");
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if (viewHolder instanceof ViewRecycleHolder) {
                    Log.e("yy", "----------onSelectedChanged2");
                    photoSelectedAdapter.endAnim((ViewRecycleHolder) viewHolder);
                }
                super.clearView(recyclerView, viewHolder);
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                // We only want the active item to change
                if (viewHolder instanceof ViewRecycleHolder) {
                    if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                        Log.e("yy", "----------onSelectedChanged1");
                        photoSelectedAdapter.startAnim((ViewRecycleHolder) viewHolder);
                    }
                }


                super.onSelectedChanged(viewHolder, actionState);
            }
        });
        itemTouchHelper.attachToRecyclerView(rv);
    }


}
