package platform.http.responsehandler;

import android.support.annotation.NonNull;

import android.text.TextUtils;
import android.util.Log;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import platform.http.internal.ToastUtils;
import platform.http.result.*;
import okhttp3.Call;
import okhttp3.Response;

import java.io.IOException;


/**
 * abstract of class/interface and so on
 *
 * @author kailun
 * @time 15/11/23.
 */
public abstract class AbstractJsonResponseHandler extends ResponseHandler {

    public static final String TAG = "ResponseHandler";

    @Override
    public IResult preProcess(@NonNull Call call, @NonNull Response response) {
        String url = call.request().url().toString();

        // 状态码不是2xx
        // 一般是404或者500之类的
        if (!response.isSuccessful()) {
            StatusCodeFailedResult r = new StatusCodeFailedResult();
            r.url = url;
            return r;
        }

        // 读取Response，转换成String
        String content = "";
        try {
            content = response.body().string();
        } catch (IOException e) {
            JsonParseFailedResult r = new JsonParseFailedResult();
            r.url = url;
            r.content = content;
            r.exception = e;
            return r;
        }

        // 解析为Json
        RootObject rootObject;
        try {
            rootObject = JSONObject.parseObject(content, RootObject.class);
        } catch (JSONException e) {
            JsonParseFailedResult r = new JsonParseFailedResult();
            r.url = url;
            r.content = content;
            r.exception = e;
            return r;
        }

        if (rootObject == null) { // 也是解析Json失败
            JsonParseFailedResult r = new JsonParseFailedResult();
            r.url = url;
            r.content = content;
            r.exception = new JSONException("cant parse string to RootObject: " + content);
            return r;
        }

        if (rootObject.errno != 0) {
            ErrNoFailedResult r = new ErrNoFailedResult();
            r.url = url;
            r.errNo = rootObject.errno;
            r.errMsg = rootObject.msg;
            return r;
        }

        // 交给实现类处理
        ProcessResult r = new ProcessResult();
        r.url = url;
        r.content = content;
        r.rootObject = rootObject;
        return handleProcessResult(r);
    }

    @Override
    public void postProcess(@NonNull IResult result) {

        switch (result.type()) {
            case ProcessResult.SUCCEED:
                // 不处理，留给子类处理
                break;

            case ProcessResult.NETWORK_FAILED: {
                NetworkFailedResult r = (NetworkFailedResult) result;
                networkFailed(r);
                break;
            }

            case ProcessResult.STATUS_CODE_FAILED: {
                StatusCodeFailedResult r = (StatusCodeFailedResult) result;
                statusCodeFailed(r);
                break;
            }

            case ProcessResult.ERR_NO_FAILED: {
                ErrNoFailedResult r = (ErrNoFailedResult) result;
                errNoFailed(r);
                break;
            }

            case ProcessResult.JSON_PARSE_FAILED: {
                JsonParseFailedResult r = (JsonParseFailedResult) result;
                jsonParseFailed(r);
                break;
            }

            default:
                throw new RuntimeException("unknown ProcessResult: " + result.type());
        }

        // 回调finish方法
        end();
    }

    protected abstract IResult handleProcessResult(@NonNull ProcessResult processResult);

    /**
     * 所有错误的回调
     * @param r r
     */
    @SuppressWarnings("UnusedParameters")
    protected void failed(FailedResult r) {
        // 缺省设置中，这个方法什么都不做
        // 如果需要拦截所有错误，可以覆盖这个方法
        // 如果不想让基类继续处理错误，请调用FailedResult.setIsHandled(true)，标记FailedResult已处理

        // TODO
    }

    /**
     * 网络导致的错误的回调
     * @param r r
     */
    protected void networkFailed(NetworkFailedResult r) {
        failed(r);

        if (r.isHandled()) {
            return;
        }

        Log.e(TAG, "无法连接到服务器：" + String.valueOf(r.exception));
        ToastUtils.show("无法连接到服务器，请稍候重试");
        r.setIsHandled(true);
    }

    /**
     * Http状态码不为2xx的回调
     * @param r r
     */
    protected void statusCodeFailed(StatusCodeFailedResult r) {
        failed(r);

        if (r.isHandled()) {
            return;
        }

        Log.e(TAG, "服务器异常：" + r.code);
        ToastUtils.show("服务器异常，请稍候重试");
        r.setIsHandled(true);
    }

    /**
     * Json解析出错的回调
     * @param r r
     */
    protected void jsonParseFailed(JsonParseFailedResult r) {
        failed(r);

        if (r.isHandled()) {
            return;
        }

        Log.e(TAG, "解析Json出错：" +  String.valueOf(r.exception));
        ToastUtils.show("数据异常，请稍候重试");
        r.setIsHandled(true);
    }

    /**
     * 服务器返回一个错误码，并且不为零
     * @param r r
     */
    protected void errNoFailed(ErrNoFailedResult r) {
        failed(r);

        if (r.isHandled()) {
            return;
        }

        Log.e(TAG, "服务器异常：" + r.errNo);

        if (!TextUtils.isEmpty(r.errMsg)) {
            ToastUtils.show(r.errMsg);
        }
        else {
            ToastUtils.show("系统错误" + r.errMsg);
        }
        r.setIsHandled(true);
//        ToastUtils.show("服务器异常，请稍候重试");
    }
}
