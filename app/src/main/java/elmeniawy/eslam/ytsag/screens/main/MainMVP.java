package elmeniawy.eslam.ytsag.screens.main;

import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2rx.RxFetch;

import java.util.List;

import elmeniawy.eslam.ytsag.api.model.Movie;
import elmeniawy.eslam.ytsag.api.model.UpdateResponse;
import elmeniawy.eslam.ytsag.storage.database.ApplicationDatabase;
import elmeniawy.eslam.ytsag.storage.database.entities.MovieEntity;
import elmeniawy.eslam.ytsag.storage.database.entities.TorrentEntity;
import elmeniawy.eslam.ytsag.storage.preferences.MySharedPreferences;
import io.reactivex.Flowable;
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

        void showUpdateErrorSnackBar();

        void showNoUpdateSnackBar();

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

        void cancelCheckingUpdatesDialog();

        void showDownloadConfirmDialog();

        void showDownloadingDialog();

        void setDownloadProgress(int progress);

        void setDownloadMaxSize(int max);

        void cancelDownloadDialog();

        void showInstallDialog(String path);

        boolean getInterstitialLoaded();

        void showInterstitialAd();

        boolean isOnline();

        void openDetails(MovieViewModel movie, List<TorrentViewModel> torrentsList);

        void closeApp();

        void startBootReceiver();

        void stopBootReceiver();

        int getVersionCode();

        RxFetch getRxFetch();

        FetchListener getFetchListener();

        void addFetchListener(FetchListener fetchListener);

        void removeFetchListener(FetchListener fetchListener);

        String getApkDirectoryPath();

        String getApkPath(long downloadStartTime);
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

        void downloadConfirmed();

        void downloadRefused();

        void downloadCanceled();

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

        void downloadError(int id);

        void downloadComplete(int id);

        void downloadProgress(int id, long downloadedBytes, long fileSize);
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

        void saveUpdateAvailable(MySharedPreferences sharedPreferences, Boolean available);

        Observable<Movie> getMovies(long timestamp, int firstPage);

        void saveMovies(ApplicationDatabase database, MySharedPreferences sharedPreferences,
                        List<Movie> movies, long time);

        Maybe<List<MovieEntity>> getMoviesOffline(ApplicationDatabase database);

        Maybe<List<TorrentEntity>> getMovieOfflineTorrents(ApplicationDatabase database,
                                                           Long movieId);

        Observable<UpdateResponse> checkUpdateAvailable(long currentTime);

        Flowable<List<Download>> downloadApk(RxFetch rxFetch);

        void rxUnsubscribe();

        boolean isUpToDate(long time);

        void saveDownloadId(MySharedPreferences sharedPreferences, long id);

        long getDownloadId(MySharedPreferences sharedPreferences);
    }
}
