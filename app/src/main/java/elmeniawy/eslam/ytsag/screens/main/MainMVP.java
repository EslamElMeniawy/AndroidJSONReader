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
 * MainMVP
 * <p>
 * Created by Eslam El-Meniawy on 11-Mar-2018.
 * CITC - Mansoura University
 */

public interface MainMVP {
    interface View {
        MySharedPreferences getSharedPreferences();

        ApplicationDatabase getDatabase();

        boolean notificationsSwitchChecked();

        void enableNotificationsSwitch();

        void disableNotificationsSwitch();

        boolean updateSwitchChecked();

        void enableUpdateSwitch();

        void disableUpdateSwitch();

        void showAboutDialog();

        void showDeveloperDialog();

        void setInternetError();

        void setGetMoviesError();

        void showErrorTv();

        void hideErrorTv();

        void showSwipeLayout();

        void hideSwipeLayout();

        void showSwipeLoading();

        void hideSwipeLoading();

        void updateMovies(MovieViewModel movie);

        void clearMovies();

        void showInternetErrorSnackBar();

        void showGetMoviesErrorSnackBar();

        void showPermissionErrorSnackBar();

        void setMainPadding(int bottom);

        void showAdView();

        void hideAdView();

        boolean isAdViewNull();

        void pauseAdView();

        void resumeAdView();

        void destroyAdView();

        boolean getDrawerOpened();

        void closeDrawer();

        boolean isStoragePermissionGranted();

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

        boolean isOnline();

        void openDetails(MovieViewModel movie, List<TorrentViewModel> torrentsList);

        void closeApp();
    }

    interface Presenter {
        void setView(MainMVP.View view);

        void notificationSwitchClicked();

        void updateSwitchClicked();

        void aboutClicked();

        void developerClicked();

        void checkUpdateClicked();

        void errorClicked();

        void loadMovies();

        void setSchedulers();

        void checkUpdate();

        void recyclerScrolled(int mOnScreenItems, int mTotalItemsInList, int mFirstVisibleItem);

        void refreshMovies();

        void bannerAdLoaded();

        void bannerAdFailed();

        void bannerClicked();

        void interstitialClicked();

        void interstitialClosed();

        void permissionCallback(boolean granted);

        void backClicked();

        void onPaused();

        void onResumed();

        void onDestroyed();

        void movieClicked(MovieViewModel movie);
    }

    interface Model {
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

        Observable<Movie> getMovies(long timestamp, int firstPage);

        void saveMovies(ApplicationDatabase database, MySharedPreferences sharedPreferences,
                        List<Movie> movies, long time);

        Maybe<List<MovieEntity>> getMoviesOffline(ApplicationDatabase database);

        Maybe<List<TorrentEntity>> getMovieOfflineTorrents(ApplicationDatabase database,
                                                           Long movieId);

        Observable<UpdateResponse> checkUpdateAvailable();

        void downloadApk();

        void rxUnsubscribe();

        boolean isUpToDate(long time);
    }
}
