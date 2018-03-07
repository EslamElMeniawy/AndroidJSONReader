package elmeniawy.eslam.ytsag.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * NetworkUtils
 * <p>
 * Created by Eslam El-Meniawy on 06-Mar-2018.
 * CITC - Mansoura University
 */

public class NetworkUtils {
    public static final String BASE_URL = "https://yts.am/api/v2/";

    /**
     * Check if device connected to Internet or not.
     *
     * @param context Context to use in getting connectivity service.
     * @return Boolean value indicating whether user is connected or not.
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = null;

        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }

        return netInfo != null && netInfo.isConnected();
    }
}
