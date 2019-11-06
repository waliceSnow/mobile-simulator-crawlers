package com.crawler.qqcrawler;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;

import android.graphics.Point;

public class MobileDevice {
    private MobileDevice() {
        
    }

    public static MobileDevice getInstance() {
        if (null == sInstance) {
            sInstance = new MobileDevice();
            sMobileSize = new Point();
            sUiDevice = UiDevice.getInstance();
            UiObject screen = new UiObject(new UiSelector());
            try {
                sMobileSize.x = screen.getBounds().right;
                sMobileSize.y = screen.getBounds().bottom;
            } catch (UiObjectNotFoundException e) {
                System.out.println("get the screen size exception, msg=" + e.getMessage());
            }
        }
        
        return sInstance;
    }
    
    public void fastScrollDown() {
        int x = sMobileSize.x / 5;
        int top = (int)(sMobileSize.y * 0.25);
        int bottom = (int)(sMobileSize.y * 0.75);
        sUiDevice.drag(x, bottom, x, top, 5);
    }
    
    public void fastScrollUp(int times) {
        for (int i = 0; i < times; i++) {
            int x = sMobileSize.x / 5;
            int top = (int)(sMobileSize.y * 0.25);
            int bottom = (int)(sMobileSize.y * 0.75);
            sUiDevice.drag(x, top, x, bottom, 5);
        }
    }
    
    public void fastScrollUp() {
        fastScrollUp(1);
    }
    
    private static Point sMobileSize = null;
    private static MobileDevice sInstance = null;
    private static UiDevice sUiDevice = null;
}
