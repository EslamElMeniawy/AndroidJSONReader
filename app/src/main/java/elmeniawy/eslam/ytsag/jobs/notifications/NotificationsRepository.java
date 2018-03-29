package elmeniawy.eslam.ytsag.jobs.notifications;

import elmeniawy.eslam.ytsag.api.MoviesApiService;
import elmeniawy.eslam.ytsag.api.model.MovieResponse;
import elmeniawy.eslam.ytsag.storage.database.ApplicationDatabase;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * NotificationsRepository
 * <p>
 * Created by Eslam El-Meniawy on 29-Mar-2018.
 * CITC - Mansoura University
 */

public class NotificationsRepository implements Repository {
    private MoviesApiService moviesApiService;

    NotificationsRepository(MoviesApiService moviesApiService) {
        this.moviesApiService = moviesApiService;
    }

    @Override
    public Observable<MovieResponse> getLatestMovie(long currentTime) {
        return moviesApiService
                .getLatestMovie(currentTime)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    @Override
    public Single<Long> getLatestId(ApplicationDatabase database) {
        return database
                .movieDao()
                .getLatestId()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }
}
