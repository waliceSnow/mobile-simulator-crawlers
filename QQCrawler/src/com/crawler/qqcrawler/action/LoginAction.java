package com.crawler.qqcrawler.action;

import com.android.uiautomator.core.*;
import com.crawler.qqcrawler.struct.ApplicationInfor;
import android.os.*;

public class LoginAction extends Action {
	LoginAction(ActionMngr actionMngr) {
		super(actionMngr);
	}
	
	public Action play() {
		UiDevice uiDevice = UiDevice.getInstance();
		UiObject uiObj = new UiObject(new UiSelector().text("QQ"));
		// wake up the device
		try {
			uiDevice.wakeUp();
			// get the home page.
			uiDevice.pressHome();
			uiDevice.pressHome();
			// press the QQ
			if (!uiObj.exists()) {
			    ApplicationInfor.warningLogging("login: QQ icon not found.");
				return null;
			}
			uiObj.click();
		} catch (RemoteException | UiObjectNotFoundException ex) {
		    ApplicationInfor.warningLogging("login: login exception, msg=" + ex.getMessage());
		}

		return this._actionMngr.getMsglistDispatchAction();
	}
}
