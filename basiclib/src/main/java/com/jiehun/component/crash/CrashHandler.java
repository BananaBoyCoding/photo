package com.jiehun.component.crash;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import com.jiehun.component.helper.ActivityManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    //日志输出Path
    private final String PATH ="/sdcard/zymlss/";
    //用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();
    private static CrashHandler                    mCrashHandler;
    private        Thread.UncaughtExceptionHandler mDeUncaughtExceptionHandler;
    //用于格式化日期
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    /**
     * 日志的存放文件的位置
     */
    private static final String mLogPath = Environment.getExternalStorageDirectory().getPath();

    /**
     * 上下文
     */
    private Context       mContext;
    /**
     * 回调接口
     */
    private CrashCallBack mCrashCallBack;

    /**
     * 获得实例对象
     *
     * @return
     */
    public static CrashHandler getInstance() {
        if (mCrashHandler == null) {
            mCrashHandler = new CrashHandler();
        }
        return mCrashHandler;
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
        mDeUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void init(Context context, CrashCallBack crashCallBack) {
        mCrashCallBack = crashCallBack;
        init(context);
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable throwable) {
        //将异常信息写入SDCard文件中
//        dumpExceptionToSDCard(throwable);
        saveCrashInfo2File(throwable);

        //异常外部接口
        if (mCrashCallBack != null) {
            mCrashCallBack.getThrowable(throwable);
        }

        //弹出错误日志对话框
        new Thread(new Runnable() {
            @Override
            public void run() {
                showExceptionDialog(throwable);// 显示错误日志对话框
            }
        }).start();


    }

    /**WeakReference
     * 设置错误提示的对话框
     *
     * @param throwable
     */
    public void showExceptionDialog(Throwable throwable) {
        // 弹出报错并强制退出的对话框
        if (ActivityManager.create().getCount() > 0) {
            Looper.prepare();
            AlertDialog dialog = new AlertDialog.Builder(ActivityManager.create().getCurrentActivity()).create();
            dialog.setMessage(Log.getStackTraceString(throwable));
            Log.e("zym","--------------->"+Log.getStackTraceString(throwable));
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 强制退出程序
                    if (dialog != null)
                        dialog.dismiss();
                    ActivityManager.create().finishAllActivity();
                    System.exit(0);
                }
            });
            dialog.show();
            Looper.loop();
        }
    }

    /**
     * 设置重新启动应用的对话框
     */
//    private void showReStartDialog() {
//     final LoginDialog loginDialog = new
//     LoginDialog(getCurrentActivity(),
//     R.style.dim_dialog, "提示", "啊哦！现金券飞走了  需要重新启动！");
//     loginDialog.setWindowParams();
//     loginDialog.findViewById(R.id.btn_sure).setOnClickListener(new
//     View.OnClickListener() {
//     @Override
//     public void onClick(View v) {
//     Intent intent = new
//     Intent(getApplicationContext(),
//     LoadingActivity.class);
//     PendingIntent restartIntent =
//     PendingIntent.getActivity(getApplicationContext(),
//     0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
//     AlarmManager mgr = (AlarmManager)
//     getSystemService(Context.ALARM_SERVICE);
//     mgr.set(AlarmManager.RTC,
//     System.currentTimeMillis() + 1000,
//     restartIntent); // 1秒钟后重启应用
//     loginDialog.dismiss();
//     finish();
//     }
//     });
//     loginDialog.findViewById(R.id.btn_cancel).setVisibility(View.GONE);
//     loginDialog.show();
//}

    /**
     * 将异常信息写入SDCard文件中
     *
     * @param ex
     */
    private void dumpExceptionToSDCard(Throwable ex) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            if (BuildConfig.DEBUG) {
//                return;
//            }
        }

        File dir = new File(mLogPath);
        if (!dir.exists())
            dir.mkdirs();

        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        File file = new File(mLogPath + "crash" + currentTime + ".trace");

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            pw.println(currentTime);
            dumpPhoneInfo(pw);
            pw.println();
            ex.printStackTrace(pw);
            pw.close();
        } catch (IOException e) {
            e.toString();
        }
    }

    /**
     * 写入设备信息
     *
     * @param printWriter
     */
    private void dumpPhoneInfo(PrintWriter printWriter) {
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            //应用版本号
            printWriter.print("App Version: ");
            printWriter.print(packageInfo.versionName);
            printWriter.print("_");
            printWriter.println(packageInfo.versionCode);
            //Android版本号
            printWriter.print("OS Version: ");
            printWriter.print(Build.VERSION.RELEASE);
            printWriter.print("_");
            printWriter.println(Build.VERSION.SDK_INT);
            //手机制作商
            printWriter.print("Vendor: ");
            printWriter.println(Build.MANUFACTURER);
            //手机型号
            printWriter.print("Model: ");
            printWriter.println(Build.MODEL);
            //CPU 架构
            printWriter.print("CPU ABI: ");
            printWriter.println(Build.CPU_ABI);
        } catch (PackageManager.NameNotFoundException e) {
        }
    }

    public interface CrashCallBack {
        void getThrowable(Throwable ex);
    }


    private String saveCrashInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".log";
            Log.e("zymyy","----->"+fileName);
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File dir = new File(PATH);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(PATH + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            Log.e("zymyy", "an error occured while writing file...", e);
        }
        return null;
    }

}
