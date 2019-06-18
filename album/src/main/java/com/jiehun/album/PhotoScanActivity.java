package com.jiehun.album;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiehun.album.adapter.PhotoPagerAdapter;
import com.jiehun.album.utils.DownLoadHelper;
import com.jiehun.component.base.BaseActivity;

import java.util.ArrayList;

/**
 * Created by zg on 2018/1/4.
 */

public class PhotoScanActivity extends BaseActivity {

    private ArrayList<String> paths;
    private ViewPager mViewPager;
    private PhotoPagerAdapter mPagerAdapter;

    private int currentItem = 0;

    private ImageView ivBack;
    private TextView  tvTitle;
    private ImageView ivDownLoad;

    private DownLoadHelper mDownLoadHelper;

    @Override
    public int layoutId() {
        return R.layout.activity_photo_scan_layout;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        paths = new ArrayList<>();
        Intent bundle = getIntent();
        if (bundle != null) {
            ArrayList<String> pathArr = bundle.getStringArrayListExtra(PhotoScanConfig.EXTRA_PHOTO_LIST);
            paths.clear();
            if (pathArr != null) {
                paths.addAll(pathArr);
            }
            currentItem = bundle.getIntExtra(PhotoScanConfig.EXTRA_CURRENT_POSITION, PhotoScanConfig.DEFAULT_POSITION);
        }

        ivBack = findViewById(R.id.iv_close);
        tvTitle = findViewById(R.id.tv_title);
        ivDownLoad = findViewById(R.id.iv_download);

        mViewPager = findViewById(R.id.vp_photos);
        mPagerAdapter = new PhotoPagerAdapter(Glide.with(this), paths);

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(currentItem);
        mDownLoadHelper = new DownLoadHelper(this);
        updateTitle();
        checkDownLoadVisiable();
    }

    @Override
    public void initData() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                updateTitle();
                checkDownLoadVisiable();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mPagerAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivDownLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mDownLoadHelper.savePicture(PhotoScanActivity.this,paths.get(currentItem));
            }
        });
    }

    private void checkDownLoadVisiable() {
        ///storage/emulated/0/Huawei/MagazineUnlock/magazine-unlock-01-2.3.1380-6729bfe02b034aafbcb4f87f4ed8d32c.jpg
        if(paths.get(currentItem).startsWith("http")) {
            ivDownLoad.setVisibility(View.VISIBLE);
        } else {
            ivDownLoad.setVisibility(View.INVISIBLE);
        }
    }

    private void updateTitle() {
        tvTitle.setText(getString(R.string.__picker_preview_title, currentItem + 1, paths.size()));
    }
}
