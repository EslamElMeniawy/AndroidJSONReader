package elmeniawy.eslam.ytsag.jobs.update;

import elmeniawy.eslam.ytsag.api.model.UpdateResponse;
import elmeniawy.eslam.ytsag.storage.preferences.MySharedPreferences;
import io.reactivex.Observable;

/**
 * Repository
 * <p>
 * Created by Eslam El-Meniawy on 29-Mar-2018.
 * CITC - Mansoura University
 */

public interface Repository {
    Observable<UpdateResponse> checkUpdate(long currentTime);

    void setUpdateAvailable(MySharedPreferences sharedPreferences);
}
