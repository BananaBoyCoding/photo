package com.jiehun.component.http;

/**
 * Created by zhouyao on 17-12-13.
 */
public abstract class BaseApiManager extends BaseRetrofitManager implements IpHostListInterface {


    public BaseApiManager() {
        super();
        ipHostChange();
        ApiManagerStack.getInstance().addIIpHostList(this);
    }

}
