package com.jiehun.component.helper;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by zhouyao
 * on 2017/12/13.
 */

public class ActivityManager {

    private static Stack<AppCompatActivity> activityStack;

    private ActivityManager() {
    }

    private static class ManagerHolder {
        private static final ActivityManager instance = new ActivityManager();
    }

    public static ActivityManager create() {
        return ManagerHolder.instance;
    }

    /**
     * 获取当前Activity栈中元素个数
     */
    public int getCount() {
        return activityStack.size();
    }

    public static Stack<AppCompatActivity> getAllActivity() {
        return activityStack;
    }


    public Activity getCurrentActivity() {
        if (activityStack.size() >= 1) {
            return activityStack.get(activityStack.size() - 1);
        }
        return null;
    }

    /**
     * 添加Activity到栈
     */
    public void addActivity(AppCompatActivity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 结束指定的Activity(重载)
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 关闭除了指定activity以外的全部activity 如果cls不存在于栈中，则栈全部清空
     *
     * @param cls
     */
    public void finishOthersActivity(Class<?> cls) {
        // 2 建一个集合，记录需要删除的元素，之后统一删除
        List<AppCompatActivity> templist = new ArrayList<AppCompatActivity>();
        for (AppCompatActivity activity : activityStack) {
            if (!activity.getClass().equals(cls)) {
                ((Activity) activity).finish();
                templist.add(activity);
            }
        }
        activityStack.removeAll(templist);
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                (activityStack.get(i)).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 清除最上层以下所有的activity
     */
    public  void finishAllBottomActivities() {
        if (activityStack.size() >= 1) {
            AppCompatActivity lastActivity = activityStack.get(activityStack.size() - 1);
            for (int i = 0; i < activityStack.size() - 1; i++) {
                Activity activity = activityStack.get(i);
                if (activity != null)
                    activity.finish();
            }
            activityStack.clear();
            activityStack.add(lastActivity);
        }
    }
}
