package elmeniawy.eslam.ytsag.screens.splash;

import elmeniawy.eslam.ytsag.storage.preferences.MySharedPreferences;

/**
 * SplashModel
 * <p>
 * Created by Eslam El-Meniawy on 07-Mar-2018.
 * CITC - Mansoura University
 */

public class SplashModel implements SplashMVP.Model {
    private Repository repository;

    SplashModel(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void saveFromNotification(MySharedPreferences mySharedPreferences,
                                     Boolean fromNotification) {
        repository.saveFromNotification(mySharedPreferences, fromNotification);
    }
}
