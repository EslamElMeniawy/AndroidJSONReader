package elmeniawy.eslam.ytsag.screens.main;

import java.util.ArrayList;
import java.util.List;

import elmeniawy.eslam.ytsag.api.model.Movie;
import elmeniawy.eslam.ytsag.api.model.Torrent;
import elmeniawy.eslam.ytsag.api.model.UpdateResponse;
import elmeniawy.eslam.ytsag.storage.database.ApplicationDatabase;
import elmeniawy.eslam.ytsag.storage.database.entities.MovieEntity;
import elmeniawy.eslam.ytsag.storage.database.entities.TorrentEntity;
import elmeniawy.eslam.ytsag.storage.preferences.MySharedPreferences;
import io.reactivex.Observable;
import timber.log.Timber;

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
        Timber.tag(MainModel.class.getSimpleName());
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
    public void saveMoviesLastFetchTime(MySharedPreferences sharedPreferences, long time) {
        repository.saveMoviesLastFetchTime(sharedPreferences, time);
    }

    @Override
    public Observable<Movie> getMovies(long timestamp, ApplicationDatabase database, int firstPage) {

        return repository.getMovies(timestamp, database, firstPage);
    }

    @Override
    public Observable<Movie> getMoviesOffline(ApplicationDatabase database) {
        return repository.getMoviesOffline(database);
    }

    @Override
    public void saveMovies(ApplicationDatabase database, List<Movie> movieList) {
        Timber.i("saveMovies");

        //
        // Map movies list to movies and torrents entities.
        //

        List<MovieEntity> movieEntityList = new ArrayList<>();
        List<TorrentEntity> torrentEntities = new ArrayList<>();

        for (Movie movie :
                movieList) {
            Timber.i("Current movie to convert: %s.", movie.toString());

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
            Timber.i("Converted movie entity: %s.", movieEntity.toString());
            movieEntityList.add(movieEntity);

            //
            // Torrent entity object.
            //

            for (Torrent torrent :
                    movie.getTorrents()) {
                TorrentEntity torrentEntity = new TorrentEntity();
                torrentEntity.setUrl(torrent.getUrl());
                torrentEntity.setQuality(torrent.getQuality());
                torrentEntity.setSize(torrent.getSize());
                torrentEntity.setMovieId(movie.getId());
                Timber.i("Converted torrent entity: %s.", torrentEntity.toString());
                torrentEntities.add(torrentEntity);
            }
        }

        //
        // Call repository method for saving data.
        //

        repository.saveMovies(database, movieEntityList, torrentEntities);
    }

    @Override
    public Observable<UpdateResponse> checkUpdateAvailable() {
        return null;
    }

    @Override
    public void downloadApk() {
        repository.downloadApk();
    }
}
