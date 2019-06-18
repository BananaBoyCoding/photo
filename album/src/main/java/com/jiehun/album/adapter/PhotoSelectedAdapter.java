package com.jiehun.album.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.jiehun.album.PhotoPickerConfig;
import com.jiehun.album.PhotoScanConfig;
import com.jiehun.album.R;
import com.jiehun.album.widget.SquareItemLayout;
import com.jiehun.component.baseglide.GlideUtils;
import com.jiehun.component.widgets.recycleview.adapter.CommonRecyclerViewAdapter;
import com.jiehun.component.widgets.recycleview.adapter.viewholder.ViewRecycleHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zg on 2018/1/9.
 */

public class PhotoSelectedAdapter extends CommonRecyclerViewAdapter<String> implements TAnimListener{
    private int maxCount = 1;
    private Activity mContext;
    private final String PHOTO_ICON = "photo";
    private boolean PHOTO_ICON_NOT_VISIBLE;

    private void addPhotoIcon(){
        if (mDatas != null && !mDatas.contains(PHOTO_ICON)&&!PHOTO_ICON_NOT_VISIBLE) {
                mDatas.add(PHOTO_ICON);
        }
    }

    public PhotoSelectedAdapter(Activity context, int maxCount) {
        super(context, R.layout.item_photoss_selected);
        this.maxCount = maxCount;
        mContext = context;
        addPhotoIcon();
    }

    public PhotoSelectedAdapter(Activity context, int maxCount, boolean photo_icon_visible) {
        super(context, R.layout.item_photoss_selected);
        this.maxCount = maxCount;
        this.PHOTO_ICON_NOT_VISIBLE = photo_icon_visible;
        mContext = context;
        addPhotoIcon();
    }

    @Override
    public void setPhotoSelectedData(List<String> list,int i) {
        if (list != null) {
            mDatas.clear();
            mDatas.addAll(list);
        }
        if (!PHOTO_ICON_NOT_VISIBLE)
            mDatas.add(PHOTO_ICON);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mDatas.size() < maxCount) {
            return mDatas.size();
        }
        return maxCount;
    }

    @Override
    protected void convert(ViewRecycleHolder holder, final String s, final int position) {
        final ImageView ivPhoto = holder.getView(R.id.iv_photo);
        ImageView ivDelete = holder.getView(R.id.iv_delete);
        final SquareItemLayout rlItem = holder.getView(R.id.rlItem);


        if (!s.equals(PHOTO_ICON)) {
            Log.e("yy","--------------------------->几次");
            ivPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ivDelete.setVisibility(View.VISIBLE);
            ivPhoto.setBackgroundResource(R.color.transparent);
            GlideUtils.loadImage(mContext, s, ivPhoto,R.drawable.album__picker_ic_photo_black_48dp);

            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatas.remove(position);
                    delImage.delImage(position);
                    notifyDataSetChanged();
                }
            });

            ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotoScanConfig.builder()
                            .setPhotoList((ArrayList<String>) getDatas())
                            .setCurPosition(position)
                            .start(mContext);
                }
            });
//            ivPhoto.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {

//                    return false;
//                }
//            });

        } else if (!PHOTO_ICON_NOT_VISIBLE) {
            Log.e("yy","--------------------------->摄像");
            ivPhoto.setScaleType(ImageView.ScaleType.CENTER);
            ivDelete.setVisibility(View.GONE);
            ivPhoto.setImageResource(R.drawable.album_icon_camera);
            ivPhoto.setBackgroundResource(R.color.cl_333333);

            ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotoPickerConfig.builder()
                            .setShowCamera(true)
                            .setPreviewEnabled(true)
                            .setGridColumnCount(4)
                            .setSelected((ArrayList<String>) getDatas())
                            .setPhotoCount(maxCount)
                            .start(mContext);
                }
            });

        }
        rlItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                scaleViewAnimation(rlItem, 1.2f);
                return true;
            }
        });

//        onScaleAnim(rlItem);

    }


    @Override
    public List<String> getDatas() {
        ArrayList<String> list = new ArrayList<>();
        if (!PHOTO_ICON_NOT_VISIBLE) {
            if (mDatas != null && mDatas.size() > 0) {
                for (int i = 0; i < mDatas.size() - 1; i++) {
                    list.add(mDatas.get(i));
                }
            }
        } else {
            if (mDatas != null && mDatas.size() > 0) {
                for (int i = 0; i < mDatas.size(); i++) {
                    list.add(mDatas.get(i));
                }
            }
        }
        return list;
    }

    @Override
    public void doPhotoIconClick() {
        PhotoPickerConfig.builder()
                .setShowCamera(true)
                .setPreviewEnabled(true)
                .setGridColumnCount(4)
                .setSelected((ArrayList<String>) getDatas())
                .setPhotoCount(maxCount)
                .start(mContext);
    }



    private void scaleViewAnimation(View view, float value) {
        view.animate().scaleX(value).scaleY(value).setDuration(80).start();
    }

    @Override
    public void startAnim(ViewRecycleHolder holder) {
        if(holder!=null){
            Log.e("yy","--------------放大开始");
//            scaleViewAnimation(holder.getView(R.id.rlItem),1.2f);
            onScaleAnim(holder.getView(R.id.rlItem),true);
        }

    }

    @Override
    public void endAnim(ViewRecycleHolder holder) {
        if(holder!=null){
            Log.e("yy","--------------放大结束");
//            scaleViewAnimation(holder.getView(R.id.rlItem),1f);
            onScaleAnim(holder.getView(R.id.rlItem),false);
        }
    }


    private void onScaleAnim(final View rlItem,boolean b){
        SpringSystem springSystem = SpringSystem.create();
        Spring spring = springSystem.createSpring();
        if(b){
            spring.setCurrentValue(1.0f);
        }else {
            spring.setCurrentValue(1.1f);
        }

        spring.setSpringConfig(new SpringConfig(50,5));
        spring.addListener(new SimpleSpringListener(){
            @Override
            public void onSpringUpdate(Spring spring) {
                super.onSpringUpdate(spring);
                float cv = (float) spring.getCurrentValue();
                rlItem.setScaleX(cv);
                rlItem.setScaleY(cv);
            }
        });
        if(b){
            spring.setEndValue(1.1f);
        }else {
            spring.setEndValue(1.0f);
        }

    }
}
