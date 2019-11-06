package com.crawler.wechatcrawler.action;

import com.android.uiautomator.core.UiCollection;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.crawler.wechatcrawler.MobileDevice;
import com.crawler.wechatcrawler.struct.ApplicationInfor;
import com.crawler.wechatcrawler.struct.GroupInformation;
import com.crawler.wechatcrawler.struct.ViewStatus;

public class BriberyMoneyCollectorAction extends Action {
    @Override
    public Action play() {
        
        ViewStatus viewStatus = ApplicationInfor.getInstance().viewStatus;
        GroupInformation groupInfor = viewStatus.groupInfor;
        System.out.println(groupInfor.getGroupName());
        MobileDevice.getInstance().fastScrollUp(5);
        UiSelector briberyMoneySelector = new UiSelector().className("android.widget.LinearLayout").resourceId("com.tencent.mm:id/b").childSelector(
                new UiSelector().className("android.widget.LinearLayout").resourceId("com.tencent.mm:id/ci").childSelector(
                        new UiSelector().className("android.widget.LinearLayout").resourceId("com.tencent.mm:id/b_")
        ));
        UiSelector messageListSelector = new UiSelector().className("android.widget.ListView").resourceId("com.tencent.mm:id/oi");
        UiSelector itemSelector = new UiSelector().className("android.widget.RelativeLayout");
        boolean hasItem = true;
        
        // 保证最上面的都是红包信息
        while (true) {
            UiCollection itemCollection = new UiCollection(messageListSelector);
            int childCounter = itemCollection.getChildCount(itemSelector);
            if (childCounter <= 0) {
                // 没有任何item.
                hasItem = false;
                break;
            }
            try {
                UiObject firstItem = itemCollection.getChildByInstance(itemSelector, 0);
                UiObject briberyMoneyObject = firstItem.getChild(briberyMoneySelector);
                if (briberyMoneyObject.exists()) {
                    try {
                        briberyMoneyObject.click();
                    } catch (UiObjectNotFoundException e) {
                        ApplicationInfor.errorLogging("Click briberyMoney exception, msg=" + e.getMessage());
                        break;
                    }
                    // TODO: check the bribery money.
                } else {
                    // TODO: delete the item.
                }
            } catch (UiObjectNotFoundException e) {
                ApplicationInfor.errorLogging("Get the first item exception, msg=" + e.getMessage());
                break;
            }
        }

        try {
            Thread.sleep(10000000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }
    
    private String checkBriberyMoney() {
        
        return "";
    }
    
    public static BriberyMoneyCollectorAction getInstance() {
        if (null == _instance) {
            _instance = new BriberyMoneyCollectorAction();
        }
        
        return _instance;
    }

    private BriberyMoneyCollectorAction() {
        super();
    }
    
    private static BriberyMoneyCollectorAction _instance;
}
