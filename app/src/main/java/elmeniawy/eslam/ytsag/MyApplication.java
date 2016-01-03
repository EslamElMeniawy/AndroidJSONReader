package elmeniawy.eslam.ytsag;

import android.app.Application;
import android.content.Context;

/**
 * Created by Eslam El-Meniawy on 02-Jan-16.
 */
public class MyApplication extends Application {
    private static MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static MyApplication getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }
}