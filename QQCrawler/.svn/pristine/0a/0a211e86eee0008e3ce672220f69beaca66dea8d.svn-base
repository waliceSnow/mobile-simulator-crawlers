package com.crawler.qqcrawler;

import com.android.uiautomator.testrunner.UiAutomatorTestCase;

import com.crawler.qqcrawler.action.*;
import com.crawler.qqcrawler.struct.ApplicationInfor;

public class QQCrawler extends UiAutomatorTestCase {
    TimerThread _timerThread;
	public void testMain() {
		// initialization the members.
		ActionMngr actionMngr = new ActionMngr();
		_timerThread = new TimerThread();
		
		if (!actionMngr.init()) {
			ApplicationInfor.errorLogging("Action manager init failed.");
			clearEnv();
			return ;
		}
		// start.
		_timerThread.start();
		Action curAction = actionMngr.getDefaultAction();
		Action nextAction = null;
		while (true) {
			nextAction = curAction.play();
			if (null == nextAction) {
				ApplicationInfor.warningLogging("Next action is null.");
				nextAction = actionMngr.getDefaultAction();
			}
			curAction.sleep();
			curAction = nextAction;
		}
	}
	
	private void clearEnv() {
	    try {
            ApplicationInfor.getInstance().logMsgQueue.put(TimerThread.sThreadEndNum);
            _timerThread.join();
        } catch (InterruptedException e) {
            System.out.println("put the end of thread failed.");
        }
	}

}
