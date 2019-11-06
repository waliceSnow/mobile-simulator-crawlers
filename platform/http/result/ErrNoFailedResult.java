package platform.http.result;

/**
 * abstract of class/interface and so on
 *
 * @author kailun on 16/2/25
 */
public class ErrNoFailedResult extends FailedResult {
    public String url = "";
    public int errNo = 0;
    public String errMsg = "";

    @Override
    public int type() {
        return ERR_NO_FAILED;
    }
}
