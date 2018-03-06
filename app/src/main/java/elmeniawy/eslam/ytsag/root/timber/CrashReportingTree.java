package elmeniawy.eslam.ytsag.root.timber;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import timber.log.Timber;

/**
 * CrashReportingTree
 * <p>
 * Created by Eslam El-Meniawy on 06-Mar-2018.
 * CITC - Mansoura University
 */

public class CrashReportingTree extends Timber.Tree {
    @Override
    protected void log(int priority, @Nullable String tag, @NotNull String message,
                       @Nullable Throwable t) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return;
        }

        Crashlytics.log(String.format("%s --> %s.", tag == null ? "NoTag" : tag, message));

        if (t != null && !(t instanceof ConnectException || t instanceof SocketTimeoutException)) {
            Crashlytics.logException(t);
        }
    }
}
