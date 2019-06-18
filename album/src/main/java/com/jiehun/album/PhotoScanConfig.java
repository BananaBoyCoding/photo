package com.jiehun.album;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;


import java.util.ArrayList;

/**
 * Created by zg on 2018/1/4.
 */

public class PhotoScanConfig {

    public static final int DEFAULT_POSITION = 0;

    public static final String EXTRA_CURRENT_POSITION = "extra_position";
    public static final String EXTRA_PHOTO_LIST = "extra_photo_list";

    public static PhotoScanConfigBuilder builder() {
        return new PhotoScanConfigBuilder();
    }

    public static class PhotoScanConfigBuilder{
        private Bundle mPhotoScanBundle;

        public PhotoScanConfigBuilder() {
            mPhotoScanBundle = new Bundle();
        }

        public void start(@NonNull Activity activity) {
            Intent intent = new Intent(activity,PhotoScanActivity.class);
            intent.putExtras(mPhotoScanBundle);
            activity.startActivity(intent);


//            Toast.makeText(activity, "ewwwe", Toast.LENGTH_SHORT).show();
        }

        public PhotoScanConfigBuilder setPhotoList(ArrayList<String> paths)  {
            mPhotoScanBundle.putStringArrayList(EXTRA_PHOTO_LIST,  paths);
            return this;
        }

        public PhotoScanConfigBuilder setCurPosition(int position) {
            mPhotoScanBundle.putInt(EXTRA_CURRENT_POSITION,position);
            return this;
        }
    }
}
