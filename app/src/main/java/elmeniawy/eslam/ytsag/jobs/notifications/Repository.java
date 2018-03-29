package elmeniawy.eslam.ytsag.jobs.notifications;

import elmeniawy.eslam.ytsag.api.model.MovieResponse;
import elmeniawy.eslam.ytsag.storage.database.ApplicationDatabase;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Repository
 * <p>
 * Created by Eslam El-Meniawy on 29-Mar-2018.
 * CITC - Mansoura University
 */

public interface Repository {
    Observable<MovieResponse> getLatestMovie(long currentTime);

    Single<Long> getLatestId(ApplicationDatabase database);
}
