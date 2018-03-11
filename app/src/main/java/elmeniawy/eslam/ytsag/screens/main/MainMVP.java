package elmeniawy.eslam.ytsag.screens.main;

import java.util.List;

import elmeniawy.eslam.ytsag.storage.database.ApplicationDatabase;
import elmeniawy.eslam.ytsag.storage.database.entities.MovieEntity;
import elmeniawy.eslam.ytsag.storage.preferences.MySharedPreferences;
import io.reactivex.Observable;

/**
 * MainMVP
 * <p>
 * Created by Eslam El-Meniawy on 11-Mar-2018.
 * CITC - Mansoura University
 */

public interface MainMVP {
    interface View {
        MySharedPreferences getSharedPreferences();

        ApplicationDatabase getDatabase();

        void enableNotificationsSwitch();

        void disableNotificationsSwitch();

        void enableUpdateSwitch();

        void disableUpdateSwitch();

        void showAboutDialog();

        void showDeveloperDialog();

        void setError(String error);

        void clearError();

        void showErrorTv();

        void hideErrorTv();

        void showSwipeLayout();

        void hideSwipeLayout();

        void showSwipeLoading();

        void hideSwipeLoading();

        void updateMovies(MovieViewModel movie);

        void showSnackBar(String message);

        void setMainPadding();

        void showAdView();

        void hideAdView();

        boolean getDrawerOpened();

        void closeDrawer();

        void requestStoragePermission();

        void showCheckingUpdatesDialog();

        void showDownloadConfirmDialog();

        void showDownloadingDialog();

        void setDownloadProgress(int progress);

        void setDownloadMaxSize(int max);

        void cancelDownloadDialog();

        void showInstallDialog();

        boolean getInterstitialLoaded();

        void showInterstitialAd();

        void openDetails(MovieViewModel movie);

        void closeApp();
    }

    interface Presenter {
        void setView(MainMVP.View view);

        void rxUnsubscribe();

        void notificationSwitchClicked();

        void updateSwitchClicked();

        void setNotificationScheduler();

        void stopNotificationScheduler();

        void setUpdateScheduler();

        void stopUpdateScheduler();

        void aboutClicked();

        void updateClicked();

        void checkUpdateClicked();

        void errorClicked();

        void loadMovies();

        void refreshMovies();

        void bannerAdLoaded();

        void bannerAdFailed();

        void bannerClicked();

        void interstitialClicked();

        void interstitialClosed();

        void permissionCallback(boolean granted);

        void backClicked();

        void movieClicked(MovieViewModel movie);
    }

    interface Model {
        boolean getRunBefore(MySharedPreferences sharedPreferences);

        boolean getFromNotification(MySharedPreferences sharedPreferences);

        boolean getNotificationsEnabled(MySharedPreferences sharedPreferences);

        boolean getUpdateEnabled(MySharedPreferences sharedPreferences);

        void saveNotificationsEnabled(MySharedPreferences sharedPreferences, Boolean enabled);

        void saveUpdateEnabled(MySharedPreferences sharedPreferences, Boolean enabled);

        Observable<MovieViewModel> getMovies();

        void saveMovies(ApplicationDatabase database, List<MovieEntity> movieList);

        void downloadApk();
    }
}
