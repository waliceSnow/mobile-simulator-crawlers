package platform.http.responsehandler;

import android.support.annotation.NonNull;
import platform.http.CallbackHandler;
import platform.http.result.IResult;
import okhttp3.Call;
import okhttp3.Response;

/**
 * abstract of class/interface and so on
 *
 * @author kailun on 16/2/24
 */
public abstract class ResponseHandler {
    /**
     * preProcess在后台线程执行，一般用作解析JSON等耗时操作，执行的结果在IResult中
     * 通过{@link CallbackHandler}转到UI线程，传递给{@link #postProcess(IResult)}
     * @param call call
     * @param response response
     * @return 返回处理后的结果信息
     */
    public abstract IResult preProcess(@NonNull Call call, @NonNull Response response);

    /**
     * postProcess在UI线程中执行，用于在UI中展现数据
     * @param result 传入的结果数据，来自{@link #preProcess(Call, Response)}
     */
    public abstract void postProcess(@NonNull IResult result);

    public void begin() {
    }

    public void end() {
    }
}
