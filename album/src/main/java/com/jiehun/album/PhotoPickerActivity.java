package com.jiehun.album;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.jiehun.album.adapter.AlbumListAdapter;
import com.jiehun.album.adapter.PhotoGridAdapter;
import com.jiehun.album.entity.Photo;
import com.jiehun.album.entity.PhotoDirectory;
import com.jiehun.album.event.CheckSelectedNumberListener;
import com.jiehun.album.event.OnItemCheckListener;
import com.jiehun.album.event.OnPhotoClickListener;
import com.jiehun.album.utils.ImageCaptureManager;
import com.jiehun.album.utils.MediaStoreHelper;
import com.jiehun.album.utils.PhotoTools;
import com.jiehun.album.vo.AlbumInfo;
import com.jiehun.component.base.BaseActivity;
import com.jiehun.component.rxpermission.AbRxPermission;
import com.jiehun.component.rxpermission.RxCallBack;
import com.jiehun.component.utils.AbDisplayUtil;
import com.jiehun.component.utils.AbPreconditions;
import com.jiehun.component.widgets.recycleview.RecyclerBuild;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.jiehun.album.PhotoPickerConfig.DEFAULT_COLUMN_NUMBER;
import static com.jiehun.album.PhotoPickerConfig.DEFAULT_MAX_COUNT;
import static com.jiehun.album.PhotoPickerConfig.EXTRA_GRID_COLUMN;
import static com.jiehun.album.PhotoPickerConfig.EXTRA_MAX_COUNT;
import static com.jiehun.album.PhotoPickerConfig.EXTRA_ORIGINAL_PHOTOS;
import static com.jiehun.album.PhotoPickerConfig.EXTRA_PREVIEW_ENABLED;
import static com.jiehun.album.PhotoPickerConfig.EXTRA_SHOW_CAMERA;
import static com.jiehun.album.PhotoPickerConfig.EXTRA_SHOW_GIF;
import static com.jiehun.album.utils.MediaStoreHelper.INDEX_ALL_PHOTOS;

/**
 * @author zg
 * @date 2017/12/20
 */
public class PhotoPickerActivity extends BaseActivity implements AlbumListAdapter.ClickListener {

    private ImageView ivClose;
    private TextView  tvDone;

    private ImageCaptureManager captureManager;
    private RecyclerView recyclerView;
    private PhotoGridAdapter    photoGridAdapter;

    Bundle mediaStoreArgs;

    private boolean showGif        = false;
    private boolean showCamera     = false;
    private boolean previewEnabled = false;

    private int maxSelectedCount;
    private int columnNumber;

    private ArrayList<String> originalPhotos = null;
    private List<PhotoDirectory> directories,allPhone,allDir;

    private RequestManager mGlideRequestManager;

    private List<PhotoDirectory> albumInfo = new ArrayList<>();
    private AlbumListAdapter albumListAdapter;
    private RecyclerView list_albums;
    private RelativeLayout fl_list_albums;
    private int tag = 0;


    @Override
    public int layoutId() {
        return R.layout.activity_pick_layout;
    }


    @Override
    public void initViews(Bundle savedInstanceState) {
        showCamera = getIntent().getBooleanExtra(EXTRA_SHOW_CAMERA, true);
        showGif = getIntent().getBooleanExtra(EXTRA_SHOW_GIF, false);
        previewEnabled = getIntent().getBooleanExtra(EXTRA_PREVIEW_ENABLED, true);

        maxSelectedCount = getIntent().getIntExtra(EXTRA_MAX_COUNT, DEFAULT_MAX_COUNT);
        columnNumber = getIntent().getIntExtra(EXTRA_GRID_COLUMN, DEFAULT_COLUMN_NUMBER);

        originalPhotos = getIntent().getStringArrayListExtra(EXTRA_ORIGINAL_PHOTOS);


        ivClose = findViewById(R.id.tv_close);
        tvDone = findViewById(R.id.tv_done);
        list_albums = findViewById(R.id.list_albums);
        fl_list_albums = findViewById(R.id.fl_list_albums);
        captureManager = new ImageCaptureManager(this);
        mGlideRequestManager = Glide.with(this);

        recyclerView = findViewById(R.id.rv_photos);
        RecyclerBuild recyclerBuild = new RecyclerBuild(recyclerView);
        recyclerBuild.setGridLayout(columnNumber).setItemSpace(AbDisplayUtil.dip2px(3),AbDisplayUtil.dip2px(3),AbDisplayUtil.dip2px(3));


        directories = new ArrayList<>();
        allPhone = new ArrayList<>();
        photoGridAdapter = new PhotoGridAdapter(this, mGlideRequestManager, directories, originalPhotos, columnNumber, maxSelectedCount);
        photoGridAdapter.setShowCamera(showCamera);
        photoGridAdapter.setPreviewEnable(previewEnabled);

        recyclerView.setAdapter(photoGridAdapter);
        updateDone();
        albumListAdapter = new AlbumListAdapter(this,allPhone,this);
        new RecyclerBuild(list_albums)
                .bindAdapter(albumListAdapter, true)
                .setLinerLayout(true);

        addListener();


        mediaStoreArgs = new Bundle();
        mediaStoreArgs.putBoolean(EXTRA_SHOW_GIF, showGif);
        MediaStoreHelper.getPhotoDirs(this, mediaStoreArgs, new MediaStoreHelper.PhotosResultCallback() {
            @Override
            public void onResultCallback(List<PhotoDirectory> dir) {
                if(tag == 0){
                    directories.clear();
                    directories.addAll(dir);
                    allPhone.clear();
                    allPhone.addAll(dir);
                    allDir = dir;
                    photoGridAdapter.notifyDataSetChanged();
                    albumListAdapter.notifyDataSetChanged();
                    tag = 1;
                }

            }
        });

        initListener();
    }


    private void addListener() {
        photoGridAdapter.setOnCameraClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(PhotoPickerActivity.this,Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(PhotoPickerActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        1);
                }else {
                    openCamera();
                }
            }
        });


        photoGridAdapter.setOnItemCheckListener(new OnItemCheckListener() {
            @Override
            public boolean onItemCheck(int position, Photo photo, int selectedItemCount) {
                if (maxSelectedCount <= 1) {
                    List<String> photos = photoGridAdapter.getSelectedPhotos();
                    if (!photos.contains(photo.getPath())) {
                        photos.clear();
                        photoGridAdapter.notifyDataSetChanged();
                    }
                    return true;
                }

                if (selectedItemCount > maxSelectedCount) {
                    return false;
                }
                return true;
            }
        });

        photoGridAdapter.setCheckSelectedNumberListener(new CheckSelectedNumberListener() {
            @Override
            public void notify(int num) {
                updateDone();
            }
        });

        photoGridAdapter.setOnPhotoClickListener(new OnPhotoClickListener() {
            @Override
            public void onClick(View v, int position, boolean showCamera) {
                PhotoPreview.builder()
                        .setPhotos((ArrayList<String>) directories.get(0).getPhotoPaths())
                        .setSelectedPhotos((ArrayList<String>) photoGridAdapter.getSelectedPhotos())
                        .setPhotoMaxCount(maxSelectedCount)
                        .setCurrentItem(showCamera ? position - 1 : position).start(PhotoPickerActivity.this);
            }
        });
    }


    private void openCamera() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);   //拍照界面的隐式意图
        startActivityForResult(intent,200);
//        try {
//            Intent intent = captureManager.dispatchTakePictureIntent();
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ActivityNotFoundException e) {
//            // TODO No Activity Found to handle Intent
//            e.printStackTrace();
//        }
    }

    @Override
    public void initData() {

    }


    private void initListener() {
        findViewById(R.id.tv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.tv_done).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(PhotoPickerConfig.KEY_SELECTED_PHOTOS, (Serializable) photoGridAdapter.getSelectedPhotos());
                Log.e("yy","0-0--------------->"+photoGridAdapter.getSelectedPhotos().size());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AbPreconditions.checkNotEmptyList(photoGridAdapter.getSelectedPhotoPaths())) {
                    PhotoPreview.builder()
                            .setPhotos((ArrayList<String>) photoGridAdapter.getSelectedPhotos())
                            .setSelectedPhotos((ArrayList<String>) photoGridAdapter.getSelectedPhotos())
                            .setPhotoMaxCount(maxSelectedCount)
                            .setOnlyPreviewSelectedPhotos(true)
                            .setCurrentItem(0).start(PhotoPickerActivity.this);
                }
            }
        });
        findViewById(R.id.btnChoose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fl_list_albums.getVisibility() == View.VISIBLE){
                    Animation animation = AnimationUtils.loadAnimation(mContext,
                            R.anim.anim_exit_from_bottom);
                    fl_list_albums.startAnimation(animation);
                    fl_list_albums.setVisibility(View.GONE);
                }else {
                    Animation animation = AnimationUtils.loadAnimation(mContext,
                            R.anim.anim_enter_from_bottom);
                    fl_list_albums.startAnimation(animation);
                    fl_list_albums.setVisibility(View.VISIBLE);
                }

            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImageCaptureManager.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Log.e("yy","1------------->");
            if (captureManager == null) {

                captureManager = new ImageCaptureManager(this);
            }

            captureManager.galleryAddPic();
            if (directories.size() > 0) {
                String path = captureManager.getCurrentPhotoPath();
                PhotoDirectory directory = directories.get(INDEX_ALL_PHOTOS);
                Photo photo = new Photo(path.hashCode(), path);
                directory.getPhotos().add(INDEX_ALL_PHOTOS, photo);
                directory.setCoverPath(path);
                photoGridAdapter.toggleSelection(photo);
                photoGridAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == PhotoPreview.REQUEST_CODE && data != null) {
            ArrayList<String> paths = data.getStringArrayListExtra(PhotoPreview.EXTRA_PHOTOS);
            Log.e("yy","2------------->"+paths.size());

            if (resultCode == PhotoPreview.BACK_RESULT_CODE) {
                photoGridAdapter.setSelecedPhotos(paths);
                updateDone();
            } else if (resultCode == PhotoPreview.DONE_RESULT_CODE) {
                Intent intent = new Intent();
                intent.putExtra(PhotoPickerConfig.KEY_SELECTED_PHOTOS, paths);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }


    /**
     * 更新选中图片数量
     */
    public void updateDone() {

        List<String> photos = photoGridAdapter.getSelectedPhotos();
        int size = photos == null ? 0 : photos.size();
        tvDone.setClickable(size > 0);
        if (size > 0) {
            tvDone.setBackgroundResource(R.color.done_bg_color);
        } else {
            tvDone.setBackgroundResource(R.color.nodone_bg_color);
        }
        if (maxSelectedCount > 1) {
            tvDone.setText(getString(R.string.__picker_done_with_count, size, maxSelectedCount));
        } else {
            tvDone.setText(getString(R.string.__picker_done));
        }

    }

    @Override
    public void itemClick(int i) {
        if(allPhone!=null){
            directories.clear();
            directories.add(allPhone.get(i));
            photoGridAdapter.notifyDataSetChanged();

        }

        Animation animation = AnimationUtils.loadAnimation(mContext,
                R.anim.anim_exit_from_bottom);
        fl_list_albums.startAnimation(animation);
        fl_list_albums.setVisibility(View.GONE);
//        startActivity(new Intent(PhotoPickerActivity.this,MyCeActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //权限获取成功
                openCamera();
            } else {

                if(!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                    //权限被拒绝并且勾选了不再询问按钮
                }else {
                    //权限被拒绝
                }
            }
        }
    }


}
