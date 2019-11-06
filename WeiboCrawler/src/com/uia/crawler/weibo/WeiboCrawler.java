package com.uia.crawler.weibo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.*;
import java.io.*;
import okhttp3.*;

import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.core.UiObject;
//import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;

import org.json.*;

public class WeiboCrawler extends UiAutomatorTestCase {
    public final int CRAWL_OK = 0;
    public final int CRAWL_FINISH = 1;
    public final int CRAWL_ERROR = 2;
    public final int MAX_ARTICLE_PRE_CRAWLED = 2;
    public final int MAX_CHOKE_SIZE = 5;
    public final int SLEEP_BLOCK = 20;      // s
    
    public final int INSERT_SUCCESS = 0;
    public final int INSERT_REPEAT = 1;
    public final int INSERT_ERROR = 2;
    
    public final int SINGLE_ARTICLE_INSERT_SUCCESS = 0;
    public final int SINGLE_ARTICLE_INSERT_REPEAT = 1000;
    
    public final String SINGLE_ARTICLE_URL = "http://123.57.249.33:8072/weibo/article/receive_single_crawled";
    
    private ArrayList<WeiboArticle> _weiboArticles;
    private HashSet<String> _articleSet;
    private String _lastCrawledArticle;
    private UiSelector _contentSelector;
    private int _lastContentObjIdx;
    
    public void testMain() {
        _contentSelector = new UiSelector().resourceId("com.sina.weibo:id/tvItemContent");
        _weiboArticles = new ArrayList<WeiboArticle>();
        _articleSet = new HashSet<String>();
        // while
        while (true) {
            // refresh the feed
            refreshFeed();
            
            // get the article information.
            crawlArticleInfo();
            
            // sleep
            // at the first, don't sleep
            long time = System.currentTimeMillis() / 1000;
            try {
                Thread.sleep((SLEEP_BLOCK - time % SLEEP_BLOCK) * 1000);
            } catch (InterruptedException ex) {
                System.out.println("sleep exception.");
            }
            System.out.println("sleep finish.");
        }
	}
    
    private void crawlArticleInfo() {
        int rtnType = 0;
        _weiboArticles.clear();
        _articleSet.clear();
        System.out.println("start crawl article info.");
        
        while (true) {
            _lastContentObjIdx = -1;
            // get the information from one screen
            rtnType = getInforFromOneScreen();
            if (CRAWL_ERROR == rtnType) {
                break;
            }
            if (_weiboArticles.size() >= MAX_ARTICLE_PRE_CRAWLED) {
                rtnType = CRAWL_FINISH;
                System.out.printf("size > %d, all finish.\n", MAX_ARTICLE_PRE_CRAWLED);
                break;
            }
            // flip the next screen
            flipNextScreen();
        }
        
        // set the last article
        if (_weiboArticles.size() > 0) {
            WeiboArticle article = _weiboArticles.get(0);
            _lastCrawledArticle = article.getContent();
            try {
                System.out.println(new String(_lastCrawledArticle.getBytes("utf8")));
                System.out.println(_lastCrawledArticle);
            } catch (UnsupportedEncodingException  ex) {
                System.out.println("string to gbk failed.");
            }
        }
    }
    
    private int getInforFromOneScreen() {
        while (true) {
            UiObject contentObj = new UiObject(_contentSelector.instance(_lastContentObjIdx + 1));
            if (!contentObj.exists()) {
                return CRAWL_FINISH;
            }
            _lastContentObjIdx++;
            try {
                String content = contentObj.getText();
                if (content.equals(_lastCrawledArticle)) {
                    System.out.println("finish by the same content.");
                    return CRAWL_FINISH;
                }
                if (_articleSet.contains(content)) {
                    //System.out.printf("content %s exist.\n", content);
                } else {
                    _articleSet.add(content);
                    WeiboArticle article = new WeiboArticle();
                    article.setContent(content);
                    _weiboArticles.add(article);
                    int _isInsert = 0; //postArticle(article);
                    if (INSERT_SUCCESS != _isInsert) {
                        if (INSERT_REPEAT != _isInsert) {
                            System.out.println("insert error.");
                        }
                        System.out.printf("article already in system. artile=%s\n", content);
                        return CRAWL_FINISH;
                    }
                }
            } catch (UiObjectNotFoundException ex) {
                System.out.println("getInfoFromOneScreen in exception!");
                return CRAWL_ERROR;
            }
        }
    }
    
    private void flipNextScreen() {
        // random for the gap of ad.
        int x = 0;
        x = new Random().nextInt() % 540 + 90;
        UiDevice deviceObj = UiDevice.getInstance();
        deviceObj.drag(x, 1150, x, 470, 20);
    }
    
    private void refreshFeed() {
        UiObject mainPageObj = new UiObject(new UiSelector().description("首页"));
        UiObject loadingObj = new UiObject(new UiSelector().text("加载中"));
        if (!mainPageObj.exists()) {
            System.out.println("main page not exists!");
            return ;
        }
        try {
            mainPageObj.click();
            // waiting for loading.
            loadingObj.waitUntilGone(5000);
        } catch (UiObjectNotFoundException ex) {
            System.out.println("flip next screen exception.");
        }
    }
    
    private int postArticle(WeiboArticle articleInfo) {
        OkHttpClient httpClient = new OkHttpClient();
        
        FormBody.Builder builder = new FormBody.Builder();
        builder.addEncoded("content", articleInfo.getContent());
        builder.addEncoded("time", Long.toString(System.currentTimeMillis() / 1000));
        Request request = new Request.Builder()
                .url(SINGLE_ARTICLE_URL)
                .post(builder.build())
                .build();

        Response response;
        try {
            response = httpClient.newCall(request).execute();
        } catch (IOException e) {
            System.out.println("post article exception.");
            e.printStackTrace();
            return INSERT_ERROR;
        }
   
        try {
            JSONObject jsonObj = new JSONObject(response.body().toString());
            int errno = jsonObj.getInt("errno");
            if (SINGLE_ARTICLE_INSERT_REPEAT == errno) {
                return INSERT_REPEAT;
            } else if (SINGLE_ARTICLE_INSERT_SUCCESS == errno) {
                return INSERT_SUCCESS;
            }
            return INSERT_ERROR;
        } catch (JSONException ex) {
            System.out.println("json decode exception.");
            return INSERT_ERROR;
        }
    }
};
