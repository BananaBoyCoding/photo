package com.jiehun.component.rxpermission;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.jiehun.component.basiclib.R;
import com.jiehun.component.dialog.MaterialDialog;
import com.jiehun.component.helper.ActivityManager;
import com.jiehun.component.utils.AbKJLoger;

/** 提供onNeverAsk的默认实现。直接弹框引导用户去实现。
 * Created by zhouyao on 16-10-13.
 */

public class RxImpCallBack implements RxCallBack {

    @Override
    public void onOk() {
        AbKJLoger.debug("RxImpCallBack","onOk");
    }

    @Override
    public void onCancel() {
        AbKJLoger.debug("RxImpCallBack","onCancel");
    }

    @Override
    public void onNeverAsk(final Activity aty, String permission) {

        AbKJLoger.debug("RxImpCallBack","onNeverAsk");
        //Material 风格
        final MaterialDialog mMaterialDialog = new MaterialDialog(aty);
        mMaterialDialog
                .setTitle(permission + aty.getResources().getString(R.string.permission_dialog_show_title))
                .setMessage(R.string.permission_dialog_show_content)
                .setPositiveButton(R.string.permission_dialog_show_confirm, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent =  new Intent();
                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.setData(Uri.fromParts("package", aty.getPackageName(), null));
                        aty.startActivity(intent);
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
    }
}