package com.uia.crawler.qq.action;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;

import com.uia.crawler.qq.Environment;
import android.os.RemoteException;

public class OpenApkAction extends Action {
    private UiDevice _uiDevice;
    
    public OpenApkAction(ActionMngr actionMngr, Environment environment) {
        super(actionMngr, environment);
        _uiDevice = UiDevice.getInstance();
    }
    public boolean play() {
        try {
            // wake up the screen
            _uiDevice.wakeUp();
            // goto home
            _uiDevice.pressHome();
            // open the QQ apk
            UiObject uiObject = new UiObject(new UiSelector().description("QQ"));
            uiObject.click();
            } catch (RemoteException | UiObjectNotFoundException e) {
                // TODO: need log.
                return false;
        }
        
        return true;
    }
    
    public Action nextAction() {
        return _actionMngr.getAction("refresh_msg_tab_action");
    }
}
