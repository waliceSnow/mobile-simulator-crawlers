package com.crawler.wechatcrawler.action;

import com.android.uiautomator.core.UiDevice;

public abstract class Action {
    protected static String sBlockMagicNum = "0216d7debf97db2d5bee6c488613f6bf";
    protected static long sBlockTime = 30000;   // 30s

	public abstract Action play();
	
	public void pressBack() {
	    UiDevice uiDevice = UiDevice.getInstance();
	    uiDevice.pressBack();
	}
	
	public void pressBack(int times) {
	    UiDevice uiDevice = UiDevice.getInstance();
	    for (int i = 0; i < times; i++) {
	        uiDevice.pressBack();
	    }
	}

	public static long DFAULT_SLEEP_TIME = 1000;
	protected final static int BACK_RETRY_TIME = 5;          // mean max z-order is 5.
	protected final static int DELETE_RETRY_TIME = 3;
}
