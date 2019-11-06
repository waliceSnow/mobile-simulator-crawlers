package com.crawler.qqcrawler.net;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.crawler.qqcrawler.struct.ApplicationInfor;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class QQMessagePoster {
    protected static String sRemoteServerUrl = "http://182.92.149.232:5001";
    
    static public boolean postUser(String nickName, String qqNum) {
        OkHttpClient client = new OkHttpClient();
        
        String query = new HttpUrl.Builder().scheme("http")
                .host("localhost")
                .setQueryParameter("wechatid", qqNum)
                .setEncodedQueryParameter("wxnickname", nickName)
                .setEncodedQueryParameter("remarkname", qqNum)
                .setQueryParameter("from", "qq").setQueryParameter("district", "")
                .build().query();
        Request request = new Request.Builder().url(sRemoteServerUrl + "/add_user_info?" + query).build();
        
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                ApplicationInfor.errorLogging("message poster: response called not success.");
                return false;
            }
        } catch (IOException e) {
            ApplicationInfor.errorLogging("message poster: post the file failed, qqNum = " + qqNum + ", msg = " + e.getMessage());
            return false;
        }
        
        return true;
    }
    
    static public boolean postFile(String qqNum, File file, String type, Long duration, long ctime) {
        if (0 >= ctime) {
            ctime = System.currentTimeMillis() / 1000;
        }
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/octet-stream");
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("remark_name", qqNum).addFormDataPart("type", type)
                .addFormDataPart("from", "qq").addFormDataPart("ctime", String.valueOf(ctime))
                .addFormDataPart("duration", String.valueOf(duration));
        if (!file.exists()) {
            ApplicationInfor.errorLogging("message poster: file " + file.getName() + " not exist!");
            return false;
        }
        builder.addFormDataPart("image01", file.getName(), RequestBody.create(mediaType, file));
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(sRemoteServerUrl + "/upload").post(requestBody).build();
        
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                ApplicationInfor.errorLogging("message poster: post file response called not success.");
                return false;
            }
        } catch (IOException e) {
            ApplicationInfor.errorLogging("message poster: post the file failed, file = " + file.getName() + ", msg = " + e.getMessage());
            return false;
        }
        
        return true;
    }
    
    static public boolean postImageUrl(String qqNum, String imageUrl, long ctime) {
        if (0 >= ctime) {
             ctime = System.currentTimeMillis() / 1000;
         }
         OkHttpClient client = new OkHttpClient();

         SortedMap<String, String> queryInfos = new TreeMap<String, String>();
         HttpUrl.Builder builder = new HttpUrl.Builder().scheme("http")
                 .host("localhost");
         queryInfos.put("url", imageUrl);
         queryInfos.put("from", "qq");
         queryInfos.put("remark_name", qqNum);
         queryInfos.put("ctime", String.valueOf(ctime));
         String sign = sign(queryInfos);
         Set<Entry<String, String>> entrys = queryInfos.entrySet();
         for (Iterator<Entry<String, String>> mapItr = entrys.iterator();
                 mapItr.hasNext(); ) {
             Entry<String, String> entry = mapItr.next();
             builder = builder.setEncodedQueryParameter(entry.getKey(), entry.getValue().replace("+", "%2B"));
         }
         builder = builder.setEncodedQueryParameter("sign", sign);
         String query = builder.build().query();
         Request request = new Request.Builder().url(sRemoteServerUrl + "/upload_url?" + query).build();
         
         try {
             Response response = client.newCall(request).execute();
             if (!response.isSuccessful()) {
                 ApplicationInfor.errorLogging("message poster: post image url response called not success.");
                 return false;
             }
         } catch (IOException e) {
             ApplicationInfor.errorLogging("message poster: post the imageUrl failed, qqNum = " + qqNum + ", msg = " + e.getMessage());
             return false;
         }
         
         return true;
    }
    
    static public boolean postExtendTitle(String qqNum, String content, long ctime) {
        if (0 >= ctime) {
            ctime = System.currentTimeMillis() / 1000;
        }
        OkHttpClient client = new OkHttpClient();

        SortedMap<String, String> queryInfos = new TreeMap<String, String>();
        HttpUrl.Builder builder = new HttpUrl.Builder().scheme("http")
                .host("localhost");
        queryInfos.put("content", content);
        queryInfos.put("from", "qq");
        queryInfos.put("remark_name", qqNum);
        queryInfos.put("ctime", String.valueOf(ctime));
        String sign = sign(queryInfos);
        Set<Entry<String, String>> entrys = queryInfos.entrySet();
        for (Iterator<Entry<String, String>> mapItr = entrys.iterator();
                mapItr.hasNext(); ) {
            Entry<String, String> entry = mapItr.next();
            builder = builder.addQueryParameter(entry.getKey(), entry.getValue().replace("+", "%2B").replace("@", "%40"));
        }
        builder = builder.addQueryParameter("sign", sign);
        String query = builder.build().query();
        Request request = new Request.Builder().url(sRemoteServerUrl + "/add_title?" + query).build();
         
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                ApplicationInfor.errorLogging("message poster: post extend-title response called not success.");
                return false;
            }
        } catch (IOException e) {
            ApplicationInfor.errorLogging("message poster: post the extend-title failed, qqNum = " + qqNum + ", msg = " + e.getMessage());
            return false;
        }

        return true;
    }
    
    static protected String sign(SortedMap<String, String> params) {
        String salt = "mhxzkhl";
        Set<Entry<String, String>> entrys = params.entrySet();
        Iterator<Entry<String, String>> it = entrys.iterator();
        StringBuffer result = new StringBuffer();
        while (it.hasNext()) {
            Entry<String, String> entry = it.next();
            result.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        result.append(salt);
        return md5(result.toString());
    }
    
    static protected String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
};