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
 * MainModel
 * <p>
 * Created by Eslam El-Meniawy on 11-Mar-2018.
 * CITC - Mansoura University
 */

public class MainModel implements MainMVP.Model {
    private Repository repository;

    MainModel(Repository repository) {
        this.repository = repository;
    }

    @Override
    public boolean getRunBefore(MySharedPreferences sharedPreferences) {
        return repository.getRunBefore(sharedPreferences);
    }

    @Override
    public boolean getFromNotification(MySharedPreferences sharedPreferences) {
        return repository.getFromNotification(sharedPreferences);
    }

    @Override
    public boolean getUpdateAvailable(MySharedPreferences sharedPreferences) {
        return repository.getUpdateAvailable(sharedPreferences);
    }

    @Override
    public long getLastCheckUpdateTime(MySharedPreferences sharedPreferences) {
        return repository.getLastCheckUpdateTime(sharedPreferences);
    }

    @Override
    public long getMoviesLastFetchTime(MySharedPreferences sharedPreferences) {
        return repository.getMoviesLastFetchTime(sharedPreferences);
    }

    @Override
    public boolean getNotificationsEnabled(MySharedPreferences sharedPreferences) {
        return repository.getNotificationsEnabled(sharedPreferences);
    }

    @Override
    public boolean getUpdateEnabled(MySharedPreferences sharedPreferences) {
        return repository.getUpdateEnabled(sharedPreferences);
    }

    @Override
    public void saveRunBefore(MySharedPreferences sharedPreferences) {
        repository.saveRunBefore(sharedPreferences);
    }

    @Override
    public void saveNotificationsEnabled(MySharedPreferences sharedPreferences, Boolean enabled) {
        repository.saveNotificationsEnabled(sharedPreferences, enabled);
    }

    @Override
    public void saveUpdateEnabled(MySharedPreferences sharedPreferences, Boolean enabled) {
        repository.saveUpdateEnabled(sharedPreferences, enabled);
    }

    @Override
    public void saveLastCheckUpdateTime(MySharedPreferences sharedPreferences, long time) {
        repository.saveLastCheckUpdateTime(sharedPreferences, time);
    }

    @Override
    public Observable<Movie> getMovies(long timestamp, int firstPage) {

        return repository.getMovies(timestamp, firstPage);
    }

    @Override
    public void saveMovies(ApplicationDatabase database, MySharedPreferences sharedPreferences,
                           List<Movie> movies, long time) {
        repository.saveMovies(database, sharedPreferences, movies, time);
    }

    @Override
    public Maybe<List<MovieEntity>> getMoviesOffline(ApplicationDatabase database) {
        return repository.getMoviesOffline(database);
    }

    @Override
    public Maybe<List<TorrentEntity>> getMovieOfflineTorrents(ApplicationDatabase database,
                                                              Long movieId) {
        return repository.getMovieOfflineTorrents(database, movieId);
    }

    @Override
    public Observable<UpdateResponse> checkUpdateAvailable() {
        return null;
    }

    @Override
    public void downloadApk() {
        repository.downloadApk();
    }

    @Override
    public void rxUnsubscribe() {
        repository.rxUnsubscribe();
    }

    @Override
    public boolean isUpToDate(long time) {
        return repository.isUpToDate(time);
    }
}
