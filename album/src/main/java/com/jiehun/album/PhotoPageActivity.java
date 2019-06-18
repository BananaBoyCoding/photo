package com.jiehun.album;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiehun.album.adapter.PhotoPagerAdapter;
import com.jiehun.album.adapter.PreviewGridAdapter;
import com.jiehun.component.base.BaseActivity;
import com.jiehun.component.utils.AbDisplayUtil;
import com.jiehun.component.widgets.recycleview.RecyclerBuild;
import com.jiehun.component.widgets.recycleview.adapter.MultiItemTypeRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.jiehun.album.PhotoPreview.EXTRA_CURRENT_ITEM;
import static com.jiehun.album.PhotoPreview.EXTRA_PHOTOS;
import static com.jiehun.album.PhotoPreview.EXTRA_SELECTED_PHOTOS;

/**
 * Created by zg on 2017/12/25.
 */

public class PhotoPageActivity extends BaseActivity {

    private ArrayList<String> paths;
    private ViewPager         mViewPager;
    private PhotoPagerAdapter mPagerAdapter;
    private int    currentItem      = 0;
    private String currentPath      = null;
    private int    maxSelectedCount = 0;
    private RecyclerView       recyclerView;
    private PreviewGridAdapter previewGridAdapter;

    private List<String>  selectedPaths  = new ArrayList<>();
    private List<Boolean> selectedStates = new ArrayList<>();

    private ImageView ivBack;
    private TextView  tvTitle;
    private ImageView ivSelected;
    private TextView  tvDone;

    private AnimatorSet animatorSet;

    private View titleBar;
    private View bottomBar;

    private boolean isSelectedPathPreview = false;

    @Override
    public int layoutId() {
        return R.layout.activity_picker_pager_layout;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        paths = new ArrayList<>();

        Intent bundle = getIntent();

        if (bundle != null) {
            ArrayList<String> pathArr = bundle.getStringArrayListExtra(EXTRA_PHOTOS);
            paths.clear();
            if (pathArr != null) {

                paths.addAll(pathArr);


            }

            ArrayList<String> selctedPathArr = bundle.getStringArrayListExtra(EXTRA_SELECTED_PHOTOS);
            selectedPaths.clear();
            if (selctedPathArr != null) {
                selectedPaths.addAll(selctedPathArr);
            }

            isSelectedPathPreview = bundle.getBooleanExtra(PhotoPreview.EXTRA_ONLY_PREVIEW_SELECTED_PHOTOS,false);

            if(isSelectedPathPreview) {
                initSelectedState(selectedPaths);

            }

            currentItem = bundle.getIntExtra(EXTRA_CURRENT_ITEM, 0);
            maxSelectedCount = bundle.getIntExtra(PhotoPickerConfig.EXTRA_MAX_COUNT, PhotoPickerConfig.DEFAULT_MAX_COUNT);
        }
        titleBar = findViewById(R.id.titlebar);
        bottomBar = findViewById(R.id.bottom_bar);

        ivBack = findViewById(R.id.iv_close);
        tvTitle = findViewById(R.id.tv_title);
        ivSelected = findViewById(R.id.iv_select);
        tvDone = findViewById(R.id.tv_done);

        mViewPager = findViewById(R.id.vp_photos);
        mPagerAdapter = new PhotoPagerAdapter(Glide.with(this), paths);

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(currentItem);

        recyclerView = findViewById(R.id.rv_photos);
        previewGridAdapter = new PreviewGridAdapter(this, R.layout.item_bottom_preview_layout, selectedPaths);

        checkCurrentIndexIsSelected();

        if (isSelectedPathPreview) {
            previewGridAdapter.setSelectedStates(selectedStates);

        }
        RecyclerBuild recyclerBuild = new RecyclerBuild(recyclerView);
        recyclerBuild.setLinerLayout(false)
                .setItemSpace(AbDisplayUtil.dip2px(10))
                .bindAdapter(previewGridAdapter, false);

        showSelectedState(currentItem);
        updateTitle();
        updateDone();

        if (selectedPaths.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void initSelectedState(List<String> list) {
        selectedStates.clear();
        for (String str : list) {
            selectedStates.add(true);
        }
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
                checkCurrentIndexIsSelected();


                showSelectedState(currentItem);
                updateTitle();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mPagerAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (animatorSet == null || !animatorSet.isRunning()) {
                    if (titleBar.getVisibility() == View.GONE) {
                        animatorSet = visibleViewSet(bottomBar, titleBar);
                    } else {
                        animatorSet = goneViewSet(titleBar, bottomBar);
                    }
                    animatorSet.start();
                }
            }
        });

        previewGridAdapter.setOnItemClickListener(new MultiItemTypeRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                currentPath = selectedPaths.get(position);
                int curpos = paths.indexOf(currentPath);

                mViewPager.setCurrentItem(curpos);
                previewGridAdapter.setSelectedPostion(position);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        ivSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetSelectedState(currentItem);
                showSelectedState(currentItem);
                updateDone();
            }
        });

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(PhotoPreview.EXTRA_PHOTOS, getSelectedPhotos());
                setResult(PhotoPreview.DONE_RESULT_CODE, intent);
                finish();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void showSelectedState(int position) {
        if (isSelectedPathPreview) {
            if (selectedStates.get(position)) {
                ivSelected.setImageResource(R.drawable.album_icon_preview_selected);
            } else {
                ivSelected.setImageResource(R.drawable.album_icon_preview_unselect);
            }
        } else {
            currentPath = paths.get(position);
            if (selectedPaths.contains(currentPath)) {
                ivSelected.setImageResource(R.drawable.album_icon_preview_selected);
            } else {
                ivSelected.setImageResource(R.drawable.album_icon_preview_unselect);
            }
        }

    }

    private void resetSelectedState(int pos) {

        if (isSelectedPathPreview) {
            selectedStates.set(pos, !selectedStates.get(pos));
            previewGridAdapter.notifyDataSetChanged();
        } else {
            currentPath = paths.get(pos);
            if (selectedPaths.contains(currentPath)) {
                selectedPaths.remove(currentPath);
                previewGridAdapter.notifyDataSetChanged();

                if (selectedPaths.size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                }

            } else {
                if (selectedPaths.size() >= maxSelectedCount) {
                    showToast(getString(R.string.__picker_over_max_count_tips, maxSelectedCount));
                } else {
                    selectedPaths.add(currentPath);
                    int postion = selectedPaths.size() - 1;

                    recyclerView.smoothScrollToPosition(postion);
                    previewGridAdapter.setSelectedPostion(postion);
                    recyclerView.setVisibility(View.VISIBLE);
                }

            }
        }

    }

    private void checkCurrentIndexIsSelected() {
        currentPath = paths.get(currentItem);
        if (selectedPaths.contains(currentPath)) {
            int repos = selectedPaths.indexOf(currentPath);
            recyclerView.smoothScrollToPosition(repos);
            previewGridAdapter.setSelectedPostion(repos);

        } else {
            previewGridAdapter.setSelectedPostion(-1);
        }
    }

    private Animator goneAnimator(final View view, boolean isUp) {
        int start = 0;
        int end = 0;
        if (isUp) {// 向上隐藏
            start = 0;
            end = -view.getHeight();

        } else { // 向下隐藏
            start = 0;
            end = view.getHeight();
        }

        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", start, end);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return animator;
    }

    private Animator visibleAnimator(final View view, boolean isUp) {
        int start = 0;
        int end = 0;
        if (isUp) {// 向上展示

            start = view.getHeight();
            end = 0;

        } else { // 向下展示
            start = -view.getHeight();
            end = 0;
        }

        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", start, end);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return animator;
    }

    private AnimatorSet goneViewSet(View upGone, View downGone) {
        if (animatorSet == null) {
            animatorSet = new AnimatorSet();
        }
        animatorSet.playTogether(goneAnimator(upGone, true), goneAnimator(downGone, false));
        animatorSet.setDuration(300);
        animatorSet.setInterpolator(new LinearInterpolator());
        return animatorSet;
    }

    private AnimatorSet visibleViewSet(View upVisible, View downVisible) {
        if (animatorSet == null) {
            animatorSet = new AnimatorSet();
        }
        animatorSet.playTogether(visibleAnimator(upVisible, true), visibleAnimator(downVisible, false));
        animatorSet.setDuration(300);
        animatorSet.setInterpolator(new LinearInterpolator());
        return animatorSet;
    }

    /**
     * 更新选中图片数量
     */
    public void updateDone() {

        int size = 0;
        if (isSelectedPathPreview) {
            for (Boolean bool : selectedStates) {
                if (bool) {
                    size++;
                }
            }
        } else {
            size = selectedPaths.size();
        }

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

    private void updateTitle() {
        tvTitle.setText(getString(R.string.__picker_preview_title, currentItem + 1, paths.size()));
    }

    private ArrayList<String> getSelectedPhotos() {
        ArrayList<String> pathlist = new ArrayList<>();
        pathlist.clear();
        if (isSelectedPathPreview) {
            for (int i = 0; i < selectedPaths.size(); i++) {
                if (selectedStates.get(i)) {
                    pathlist.add(selectedPaths.get(i));
                }
            }
        } else {
            pathlist.addAll(selectedPaths);
        }

        return pathlist;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(PhotoPreview.EXTRA_PHOTOS, getSelectedPhotos());
        setResult(PhotoPreview.BACK_RESULT_CODE, intent);
        super.onBackPressed();

    }
}
