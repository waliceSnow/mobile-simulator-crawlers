package platform.http;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import platform.http.responsehandler.ResponseHandler;
import platform.http.result.IResult;

/**
 * abstract of class/interface and so on
 *
 * @author kailun on 16/2/24
 */
public class CallbackHandler extends Handler {

    public static final int WHAT = 1;

    @Override
    public void handleMessage(Message msg) {
        if (msg.what != WHAT) {
            return;
        }

        CallbackObject m = (CallbackObject) msg.obj;
        if (m == null) {
            throw new RuntimeException("CallbackObject must not be null");
        }

        m.ResponseHandler.postProcess(m.result);
    }

    public void sendCallbackMessage(@NonNull ResponseHandler ResponseHandler, @NonNull IResult result) {
        CallbackObject callbackObject = new CallbackObject(ResponseHandler, result);
        obtainMessage(WHAT, callbackObject).sendToTarget();
    }

    private static class CallbackObject {
        @NonNull
        public final ResponseHandler ResponseHandler;

        @NonNull
        public final IResult result;

        public CallbackObject(@NonNull ResponseHandler ResponseHandler, @NonNull IResult result) {
            this.ResponseHandler = ResponseHandler;
            this.result = result;
        }
    }
}
