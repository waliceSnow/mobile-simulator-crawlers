package platform.http.result;

/**
 * abstract of class/interface and so on
 *
 * @author kailun on 16/2/25
 */
public class JsonParseFailedResult extends FailedResult {

    public String url = "";
    public String content = "";
    public Exception exception = null;

    @Override
    public int type() {
        return JSON_PARSE_FAILED;
    }
}
