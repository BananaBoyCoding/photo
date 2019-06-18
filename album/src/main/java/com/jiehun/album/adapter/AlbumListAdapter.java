package com.jiehun.album.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.jiehun.album.R;
import com.jiehun.album.entity.PhotoDirectory;
import com.jiehun.album.vo.AlbumInfo;
import com.jiehun.component.utils.AbDisplayUtil;

import java.io.File;
import java.util.List;

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.ViewHolder> {
    private List<PhotoDirectory> albumInfos;
    private ClickListener clickListener;
    private RequestManager glide;
    private Context context;
    private int imageSize = 0;
    public AlbumListAdapter(Context context,List<PhotoDirectory> albumInfos,ClickListener clickListener){
        this.context = context;
        this.albumInfos = albumInfos;
        glide = Glide.with(context);
        imageSize = AbDisplayUtil.dip2px(60);
        this.clickListener = clickListener;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_albums,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        if(albumInfos!=null&&albumInfos.get(i)!=null){
            holder.name.setText(albumInfos.get(i).getName());
            if(albumInfos.get(i)!=null&&albumInfos.get(i).getPhotos()!=null){
                holder.tvNum.setText(albumInfos.get(i).getPhotos().size()+"");
            }
            final RequestOptions options = new RequestOptions();
            options.centerCrop()
                    .dontAnimate()
                    .override(imageSize, imageSize)
                    .placeholder(R.drawable.album__picker_ic_photo_black_48dp)
                    .error(R.drawable.album__picker_ic_broken_image_black_48dp);
            glide.setDefaultRequestOptions(options)
                    .load(albumInfos.get(i).getCoverPath())
                    .thumbnail(0.5f)
                    .into(holder.img_album_cover);
            holder.lLitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clickListener!=null){
                        clickListener.itemClick(i);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if(albumInfos == null){
            return 0;
        }
        return albumInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,tvNum;
        ImageView img_album_cover;
        LinearLayout lLitem;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_album_name);
            img_album_cover = itemView.findViewById(R.id.img_album_cover);
            lLitem = itemView.findViewById(R.id.LLitem);
            tvNum = itemView.findViewById(R.id.tvNum);

        }
    }

    public interface ClickListener{
        void itemClick(int i);
    }
}
