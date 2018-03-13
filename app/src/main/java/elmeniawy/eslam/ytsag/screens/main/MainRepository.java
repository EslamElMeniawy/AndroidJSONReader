package elmeniawy.eslam.ytsag.screens.main;

import java.util.List;

import elmeniawy.eslam.ytsag.api.model.Movie;
import elmeniawy.eslam.ytsag.storage.database.ApplicationDatabase;
import elmeniawy.eslam.ytsag.storage.preferences.MySharedPreferences;
import elmeniawy.eslam.ytsag.utils.PreferencesUtils;
import io.reactivex.Observable;

/**
 * MainRepository
 * <p>
 * Created by Eslam El-Meniawy on 11-Mar-2018.
 * CITC - Mansoura University
 */

public class MainRepository implements Repository {
    @Override
    public boolean getRunBefore(MySharedPreferences sharedPreferences) {
        return sharedPreferences.getData(PreferencesUtils.KEY_RUN_BEFORE);
    }

    @Override
    public boolean getFromNotification(MySharedPreferences sharedPreferences) {
        return sharedPreferences.getData(PreferencesUtils.KEY_FROM_NOTIFICATION);
    }

    @Override
    public boolean getNotificationsEnabled(MySharedPreferences sharedPreferences) {
        return sharedPreferences.getData(PreferencesUtils.KEY_NOTIFICATION_ENABLED);
    }

    @Override
    public boolean getUpdateEnabled(MySharedPreferences sharedPreferences) {
        return sharedPreferences.getData(PreferencesUtils.KEY_UPDATE_ENABLED);
    }

    @Override
    public void saveRunBefore(MySharedPreferences sharedPreferences) {
        sharedPreferences.putData(PreferencesUtils.KEY_RUN_BEFORE, true);
    }

    @Override
    public void saveNotificationsEnabled(MySharedPreferences sharedPreferences, Boolean enabled) {
        sharedPreferences.putData(PreferencesUtils.KEY_NOTIFICATION_ENABLED, enabled);
    }

    @Override
    public void saveUpdateEnabled(MySharedPreferences sharedPreferences, Boolean enabled) {
        sharedPreferences.putData(PreferencesUtils.KEY_UPDATE_ENABLED, enabled);
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
    public void saveMovies(ApplicationDatabase database, List<Movie> movieList) {

    }

    @Override
    public void downloadApk() {

    }
}
