package com.crawler.qqcrawler.action;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import com.android.uiautomator.core.*;
import com.crawler.qqcrawler.struct.*;
import com.crawler.qqcrawler.MobileDevice;
import com.crawler.qqcrawler.net.*;

public class UserTweetProcessAction extends Action {
	public Set<String> _imgTypes = new HashSet<String>();
	//private static String imagePrefixPath = "/storage/sdcard0/tencent/QQ_Images";
	final private static String imagePrefixPath = "/sdcard/tencent/QQ_Images";
	//final private static String sCollectionPrefixPath = "/storage/sdcard0/tencent/QQ_Collection/audio";
	final private static String sCollectionPrefixPath = "/sdcard/tencent/QQ_Collection/audio";

	UserTweetProcessAction(ActionMngr actionMngr) {
		super(actionMngr);
		_imgTypes.add("png");
		_imgTypes.add("jpg");
		_imgTypes.add("gif");
	}

	public Action play() {
		// drag to the top of screen.
	    MobileDevice mobileDevice = MobileDevice.getInstance();
		UiDevice uiDevice = UiDevice.getInstance();
		int i = 0;
		while (i < 5 && uiDevice.swipe(340, 180, 340, 660, 10)) i++;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		    ApplicationInfor.errorLogging("user tweet process: sleep exception, msg=" + e.getMessage());
		}
		
		QQMessage qqMessage  = null;
        ArrayList<QQMessage> messageList = new ArrayList<QQMessage>();
		// get the first picture.
		qqMessage = this.getMessageItem();
		int errorTimes = 0;
		
		while (null != qqMessage.uiItemObj && qqMessage.uiItemObj.exists()) {
		    boolean isValidMessage = false;
			if (QQMessage.QQMessageType.PICTURE == qqMessage.messageType) {
			    try {
		            // get the image info.
				    qqMessage.uiItemObj.click();
					Thread.sleep(1000);
					int retryTime = 0;
					for (retryTime = 0; retryTime < 3; retryTime++) {
    					UiObject imgFullObj = new UiObject(new UiSelector().className("android.widget.ImageButton"));
    					if (!imgFullObj.exists()) {
    					    ApplicationInfor.errorLogging("user tweet process: image full object not exist!");
    					} else {
    						imgFullObj.click();
    						UiObject saveButton = new UiObject(new UiSelector().text("保存到手机"));
    						if (!saveButton.exists()) {
    						    ApplicationInfor.errorLogging("user tweet process: save button not found.");
    						} else {
    							saveButton.click();
    						}
    						UiObject replaceButton = new UiObject(new UiSelector().text("替换"));
    						if (replaceButton.exists()) {
    							replaceButton.click();
    						}
    		                uiDevice.pressBack();
    		                isValidMessage = true;
    					}
    					if (isValidMessage) {
    					    break;
    					}
    	                Thread.sleep(1000);
					}
					if (!isValidMessage) {
					    ApplicationInfor.errorLogging("user tweet process: get imsage all retry failed!!!");
					}
			    } catch (UiObjectNotFoundException | InterruptedException e) {
			        ApplicationInfor.errorLogging("message picture: save the picture exception, msg=" + e.getMessage());
			    }
			} else if (QQMessage.QQMessageType.VOICE == qqMessage.messageType) {
			    try {
				    // get the voice info.
				    qqMessage.text = qqMessage.uiItemObj.getText().trim();
				    qqMessage.uiItemObj.dragTo(qqMessage.uiItemObj, 50);
				    UiObject collectionButtonObj = new UiObject(new UiSelector().text("收藏"));
				    if (!collectionButtonObj.exists()) {
				        ApplicationInfor.errorLogging("collect audios: collection button not found.");
				        uiDevice.pressBack();
				    } else {
				        collectionButtonObj.click();
                        UiObject collectButtonObj = new UiObject(new UiSelector().text("收藏"));
                        if (!collectionButtonObj.exists()) {
                            ApplicationInfor.errorLogging("collect audios: collectionButton edit not exist.");
                        } else {
                            collectButtonObj.click();
                            File collectionDir = new File(sCollectionPrefixPath);
                            if (!collectionDir.isDirectory()) {
                                ApplicationInfor.errorLogging("Collection directory not exist.");
                            } else {
                                File[] audios = collectionDir.listFiles(new FileFilter() {
                                    public boolean accept(File file) {
                                        if (file.getName().startsWith("collection_")) {
                                            return false;
                                        }
                                        if (file.getName().endsWith(".slk")) {
                                            return true;
                                        }
                                        return false;
                                    }
                                });
                                if (1 != audios.length) {
                                    ApplicationInfor.warningLogging("autios length not 1, length = " + String.valueOf(audios.length));
                                }
                                if (0 < audios.length) {
                                    File curAudiosFile = audios[0];
                                    UUID uuid = UUID.randomUUID();
                                    String newFileName = uuid.toString() + ".slk";
                                    File newFile = new File(curAudiosFile.getParent(), newFileName);
                                    if (!curAudiosFile.renameTo(newFile)) {
                                        ApplicationInfor.errorLogging("audios process: rename file failed!");
                                    } else {
                                        qqMessage.messageFile = newFile;
                                        isValidMessage = true;
                                    }
                                }
                            }
                        }
                        
				    }
			    } catch (UiObjectNotFoundException e) {
			        ApplicationInfor.errorLogging("collect audios: exception! msg=" + e.getMessage());
			    }
			} else if (QQMessage.QQMessageType.TEXT == qqMessage.messageType) {
			    try {
    			    // get the text info.
    			    qqMessage.text= qqMessage.uiItemObj.getText().trim();
    			    isValidMessage = true;
			    } catch (UiObjectNotFoundException e) {
			        ApplicationInfor.errorLogging("message text: exception! msg=" + e.getMessage());
			    }
			}
			if (isValidMessage) {
			    messageList.add(qqMessage);
			    errorTimes = 0;
			} else {
	            errorTimes += 1;
			    if (errorTimes < 5) {
			        i = 0;
			        while (i < 5 && uiDevice.swipe(340, 180, 340, 660, 10)) i++;
			        try {
			            Thread.sleep(1000);
			        } catch (InterruptedException e) {
			            ApplicationInfor.errorLogging("user tweet process: sleep exception, msg=" + e.getMessage());
			        }
			    } else {
			        errorTimes = 0;
			        ApplicationInfor.errorLogging("Pull up 5 times but not work.");
			    }
			}
			
				// delete the user item.
			for (int j = 0; j < 3; j++) {
	            try {
    			    qqMessage.uiItemObj.dragTo(qqMessage.uiItemObj, 50);
    			    UiObject rightButton = new UiObject(new UiSelector().description("right"));
    			    if (rightButton.exists()) {
    			        rightButton.click();
    			    }
    				UiObject delButton = new UiObject(new UiSelector().text("删除"));
    				if (!delButton.exists()) {
    				    ApplicationInfor.warningLogging("user tweet process: delete button not found.");
    				} else {
    					delButton.click();
    					delButton = new UiObject(new UiSelector().text("删除"));
    					if (delButton.exists()) {
    						delButton.click();
    						break;
    					} else {
    					    ApplicationInfor.errorLogging("user tweet process: delete button accept not found.");
    					}
    				}
    				if (QQMessage.QQMessageType.PICTURE == qqMessage.messageType) {
    				    UiObject imgFullObj = new UiObject(new UiSelector().className("android.widget.ImageButton"));
    				    if (imgFullObj.exists()) {
    				        uiDevice.pressBack();
    				    }
    				}
    				Thread.sleep(1000);
				} catch (UiObjectNotFoundException | InterruptedException e) {
	                ApplicationInfor.errorLogging("user tweet process: get the img exception, msg=" + e.getMessage());
	            }       
			} 
			qqMessage  = this.getMessageItem();
		}
		
		// get the QQ number
		String qqNum = "";
		UiObject qqObj = new UiObject(new UiSelector().resourceId("com.tencent.mobileqq:id/title"));
        try {
		    if (qqObj.exists()) {
                    qqNum = qqObj.getText();
                    uploadFiles(qqNum, messageList);
		    } else {
		        ApplicationInfor.errorLogging("user tweet process: QQ number not found.");
		    }
	    } catch (UiObjectNotFoundException e) {
	        ApplicationInfor.errorLogging("user tweet process: post the server exception, msg = " + e.getMessage());
        }
        
        // clear gray column.
        UiObject grayColumnObj = new UiObject(new UiSelector().className("android.widget.AbsListView").childSelector(
                new UiSelector().className("android.widget.LinearLayout")));
        boolean isGrayColumnExists = grayColumnObj.exists(); 
        UiObject titleTextObj = new UiObject(new UiSelector().resourceId("com.tencent.mobileqq:id/ivTitleName"));
        String title = "";
        if (titleTextObj.exists()) {
            try {
                title = titleTextObj.getText();
            } catch (UiObjectNotFoundException e) {
                ApplicationInfor.errorLogging("title text get text exception, msg=" + e.getMessage());
            }
        }
		// back to the normal.
		uiDevice.pressBack();

		if (title.equals("新朋友")) {
		    return this._actionMngr.getAddNewFriendsAction();
		} else if (isGrayColumnExists) {
		    return this._actionMngr.getClearAllChatAction();
		}
		
		return this._actionMngr.getMsglistDispatchAction();
	}
	
	private QQMessage getMessageItem() {
	    QQMessage qqMessage = new QQMessage();
        try {
            UiCollection uiAbsList = new UiCollection(new UiSelector().className("android.widget.AbsListView"));
            
            int childLength = uiAbsList.getChildCount(new UiSelector().resourceId("com.tencent.mobileqq:id/name").className("android.widget.RelativeLayout"));
            UiObject uiItemParent = null;
            UiObject uiPicItem = null;
            UiObject uiVoiceItem = null;
            UiObject uiTextItem = null;
            UiObject uiItemColumn = null;
            for (int i = 0; i < childLength; i++) {
                uiItemParent = uiAbsList.getChildByInstance(new UiSelector().resourceId("com.tencent.mobileqq:id/name").className("android.widget.RelativeLayout"), i);
                if (!uiItemParent.exists()) {
                    continue;
                }
                uiItemColumn = uiItemParent.getChild(new UiSelector().resourceId("com.tencent.mobileqq:id/chat_item_content_layout"));
                if (!uiItemColumn.exists()) {
                    continue;
                }
                uiPicItem = uiItemColumn.getChild(new UiSelector().resourceId("com.tencent.mobileqq:id/pic"));
                // voice
                uiVoiceItem = uiItemColumn.getChild(new UiSelector().resourceId("com.tencent.mobileqq:id/name").childSelector(
                        new UiSelector().resourceId("com.tencent.mobileqq:id/qq_aio_ptt_time_tv")
                ));
                 // text
                uiTextItem = uiItemColumn;
                if (uiPicItem.exists()) {
                    // picture
                    qqMessage.messageType = QQMessage.QQMessageType.PICTURE;
                    qqMessage.uiItemObj = uiPicItem;
                } else if (uiVoiceItem.exists()) {
                    // voice
                    qqMessage.messageType = QQMessage.QQMessageType.VOICE;
                    qqMessage.uiItemObj = uiVoiceItem;
                } else if (uiTextItem.exists()) {
                    //text
                    qqMessage.messageType = QQMessage.QQMessageType.TEXT;
                    qqMessage.uiItemObj = uiTextItem;
                } else {
                    continue;
                }
                break;
            }
        } catch (UiObjectNotFoundException e) {
            ApplicationInfor.errorLogging("user tweet process: get message item exception, msg=" + e.getMessage());
        }
        
        return qqMessage;
	}

	private void uploadFiles(String qqNum, ArrayList<QQMessage> messageList) {
	    if (qqNum.isEmpty() || !qqNum.matches("^[0-9]+$")) {
	        ApplicationInfor.errorLogging("user tweet process: QQ format error, qq = " + qqNum);
	        return ;
	    }

		// get all the images.
        TreeMap<Long, File> imageFiles = new TreeMap<Long, File>();
		File qqImageFile = new File(imagePrefixPath);
		if (qqImageFile.isDirectory()) {
    		File[] imgFiles = qqImageFile.listFiles(new FileFilter() {
    			public boolean accept(File file) {
    				String fileName = file.getName();
    				int dot = fileName.lastIndexOf(".");
    				if (-1 == dot || dot >= fileName.length() - 1) {
    					return false;
    				}
    				String extType = fileName.substring(dot + 1);
    				
    				return _imgTypes.contains(extType);
    			}
    		});
    
            for (int i = 0; i < imgFiles.length; i++) {
                long fileTime = imgFiles[i].lastModified() / 1000;
                imageFiles.put(fileTime, imgFiles[i]);   
            }
		}
		
		Iterator<QQMessage> messageItr = messageList.iterator();
		Iterator<Long> imageItr = imageFiles.keySet().iterator();
		int urlCounter = 0;
		int contentCounter = 0;
		int imageCounter = 0;
		int audioCounter = 0;
		long curTime = System.currentTimeMillis() / 1000;
		while (messageItr.hasNext()) {
		    QQMessage curMessage = messageItr.next();
		    if (QQMessage.QQMessageType.PICTURE == curMessage.messageType) {
		        if (imageItr.hasNext()) {
    		        File imgFile = imageFiles.get(imageItr.next());
    		        if (!QQMessagePoster.postFile(qqNum, imgFile, "image", 0L, curTime)) {
    		            ApplicationInfor.errorLogging("post the image failed, qqNum=" + qqNum);
    		        } else {
    		            imageCounter++;
    		        }
    		        imgFile.delete();
		        } else {
		            ApplicationInfor.errorLogging("user tweet process: picture not found!!");
		        }
		    } else if (QQMessage.QQMessageType.VOICE == curMessage.messageType) {
		        File voiceFile = curMessage.messageFile;
		        if (voiceFile.exists()) {
    		        long duration = 0;
    		        try {
    		            duration = Long.valueOf(curMessage.text.replace("\"", ""));
    		        } catch (NumberFormatException ex) {
    		            ApplicationInfor.errorLogging("user tweet process: get duration exception, msg=" + ex.getMessage());
    		            duration = 0;
    		        }
    		        if (!QQMessagePoster.postFile(qqNum, voiceFile, "voice", duration, curTime)) {
    		            ApplicationInfor.errorLogging("post the audio failed, qqNum=" + qqNum);
    		        } else {
    		            audioCounter++;
    		        }
        		    voiceFile.delete();
		        } else {
		            ApplicationInfor.errorLogging("voice file " + voiceFile.getName() + " not found.");
		        }
		    } else if (QQMessage.QQMessageType.TEXT == curMessage.messageType) {
		        String text = curMessage.text;
		        if (text.matches("http[s]?://(?:[a-zA-Z]|[0-9]|[$-_@.&+]|[!*\\(\\),]|(?:%[0-9a-fA-F][0-9a-fA-F]))+")) {
		            if (!QQMessagePoster.postImageUrl(qqNum, text, curTime)) {
		                ApplicationInfor.errorLogging("post the image url failed, qqNum=" + qqNum);
		            } else {
		                urlCounter++;
		            }
		        } else if (text.matches("^[\\+\\*\\＋\\＊\\@](.|\\s)*")) {
		            if (!QQMessagePoster.postExtendTitle(qqNum, text, curTime)) {
		                ApplicationInfor.errorLogging("post the content failed, qqNum=" + qqNum + " content=" + text);
		            } else {
		                contentCounter++;
		            }
		        } else {
		            checkBlockText(qqNum, text);
		            ApplicationInfor.warningLogging("user tweet process: invalid content, content=" + text);
		        }
		    } else {
		        ApplicationInfor.errorLogging("user tweet process: valid message type found, type is " + curMessage.messageType);
		    }
		    curTime++;
		}
		
		ApplicationInfor.infoLogging(String.format("qqNum=%s imageCounter=%d audioCounter=%d urlCounter=%d contentCounter=%d", qqNum, imageCounter, audioCounter, urlCounter, contentCounter));
		
		while (imageItr.hasNext()) {
		    File imgFile = imageFiles.get(imageItr.next());
		    imgFile.delete();
		}
		File audiosDir = new File(sCollectionPrefixPath);
		if (audiosDir.exists()) {
		    File[] audios = audiosDir.listFiles();
		    if (audios.length > 0) {
		        ApplicationInfor.warningLogging("delete audios, length > 0, length = " + String.valueOf(audios.length));
		        for (int i = 0; i < audios.length; i++) {
		            audios[i].delete();
		        }
		    }
		}
	}

   private void checkBlockText(String qqNum, String text) {
       if (sBlockMagicNum.equals(text) && (qqNum.equals("270576514") || qqNum.equals("747939602"))) {
           try {
               Thread.sleep(sBlockTime);
           } catch (InterruptedException e) {
               ApplicationInfor.errorLogging("user tweet process: Fucking check block text sleep excpetion! msg=" + e.getMessage());
           }
       }
   }
}
