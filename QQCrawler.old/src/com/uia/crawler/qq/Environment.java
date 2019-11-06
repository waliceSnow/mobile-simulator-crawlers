package com.uia.crawler.qq;

/*
 * 这个类存储当前运行过程中产生的任何状态属性
 */

import com.android.uiautomator.core.UiObject;

public class Environment {
    public final static long DEFAULT_SLEEP_TIME = 1000;          // default sleep 100ms.
    public final static long DEFAULT_MAX_PAGE_WARTING_TIME = 5000;
    
    public UiObject getUnreadUserObject() {
        return _unreadUserObj;
    }
    
    public void setUnreadUserObject(UiObject uiObj) {
        this._unreadUserObj = uiObj;
    }
    
    private UiObject _unreadUserObj; 
}
