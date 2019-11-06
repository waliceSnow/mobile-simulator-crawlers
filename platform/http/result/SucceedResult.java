package platform.http.result;

/**
 * abstract of class/interface and so on
 *
 * @author kailun on 16/2/25
 */
public class SucceedResult implements IResult {

    public Object data = null;

    @Override
    public int type() {
        return SUCCEED;
    }
}
