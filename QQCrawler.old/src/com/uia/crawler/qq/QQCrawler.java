package com.uia.crawler.qq;

import com.android.uiautomator.testrunner.UiAutomatorTestCase;

import com.uia.crawler.qq.Environment;
import com.uia.crawler.qq.action.ActionMngr;
import com.uia.crawler.qq.action.Action;

public class QQCrawler extends UiAutomatorTestCase {
    private ActionMngr _actionMngr;
    private Environment _environment;
    
    public QQCrawler() {
        _environment = new Environment();
        _actionMngr = new ActionMngr(_environment);
    }
    
    public void testMain() {
        if (!_actionMngr.init()) {
            return ;
        }
        boolean execSuccess = true;
        Action curAction = _actionMngr.firstAction();
        while (null != curAction) {
            // do action
            execSuccess = curAction.play();

            if (!execSuccess) {
                // TODO: need log
                curAction = _actionMngr.firstAction();
                continue;
            }
            // get the next action
            curAction = curAction.nextAction();
        }
        
        if (null == curAction) {
            // logging.
        }
    }

}
