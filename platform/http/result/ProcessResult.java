package platform.http.result;

import platform.http.responsehandler.RootObject;

/**
 * abstract of class/interface and so on
 *
 * @author kailun on 16/2/25
 */
public class ProcessResult implements IResult {

    public int type = SUCCEED;

    public String url = "";
    public String content = "";
    public RootObject rootObject = null;

    @Override
    public int type() {
        return type;
    }

}
