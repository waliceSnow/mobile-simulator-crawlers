package com.crawler.qqcrawler.action;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.crawler.qqcrawler.struct.ApplicationInfor;

public class ClearAllChatAction extends Action {

    ClearAllChatAction(ActionMngr actionMngr) {
        super(actionMngr);
    }

    @Override
    public Action play() {
        // 点击头像进入设置页面
        UiObject settingAndHeaderButtonObj = new UiObject(new UiSelector().description("帐户及设置按钮"));
        if (!settingAndHeaderButtonObj.exists()) {
            ApplicationInfor.errorLogging("Setting-header button not exist!");
            return _actionMngr.getMsglistDispatchAction();
        }
        try {
            settingAndHeaderButtonObj.click();
        } catch (UiObjectNotFoundException e) {
            ApplicationInfor.errorLogging("Setting-header button click exception, msg=" + e.getMessage());
            return _actionMngr.getMsglistDispatchAction();
        }
        // 点击设置按钮
        UiObject settingButtonObj = new UiObject(new UiSelector().text("设置"));
        if (!settingButtonObj.exists()) {
            ApplicationInfor.errorLogging("setting-btton not exist!");
            pressBack();
            return _actionMngr.getMsglistDispatchAction();
        }
        try {
            settingButtonObj.click();
        } catch (UiObjectNotFoundException e) {
            ApplicationInfor.errorLogging("setting-button exception, msg=" + e.getMessage());
            pressBack();
            return _actionMngr.getMsglistDispatchAction();
        }
        // 点击聊天记录按钮
        UiObject chatHistoryButton = new UiObject(new UiSelector().text("聊天记录"));
        if (!chatHistoryButton.exists()) {
            ApplicationInfor.errorLogging("chat-history button not exist!");
            pressBack(2);
            return _actionMngr.getMsglistDispatchAction();
        }
        try {
            chatHistoryButton.click();
        } catch (UiObjectNotFoundException e) {
            ApplicationInfor.errorLogging("chat-history button exception, msg=" + e.getMessage());
            pressBack(2);
            return _actionMngr.getMsglistDispatchAction();
        }
        // 点击清空所有聊天记录按钮
        UiObject clearChatHistoryButton = new UiObject(new UiSelector().text("清空所有聊天记录"));
        if (!clearChatHistoryButton.exists()) {
            ApplicationInfor.errorLogging("clear chat-history button not exist!");
            pressBack(3);
            return _actionMngr.getMsglistDispatchAction();
        }
        try {
            clearChatHistoryButton.click();
        } catch (UiObjectNotFoundException e) {
            ApplicationInfor.errorLogging("click clear chat-history button exception, msg=" + e.getMessage());
            pressBack(3);
            return _actionMngr.getMsglistDispatchAction();
        }
        // 点击清空所有聊天记录按钮
        UiObject acceptClearChatHistoryButton = new UiObject(new UiSelector().text("清空所有聊天记录"));
        if (!acceptClearChatHistoryButton.exists()) {
            ApplicationInfor.errorLogging("accept clear chat-history button not exist!");
            pressBack(3);
            return _actionMngr.getMsglistDispatchAction();
        }
        try {
            acceptClearChatHistoryButton.click();
        } catch (UiObjectNotFoundException e) {
            ApplicationInfor.errorLogging("accept clear chat-history button exception, msg=" + e.getMessage());
            pressBack(4);
            return _actionMngr.getMsglistDispatchAction();
        }
        try {
            Thread.sleep(5000);
            pressBack(3);
            Thread.sleep(2000);
        } catch (InterruptedException e1) {
            ApplicationInfor.errorLogging("Sleep exception! msg=" + e1.getMessage());
        }
        
        // 下拉刷新
        UiDevice uiDevice = UiDevice.getInstance();
        uiDevice.swipe(340, 180, 340, 700, 50);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            ApplicationInfor.errorLogging("sleep exception!, msg=" + e.getMessage());
        }

        return _actionMngr.getMsglistDispatchAction();
    }

}
