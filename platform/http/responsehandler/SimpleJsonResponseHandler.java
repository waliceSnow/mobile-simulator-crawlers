package platform.http.responsehandler;

import android.support.annotation.NonNull;
import platform.http.result.IResult;
import platform.http.result.ProcessResult;
import platform.http.result.SucceedResult;

/**
 * 简单的Json Response处理器
 * 用于处理只包括errno, msg的简单Json
 * {
 *     "errno": 0,
 *     "msg": ""
 * }
 * @author kailun on 16/2/25
 */
public abstract class SimpleJsonResponseHandler extends AbstractJsonResponseHandler {

    @Override
    protected IResult handleProcessResult(@NonNull ProcessResult processResult) {
        return new SucceedResult(); // 到了这儿，就一定成功了
    }

    @Override
    public void postProcess(@NonNull IResult obj) {
        if (obj.type() != ProcessResult.SUCCEED) {
            super.postProcess(obj);
        } else {
            success();
            end(); // 一定不能忘了回调finish方法
            return;
        }
    }

    public abstract void success();
}
