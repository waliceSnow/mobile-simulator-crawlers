package com.uia.crawler.qq.action;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.uia.crawler.qq.Environment;

public class ProcessUserMsgAction extends Action {

    public ProcessUserMsgAction(ActionMngr actionMngr, Environment environment) {
        super(actionMngr, environment);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean play() {
        UiObject unreadUserObj = this._environment.getUnreadUserObject();
        try {
            if (!unreadUserObj.click()) {
                // logging, click failed.
                return false;
            }
             
        } catch (UiObjectNotFoundException e) {
            // TODO need logging.
            return false;
        }
        return true;
    }

    @Override
    public Action nextAction() {
        // TODO Auto-generated method stub
        return null;
    }

}
