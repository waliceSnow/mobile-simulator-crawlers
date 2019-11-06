package com.crawler.wechatcrawler.struct;

import java.util.HashMap;
import java.util.Map;

public class GroupInformation {
    
    private GroupInformation(String groupName) {
        _groupName = groupName;
        _briberyMoneys = new HashMap<String, BriberyMoney>();
        _hasBriberyMoney = true;
    }
    public static GroupInformation getGroup(String groupName) {
        GroupInformation groupInfo = sGroups.get(groupName);
        if (null == groupInfo) {
            groupInfo = new GroupInformation(groupName);
            sGroups.put(groupName, groupInfo);
        }
        return groupInfo;
    }
    
    public boolean hasBriberyMoney() {
        return _hasBriberyMoney;
    }
    
    public String getGroupName() {
        return _groupName;
    }
    
    public BriberyMoney getBriberyMoneys(String briberyMoneyName) {
        BriberyMoney briberyMoney = _briberyMoneys.get(briberyMoneyName);
        if (null == briberyMoney) {
            briberyMoney = new BriberyMoney(briberyMoneyName);
            _briberyMoneys.put(briberyMoneyName, briberyMoney);
        }
        
        return briberyMoney;
    }
    
    private String _groupName;
    private Map<String, BriberyMoney> _briberyMoneys;
    private boolean _hasBriberyMoney;
    private static Map<String, GroupInformation> sGroups = new HashMap<String, GroupInformation>();
}
