adb push bin\QQCrawler.jar data/local/tmp
adb shell uiautomator runtest QQCrawler.jar -c com.uia.crawler.qq.QQCrawler