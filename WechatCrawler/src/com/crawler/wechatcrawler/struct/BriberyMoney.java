package com.crawler.wechatcrawler.struct;

public class BriberyMoney {
    public BriberyMoney(String hashName) {
        _hashName = hashName;
    }
    
    public String getHashName() {
        return _hashName;
    }
    
    private String _hashName;
}
