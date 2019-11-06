package com.crawler.wechatcrawler.action;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.crawler.wechatcrawler.struct.ApplicationInfor;

import android.os.RemoteException;

public class LoginAction extends Action {

    private LoginAction() {
        super();
    }
    
    @Override
    public Action play() {
        UiDevice uiDevice = UiDevice.getInstance();
        UiObject wechatIcon = new UiObject(new UiSelector().text("微信"));
        try {
            uiDevice.wakeUp();
            // get the home page.
            uiDevice.pressHome();
            uiDevice.pressHome();
            // TODO: temp for press the weichat icon
            if (!wechatIcon.exists()) {
                ApplicationInfor.errorLogging("Wechat Icon not found!");
            }
            wechatIcon.click();
            //iDevice.click(440, 1220);
            return MsgListDispatchAction.getInstance();
        } catch (RemoteException | UiObjectNotFoundException  ex) {
            ApplicationInfor.errorLogging("login: login exception, msg=" + ex.getMessage());
        }

        return null;
    }

    static public Action getInstance() {
        if (null == _singletonAction) {
            _singletonAction = (Action)new LoginAction();
        }
        
        return _singletonAction;
    }
    
    private static Action _singletonAction;
}
