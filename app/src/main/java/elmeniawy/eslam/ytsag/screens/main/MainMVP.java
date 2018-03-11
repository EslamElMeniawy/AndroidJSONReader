package elmeniawy.eslam.ytsag.screens.main;

import elmeniawy.eslam.ytsag.storage.database.ApplicationDatabase;
import elmeniawy.eslam.ytsag.storage.preferences.MySharedPreferences;

/**
 * MainMVP
 * <p>
 * Created by Eslam El-Meniawy on 11-Mar-2018.
 * CITC - Mansoura University
 */

public interface MainMVP {
    interface View {
        MySharedPreferences getSharedPreferences();

        ApplicationDatabase getDatabase();
    }

    interface Presenter {
        void setView(MainMVP.View view);
    }

    interface Model {
        boolean getRunBefore(MySharedPreferences sharedPreferences);

        boolean getFromNotification(MySharedPreferences sharedPreferences);

        boolean getNotificationsEnabled(MySharedPreferences sharedPreferences);

        boolean getUpdateEnabled(MySharedPreferences sharedPreferences);

        void saveNotificationsEnabled(MySharedPreferences sharedPreferences, Boolean enabled);

        void saveUpdateEnabled(MySharedPreferences sharedPreferences, Boolean enabled);
    }
}
