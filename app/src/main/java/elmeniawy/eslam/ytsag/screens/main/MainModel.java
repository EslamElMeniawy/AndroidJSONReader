package elmeniawy.eslam.ytsag.screens.main;

import java.util.List;

import elmeniawy.eslam.ytsag.storage.database.ApplicationDatabase;
import elmeniawy.eslam.ytsag.storage.database.entities.MovieEntity;
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
        return false;
    }

    @Override
    public boolean getFromNotification(MySharedPreferences sharedPreferences) {
        return false;
    }

    @Override
    public boolean getNotificationsEnabled(MySharedPreferences sharedPreferences) {
        return false;
    }

    @Override
    public boolean getUpdateEnabled(MySharedPreferences sharedPreferences) {
        return false;
    }

    @Override
    public void saveNotificationsEnabled(MySharedPreferences sharedPreferences, Boolean enabled) {

    }

    @Override
    public void saveUpdateEnabled(MySharedPreferences sharedPreferences, Boolean enabled) {

    }

    @Override
    public Observable<MovieViewModel> getMovies() {
        return null;
    }

    @Override
    public void saveMovies(ApplicationDatabase database, List<MovieEntity> movieList) {

    }

    @Override
    public void downloadApk() {

    }
}
