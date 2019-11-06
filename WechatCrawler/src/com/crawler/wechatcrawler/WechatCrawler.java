package com.crawler.wechatcrawler;

import com.android.uiautomator.testrunner.UiAutomatorTestCase;

import com.crawler.wechatcrawler.action.*;
import com.crawler.wechatcrawler.struct.ApplicationInfor;

public class WechatCrawler extends UiAutomatorTestCase {
    TimerThread _timerThread;
    public void testMain() {
        // initialization the members
        _timerThread = new TimerThread();
        
        // start
        _timerThread.start();
        Action curAction = LoginAction.getInstance();
        Action nextAction = null;
        while (true) {
            nextAction = curAction.play();
            if (null == nextAction) {
                ApplicationInfor.errorLogging("Next action is null.");
                nextAction = LoginAction.getInstance();
            }
            curAction = nextAction;
        }
    }
}
