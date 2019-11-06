package com.uia.crawler.qq.action;

import com.uia.crawler.qq.Environment;

public abstract class Action {
    public Action(ActionMngr actionMngr, Environment environment) {
        _actionMngr = actionMngr;
        _environment = environment;
    }
    public abstract boolean play();
    public abstract Action nextAction();

    protected ActionMngr _actionMngr;
    protected Environment _environment;
}
