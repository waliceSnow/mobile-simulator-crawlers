package platform.http;

import android.support.annotation.NonNull;
import okhttp3.*;
import platform.http.internal.MediaTypes;
import platform.http.responsehandler.ResponseHandler;
import platform.http.result.IResult;
import platform.http.result.NetworkFailedResult;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * abstract of class/interface and so on
 *
 * @author kailun on 16/2/24
 */
public class HttpClient {

    CallbackHandler callBackHandler;
    OkHttpClient client;
    {
        callBackHandler = new CallbackHandler();
        client = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    static HttpClient sHttpClient = null;

    private HttpClient() {
        // 单例模式
    }

    public static HttpClient instance() {
        if (sHttpClient == null) {
            synchronized (HttpClient.class) {
                if (sHttpClient == null) {
                    sHttpClient = new HttpClient();
                }
            }
        }
        return sHttpClient;
    }

    public void get(final String url, final Map<String, String> queryParams,
                    final ResponseHandler handler) {
        HttpUrl httpUrl = makeUrl(url, queryParams);
        Request request = new Request.Builder()
                .url(httpUrl)
                .get()
                .build();

        handler.begin(); // 发起请求前回调start方法
        client.newCall(request).enqueue(new HttpCallBack(handler));
    }

    public void urlEncodedPost(final String url, final Map<String, String> queryParams, final Map<String, String> urlEncodedParams,
                     final ResponseHandler handler) {
        HttpUrl httpUrl = makeUrl(url, queryParams);
        FormBody body = makeUrlEncodedFormBody(urlEncodedParams);

        Request request = new Request.Builder()
                .url(httpUrl)
                .post(body)
                .build();

        handler.begin(); // 发起请求前回调start方法
        client.newCall(request).enqueue(new HttpCallBack(handler));
    }

    public void multiPartPost(final String url,
                              final Map<String, String> queryParams,
                              final Map<String, String> multipartParams,
                              final Map<String, File> multipartFileParams,
                              final ResponseHandler handler) {
        HttpUrl httpUrl = makeUrl(url, queryParams);
        MultipartBody body = makeMultipartFormBody(multipartParams, multipartFileParams);

        Request request = new Request.Builder()
                .url(httpUrl)
                .post(body)
                .build();

        handler.begin(); // 发起请求前回调start方法
        client.newCall(request).enqueue(new HttpCallBack(handler));
    }


    class HttpCallBack implements Callback {

        final ResponseHandler responseHandler;

        public HttpCallBack(final ResponseHandler responseHandler) {
            this.responseHandler = responseHandler;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            NetworkFailedResult r = new NetworkFailedResult();
            r.url = call.request().url().toString();
            r.exception = e;

            callBackHandler.sendCallbackMessage(responseHandler, r);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            IResult r = responseHandler.preProcess(call, response);
            callBackHandler.sendCallbackMessage(responseHandler, r);
        }
    }

    /* package */ static HttpUrl makeUrl(@NonNull String url, @NonNull Map<String, String> query) {
        HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder();
        for (Map.Entry<String, String> entry: query.entrySet()) {
             builder.addQueryParameter(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }

    /* package */ static FormBody makeUrlEncodedFormBody(@NonNull Map<String, String> urlEncodedParams) {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry: urlEncodedParams.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }

    /* package */ static MultipartBody makeMultipartFormBody(
            @NonNull Map<String, String> multipartParams,
            @NonNull Map<String, File> multipartFileParams) {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        for (Map.Entry<String, String> entry: multipartParams.entrySet()) {
            builder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, File> entry: multipartFileParams.entrySet()) {
            String key = entry.getKey();
            File value = entry.getValue();
            builder.addFormDataPart(
                    key,
                    value.getName(),
                    RequestBody.create(MediaTypes.fromFile(value), value));
        }
        return builder.build();
    }
}
