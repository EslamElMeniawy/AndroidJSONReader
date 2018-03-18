package elmeniawy.eslam.ytsag.screens.main;

import java.util.List;

import elmeniawy.eslam.ytsag.api.model.Movie;
import elmeniawy.eslam.ytsag.api.model.UpdateResponse;
import elmeniawy.eslam.ytsag.storage.database.ApplicationDatabase;
import elmeniawy.eslam.ytsag.storage.database.entities.MovieEntity;
import elmeniawy.eslam.ytsag.storage.database.entities.TorrentEntity;
import elmeniawy.eslam.ytsag.storage.preferences.MySharedPreferences;
import io.reactivex.Maybe;
import io.reactivex.Observable;

/**
 * Repository
 * <p>
 * Created by Eslam El-Meniawy on 11-Mar-2018.
 * CITC - Mansoura University
 */

public interface Repository {
    boolean getRunBefore(MySharedPreferences sharedPreferences);

    boolean getFromNotification(MySharedPreferences sharedPreferences);

    boolean getUpdateAvailable(MySharedPreferences sharedPreferences);

    long getLastCheckUpdateTime(MySharedPreferences sharedPreferences);

    long getMoviesLastFetchTime(MySharedPreferences sharedPreferences);

    boolean getNotificationsEnabled(MySharedPreferences sharedPreferences);

    boolean getUpdateEnabled(MySharedPreferences sharedPreferences);

    void saveRunBefore(MySharedPreferences sharedPreferences);

    void saveNotificationsEnabled(MySharedPreferences sharedPreferences, Boolean enabled);

    void saveUpdateEnabled(MySharedPreferences sharedPreferences, Boolean enabled);

    void saveLastCheckUpdateTime(MySharedPreferences sharedPreferences, long time);

    Observable<Movie> getMovies(long timestamp, ApplicationDatabase database, int firstPage,
                                MySharedPreferences sharedPreferences);

    Maybe<List<MovieEntity>> getMoviesOffline(ApplicationDatabase database);

    Maybe<List<TorrentEntity>> getMovieOfflineTorrents(ApplicationDatabase database, Long movieId);

    Observable<UpdateResponse> checkUpdateAvailable();

    void downloadApk();

    void rxUnsubscribe();
}
