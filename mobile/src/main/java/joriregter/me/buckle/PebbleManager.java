package joriregter.me.buckle;

/**
 * Created by Jonathan on 22-6-2016.
 */
import android.app.Activity;
import android.util.Log;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import java.util.UUID;

/**
 * Created by joriregter on 22/06/16.
 */
public class PebbleManager {
    private final static int KEY = 0;
    private final static String STRING_ID = "652c2b1f-8a18-43e3-93c3-d227d15f5e20";
    private final static UUID ID = UUID.fromString(STRING_ID);

    public static void vibratePebble (Activity app, String vibration) {
        if (PebbleKit.isWatchConnected(app)) {
            PebbleDictionary dict = new PebbleDictionary();
            dict.addString(KEY, vibration);
            PebbleKit.sendDataToPebble(app, ID, dict);
        } else {
            Log.e("PEBBLE_MANAGER", "Pebble not connected.");
        }
    }

    public static void startNavigating(Activity activity) {
        PebbleKit.startAppOnPebble(activity, ID);
    }
    public static void stopNavigating(Activity activity) {
        PebbleKit.closeAppOnPebble(activity, ID);
    }
}