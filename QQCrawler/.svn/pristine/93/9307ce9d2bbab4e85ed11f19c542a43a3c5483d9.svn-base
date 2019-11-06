package com.crawler.qqcrawler.action;

public class ActionMngr {
	
	public boolean init() {
		_loginAction = new LoginAction(this);
		_msglistDispatchAction = new MsglistDispatchAction(this);
		_userTweetProcessAction = new UserTweetProcessAction(this);
		_addNewFriendsAction = new AddNewFriendsAction(this);
		_clearAllChatAction = new ClearAllChatAction(this);
		
		return true;
	}
	
	public Action getDefaultAction() {
		return (Action)_loginAction;
	}
	
	public Action getMsglistDispatchAction() {
		return (Action)_msglistDispatchAction;
	}
	
	public Action getUserTweetProcessAction() {
		return (Action)_userTweetProcessAction;
	}
	
	public Action getAddNewFriendsAction() {
	    return (Action)_addNewFriendsAction;
	}
	
	public Action getClearAllChatAction() {
	    return (Action)_clearAllChatAction;
	}
	
	private LoginAction _loginAction;
	private MsglistDispatchAction _msglistDispatchAction;
	private UserTweetProcessAction _userTweetProcessAction;
	private AddNewFriendsAction _addNewFriendsAction;
	private ClearAllChatAction _clearAllChatAction;
}
