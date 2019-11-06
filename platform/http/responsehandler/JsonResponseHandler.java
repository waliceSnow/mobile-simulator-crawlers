package platform.http.responsehandler;

import android.support.annotation.NonNull;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.TypeUtils;
import platform.http.result.*;
import platform.http.result.JsonParseFailedResult;
import platform.http.result.ProcessResult;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 普通的Json Response处理器
 * 用于处理只包括errno, msg和data的常规结构的Json
 * 它的抽象方法success返回的是data结点解析后得到的对象
 * {
 *     "errno": 0,
 *     "msg": "",
 *     "data": {
 *     }
 * }
 * @author kailun
 * @time 15/11/23.
 */
public abstract class JsonResponseHandler<T> extends AbstractJsonResponseHandler {

    @Override
    protected IResult handleProcessResult(@NonNull ProcessResult processResult) {
        ParameterizedType superType = (ParameterizedType)getClass().getGenericSuperclass();
        Type[] params = superType.getActualTypeArguments();

        @SuppressWarnings("unchecked")
        Class<T> type = (Class<T>) params[0];

        T data;
        try {
            data = TypeUtils.castToJavaBean(processResult.rootObject.data, type);
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

        SucceedResult r= new SucceedResult();
        r.data = data;
        return r;
    }

    @Override
    public void postProcess(@NonNull IResult result) {
        if (result.type() != ProcessResult.SUCCEED) {
            super.postProcess(result);
        } else {
            @SuppressWarnings("unchecked")
            SucceedResult r = (SucceedResult) result;

            @SuppressWarnings("unchecked")
            T data = (T) r.data;

            success(data);
            end(); // 一定不能忘了回调finish方法
        }

    }

    public abstract void success(@NonNull T data);
}
