package elmeniawy.eslam.ytsag.screens.main;

import java.util.List;

import elmeniawy.eslam.ytsag.api.model.Movie;
import elmeniawy.eslam.ytsag.storage.database.ApplicationDatabase;
import elmeniawy.eslam.ytsag.storage.preferences.MySharedPreferences;
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
    public Observable<Movie> getMovies() {
        return repository.getMovies();
    }

    @Override
    public void saveMovies(ApplicationDatabase database, List<Movie> movieList) {
        repository.saveMovies(database, movieList);
    }

    @Override
    public void downloadApk() {
        repository.downloadApk();
    }
}
