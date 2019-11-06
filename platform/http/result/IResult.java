package platform.http.result;

/**
 * abstract of class/interface and so on
 *
 * @author kailun on 16/2/25
 */
public interface IResult {
    int SUCCEED = 0;
    int STATUS_CODE_FAILED = 1;
    int NETWORK_FAILED = 2;
    int JSON_PARSE_FAILED = 3;
    int ERR_NO_FAILED = 4;

    int type();
}
