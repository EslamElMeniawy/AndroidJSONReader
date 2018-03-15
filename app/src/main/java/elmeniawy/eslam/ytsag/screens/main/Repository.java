package elmeniawy.eslam.ytsag.screens.main;

import java.util.List;

import elmeniawy.eslam.ytsag.api.model.Movie;
import elmeniawy.eslam.ytsag.api.model.UpdateResponse;
import elmeniawy.eslam.ytsag.storage.database.ApplicationDatabase;
import elmeniawy.eslam.ytsag.storage.database.entities.MovieEntity;
import elmeniawy.eslam.ytsag.storage.database.entities.TorrentEntity;
import elmeniawy.eslam.ytsag.storage.preferences.MySharedPreferences;
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

    void saveMoviesLastFetchTime(MySharedPreferences sharedPreferences, long time);

    Observable<Movie> getMoviesOnline(int firstPage, MySharedPreferences sharedPreferences,
                                      ApplicationDatabase database);

    Observable<Movie> getMoviesOffline(ApplicationDatabase database);

    Observable<Movie> getMovies(long timestamp, ApplicationDatabase database, int firstPage,
                                MySharedPreferences sharedPreferences);

    void saveMovies(ApplicationDatabase database, List<Movie> movieList,
                    List<TorrentEntity> torrentList);

    Observable<UpdateResponse> checkUpdateAvailable();

    void downloadApk();
}
