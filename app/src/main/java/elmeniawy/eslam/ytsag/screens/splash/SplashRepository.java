package elmeniawy.eslam.ytsag.screens.splash;

import elmeniawy.eslam.ytsag.storage.preferences.MySharedPreferences;
import elmeniawy.eslam.ytsag.utils.PreferencesUtils;

/**
 * SplashRepository
 * <p>
 * Created by Eslam El-Meniawy on 07-Mar-2018.
 * CITC - Mansoura University
 */

public class SplashRepository implements Repository {
    @Override
    public void saveFromNotification(MySharedPreferences sharedPreferences,
                                     Boolean fromNotification) {
        sharedPreferences.putBoolean(PreferencesUtils.KEY_FROM_NOTIFICATION, fromNotification);
    }
}
