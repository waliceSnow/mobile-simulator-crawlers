package platform.http;

import android.support.annotation.NonNull;

import java.util.Map;

/**
 * abstract of class/interface and so on
 *
 * @author kailun on 16/2/29
 */
public interface IPhoneInfoProvider {
    void modifyForGet(@NonNull Map<String, String> params);
}
