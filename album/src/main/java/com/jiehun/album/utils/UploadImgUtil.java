package com.jiehun.album.utils;

import com.jiehun.component.utils.AbPreconditions;

import java.io.File;
import java.util.List;

//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.RequestBody;

/**
 * Created by zg on 2018/1/17.
 */

public class UploadImgUtil {
//    public static void uploadImgList(BaseActivity context, BizCodeEnum codeEnum, List<String> paths, final UploadImgListCallBack callBack) {
//        if (!AbPreconditions.checkNotEmptyList(paths)) {
//            return;
//        }
//        MultipartBody multipartBody = filesToMultipartBody(codeEnum.getValue(), paths);
//        Observable observable = ApiManager.getInstance().uploadImgList(multipartBody).doOnSubscribe(context);
//
//        AbRxJavaUtils.toSubscribe(observable, context.bindToLifecycleDestroy(), new NetSubscriber<UploadImgListVo>(context.getRequestDialog()) {
//
//            @Override
//            public void onNext(HttpResult<UploadImgListVo> uploadImgListVoHttpResult) {
//                UploadImgListVo vo = uploadImgListVoHttpResult.getData();
//                if (vo != null) {
//                    List<UploadImgListVo.SuccessVo> successVos = vo.getSuccess();
//                    if (AbPreconditions.checkNotEmptyList(successVos)) {
//                        Map<String, String> map = new HashMap<>();
//                        for (UploadImgListVo.SuccessVo item : successVos) {
//                            map.put(item.getOrigname(), item.getUrl());
//                        }
//
//                        if (callBack != null) {
//                            callBack.success(map);
//                        }
//                    }
//
//                    if (callBack != null) {
//                        callBack.fail(vo.getFail());
//                    }
//                }
//            }
//        });
//    }

//    private static MultipartBody filesToMultipartBody(int code, List<String> paths) {
//        MultipartBody.Builder builder = new MultipartBody.Builder();
//        builder.addFormDataPart("bizcode",code+"");
//
//        for (String path : paths) {
//            if (AbPreconditions.checkFileExit(path)) {
//                File file = new File(path);
//                file = BitmapUtil.compressImageFromFile(file);
//                RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
//                builder.addFormDataPart("files", file.getName(), requestBody);
//            }
//
//        }
//
//        builder.setType(MultipartBody.FORM);
//        MultipartBody multipartBody = builder.build();
//        return multipartBody;
//    }
}
