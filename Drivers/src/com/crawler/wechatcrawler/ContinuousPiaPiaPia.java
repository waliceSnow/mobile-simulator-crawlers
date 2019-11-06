package com.crawler.wechatcrawler;

import com.android.uiautomator.testrunner.UiAutomatorTestCase;

import android.graphics.Rect;

import java.io.File;
import java.math.*;
import java.io.IOException;

import android.graphics.*;

import com.android.uiautomator.core.*;

public class ContinuousPiaPiaPia extends UiAutomatorTestCase {
    final static String PORT = "5001";
    public void testMain() {
        while (true) {
            if (this.startPiaPiaPia()) {
                System.out.println("Start piapiapia success.");
            } else if (this.startPiaPiaPiaByPos()) {
                System.out.println("Start piapiapia position success.");
            }
            while (this.isPiaPiaPia()) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    System.out.println("WTF, sleep failed!");
                }
            }
            
        }
    }
    
    public boolean isPiaPiaPia() {
        UiObject closeButton = new UiObject(new UiSelector().text("关闭服务"));
        if (closeButton.exists()) {
            return true;
        }
        closeButton = new UiObject(new UiSelector().className("android.widget.LinearLayout").resourceId("android:id/buttonPanel"));
        return closeButton.exists();
    }
    
    public boolean startPiaPiaPia() {
        UiDevice uiDevice = UiDevice.getInstance();
        // goto home page, double click home can back to home page.
        for (int i = 0; i < 5; i++) {
            uiDevice.pressBack();
        }
        uiDevice.pressHome();
        uiDevice.pressHome();
        // click the wechat icon.
        UiObject wechatButton = new UiObject(new UiSelector().text("微信"));
        if (!wechatButton.exists()) {
            System.out.println("Wecht not found.");
            return false;
        }
        try {
            wechatButton.click();
            Thread.sleep(5000);
        } catch (UiObjectNotFoundException | InterruptedException e) {
            System.out.println("Click wechat button exception, ex=" + e.getMessage());
            return false;
        }
        // click property button.
        UiObject propertyButton = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/acu"));
        int retryTimes = 5;     // max back times
        int execTimes = 0;
        for (;execTimes < retryTimes; execTimes++) {
            if (!propertyButton.exists()) {
                System.out.println("propertyButton not found.");
                uiDevice.pressBack();
                continue;
            }
            try {
                if (!propertyButton.click()) {
                    System.out.println("Click property button failed.");
                    uiDevice.pressBack();
                    continue;
                }
                break;
            } catch (UiObjectNotFoundException e) {
                System.out.println("Click property button exception, ex=" + e.getMessage());
                uiDevice.pressBack();
                continue;
            }
        }
        if (execTimes > retryTimes) {
            System.out.println("execTimes > retryTimes, all failed.");
            return false;
        }
        // click driver button.
        UiObject driverButton = new UiObject(new UiSelector().text("开始吧老司机"));
        if (!driverButton.exists()) {
            System.out.println("driver button not found!");
            return false;
        }
        try {
            if (!driverButton.click()) {
                System.out.println("Driver button click failed.");
                return false;
            }
        } catch (UiObjectNotFoundException e) {
            System.out.println("Click driver button exception, ex=" + e.getMessage());
            return false;
        }
        // update port text.
        UiObject portText = new UiObject(new UiSelector().text("5000"));
        if (!portText.exists()) {
            System.out.println("port text not found.");
            return false;
        }
        try {
            if (!portText.setText(ContinuousPiaPiaPia.PORT)) {
                System.out.println("port text set text failed.");
                return false;
            }
        } catch (UiObjectNotFoundException e) {
            System.out.println("Set port exception, ex=" + e.getMessage());
            return false;
        }
        // click start button.
        UiObject startButton = new UiObject(new UiSelector().resourceId("android:id/button2").text("开始"));
        if (!startButton.exists()) {
            System.out.println("Start button not found.");
            return false;
        }
        try {
            if (!startButton.click()) {
                
            }
        } catch (UiObjectNotFoundException e) {
            System.out.println("start button click exception, ex=" + e.getMessage());
            return false;
        }
        
        return true;
    }

    public boolean startPiaPiaPiaByPos() {
        UiDevice uiDevice = UiDevice.getInstance();
        // goto home page, double click home can back to home page.
        for (int i = 0; i < 5; i++) {
            uiDevice.pressBack();
        }
        uiDevice.pressHome();
        uiDevice.pressHome();
        // click the wechat icon.
        UiObject wechatButton = new UiObject(new UiSelector().text("微信"));
        if (!wechatButton.exists()) {
            System.out.println("Wecht not found.");
            return false;
        }
        try {
            wechatButton.click();
            Thread.sleep(5000);
        } catch (UiObjectNotFoundException | InterruptedException e) {
            System.out.println("Click wechat button exception, ex=" + e.getMessage());
            return false;
        }
        Rect topBarRect = null;
        // click property button.
        UiObject propertyButton = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/acu"));
        int retryTimes = 5;     // max back times
        int execTimes = 0;
        for (;execTimes < retryTimes; execTimes++) {
            // calc the rec.
            UiObject topBar = new UiObject(new UiSelector().resourceId("android:id/action_bar"));
            if (!topBar.exists()) {
                System.out.println("Top bar not found.");
                uiDevice.pressBack();
                continue;
            }
            try {
                topBarRect = topBar.getBounds();
            } catch (UiObjectNotFoundException e1) {
                System.out.println("Get top bar rect excpetion, ex=" + e1.getMessage());
                uiDevice.pressBack();
                continue;
            }
            if (!propertyButton.exists()) {
                System.out.println("propertyButton not found.");
                uiDevice.pressBack();
                continue;
            }
            try {
                if (!propertyButton.click()) {
                    System.out.println("Click property button failed.");
                    uiDevice.pressBack();
                    continue;
                }
                break;
            } catch (UiObjectNotFoundException e) {
                System.out.println("Click property button exception, ex=" + e.getMessage());
                uiDevice.pressBack();
                continue;
            }
        }
        if (execTimes > retryTimes) {
            System.out.println("execTimes > retryTimes, all failed.");
            return false;
        }
        // click driver button.
        UiObject driverButton = new UiObject(new UiSelector().text("开始吧老司机"));
        if (!driverButton.exists()) {
            System.out.println("driver button not found!");
            return false;
        }
        Rect driverRect = null;
        try {
            driverRect = driverButton.getBounds();
        } catch (UiObjectNotFoundException e) {
            System.out.println("Get driver button bound exception, ex=" + e.getMessage());
            return false;
        }
        UiObject fatherObj = new UiObject(new UiSelector().className("android.widget.FrameLayout"));
        if (!fatherObj.exists()) {
            System.out.println("Father object not found!");
            return false;
        }
        Rect fatherRect = null;
        try {
            fatherRect = fatherObj.getBounds();
        } catch (UiObjectNotFoundException e) {
            System.out.println("Father object get bound exception, ex=" + e.getMessage());
            return false;
        }
        int ordinate = topBarRect.bottom - fatherRect.top;
        int screenWidth = topBarRect.right;
        topBarRect.right -= 30;     // right side
        int coordinate = topBarRect.right - fatherRect.right;
        driverRect.top += ordinate;
        driverRect.bottom += ordinate;
        driverRect.left += coordinate;
        driverRect.right += coordinate;
        if (!uiDevice.click((driverRect.left + driverRect.right) / 2, (driverRect.top + driverRect.bottom) / 2)) {
            System.out.println("Click new driver button failed.");
            return false;
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        String pngPath = "/sdcard/tencent/MicroMsg/startPage.png";
        File screenPicPath = new File(pngPath);
        if (!uiDevice.takeScreenshot(screenPicPath)) {
            System.out.println("Take screen short failed.");
            return false;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(pngPath);
        
        int xPos = -1;
        int yPos = -1;
        int length = 0;
        for (int i = 0 ; i < bitmap.getHeight(); i++) {
            if (length >= 200) {
                break;
            }
            for (int j = 0; j < bitmap.getWidth(); j++) {
                if (length >= 200) {
                    break;
                }
                int color = bitmap.getPixel(j, i);
                int red = (color & 0x00ff0000) >> 16;
                int green = (color & 0x0000ff00) >> 8;
                int blue = color & 0x000000ff;
                int det = Math.abs(red - 80) + Math.abs(green - 196) + Math.abs(blue - 40);
                if (30 > det) {
                    xPos = i;
                    yPos = j;
                    while (j < bitmap.getWidth() && length < 200) {
                        color = bitmap.getPixel(j, i);
                        red = (color & 0x00ff0000) >> 16;
                        green = (color & 0x0000ff00) >> 8;
                        blue = color & 0x000000ff;
                        det = Math.abs(red - 80) + Math.abs(green - 196) + Math.abs(blue - 40);
                        if (det > 30) {
                            xPos = -1;
                            yPos = -1;
                            length = 0;
                            break;
                        } else {
                            length++;
                        }
                        j++;
                    }
                }
            }
        }
        if (xPos <= 0 || yPos <= 0) {
            System.out.println("Not found the dialog det.");
            return false;
        }
        // update port text.
        int leftDet = 0;
        int topDet = 0;
        UiObject ipText = new UiObject(new UiSelector().className("android.widget.EditText"));
        if (!ipText.exists()) {
            System.out.println("ip text not found.");
            return false;
        }
        try {
            Rect objRect = ipText.getBounds();
            int width = objRect.right - objRect.left;
            int left = (screenWidth - width) / 2;
            leftDet = left - objRect.left;
            int top = objRect.top + xPos - objRect.bottom;
            topDet = top - objRect.top;
        } catch (UiObjectNotFoundException e) {
            System.out.println("fixstartpos obj rect exception, ex=" + e.getMessage());
        }
        UiObject portText = new UiObject(new UiSelector().text("5000"));
        if (!portText.exists()) {
            System.out.println("port text not found.");
            return false;
        }
        try {
            Rect portRect = portText.getBounds();
            int x = portRect.centerX() + leftDet;
            int y = portRect.centerY() + topDet;
            uiDevice.click(x, y);
            Thread.sleep(1000);
            UiObject realPortText = new UiObject(new UiSelector().focused(true));
            realPortText.setText(ContinuousPiaPiaPia.PORT);
        } catch (UiObjectNotFoundException | InterruptedException e1) {
            System.out.println("Get port text bound exception, ex=" + e1.getMessage());
            return false;
        }
        
        try {
            if (!portText.setText(ContinuousPiaPiaPia.PORT)) {
                System.out.println("port text set text failed.");
                return false;
            }
            Thread.sleep(1000);
        } catch (UiObjectNotFoundException | InterruptedException e) {
            System.out.println("Set port exception, ex=" + e.getMessage());
        }
        
        // click start button.
        UiObject startButton = new UiObject(new UiSelector().resourceId("android:id/button2").text("开始"));
        if (!startButton.exists()) {
            System.out.println("Start button not found.");
            return false;
        }
        try {
            Rect startRect = startButton.getBounds();
            int x = startRect.centerX() + leftDet;
            int y = startRect.centerY() + topDet;
            uiDevice.click(x, y);
        } catch (UiObjectNotFoundException e) {
            System.out.println("start button click exception, ex=" + e.getMessage());
            return false;
        }
        
        return true;
    }
    
    public void printRect(String name, Rect rect) {
        System.out.println(name + " top=" + rect.top + " bottom=" + rect.bottom + " left=" + rect.left + " right=" + rect.right);
    }
    
    public void fixStartPos(UiObject obj, int screenWidth, int y) {

    }
}
