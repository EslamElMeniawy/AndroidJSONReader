package elmeniawy.eslam.ytsag.utils;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;

import elmeniawy.eslam.ytsag.BuildConfig;

/**
 * FabricEvents
 * <p>
 * Created by Eslam El-Meniawy on 06-Mar-2018.
 * CITC - Mansoura University
 */

public class FabricEvents {
    /**
     * Log content view fabric events.
     *
     * @param contentName The content opened to log.
     */
    public static void logContentViewEvent(String contentName) {
        if (!BuildConfig.DEBUG) {
            Answers.getInstance().logContentView(new ContentViewEvent()
                    .putContentName(contentName));
        }
    }
}
