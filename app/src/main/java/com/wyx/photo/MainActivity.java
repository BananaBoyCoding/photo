package com.wyx.photo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jiehun.album.PhotoPickerConfig;
import com.wyx.photo.lssbase.LSSBaseTitleActivity;
import com.wyx.photo.view.PhotoGridView;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends LSSBaseTitleActivity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }

    @BindView(R.id.pgv)
    PhotoGridView pgv;

    @Override
    public int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
//        SunUiUtil.fixLayout(this);
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("yy", "dsf---->");
        if (requestCode == PhotoPickerConfig.REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                ArrayList<String> paths = data.getStringArrayListExtra(PhotoPickerConfig.KEY_SELECTED_PHOTOS);
                if (paths != null) {
                    Log.e("yy", "---->" + paths.size());
                    pgv.setPaths(paths);
                }
            }
        }

    }
}
