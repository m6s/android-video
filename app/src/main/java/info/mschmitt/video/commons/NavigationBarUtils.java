package info.mschmitt.video.commons;

import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * @author Matthias Schmitt
 */

public class NavigationBarUtils {
//    /**
//     * http://stackoverflow.com/a/29120269/2317680
//     */
//    public static boolean hasNavigationBar(Context context) {
//        Resources resources = context.getResources();
//        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
//        if (Build.FINGERPRINT.startsWith("generic")) {
//            return true;
//        } else if (id > 0) {
//            return resources.getBoolean(id);
//        } else {    // Check for keys
//            boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
//            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
//            return !hasMenuKey && !hasBackKey;
//        }
//    }

    /**
     * http://stackoverflow.com/a/30367339/2317680
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean hasNavigationBar(Activity activity) {
        Point realSize = new Point();
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getRealSize(realSize);
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return !realSize.equals(size);
    }
}
