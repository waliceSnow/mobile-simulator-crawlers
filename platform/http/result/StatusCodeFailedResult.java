package platform.http.result;

/**
 * abstract of class/interface and so on
 *
 * @author kailun on 16/2/25
 */
public class StatusCodeFailedResult extends FailedResult {

    public String url = "";
    public int code = 0;
    public String message = "";

    @Override
    public int type() {
        return STATUS_CODE_FAILED;
    }
}
