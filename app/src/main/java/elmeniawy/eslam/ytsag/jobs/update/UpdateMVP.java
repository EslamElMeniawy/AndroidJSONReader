package elmeniawy.eslam.ytsag.jobs.update;

import elmeniawy.eslam.ytsag.api.model.UpdateResponse;
import elmeniawy.eslam.ytsag.storage.preferences.MySharedPreferences;
import io.reactivex.Observable;

/**
 * UpdateMVP
 * <p>
 * Created by Eslam El-Meniawy on 29-Mar-2018.
 * CITC - Mansoura University
 */

public interface UpdateMVP {
    interface View {
        MySharedPreferences getSharedPreferences();

        int getVersionCode();

        String getTitle();

        String getMessage();

        void showNotification(int id, String title, String message);
    }

    interface Presenter {
        void setView(UpdateMVP.View view);

        void runJob();

        void rxUnsubscribe();
    }

    interface Model {
        Observable<UpdateResponse> checkUpdate(long currentTime);

        void setUpdateAvailable(MySharedPreferences sharedPreferences);
    }
}
