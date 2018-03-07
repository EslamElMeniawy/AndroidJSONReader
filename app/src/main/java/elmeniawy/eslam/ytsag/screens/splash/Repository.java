package elmeniawy.eslam.ytsag.screens.splash;

import elmeniawy.eslam.ytsag.storage.preferences.MySharedPreferences;

/**
 * Repository
 * <p>
 * Created by Eslam El-Meniawy on 07-Mar-2018.
 * CITC - Mansoura University
 */

public interface Repository {
    void saveFromNotification(MySharedPreferences sharedPreferences, Boolean fromNotification);
}
