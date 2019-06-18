package com.jiehun.album;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jiehun.album.adapter.PhotoSelectedAdapter;
import com.jiehun.album.utils.UploadImgUtil;
import com.jiehun.component.utils.AbDisplayUtil;
import com.jiehun.component.utils.AbLazyLogger;
import com.jiehun.component.widgets.recycleview.RecyclerBuild;
import com.jiehun.componentservice.base.JHBaseActivity;
import com.jiehun.componentservice.base.config.albummoudle.PhotoPickerConfig;
import com.jiehun.componentservice.base.config.albummoudle.PhotoScanConfig;
import com.jiehun.componentservice.base.config.webviewmoudle.WebViewConfig;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 图片选择器测试类
 *
 * @author zg
 * @date 2017/12/20
 */

public class MainActivity extends JHBaseActivity {
    private ArrayList<String> photos = new ArrayList<>();
    private RecyclerView         recyclerView;
    private PhotoSelectedAdapter photoSelectedAdapter;

    @Override
    public int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        photos.add("http://2.tthunbohui.cn/n/00405a_V00kE19DZI1tMfF8-t2000x1334-136479.jpg");
        photos.add("http://3.tthunbohui.cn/n/00405a_K00kJ19LMthtMfE8-t2000x1333-105306.jpg");
        photos.add("http://4.tthunbohui.cn/n/00805cmL00kS0kFEExtMfE8-t2000x1333-187e44.jpg");


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPickerConfig.builder()
                        .setPhotoCount(9)
                        .setGridColumnCount(4)
                        .setPreviewEnabled(true)
                        .start(MainActivity.this);
            }
        });

        findViewById(R.id.button_no_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                UploadImgUtil.uploadImgList(MainActivity.this, photoSelectedAdapter.getDatas());
            }
        });

        findViewById(R.id.button_one_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoScanConfig.builder().setCurPosition(0).setPhotoList(photos).start(MainActivity.this);
            }
        });

        findViewById(R.id.button_photo_gif).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPickerConfig.builder()
                        .setShowCamera(true)
                        .setShowGif(true)
                        .start(MainActivity.this);
            }
        });

        initRecylerview();
    }

    private void initRecylerview() {
        recyclerView = findViewById(R.id.recycler_view);
        photoSelectedAdapter = new PhotoSelectedAdapter(this, 9);
        RecyclerBuild recyclerBuild = new RecyclerBuild(recyclerView);
        recyclerBuild
                .setGridLayout(4)
                .setItemSpace(AbDisplayUtil.dip2px(5))
                .bindAdapter(photoSelectedAdapter, false);

    }


    @Override
    public void initData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PhotoPickerConfig.REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> paths = data.getStringArrayListExtra(PhotoPickerConfig.KEY_SELECTED_PHOTOS);
            photoSelectedAdapter.setPhotoSelectedData(paths);
        }
    }
}
