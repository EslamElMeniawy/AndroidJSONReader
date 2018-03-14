package elmeniawy.eslam.ytsag.screens.main;

import java.util.List;

import elmeniawy.eslam.ytsag.api.model.Movie;
import elmeniawy.eslam.ytsag.api.model.UpdateResponse;
import elmeniawy.eslam.ytsag.storage.database.ApplicationDatabase;
import elmeniawy.eslam.ytsag.storage.database.entities.MovieEntity;
import elmeniawy.eslam.ytsag.storage.database.entities.TorrentEntity;
import elmeniawy.eslam.ytsag.storage.preferences.MySharedPreferences;
import elmeniawy.eslam.ytsag.utils.PreferencesUtils;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * MainRepository
 * <p>
 * Created by Eslam El-Meniawy on 11-Mar-2018.
 * CITC - Mansoura University
 */

public class MainRepository implements Repository {
    @Override
    public boolean getRunBefore(MySharedPreferences sharedPreferences) {
        return sharedPreferences.getBoolean(PreferencesUtils.KEY_RUN_BEFORE);
    }

    @Override
    public boolean getFromNotification(MySharedPreferences sharedPreferences) {
        return sharedPreferences.getBoolean(PreferencesUtils.KEY_FROM_NOTIFICATION);
    }

    @Override
    public boolean getUpdateAvailable(MySharedPreferences sharedPreferences) {
        return sharedPreferences.getBoolean(PreferencesUtils.KEY_UPDATE_AVAILABLE);
    }

    @Override
    public long getLastCheckUpdateTime(MySharedPreferences sharedPreferences) {
        return sharedPreferences.getLong(PreferencesUtils.KEY_UPDATE_LAST_CHECK);
    }

    @Override
    public boolean getNotificationsEnabled(MySharedPreferences sharedPreferences) {
        return sharedPreferences.getBoolean(PreferencesUtils.KEY_NOTIFICATION_ENABLED);
    }

    @Override
    public boolean getUpdateEnabled(MySharedPreferences sharedPreferences) {
        return sharedPreferences.getBoolean(PreferencesUtils.KEY_UPDATE_ENABLED);
    }

    @Override
    public void saveRunBefore(MySharedPreferences sharedPreferences) {
        sharedPreferences.putBoolean(PreferencesUtils.KEY_RUN_BEFORE, true);
    }

    @Override
    public void saveNotificationsEnabled(MySharedPreferences sharedPreferences, Boolean enabled) {
        sharedPreferences.putBoolean(PreferencesUtils.KEY_NOTIFICATION_ENABLED, enabled);
    }

    @Override
    public void saveUpdateEnabled(MySharedPreferences sharedPreferences, Boolean enabled) {
        sharedPreferences.putBoolean(PreferencesUtils.KEY_UPDATE_ENABLED, enabled);
    }

    @Override
    public Observable<Movie> getMoviesOnline() {
        return null;
    }

    @Override
    public Observable<Movie> getMoviesOffline() {
        return null;
    }

    @Override
    public Observable<Movie> getMovies() {
        return getMoviesOffline().switchIfEmpty(getMoviesOnline());
    }

    @Override
    public void saveMovies(ApplicationDatabase database, List<MovieEntity> movieList,
                           List<TorrentEntity> torrentList) {
        //
        // Get current saved movies.
        //

        database
                .movieDao()
                .getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieEntityList -> {
                    if (movieEntityList != null && movieEntityList.size() > 0) {
                        Completable.fromAction(() -> {
                            int deletedRowaCount = database
                                    .movieDao()
                                    .deleteData(movieEntityList);

                            Timber.i("Deleted rows count: %d.", deletedRowaCount);
                            insertMovies(database, movieList, torrentList);
                        });
                    } else {
                        insertMovies(database, movieList, torrentList);
                    }
                });
    }

    @Override
    public Observable<UpdateResponse> checkUpdateAvailable() {
        return null;
    }

    @Override
    public void downloadApk() {

    }

    private void insertMovies(ApplicationDatabase database, List<MovieEntity> movieList,
                              List<TorrentEntity> torrentList) {
        Completable.fromAction(() -> {
            List<Long> moviesIds = database.movieDao().insertData(movieList);
            Timber.i("Inserted movies ids: %s.", moviesIds.toString());
            List<Long> torrentsIds = database.torrentDao().insertData(torrentList);
            Timber.i("Inserted torrents ids: %s.", torrentsIds.toString());
        });
    }
}
