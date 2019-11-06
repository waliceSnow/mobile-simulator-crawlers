package com.crawler.wechatcrawler.struct;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import com.android.uiautomator.core.UiDevice;

public class ApplicationInfor {
    public BlockingQueue<String> logMsgQueue;
    public ViewStatus viewStatus;
    static private ApplicationInfor _applicationInfor;
    static private SimpleDateFormat _logDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    final static private String sErrorScreenShot = "/sdcard/tencent/MobileQQ/log/screen_shot";
    
    private ApplicationInfor() {
        logMsgQueue = new LinkedBlockingDeque<String>();
        viewStatus = new ViewStatus();
    }
   
    static public ApplicationInfor getInstance() {
        if (null == _applicationInfor) {
            _applicationInfor = new ApplicationInfor();
        }
        
        return _applicationInfor;
    }
    
    static public void errorLogging(String logStr) {
        if (null == _applicationInfor) {
            _applicationInfor = new ApplicationInfor();
        }
        // take a picture shot.
        UUID uuid = UUID.randomUUID();
        File screenShotFile = new File(sErrorScreenShot, uuid.toString() + ".png");
        UiDevice.getInstance().takeScreenshot(screenShotFile);
        String logInfor = "ERROR " + _logDateFormat.format(Calendar.getInstance().getTime()) + " " + uuid.toString() + " " + logStr;
        _applicationInfor.logMsgQueue.offer(logInfor);
        System.out.println(logInfor);
    }
    
    static public void warningLogging(String logStr) {
        if (null == _applicationInfor) {
            _applicationInfor = new ApplicationInfor();
        }
        UUID uuid = UUID.randomUUID();
        String logInfor = "WARNING " + _logDateFormat.format(Calendar.getInstance().getTime()) + " " + uuid.toString() + " " + logStr;
        _applicationInfor.logMsgQueue.offer(logInfor);
        System.out.println(logInfor);
    }
    
    static public void infoLogging(String logStr) {
        if (null == _applicationInfor) {
            _applicationInfor = new ApplicationInfor();
        }
        UUID uuid = UUID.randomUUID();
        _applicationInfor.logMsgQueue.offer("INFO " + _logDateFormat.format(Calendar.getInstance().getTime()) + " " + uuid.toString() + " " + logStr);
    }
    
    static public void debugLogging(String logStr) {
        if (null == _applicationInfor) {
            _applicationInfor = new ApplicationInfor();
        }
        // take a picture shot.
        UUID uuid = UUID.randomUUID();
        File screenShotFile = new File(sErrorScreenShot, uuid.toString() + ".png");
        UiDevice.getInstance().takeScreenshot(screenShotFile);
        _applicationInfor.logMsgQueue.offer("DEBUG " + _logDateFormat.format(Calendar.getInstance().getTime()) + " " + uuid.toString() + " " + logStr);
    }
}