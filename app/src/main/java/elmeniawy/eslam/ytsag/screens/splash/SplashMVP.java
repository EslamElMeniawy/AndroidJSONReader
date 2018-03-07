package elmeniawy.eslam.ytsag.screens.splash;

import elmeniawy.eslam.ytsag.storage.preferences.MySharedPreferences;

/**
 * SplashMVP
 * <p>
 * Created by Eslam El-Meniawy on 07-Mar-2018.
 * CITC - Mansoura University
 */

public interface SplashMVP {
    interface View {
        MySharedPreferences getSharedPreferences();

        Boolean getFromNotification();

        void openMainActivity();
    }

    interface Presenter {
        void setView(SplashMVP.View view);

        void saveFromNotification();

        void startWait();
    }

    interface Model {
        void saveFromNotification(MySharedPreferences sharedPreferences, Boolean fromNotification);
    }
}
