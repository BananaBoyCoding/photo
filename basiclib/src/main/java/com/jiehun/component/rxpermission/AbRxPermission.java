package com.jiehun.component.rxpermission;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.view.View;

import com.jiehun.component.basiclib.R;
import com.jiehun.component.dialog.MaterialDialog;
import com.jiehun.component.helper.ActivityManager;
import com.jiehun.component.utils.AbLazyLogger;
import com.jiehun.component.utils.AbPreconditions;
import com.jiehun.component.utils.AbStringUtils;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

/** 提供权限申请
 * Created by zhouyao on 16-10-13.
 */

public class AbRxPermission {

    private static final String TAG = "AbRxPermission";
    private static final int OK = 0;//同意权限
    private static final int CANCEL = 1;//拒绝权限
    private static final int NEVERASK = 2;//不再询问
    private static List<String> neverList;

    //多个权限检查的调用此方法
    public static void checkPermissions(final Activity aty, final RxCallBack callBack, String... permissions) {
        RxPermissions rxPermissions = new RxPermissions(aty);
        rxPermissions
                .requestEach(permissions)
                .subscribe(new Action1<Permission>() {
                    @Override
                    public void call(Permission permission) {
                        if (permission.granted){
                            callBack.onOk();
                        }else if (permission.shouldShowRequestPermissionRationale){
                            //已拒绝权限请求
                            callBack.onCancel();
                        }else {
                            //拒绝权限请求并不在询问
                            callBack.onNeverAsk(aty,getPermissionCnStr2(permission.name));
                        }
                    }
                });
    }
//    public static void checkPermissions(final Activity aty, final RxCallBack callBack, String... permission) {
//        neverList = new ArrayList<>();
//        RxPermissions rxPermissions = new RxPermissions(aty);
//        rxPermissions.requestEach(permission)
//                .toList()
//                .flatMap(new Func1<List<Permission>, Observable<Integer>>() {//归并权限，获取最终结果
//                    @Override
//                    public Observable<Integer> call(List<Permission> permissions) {
//                        int state = OK;
//                        for (Permission p : permissions) {
//                            if (isGranted(aty,p.name)){
//                                state = OK;
//                            }else if (p.shouldShowRequestPermissionRationale){
//                                //已拒绝权限请求
//                                state = CANCEL;
//                            }else {
//                                //拒绝权限请求并不在询问
//                                state = NEVERASK;
//                                neverList.add(p.name);
//                            }
//                        }
//                        return Observable.just(state);
//                    }
//                })
//                .subscribe(new Action1<Integer>() {
//                               @Override
//                               public void call(Integer state) {
//                                   if (callBack != null) {
//                                       switch (state) {
//                                           case OK:
//                                               callBack.onOk();
//                                               break;
//                                           case CANCEL:
//                                               callBack.onCancel();
//                                               break;
//                                           case NEVERASK:
//                                               callBack.onNeverAsk(aty, getPermissionCnStr(neverList));
//                                               break;
//                                       }
//                                   }
//                               }
//                           }, new Action1<Throwable>() {
//                               @Override
//                               public void call(Throwable t) {
//                                   AbLazyLogger.e(TAG, "onError", t);
//                               }
//                           },
//                        new Action0() {
//                            @Override
//                            public void call() {
//                                AbLazyLogger.i(TAG, "OnComplete");
//                            }
//                        });
//    }

//    public static void checkPermission(final Activity aty, final RxCallBack callBack, String permissions) {
//        neverList = new ArrayList<>();
//        RxPermissions rxPermissions = new RxPermissions(aty);
//        rxPermissions
//                .requestEach(permissions)
//                .subscribe(new Action1<Permission>() {
//                    @Override
//                    public void call(Permission permission) {
//                        if (permission.granted){
//                            callBack.onOk();
//                        }else if (permission.shouldShowRequestPermissionRationale){
//                            //已拒绝权限请求
//                            callBack.onCancel();
//                        }else {
//                            //拒绝权限请求并不在询问
//                            neverList.add(permission.name);
//                            callBack.onNeverAsk(aty,getPermissionCnStr(neverList));
//                        }
//                    }
//                });
//    }

    //>=23时判断权限是否加载
    private static boolean isGranted(Activity activity, String permission){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.M) {
                return ActivityCompat.checkSelfPermission(activity,permission) == PackageManager.PERMISSION_GRANTED;
            } else {
                return PermissionChecker.checkSelfPermission(activity, permission) == PermissionChecker.PERMISSION_GRANTED;
            }
        }
        return true;
    }
    //判断是否需要动态申请权限
    private static boolean isDynamicApply(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.M) {
            return true;
        }
        return false;
    }
    //<23时权限的处理
    private static void handlePermission(final Activity activity, RxCallBack callBack, String... permissions){
        boolean hasPermission = false;
        String errorPermission = "";
        try {
            for (String permission : permissions) {
                errorPermission = permission;
                hasPermission = ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
            }
        } catch (Exception e) {
            e.printStackTrace();
            hasPermission = false;
        }

        if (hasPermission){
            callBack.onOk();
        }else {
            List<String> permissionList = new ArrayList<>();
            permissionList.add(errorPermission);
            final MaterialDialog mMaterialDialog = new MaterialDialog(activity);
            mMaterialDialog
                    .setTitle(getPermissionCnStr(permissionList) + activity.getResources().getString(R.string.permission_dialog_show_title))
                    .setMessage(R.string.permission_dialog_show_content)
                    .setPositiveButton(R.string.permission_dialog_show_confirm, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent =  new Intent();
                            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                            intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
                            activity.startActivity(intent);
                            mMaterialDialog.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.permission_dialog_show_exit,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mMaterialDialog.dismiss();
                                    ActivityManager.create().finishAllActivity();
                                }
                            })
                    .setCanceledOnTouchOutside(false)
                    .show();
            callBack.onCancel();
        }
    }


    /**
     * @param aty      当前Activity
     * @param callBack 执行完成回调
     *                 检测相机权限和存储权限
     */
    public static void checkCameraPermission(final Activity aty, final RxCallBack callBack) {
        if (isDynamicApply(aty)) {
            checkPermissions(aty, callBack, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
        }else {
            handlePermission(aty,callBack,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
        }
    }
    /**
     * @param aty      当前Activity
     * @param callBack 执行完成回调
     *                 检测存储权限
     */
    public static void checkWritePermission(final Activity aty, final RxCallBack callBack) {
        if (isDynamicApply(aty)) {
            checkPermissions(aty, callBack, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }else {
            handlePermission(aty,callBack,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    /**
     * @param aty      当前Activity
     * @param callBack 执行完成回调
     *                 检测录音权限
     */
    public static void checkRecordPermission(final Activity aty, final RxCallBack callBack) {
        if (isDynamicApply(aty)) {
            checkPermissions(aty, callBack, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO);
        }else {
            handlePermission(aty,callBack,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO);
        }
    }

    /**
     * @param aty      当前Activity
     * @param callBack 执行完成回调
     *                 检测拨打电话权限
     */
    public static void checkPhonePermission(final Activity aty, final RxCallBack callBack) {
        if (isDynamicApply(aty)) {
            checkPermissions(aty, callBack, Manifest.permission.CALL_PHONE);
        }else {
            handlePermission(aty,callBack,Manifest.permission.CALL_PHONE);
        }
    }

    /**
     * @param aty      当前Activity
     * @param callBack 执行完成回调
     *                 检测定位权限
     */
    public static void checkLocationPermission(final Activity aty, final RxCallBack callBack) {
        if (isDynamicApply(aty)) {
            checkPermissions(aty, callBack, Manifest.permission.ACCESS_FINE_LOCATION);
        }else {
            handlePermission(aty,callBack,Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    /**
     * @param aty      当前Activity
     * @param callBack 执行完成回调
     *                 检测通讯录权限
     */
    public static void checkContactPermission(final Activity aty, final RxCallBack callBack) {
        if (isDynamicApply(aty)) {
            checkPermissions(aty, callBack, Manifest.permission.WRITE_CONTACTS);
        }else {
            handlePermission(aty,callBack,Manifest.permission.WRITE_CONTACTS);
        }
    }

    /**
     * @param aty      当前Activity
     * @param callBack 执行完成回调
     *                 检测文件权限
     */
    public static void checkWriteStoragePermission(final Activity aty, final RxCallBack callBack) {
        if (isDynamicApply(aty)) {
            checkPermissions(aty, callBack, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }else {
            handlePermission(aty,callBack,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }


    /**
     *
     * @param aty      当前Activity
     * @param callBack 执行完成回调
     *                 检测读取文件权限
     */
    public static void checkReadStoragePermission(final Activity aty,final RxCallBack callBack){
        if (isDynamicApply(aty)) {
            checkPermissions(aty, callBack, Manifest.permission.READ_EXTERNAL_STORAGE);
        }else {
            handlePermission(aty,callBack,Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    /**
     * @param aty      当前Activity
     * @param callBack 执行完成回调
     *                 检测短信权限
     */
    public static void checkSmsPermission(final Activity aty, final RxCallBack callBack) {
        if (isDynamicApply(aty)) {
            checkPermissions(aty, callBack, Manifest.permission.SEND_SMS);
        }else {
            handlePermission(aty,callBack,Manifest.permission.SEND_SMS);
        }
    }

    /**
     * 读取设备信息
     * @param aty
     * @param callBack
     */
    public static void checkPhoneStatePermission(final Activity aty, final RxCallBack callBack){
        if (isDynamicApply(aty)) {
            checkPermissions(aty, callBack, Manifest.permission.READ_PHONE_STATE);
        }else {
            handlePermission(aty,callBack,Manifest.permission.READ_PHONE_STATE);
        }
    }


    private static String getPermissionCnStr(List<String> permissions){
        List<String> pStrs = new ArrayList<>();
        String result =  "";
        if (!AbPreconditions.checkNotEmptyList(permissions)){
            return "";
        }

        for (String permission : permissions) {
            if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                pStrs.add("读写");
            }
            if (permission.equals(Manifest.permission.CAMERA)) {
                pStrs.add("打开摄像头");
            }
            if (permission.equals(Manifest.permission.RECORD_AUDIO)) {
                pStrs.add("使用话筒录音/通话录音");
            }
            if (permission.equals(Manifest.permission.CALL_PHONE)) {
                pStrs.add("拨打电话");
            }
            if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                pStrs.add("获取位置信息");
            }
            if (permission.equals(Manifest.permission.WRITE_CONTACTS)) {
                pStrs.add("写入/删除联系人信息");
            }
            if (permission.equals(Manifest.permission.SEND_SMS)) {
                pStrs.add("发送短信");
            }
            if (permission.equals(Manifest.permission.READ_PHONE_STATE)){
                pStrs.add("读取设备信息");
            }
        }

        for (int i = 0; i < pStrs.size(); i++){
            if (i != pStrs.size() - 1){
                result += pStrs.get(i) + ",";
            }else {
                result += pStrs.get(i);
            }
        }
        return result;
    }

    private static String getPermissionCnStr2(String permission){
        if (AbStringUtils.isNull(permission)){
            return "";
        }
        if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            return "读写";
        }
        if (permission.equals(Manifest.permission.CAMERA)){
            return "打开摄像头";
        }
        if (permission.equals(Manifest.permission.RECORD_AUDIO)){
            return "使用话筒录音/通话录音";
        }
        if (permission.equals(Manifest.permission.CALL_PHONE)){
            return "拨打电话";
        }
        if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)){
            return "获取位置信息";
        }
        if (permission.equals(Manifest.permission.WRITE_CONTACTS)){
            return "写入/删除联系人信息";
        }
        if (permission.equals(Manifest.permission.SEND_SMS)){
            return "发送短信";
        }
        return "";
    }

    //检查授权仅仅相机的时候使用




}
