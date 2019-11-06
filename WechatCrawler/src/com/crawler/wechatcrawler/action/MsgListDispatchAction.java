package com.crawler.wechatcrawler.action;

import com.android.uiautomator.core.UiCollection;
import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.crawler.wechatcrawler.MobileDevice;
import com.crawler.wechatcrawler.struct.ApplicationInfor;
import com.crawler.wechatcrawler.struct.GroupInformation;

public class MsgListDispatchAction extends Action {
    
    private MsgListDispatchAction() {
        super();
    }

    @Override
    public Action play() {
        UiDevice uiDevice = UiDevice.getInstance();
        int retryTime = 0;
        UiObject messageButton = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/ic").text("微信"));
        
        // get the home message button.
        for (; retryTime < BACK_RETRY_TIME; retryTime++) {
            if (isHomeFrame() && messageButton.exists()) {
                break;
            }
            uiDevice.pressBack();
        }
        if (BACK_RETRY_TIME <= retryTime) {
            ApplicationInfor.errorLogging("Message button not found in 3 times.");
            return null;
        }
        try {
            messageButton.click();
        } catch (UiObjectNotFoundException e2) {
            ApplicationInfor.errorLogging("Message-button clicked exception, msg=" + e2.getMessage());
            return null;
        }
        MobileDevice.getInstance().fastScrollDown();
        // get all lines.
        UiCollection messageList = new UiCollection(new UiSelector().resourceId("com.tencent.mm:id/kd"));
        UiSelector lineSelector = new UiSelector().resourceId("com.tencent.mm:id/c8");
        UiSelector redDotSelector = new UiSelector().resourceId("com.tencent.mm:id/ca");
        UiSelector silenceSelector = new UiSelector().resourceId("com.tencent.mm:id/ce");
        try {
            int counter = messageList.getChildCount(lineSelector);
            while (counter > 0) {
                UiObject curLineObj = messageList.getChildByInstance(lineSelector, counter - 1);
                UiObject redDotObj = curLineObj.getChild(redDotSelector);
                UiObject silenceObj = curLineObj.getChild(silenceSelector);
                if (redDotObj.exists() && silenceObj.exists()) {
                    UiObject groupNameObj = curLineObj.getChild(new UiSelector().resourceId("com.tencent.mm:id/cb"));
                    if (groupNameObj.exists()) {
                        String groupName = groupNameObj.getText();
                        GroupInformation groupInfor = GroupInformation.getGroup(groupName);
                        if (!groupInfor.hasBriberyMoney()) {
                            deleteLineObject(curLineObj);
                        } else {
                            // mark the group information.
                            ApplicationInfor.getInstance().viewStatus.groupInfor = groupInfor;
                            curLineObj.click();
                            return BriberyMoneyCollectorAction.getInstance();
                        }
                    } else {
                        ApplicationInfor.errorLogging("Group name object not found, descr=" + curLineObj.getContentDescription());
                    }
                } else {
                    deleteLineObject(curLineObj);
                }
                counter = messageList.getChildCount(new UiSelector().resourceId("com.tencent.mm:id/c8"));
            }
        } catch (UiObjectNotFoundException e1) {
            ApplicationInfor.errorLogging("Message list process exception, msg=" + e1.getMessage());
        }

        return getInstance();
    }
    
    private boolean deleteLineObject(UiObject lineObj) {
        int retryTime = 0;
        UiObject deleteObj = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/gr").text("删除该聊天"));
        for (;retryTime < DELETE_RETRY_TIME; retryTime++) {
            try {
                lineObj.dragTo(lineObj, 45);
                if (deleteObj.exists()) {
                    deleteObj.click();
                    break;
                }
            } catch (UiObjectNotFoundException e) {
                ApplicationInfor.errorLogging("delete the line object exception, msg=" + e.getMessage());
                return false;
            }
 
        }
        if (DELETE_RETRY_TIME <= retryTime) {
            ApplicationInfor.errorLogging("delete the line object failed in 3 times.");
            return false;
        }
        return true;
    }
    
    private boolean isHomeFrame() {
        UiObject dialogObj = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/f"));
        if (dialogObj.exists()) {
            return false;
        }
        return true;
    }

    static public Action getInstance() {
        if (null == _singletonAction) {
            _singletonAction = (Action)new MsgListDispatchAction();
        }
        
        return _singletonAction;
    }
    
    private static Action _singletonAction;
}
