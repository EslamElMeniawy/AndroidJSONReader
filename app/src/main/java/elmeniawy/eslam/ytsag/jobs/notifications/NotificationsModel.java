package elmeniawy.eslam.ytsag.jobs.notifications;

import elmeniawy.eslam.ytsag.api.model.MovieResponse;
import elmeniawy.eslam.ytsag.storage.database.ApplicationDatabase;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * NotificationsModel
 * <p>
 * Created by Eslam El-Meniawy on 29-Mar-2018.
 * CITC - Mansoura University
 */

public class NotificationsModel implements NotificationsMVP.Model {
    private Repository repository;

    NotificationsModel(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<MovieResponse> getLatestMovie(long currentTime) {
        return repository.getLatestMovie(currentTime);
    }

    @Override
    public Single<Long> getLatestId(ApplicationDatabase database) {
        return repository.getLatestId(database);
    }
}
