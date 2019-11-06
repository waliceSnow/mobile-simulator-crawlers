package platform.http.responsehandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * abstract of class/interface and so on
 *
 * @author kailun on 15/11/23.
 */
public class RootObject {

    @JSONField(name = "errno")
    public int errno;

    @JSONField(name = "msg")
    public String msg = "";

    @JSONField(name = "time")
    public long time;

    @JSONField(name = "data")
    public JSON data;
}
