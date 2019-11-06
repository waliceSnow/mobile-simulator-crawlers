package com.crawler.qqcrawler.struct;

import java.io.File;

import com.android.uiautomator.core.UiObject;

public class QQMessage {
    public enum QQMessageType {
        PICTURE,
        VOICE,
        TEXT,
        OTHERS,
    };
    
    public QQMessage() {
        messageType = QQMessageType.OTHERS;
    }
   
    public QQMessageType messageType;
    public String text;
    public UiObject uiItemObj;
    public File messageFile;
};