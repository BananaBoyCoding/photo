package com.jiehun.album;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.jiehun.component.rxpermission.AbRxPermission;
import com.jiehun.component.rxpermission.RxCallBack;

import java.util.ArrayList;

/**
 * Created by zg on 2017/12/20.
 */

public class PhotoPickerConfig {
    public static final int REQUEST_CODE = 233;

    public final static int DEFAULT_MAX_COUNT     = 9;
    public final static int DEFAULT_COLUMN_NUMBER = 3;

    public final static String KEY_SELECTED_PHOTOS = "SELECTED_PHOTOS";

    public final static String EXTRA_MAX_COUNT       = "MAX_COUNT";
    public final static String EXTRA_SHOW_CAMERA     = "SHOW_CAMERA";
    public final static String EXTRA_SHOW_GIF        = "SHOW_GIF";
    public final static String EXTRA_GRID_COLUMN     = "column";
    public final static String EXTRA_ORIGINAL_PHOTOS = "ORIGINAL_PHOTOS";
    public final static String EXTRA_PREVIEW_ENABLED = "PREVIEW_ENABLED";


    public static PhotoPickerBuilder builder() {
        return new PhotoPickerBuilder();
    }

    public static class PhotoPickerBuilder {
        private Bundle mPickerOptionsBundle;


        /**
         * Send the crop Intent from an Activity
         *
         * @param activity Activity to receive result
         */
        public void start(@NonNull Activity activity) {
            start(activity, REQUEST_CODE);
        }

        /**
         * Send the Intent from an Activity with a custom request code
         *
         * @param activity    Activity to receive result
         * @param requestCode requestCode for result
         */
        public void start(@NonNull final Activity activity, final int requestCode) {

            AbRxPermission.checkReadStoragePermission(activity, new RxCallBack() {
                @Override
                public void onOk() {
                    activity.startActivityForResult(new Intent(activity,PhotoPickerActivity.class),PhotoPickerConfig.REQUEST_CODE);

                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onNeverAsk(Activity aty, String permission) {

                }

            });

        }


        public PhotoPickerBuilder() {
            mPickerOptionsBundle = new Bundle();
        }


        public PhotoPickerBuilder setPhotoCount(int photoCount) {
            mPickerOptionsBundle.putInt(EXTRA_MAX_COUNT, photoCount);
            return this;
        }

        public PhotoPickerBuilder setGridColumnCount(int columnCount) {
            mPickerOptionsBundle.putInt(EXTRA_GRID_COLUMN, columnCount);
            return this;
        }

        public PhotoPickerBuilder setShowGif(boolean showGif) {
            mPickerOptionsBundle.putBoolean(EXTRA_SHOW_GIF, showGif);
            return this;
        }

        public PhotoPickerBuilder setShowCamera(boolean showCamera) {
            mPickerOptionsBundle.putBoolean(EXTRA_SHOW_CAMERA, showCamera);
            return this;
        }

        public PhotoPickerBuilder setSelected(ArrayList<String> imagesUri) {
            mPickerOptionsBundle.putStringArrayList(EXTRA_ORIGINAL_PHOTOS, imagesUri);
            return this;
        }

        public PhotoPickerBuilder setPreviewEnabled(boolean previewEnabled) {
            mPickerOptionsBundle.putBoolean(EXTRA_PREVIEW_ENABLED, previewEnabled);
            return this;
        }
    }
}
