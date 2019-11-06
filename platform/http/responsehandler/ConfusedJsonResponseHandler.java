package platform.http.responsehandler;

import android.support.annotation.NonNull;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import platform.http.result.*;
import platform.http.result.IResult;
import platform.http.result.ProcessResult;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 不规则的Json Response处理器
 * 处理不规则接口的Json，这部分Json的内容除了errno, msg和data，还包括其他结点
 * 建议server在设计新接口时避免这种糟糕的设计
 *
 * @author kailun
 * @time 15/11/24.
 */
public abstract class ConfusedJsonResponseHandler<T> extends AbstractJsonResponseHandler {

    @Override
    protected IResult handleProcessResult(@NonNull ProcessResult processResult) {
        ParameterizedType superType = (ParameterizedType)getClass().getGenericSuperclass();
        Type[] params = superType.getActualTypeArguments();

        @SuppressWarnings("unchecked")
        Class<T> type = (Class<T>) params[0];

        T data;
        try {
            data = JSONObject.parseObject(processResult.content, type);
        } catch (JSONException e) {
            JsonParseFailedResult r = new JsonParseFailedResult();
            r.content = processResult.content;
            r.exception = e;
            return r;
        }

        if (data == null) {
            JsonParseFailedResult r = new JsonParseFailedResult();
            r.content = processResult.content;
            r.exception = new JSONException("cant parse string to RootObject: " +
                    processResult.rootObject.data.toJSONString());
            return r;
        }

        SucceedResult r = new SucceedResult();
        r.data = data;
        return r;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void postProcess(@NonNull IResult result) {
        if (result.type() != ProcessResult.SUCCEED) {
            super.postProcess(result);
        } else {
            SucceedResult r = (SucceedResult) result;
            T data = (T) r.data;
            success(data);
            end(); // 一定不能忘了回调finish方法
        }
    }

    public abstract void success(@NonNull T data);
}
