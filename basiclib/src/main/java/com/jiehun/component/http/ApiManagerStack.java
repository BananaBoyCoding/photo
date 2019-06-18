package com.jiehun.component.http;

import com.jiehun.component.utils.AbPreconditions;

import java.util.Stack;

/**
 * Created by zhouyao on 17-12-13.
 */
public class ApiManagerStack {

    private static Stack<IpHostListInterface> apiManagerStack;


    /**
     * 单例模式
     *
     * @return
     */
    private static class HelperHolder {
        public static final ApiManagerStack helper = new ApiManagerStack();
    }

    public static ApiManagerStack getInstance() {
        return HelperHolder.helper;
    }

    /**
     * 添加ipHostList到栈
     */
    public void addIIpHostList(IpHostListInterface mIIpHostList) {
        if (apiManagerStack == null) {
            apiManagerStack = new Stack<>();
        }
        apiManagerStack.add(mIIpHostList);
    }

    public void ipHostChange() {
        if (AbPreconditions.checkNotNullRetureBoolean(apiManagerStack)) {
            for (IpHostListInterface mIIpHostList : apiManagerStack) {
                mIIpHostList.ipHostChange();
            }
        }
    }


}
