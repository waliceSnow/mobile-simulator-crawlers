cd C:\Users\wal_ice\Documents\JavaWP\WeiboCrawler
adb push bin/WeiboCrawler.jar data/local/tmp
adb shell uiautomator runtest WeiboCrawler.jar -c com.uia.crawler.weibo.WeiboCrawler