package platform.http;

import android.support.annotation.NonNull;
import okhttp3.OkHttpClient;
import platform.http.internal.Md5Utils;
import platform.http.internal.Sha1Utils;
import platform.http.responsehandler.ResponseHandler;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * abstract of class/interface and so on
 *
 * @author kailun on 16/2/25
 */
public class HttpUtils {
    public static final String SIGN = "sign";

    static IPhoneInfoProvider phoneInfoProvider;

    public static void bindPhoneInfoProvider(IPhoneInfoProvider provider) {
        phoneInfoProvider = provider;
    }

    public static void get(String url, Map<String, String> queryParams, ResponseHandler handler) {

        appendPhoneInfo(queryParams);
        queryParams.put(SIGN, calculateSign(queryParams));

        HttpClient.instance().get(url, queryParams, handler);

        OkHttpClient client = HttpClient.instance().client;
        client.dispatcher().cancelAll();
    }

    /**
     * Http UrlEncoded方式的Post请求
     * @param url url地址
     * @param urlQueryParams query参数
     * @param urlEncodedParams 需要编码的post参数
     * @param handler 请求结果处理器
     */
    public static void urlEncodedPost(String url,
                                      Map<String, String> urlQueryParams,
                                      Map<String, String> urlEncodedParams,
                                      ResponseHandler handler) {

        appendPhoneInfo(urlQueryParams);

        // 添加签名
        Map<String, String> merged = mergeParams(urlQueryParams, urlEncodedParams);
        urlQueryParams.put(SIGN, calculateSign(merged));

        HttpClient.instance().urlEncodedPost(url, urlQueryParams, urlEncodedParams, handler);
    }

    public static void urlEncodedPost(String url,
                                      Map<String, String> urlEncodedParams,
                                      ResponseHandler handler) {
        urlEncodedPost(url, new HashMap<String, String>(), urlEncodedParams, handler);
    }

     /**
     * Http Multipart方式的Post请求
     * @param url url地址
     * @param urlQueryParams query参数
     * @param multipartParams mutipart的文本参数
     * @param multipartFileParams multipart的文件参数
     * @param handler 请求结果处理器
     */
    public static void multipartPost(String url,
                                     Map<String, String> urlQueryParams,
                                     Map<String, String> multipartParams,
                                     Map<String, File> multipartFileParams,
                                     ResponseHandler handler) {
        appendPhoneInfo(urlQueryParams);

        // 添加签名
        Map<String, String> merged = mergeParams(urlQueryParams, multipartParams);
        urlQueryParams.put(SIGN, calculateSign(merged));

        HttpClient.instance().multiPartPost(url, urlQueryParams, multipartParams, multipartFileParams, handler);
    }

    public static void multipartPost(String url,
                                     Map<String, String> multipartParams,
                                     Map<String, File> multipartFileParams,
                                     ResponseHandler handler) {
        multipartPost(url, new HashMap<String, String>(), multipartParams, multipartFileParams, handler);
    }


    /**
     * 添加Phone Info
     * @param params 原有的参数
     */
    private static void appendPhoneInfo(Map<String, String> params) {
        if (phoneInfoProvider != null) {
            phoneInfoProvider.modifyForGet(params);
        }
    }


    private static Map<String, String> mergeParams(@NonNull Map<String, String> params1,
                                                         @NonNull Map<String, String> params2) {
        if(params1.isEmpty()) {
            return params2;
        }

        if(params2.isEmpty()) {
            return params1;
        }

        Map<String, String> merged = new HashMap<>();
        merged.putAll(params1);
        merged.putAll(params2);
        return merged;
    }
    /**
     * 计算签名
     * @param params 所有参数
     * @return 签名的值
     */
    private static String calculateSign(@NonNull Map<String, String> params) {
        Set<String> keySet = params.keySet();
        String[] keys = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keys);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.length; i++) {
            String k = keys[i];
            String v = params.get(k);

            sb.append(k).append("=").append(v);
            if (i != keys.length - 1) {
                sb.append("&");
            }
        }

        String url = sb.toString();
        return Md5Utils.encode(Sha1Utils.encode(url));
    }

//    /**
//     * Http UrlEncoded方式的Post请求
//     * @param url url地址
//     * @param urlQueryParams query参数
//     * @param urlEncodedParams 需要编码的post参数
//     * @param responseHandler 请求结果处理器
//     */
//    public static void newUrlEncodedPost(String url,
//                                         Map<String, String> urlQueryParams,
//                                         Map<String, String> urlEncodedParams,
//                                         ResponseHandlerInterface responseHandler) {
//
//        appendPhoneInfo(urlQueryParams);
//
//        // Post都要方Token
//        urlQueryParams.put(URLDefine.TOKEN, AccountManager.getInstance().getSession());
//
//        // 添加签名
//        Map<String, String> merged = mergeParams(urlQueryParams, urlEncodedParams);
//        urlQueryParams.put(SIGN, newGetSign(merged));
//
//        String urlFinal = AsyncHttpClient.getUrlWithQueryString(true, url, new RequestParams(urlQueryParams));
//        RequestParams requestParams = new RequestParams(urlEncodedParams);
//        client.post(urlFinal, requestParams, responseHandler);
//    }
//
//    /**
//     * Http UrlEncoded方式的Post请求
//     * @param url url地址
//     * @param urlEncodedParams 需要编码的post参数
//     * @param responseHandler 请求结果处理器
//     */
//    public static void newUrlEncodedPost(String url,
//                                         Map<String, String> urlEncodedParams,
//                                         ResponseHandlerInterface responseHandler) {
//        newUrlEncodedPost(url, new HashMap<String, String>(), urlEncodedParams, responseHandler);
//    }
//
//    /**
//     * Http Multipart方式的Post请求
//     * @param url url地址
//     * @param urlQueryParams query参数
//     * @param multipartParams mutipart的文本参数
//     * @param multipartFileParams multipart的文件参数
//     * @param responseHandler 请求结果处理器
//     */
//    public static void newMutipartPost(String url,
//                                       Map<String, String> urlQueryParams,
//                                       Map<String, String> multipartParams,
//                                       Map<String, File> multipartFileParams,
//                                       ResponseHandlerInterface responseHandler) {
//        appendPhoneInfo(multipartParams);
//
//        // Post都要方Token
//        multipartParams.put(URLDefine.TOKEN, AccountManager.getInstance().getSession());
//
//        // 添加签名
//        Map<String, String> merged = mergeParams(urlQueryParams, multipartParams);
//        multipartParams.put(SIGN, newGetSign(merged));
//
//        String urlFinal = AsyncHttpClient.getUrlWithQueryString(true, url, new RequestParams(urlQueryParams));
//
//        RequestParams requestParams = new RequestParams(multipartParams);
//
//        for(Map.Entry<String, File> entry : multipartFileParams.entrySet()) {
//            try {
//                requestParams.put(entry.getKey(), entry.getValue());
//            } catch (FileNotFoundException e) {
//                // 找不到文件
//            }
//        }
//
//        client.post(urlFinal, requestParams, responseHandler);
//    }
//
//    /**
//     * Http Multipart方式的Post请求
//     * @param url url地址
//     * @param multipartParams mutipart的文本参数
//     * @param multipartFileParams multipart的文件参数
//     * @param responseHandler 请求结果处理器
//     */
//    public static void newMutipartPost(String url,
//                                       Map<String, String> multipartParams,
//                                       Map<String, File> multipartFileParams,
//                                       ResponseHandlerInterface responseHandler) {
//        newMutipartPost(url, new HashMap<String, String>(), multipartParams, multipartFileParams, responseHandler);
//    }
//
//
//    protected static void newInternalMutipartPost(String url,
//                                                  Map<String, String> urlQueryParams,
//                                                  Map<String, String> multipartParams,
//                                                  RequestParams multipartFileParams,
//                                                  ResponseHandlerInterface responseHandler) {
//        appendPhoneInfo(multipartParams);
//
//        // Post都要方Token
//        multipartParams.put(URLDefine.TOKEN, AccountManager.getInstance().getSession());
//
//        // 添加签名
//        Map<String, String> merged = mergeParams(urlQueryParams, multipartParams);
//        multipartParams.put(SIGN, newGetSign(merged));
//
//        String urlFinal = AsyncHttpClient.getUrlWithQueryString(true, url, new RequestParams(urlQueryParams));
//
//        for(Map.Entry<String, String> entry : multipartParams.entrySet()) {
//            multipartFileParams.put(entry.getKey(), entry.getValue());
//        }
//
//        client.post(urlFinal, multipartFileParams, responseHandler);
//    }
}
