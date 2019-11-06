package com.uia.crawler.qq.action;

import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;

import com.uia.crawler.qq.Environment;

public class RefreshMsgTabAction extends Action {
    UiObject _uiObject = null;
    private UiSelector _msgNumSelector;

    public RefreshMsgTabAction(ActionMngr actionMngr, Environment environment) {
        super(actionMngr, environment);
        _uiObject = new UiObject(new UiSelector().resourceId("android:id/tabs").childSelector(
                new UiSelector().className("android.widget.RelativeLayout").instance(0).childSelector(
                        new UiSelector().resourceId("com.tencent.mobileqq:id/name")
        )));
        _msgNumSelector = new UiSelector().resourceId("com.tencent.mobileqq:id/relativeItem").childSelector(
                new UiSelector().resourceId("com.tencent.mobileqq:id/unreadmsg")
        );
    }

    @Override
    public boolean play() {
        try {
            boolean bNotFound = _uiObject.waitForExists(Environment.DEFAULT_MAX_PAGE_WARTING_TIME);
            if (!bNotFound) {
                return false;
            }
            // double click the message button
            _uiObject.click();
            _uiObject.click();
        } catch (UiObjectNotFoundException ex) {
            // TODO: need log.
            return false;
        }
        
        return true;
    }

    @Override
    public Action nextAction() {
        // found the view which has message text.
        UiObject uiObj = new UiObject(_msgNumSelector);
        if (!uiObj.exists()) {
            // not found unread message
            // TODO: if time up, then check the friend list.
            // TODO: if not the time, clear silence user.
            // TODO: check if user exist, if not exist, sleep some time.
            return _actionMngr.getAction("refresh_msg_tab_action");
        }
        this._environment.setUnreadUserObject(uiObj);
        return _actionMngr.getAction("process_user_msg_action");
    }
}
