package platform.http.result;

/**
 * abstract of class/interface and so on
 *
 * @author kailun on 16/2/29
 */
public abstract class FailedResult implements IResult {

    private boolean isHandled;

    public boolean isHandled() {
        return this.isHandled;
    }

    public void setIsHandled(boolean handled){
        this.isHandled = handled;
    }

}
