package com.crawler.qqcrawler.action;

import com.android.uiautomator.core.*;
import com.crawler.qqcrawler.struct.ApplicationInfor;

public class MsglistDispatchAction extends Action {
    final static private long sWaitUnreadTime = 5000;
    private boolean _hasUnreadUser;

	MsglistDispatchAction(ActionMngr actionMngr) {
		super(actionMngr);
		_hasUnreadUser = false;
	}

	public Action play() {
	    if (!_hasUnreadUser) {
	        UiObject unreadMsgInfo = new UiObject(new UiSelector().resourceId("com.tencent.mobileqq:id/relativeItem").childSelector(
                    new UiSelector().resourceId("com.tencent.mobileqq:id/unreadmsg")
            ));
	        if (unreadMsgInfo.waitForExists(sWaitUnreadTime)) {
	            _hasUnreadUser = true;
	        }
	    }
		UiDevice uiDevice = UiDevice.getInstance();
		UiObject menuButton = new UiObject(new UiSelector().className("android.widget.TabWidget").childSelector(
				new UiSelector().className("android.widget.RelativeLayout").childSelector(
						new UiSelector().className("android.widget.ImageView").instance(1)
		)));
		UiObject unreadMsgInfo = null;

		try {
			// press the menu button & get the top of menu.
	        // avoid the current page not the main page.
	        int tryTime = 0;
	        for (; tryTime < BACK_RETRY_TIME; tryTime++) {
	            if (menuButton.exists()) {
	                break;
	            }
	            uiDevice.pressBack();
	        }
	        if (tryTime >= BACK_RETRY_TIME) {
	            ApplicationInfor.warningLogging("message list dispatch: not found the connector-menu button.");
	            return null;
	        }
			menuButton.click();
			// get the unreadMsginfo
			unreadMsgInfo = new UiObject(new UiSelector().resourceId("com.tencent.mobileqq:id/relativeItem").childSelector(
					new UiSelector().resourceId("com.tencent.mobileqq:id/unreadmsg")
			));
			
			if (!unreadMsgInfo.exists()) {
				// drag the list to the bottom.
				uiDevice.swipe(340, 700, 340, 180, 10);
				Thread.sleep(1000);
			} else {
				// click into the detail list.
				UiObject parentObj = unreadMsgInfo.getFromParent(new UiSelector().className("android.view.View"));
				parentObj.click();
				// return the detail list process.
				_hasUnreadUser = true;
				return this._actionMngr.getUserTweetProcessAction();
			}
			
			// doesn't found the useful information.
			UiCollection allUserItems = new UiCollection(new UiSelector().resourceId("com.tencent.mobileqq:id/recent_chat_list"));
			for (int i = allUserItems.getChildCount(new UiSelector().className("android.widget.LinearLayout")) - 1; i > 1; i--) {
				UiObject childItem = allUserItems.getChildByInstance(new UiSelector().className("android.widget.LinearLayout"), i);
				childItem.dragTo(childItem, 60);
				UiObject delObj = new UiObject(new UiSelector().text("删除"));
				if (delObj.exists()) {
					delObj.click();
				}
			}
		} catch (UiObjectNotFoundException | InterruptedException e) {
		    ApplicationInfor.warningLogging("message list dispatch: click the menu Button failed, msg=" + e.getMessage());
			return null;
		}
		_hasUnreadUser = false;
		return this;
	}

}
