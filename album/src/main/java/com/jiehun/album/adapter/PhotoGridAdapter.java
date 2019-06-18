package com.jiehun.album.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.jiehun.album.R;
import com.jiehun.album.entity.Photo;
import com.jiehun.album.entity.PhotoDirectory;
import com.jiehun.album.event.CheckSelectedNumberListener;
import com.jiehun.album.event.OnItemCheckListener;
import com.jiehun.album.event.OnPhotoClickListener;
import com.jiehun.album.utils.AndroidLifecycleUtils;
import com.jiehun.album.utils.MediaStoreHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by donglua on 15/5/31.
 */
public class PhotoGridAdapter extends SelectableAdapter<PhotoGridAdapter.PhotoViewHolder> {

    private RequestManager glide;

    private OnItemCheckListener onItemCheckListener = null;
    private OnPhotoClickListener onPhotoClickListener = null;
    private View.OnClickListener onCameraClickListener = null;
    private CheckSelectedNumberListener checkSelectedNumberListener = null;

    public final static int ITEM_TYPE_CAMERA = 100;
    public final static int ITEM_TYPE_PHOTO = 101;
    private final static int COL_NUMBER_DEFAULT = 3;

    private boolean hasCamera = true;
    private boolean previewEnable = true;

    private int imageSize;
    private int columnNumber = COL_NUMBER_DEFAULT;

    private int maxSelectedCount = 0;

    private int icon_selected[] = new int[]{R.drawable.album_icon_selected_1, R.drawable.album_icon_selected_2
            , R.drawable.album_icon_selected_3, R.drawable.album_icon_selected_4
            , R.drawable.album_icon_selected_5, R.drawable.album_icon_selected_6
            , R.drawable.album_icon_selected_7, R.drawable.album_icon_selected_8, R.drawable.album_icon_selected_9};


    public PhotoGridAdapter(Context context, RequestManager requestManager, List<PhotoDirectory> photoDirectories) {
        this.photoDirectories = photoDirectories;
        this.glide = requestManager;
        setColumnNumber(context, columnNumber);
    }

    public PhotoGridAdapter(Context context, RequestManager requestManager, List<PhotoDirectory> photoDirectories, ArrayList<String> orginalPhotos, int colNum,int maxSelectedCount) {
        this(context, requestManager, photoDirectories);
        setColumnNumber(context, colNum);
        this.maxSelectedCount = maxSelectedCount;
        selectedPhotos = new ArrayList<>();
        if (orginalPhotos != null) {
            selectedPhotos.addAll(orginalPhotos);
        }
    }

    public void setSelecedPhotos(ArrayList<String> orginalPhotos) {
        if(orginalPhotos!=null) {
            clearSelection();
            selectedPhotos.addAll(orginalPhotos);
            notifyDataSetChanged();
        }

    }

    private void setColumnNumber(Context context, int columnNumber) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        imageSize = widthPixels / columnNumber;
    }

    @Override
    public int getItemViewType(int position) {
        return (showCamera() && position == 0) ? ITEM_TYPE_CAMERA : ITEM_TYPE_PHOTO;
    }


    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picker_photo_layout, parent, false);
        final PhotoViewHolder holder = new PhotoViewHolder(itemView);

        return holder;
    }


    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, int position) {

        if (getItemViewType(position) == ITEM_TYPE_PHOTO) {

            List<Photo> photos = getCurrentPhotos();
            final Photo photo;

            if (showCamera()) {
                photo = photos.get(position - 1);
            } else {
                photo = photos.get(position);
            }

            boolean canLoadImage = AndroidLifecycleUtils.canLoadImage(holder.ivPhoto.getContext());

            if (canLoadImage) {
                final RequestOptions options = new RequestOptions();
                options.centerCrop()
                        .dontAnimate()
                        .override(imageSize, imageSize)
                        .placeholder(R.drawable.album__picker_ic_photo_black_48dp)
                        .error(R.drawable.album__picker_ic_broken_image_black_48dp);

                glide.setDefaultRequestOptions(options)
                        .load(new File(photo.getPath()))
                        .thumbnail(0.5f)
                        .into(holder.ivPhoto);
            }

            final boolean showOverlay = (getSelectedPhotos().size() == maxSelectedCount);

            if (showOverlay) {
                holder.vOverlay.setVisibility(View.VISIBLE);
            } else {
                holder.vOverlay.setVisibility(View.GONE);
            }


            final boolean isChecked = isSelected(photo);

            if (isChecked) {
                int index = getIndexInSelections(photo);
                holder.vSelected.setImageResource(icon_selected[index]);
                holder.vOverlay.setVisibility(View.GONE);
            } else {
                holder.vSelected.setImageResource(R.drawable.album_icon_selected_default);
            }
            holder.ivPhoto.setSelected(isChecked);

            holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onPhotoClickListener != null) {
                        int pos = holder.getAdapterPosition();
                        if (previewEnable) {
                            onPhotoClickListener.onClick(view, pos, showCamera());
                        } else {
                            holder.vSelected.performClick();
                        }
                    }
                }
            });
            holder.vSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = holder.getAdapterPosition();
                    boolean isEnable = true;

                    if (onItemCheckListener != null) {
                        isEnable = onItemCheckListener.onItemCheck(pos, photo,
                                getSelectedPhotos().size() + (isSelected(photo) ? -1 : 1));
                    }
                    if (isEnable) {
                        toggleSelection(photo);
                        notifyDataSetChanged();

                    }
                }
            });

        } else {
            if (getItemViewType(position) == ITEM_TYPE_CAMERA) {
                holder.vSelected.setVisibility(View.GONE);
                holder.ivPhoto.setScaleType(ImageView.ScaleType.CENTER);
                final boolean showOverlay = (getSelectedPhotos().size() == maxSelectedCount);
                if (showOverlay) {
                    holder.vOverlay.setVisibility(View.VISIBLE);
                } else {
                    holder.vOverlay.setVisibility(View.GONE);
                }

                holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onCameraClickListener != null && !showOverlay) {
                            onCameraClickListener.onClick(view);
                        }
                    }
                });
            }
            holder.ivPhoto.setImageResource(R.drawable.album_icon_camera);
        }
    }

    @Override
    public void toggleSelection(Photo photo) {
        super.toggleSelection(photo);
        if(checkSelectedNumberListener!=null) {
            checkSelectedNumberListener.notify(getSelectedPhotos().size());
        }
    }

    @Override
    public int getItemCount() {
        int photosCount =
                photoDirectories.size() == 0 ? 0 : getCurrentPhotos().size();
        if (showCamera()) {
            return photosCount + 1;
        }
        return photosCount;
    }


    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private ImageView vSelected;
        private View vOverlay;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            vSelected = (ImageView) itemView.findViewById(R.id.v_selected);
            vOverlay = itemView.findViewById(R.id.view_overlay);
        }
    }


    public void setOnItemCheckListener(OnItemCheckListener onItemCheckListener) {
        this.onItemCheckListener = onItemCheckListener;
    }


    public void setOnPhotoClickListener(OnPhotoClickListener onPhotoClickListener) {
        this.onPhotoClickListener = onPhotoClickListener;
    }


    public void setOnCameraClickListener(View.OnClickListener onCameraClickListener) {
        this.onCameraClickListener = onCameraClickListener;
    }

    public void setCheckSelectedNumberListener(CheckSelectedNumberListener checkSelectedNumberListener) {
        this.checkSelectedNumberListener = checkSelectedNumberListener;
    }


    public ArrayList<String> getSelectedPhotoPaths() {
        ArrayList<String> selectedPhotoPaths = new ArrayList<>(getSelectedItemCount());

        for (String photo : selectedPhotos) {
            selectedPhotoPaths.add(photo);
        }

        return selectedPhotoPaths;
    }


    public void setShowCamera(boolean hasCamera) {
        this.hasCamera = hasCamera;
    }

    public void setPreviewEnable(boolean previewEnable) {
        this.previewEnable = previewEnable;
    }

    public boolean showCamera() {
        return (hasCamera && currentDirectoryIndex == MediaStoreHelper.INDEX_ALL_PHOTOS);
    }

    @Override
    public void onViewRecycled(PhotoViewHolder holder) {
        glide.clear(holder.ivPhoto);
        super.onViewRecycled(holder);
    }
}
