package platform.http.result;

/**
 * abstract of class/interface and so on
 *
 * @author kailun on 16/2/25
 */
public class NetworkFailedResult extends FailedResult {

    // 只要请求返回就有
    public String url = "";

    public Exception exception = null;

    @Override
    public int type() {
        return IResult.NETWORK_FAILED;
    }

}
