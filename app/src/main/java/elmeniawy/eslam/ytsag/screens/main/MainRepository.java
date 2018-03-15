package elmeniawy.eslam.ytsag.screens.main;

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
    private MoviesApiService moviesApiService;
    private UpdateApiService updateApiService;

    //
    // Cash results for 30 seconds.
    //

    private static final long STALE_MS = 30 * 1000;

    MainRepository(MoviesApiService moviesApiService, UpdateApiService updateApiService) {
        this.moviesApiService = moviesApiService;
        this.updateApiService = updateApiService;
        Timber.tag(MainRepository.class.getSimpleName());
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
    public Observable<Movie> getMoviesOnline(int firstPage) {
        Timber.i("getMoviesOnline");
        Timber.i("First page to get: %d.", firstPage);

        Observable<MovieResponse> movieObservable = moviesApiService
                .getMovies(firstPage)
                .concatWith(moviesApiService.getMovies(firstPage + 1));

        return movieObservable
                .concatMap(movieResponse -> Observable
                        .fromIterable(movieResponse.getData().getMovies()));
    }

    @Override
    public Observable<Movie> getMoviesOffline(ApplicationDatabase database) {
        Timber.i("getMoviesOffline");
        List<Movie> movies = new ArrayList<>();

        //
        // Get movies entities from database.
        //

        List<MovieEntity> movieEntityList = database
                .movieDao()
                .getData()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .blockingGet();

        Timber.i("movieEntityList: %s.", movieEntityList);

        if (movieEntityList != null && movieEntityList.size() > 0) {
            for (MovieEntity movieEntity :
                    movieEntityList) {
                Timber.i("Current movie entity to convert: %s.", movieEntity.toString());

                //
                // Map movie entity to movie object.
                //

                Movie movie = new Movie();
                movie.setId(movieEntity.getId());
                movie.setImdbCode(movieEntity.getImdbCode());
                movie.setTitle(movieEntity.getTitle());
                movie.setYear(movieEntity.getYear());
                movie.setRating(movieEntity.getRating());
                movie.setGenres(movieEntity.getGenres());
                movie.setSynopsis(movieEntity.getSynopsis());
                movie.setBackgroundImage(movieEntity.getBackgroundImage());
                movie.setMediumCoverImage(movieEntity.getMediumCoverImage());


                //
                // Get torrents entities for current movie from database.
                //

                List<TorrentEntity> torrentEntities = database
                        .torrentDao()
                        .getData(movieEntity.getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .blockingGet();

                Timber.i("torrentEntities: %s.", torrentEntities);
                List<Torrent> torrents = new ArrayList<>();

                if (torrentEntities != null && torrentEntities.size() > 0) {
                    for (TorrentEntity torrentEntity :
                            torrentEntities) {
                        Torrent torrent = new Torrent();
                        torrent.setUrl(torrentEntity.getUrl());
                        torrent.setQuality(torrentEntity.getQuality());
                        torrent.setSize(torrentEntity.getSize());
                        Timber.i("Converted torrent: %s.", torrent.toString());
                        torrents.add(torrent);
                    }

                    movie.setTorrents(torrents);
                }

                //
                // Add movie to movies list.
                //

                Timber.i("Converted movie: %s.", movie.toString());
                movies.add(movie);
            }
        }

        return Observable.fromIterable(movies);
    }

    @Override
    public Observable<Movie> getMovies(long timestamp, ApplicationDatabase database, int firstPage) {
        if (firstPage == 1) {
            if (isUpToDate(timestamp)) {
                return getMoviesOffline(database).switchIfEmpty(getMoviesOnline(firstPage));
            } else {
                return getMoviesOnline(firstPage);
            }
        } else {
            return getMoviesOnline(firstPage);
        }
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
                            int deletedRowsCount = database
                                    .movieDao()
                                    .deleteData(movieEntityList);

                            Timber.i("Deleted rows count: %d.", deletedRowsCount);
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

    private boolean isUpToDate(long timestamp) {
        Timber.i("Last movies fetch time: %d.", timestamp);
        return timestamp != 0 && System.currentTimeMillis() - timestamp < STALE_MS;
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
