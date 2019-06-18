package com.jiehun.album.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.jiehun.component.base.BaseActivity;
import com.jiehun.component.utils.AbFileUtils;
import com.jiehun.component.utils.AbLazyLogger;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zg on 2018/1/4.
 */

public class DownLoadHelper {
    private BaseActivity mContext;

    public DownLoadHelper(BaseActivity context) {
        mContext = context;
    }

    //Glide保存图片

    public void savePicture(BaseActivity context, String url) {


        Glide.with(mContext).asBitmap().load(url).into(target);
    }


    public void galleryAddPic(Context context, String photoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        if (TextUtils.isEmpty(photoPath)) {
            return;
        }

        File f = new File(photoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        if (!storageDir.exists()) {
            if (!storageDir.mkdir()) {
                Log.e("TAG", "Throwing Errors....");
                throw new IOException();
            }
        }

        File image = new File(storageDir, imageFileName);

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }

    private SimpleTarget target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
            try {
                File file = createImageFile();
                String path = file.getAbsolutePath();
                AbFileUtils.bitmapToFile(resource, path);
                galleryAddPic(mContext, path);
                mContext.showToast("保存成功");
            } catch (IOException e) {
                e.printStackTrace();
            }


//            galleryAddPic(context,path);
        }

    };


}
