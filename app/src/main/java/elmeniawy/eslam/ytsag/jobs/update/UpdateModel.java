package elmeniawy.eslam.ytsag.jobs.update;

import elmeniawy.eslam.ytsag.api.model.UpdateResponse;
import elmeniawy.eslam.ytsag.storage.preferences.MySharedPreferences;
import io.reactivex.Observable;

/**
 * UpdateModel
 * <p>
 * Created by Eslam El-Meniawy on 29-Mar-2018.
 * CITC - Mansoura University
 */

public class UpdateModel implements UpdateMVP.Model {
    private Repository repository;

    UpdateModel(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<UpdateResponse> checkUpdate(long currentTime) {
        return repository.checkUpdate(currentTime);
    }

    @Override
    public void setUpdateAvailable(MySharedPreferences sharedPreferences) {
        repository.setUpdateAvailable(sharedPreferences);
    }
}
