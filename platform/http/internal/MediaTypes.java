package platform.http.internal;

import android.support.annotation.NonNull;
import okhttp3.MediaType;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * abstract of class/interface and so on
 *
 * @author kailun on 16/2/25
 */
public class MediaTypes {

    static Map<String, String> map;
    static {
        map = new HashMap<>();
        map.put("jpeg", "image/jpeg");
        map.put("jpg", "image/jpeg");
        map.put("png", "image/png");
        map.put("css", "text/css");
    }

    public static MediaType fromFile(@NonNull File file) {
        String name = file.getName();
        int i = name.lastIndexOf('.');

        String postFix = "";
        if (i > 0 && i != name.length() - 1) {
            postFix = name.substring(i + 1);
        }

        String mine = map.get(postFix);
        if (mine == null) {
            mine = "";
        }

        return MediaType.parse(mine);
    }
}
