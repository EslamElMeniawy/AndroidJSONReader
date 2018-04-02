package elmeniawy.eslam.ytsag.screens.main;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import elmeniawy.eslam.ytsag.api.MoviesApiService;
import elmeniawy.eslam.ytsag.api.UpdateApiService;
import elmeniawy.eslam.ytsag.api.model.Movie;
import elmeniawy.eslam.ytsag.api.model.MovieResponse;
import elmeniawy.eslam.ytsag.api.model.Torrent;
import elmeniawy.eslam.ytsag.api.model.UpdateResponse;
import elmeniawy.eslam.ytsag.storage.database.ApplicationDatabase;
import elmeniawy.eslam.ytsag.storage.database.entities.MovieEntity;
import elmeniawy.eslam.ytsag.storage.database.entities.TorrentEntity;
import elmeniawy.eslam.ytsag.storage.preferences.MySharedPreferences;
import elmeniawy.eslam.ytsag.utils.PreferencesUtils;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * MainRepository
 * <p>
 * Created by Eslam El-Meniawy on 11-Mar-2018.
 * CITC - Mansoura University
 */

public class MainRepository implements Repository {
    private MoviesApiService moviesApiService;
    private UpdateApiService updateApiService;

    //
    // Cash results for 30 seconds.
    //

    private static final long STALE_MS = 30 * 1000;

    private Disposable disposableGetMovies = null,
            disposableDeleteMovies = null,
            disposableInsertMovies = null;

    private List<Movie> movies = new ArrayList<>();

    MainRepository(MoviesApiService moviesApiService, UpdateApiService updateApiService) {
        this.moviesApiService = moviesApiService;
        this.updateApiService = updateApiService;
    }

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
    public long getMoviesLastFetchTime(MySharedPreferences sharedPreferences) {
        return sharedPreferences.getLong(PreferencesUtils.KEY_MOVIES_LAST_FETCH);
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
    public void saveLastCheckUpdateTime(MySharedPreferences sharedPreferences, long time) {
        sharedPreferences.putLong(PreferencesUtils.KEY_UPDATE_LAST_CHECK, time);
    }

    private Observable<Movie> getMoviesOnline(int firstPage) {
        Timber.i("getMoviesOnline");
        Timber.i("First page to get: %d.", firstPage);

        if (firstPage == 1) {
            movies = new ArrayList<>();
        }

        Observable<MovieResponse> movieObservable = moviesApiService
                .getMovies(firstPage)
                .concatWith(moviesApiService.getMovies(firstPage + 1))
                .doOnNext(movieResponse -> {
                    if (firstPage == 1) {
                        movies.addAll(movieResponse.getData().getMovies());
                    }
                });

        return movieObservable
                .concatMap(movieResponse -> Observable
                        .fromIterable(movieResponse.getData().getMovies()));
    }

    @Override
    public Maybe<List<MovieEntity>> getMoviesOffline(ApplicationDatabase database) {
        Timber.i("getMoviesOffline");
        return database
                .movieDao()
                .getData();
    }

    @Override
    public Maybe<List<TorrentEntity>> getMovieOfflineTorrents(ApplicationDatabase database,
                                                              Long movieId) {
        Timber.i("getMovieOfflineTorrents");
        return database
                .torrentDao()
                .getData(movieId);
    }

    @Override
    public Observable<Movie> getMovies(long timestamp, int firstPage) {
        if (firstPage == 1) {
            if (isUpToDate(timestamp)) {
                return Observable.fromIterable(movies);
            } else {
                return getMoviesOnline(firstPage);
            }
        } else {
            return getMoviesOnline(firstPage);
        }
    }

    @Override
    public void saveMovies(ApplicationDatabase database, MySharedPreferences sharedPreferences,
                           List<Movie> movieList, long time) {
        Timber.i("saveMovies");

        //
        // Map movies list to movies and torrents entities.
        //

        List<MovieEntity> movieEntityList = new ArrayList<>();
        List<TorrentEntity> torrentEntities = new ArrayList<>();

        for (Movie movie :
                movieList) {
            Gson gson = new Gson();
            Timber.i("Current movie to convert: %s.", gson.toJson(movie));

            //
            // Movie entity object.
            //

            MovieEntity movieEntity = new MovieEntity();
            movieEntity.setId(movie.getId());
            movieEntity.setImdbCode(movie.getImdbCode());
            movieEntity.setTitle(movie.getTitle());
            movieEntity.setYear(movie.getYear());
            movieEntity.setRating(movie.getRating());
            movieEntity.setGenres(movie.getGenres());
            movieEntity.setSynopsis(movie.getSynopsis());
            movieEntity.setBackgroundImage(movie.getBackgroundImage());
            movieEntity.setMediumCoverImage(movie.getMediumCoverImage());
            Timber.i("Converted movie entity: %s.", movieEntity);
            movieEntityList.add(movieEntity);

            //
            // Torrent entity object.
            //

            if (movie.getTorrents() != null) {
                for (Torrent torrent :
                        movie.getTorrents()) {
                    TorrentEntity torrentEntity = new TorrentEntity();
                    torrentEntity.setUrl(torrent.getUrl());
                    torrentEntity.setQuality(torrent.getQuality());
                    torrentEntity.setSize(torrent.getSize());
                    torrentEntity.setMovieId(movie.getId());
                    Timber.i("Converted torrent entity: %s.", torrentEntity);
                    torrentEntities.add(torrentEntity);
                }
            }
        }

        //
        // Call repository method for saving data.
        //

        disposableGetMovies = database
                .movieDao()
                .getData()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(movieEntityList1 -> {
                    Timber.i("Got entities: %s.", movieEntityList1);

                    if (movieEntityList1 != null && movieEntityList1.size() > 0) {
                        deleteMovies(database, sharedPreferences, movieEntityList1, movieEntityList,
                                torrentEntities, time);
                    } else {
                        insertMovies(database, sharedPreferences, movieEntityList, torrentEntities,
                                time);
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

    @Override
    public void rxUnsubscribe() {
        if (disposableGetMovies != null && !disposableGetMovies.isDisposed()) {
            disposableGetMovies.dispose();
        }

        if (disposableDeleteMovies != null && !disposableDeleteMovies.isDisposed()) {
            disposableDeleteMovies.dispose();
        }

        if (disposableInsertMovies != null && !disposableInsertMovies.isDisposed()) {
            disposableInsertMovies.dispose();
        }
    }

    @Override
    public boolean isUpToDate(long timestamp) {
        Timber.i("Last movies fetch time: %d.", timestamp);
        return timestamp != 0 && System.currentTimeMillis() - timestamp < STALE_MS;
    }

    private void deleteMovies(ApplicationDatabase database,
                              MySharedPreferences sharedPreferences,
                              List<MovieEntity> moviesToDelete,
                              List<MovieEntity> moviesToInsert,
                              List<TorrentEntity> torrentsToInsert,
                              long time) {
        disposableDeleteMovies = Completable
                .fromAction(() -> {
                    int deletedRowsCount = database
                            .movieDao()
                            .deleteData(moviesToDelete);

                    Timber.i("Deleted rows count: %d.", deletedRowsCount);

                    insertMovies(database, sharedPreferences, moviesToInsert, torrentsToInsert,
                            time);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe();
    }

    private void insertMovies(ApplicationDatabase database, MySharedPreferences sharedPreferences,
                              List<MovieEntity> movieList, List<TorrentEntity> torrentList,
                              long time) {
        disposableInsertMovies = Completable
                .fromAction(() -> {
                    List<Long> moviesIds = database.movieDao().insertData(movieList);
                    Timber.i("Inserted movies ids: %s.", moviesIds.toString());
                    List<Long> torrentsIds = database.torrentDao().insertData(torrentList);
                    Timber.i("Inserted torrents ids: %s.", torrentsIds.toString());
                    saveMoviesLastFetchTime(sharedPreferences, time);
                })
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    private void saveMoviesLastFetchTime(MySharedPreferences sharedPreferences, long time) {
        Timber.i("Save last fetch time: %d.", time);
        sharedPreferences.putLong(PreferencesUtils.KEY_MOVIES_LAST_FETCH, time);
    }
}
