package com.jiehun.album.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.jiehun.album.R;
import com.jiehun.album.utils.AndroidLifecycleUtils;
import com.jiehun.component.utils.AbDisplayUtil;
import com.jiehun.component.widgets.recycleview.adapter.CommonRecyclerViewAdapter;
import com.jiehun.component.widgets.recycleview.adapter.viewholder.ViewRecycleHolder;

import java.io.File;
import java.util.List;

/**
 * Created by zg on 2017/12/25.
 */

public class PreviewGridAdapter extends CommonRecyclerViewAdapter<String> {
    private RequestManager glide;
    private int imageSize = 0;
    private int seletedPositon = -1;
    private List<Boolean> selectedStates;

    public PreviewGridAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    public PreviewGridAdapter(Context context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);

        glide = Glide.with(context);
        imageSize = AbDisplayUtil.dip2px(60);
    }

    public void setSelectedStates(List<Boolean> selectedStates) {
        this.selectedStates = selectedStates;
    }

    @Override
    protected void convert(ViewRecycleHolder holder, String s, int position) {
        ImageView iv_photo = holder.getView(R.id.iv_photo);
        View vOverLay = holder.getView(R.id.view_overlay);

        if(selectedStates!=null&&!selectedStates.get(position)) {
            vOverLay.setVisibility(View.VISIBLE);
        } else {
            vOverLay.setVisibility(View.GONE);
        }

        boolean canLoadImage = AndroidLifecycleUtils.canLoadImage(iv_photo.getContext());

        if (canLoadImage) {
            final RequestOptions options = new RequestOptions();
            options.centerCrop()
                    .dontAnimate()
                    .override(imageSize, imageSize)
                    .placeholder(R.drawable.album__picker_ic_photo_black_48dp)
                    .error(R.drawable.album__picker_ic_broken_image_black_48dp);

            glide.setDefaultRequestOptions(options)
                    .load(new File(s))
                    .thumbnail(0.5f)
                    .into(iv_photo);

            if (seletedPositon == position) {
                iv_photo.setSelected(true);
            } else {
                iv_photo.setSelected(false);
            }
        }
    }

    public void setSelectedPostion(int position) {
        seletedPositon = position;
        notifyDataSetChanged();
    }
}
