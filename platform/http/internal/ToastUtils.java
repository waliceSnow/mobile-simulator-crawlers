package platform.http.internal;

import android.app.Application;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * abstract of class/interface and so on
 *
 * @author kailun on 16/1/6
 */
public class ToastUtils {

    static Application app = null;

    public static void init(Application app) {
        ToastUtils.app = app;
    }

    /**
     * 显示Toast，显示时常缺省为 Toast.LENGTH_SHORT
     * @param text text
     */
    public static void show(CharSequence text) {
        checkInit();
        Toast.makeText(app, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示Toast
     * @param text text
     * @param duration duration
     */
    public static void show(CharSequence text, int duration) {
        checkInit();
        Toast.makeText(app, text, duration).show();
    }

    /**
     * 显示Toast，显示时常缺省为 Toast.LENGTH_SHORT
     * @param resId resId
     */
    public static void show(@StringRes int resId) {
        checkInit();
        Toast.makeText(app, resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示Toast
     * @param resId resId
     * @param duration duration
     */
    public static void show(@StringRes int resId, int duration) {
        checkInit();
        Toast.makeText(app, resId, duration).show();
    }

    private static void checkInit() {
        if (app == null) {
            throw new RuntimeException("ToastUtils not inited");
        }
    }
}
