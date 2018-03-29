package elmeniawy.eslam.ytsag.jobs.update;

import elmeniawy.eslam.ytsag.api.UpdateApiService;
import elmeniawy.eslam.ytsag.api.model.UpdateResponse;
import elmeniawy.eslam.ytsag.storage.preferences.MySharedPreferences;
import elmeniawy.eslam.ytsag.utils.PreferencesUtils;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * UpdateRepository
 * <p>
 * Created by Eslam El-Meniawy on 29-Mar-2018.
 * CITC - Mansoura University
 */

public class UpdateRepository implements Repository {
    private UpdateApiService updateApiService;

    UpdateRepository(UpdateApiService updateApiService) {
        this.updateApiService = updateApiService;
    }

    @Override
    public Observable<UpdateResponse> checkUpdate(long currentTime) {
        return updateApiService
                .checkUpdate(currentTime)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    @Override
    public void setUpdateAvailable(MySharedPreferences sharedPreferences) {
        sharedPreferences
                .putBoolean(PreferencesUtils.KEY_UPDATE_AVAILABLE, true);
    }
}
