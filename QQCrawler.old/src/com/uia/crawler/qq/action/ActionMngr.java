package com.uia.crawler.qq.action;

import java.util.*;
import com.uia.crawler.qq.Environment;

import com.uia.crawler.qq.action.Action;

public class ActionMngr {
    public ActionMngr(Environment environment) {
        _actionMap = new HashMap<String, Action>();
        _environment = environment;
        
        _openApkAction = new OpenApkAction(this, _environment);
        _actionMap.put("open_apk_action", (Action)_openApkAction);
        _refreshMsgTabAction = new RefreshMsgTabAction(this, _environment);
        _actionMap.put("refresh_msg_tab_action", (Action)_refreshMsgTabAction);
        _processUserMsgAction = new ProcessUserMsgAction(this, _environment);
        _actionMap.put("process_user_msg_action", _processUserMsgAction);
    }
    
    public boolean init() {
        return true;
    }
    
    public Action firstAction() {
        return (Action)_openApkAction;
    }
    
    public Action getAction(String actionName) {
        return _actionMap.get(actionName);
    }
    
    private Map<String, Action> _actionMap;
    private Environment _environment;
    
    private OpenApkAction _openApkAction;
    private RefreshMsgTabAction _refreshMsgTabAction;
    private ProcessUserMsgAction _processUserMsgAction;
}
