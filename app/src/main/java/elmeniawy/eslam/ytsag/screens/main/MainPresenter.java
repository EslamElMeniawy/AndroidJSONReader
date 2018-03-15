package elmeniawy.eslam.ytsag.screens.main;

import android.support.annotation.Nullable;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import elmeniawy.eslam.ytsag.api.model.Movie;
import elmeniawy.eslam.ytsag.api.model.Torrent;
import elmeniawy.eslam.ytsag.utils.FabricEvents;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * MainPresenter
 * <p>
 * Created by Eslam El-Meniawy on 11-Mar-2018.
 * CITC - Mansoura University
 */

public class MainPresenter implements MainMVP.Presenter {
    private static final String TAG = MainPresenter.class.getSimpleName();
    private boolean mLoadingItems = true;
    private MovieViewModel movieToOpen = null;
    private int firstPage = 1;
    private Disposable moviesDisposable = null;

    @Nullable
    private MainMVP.View view;

    private MainMVP.Model model;

    MainPresenter(MainMVP.Model model) {
        this.model = model;
        Timber.tag(TAG);
    }

    @Override
    public void setView(@Nullable MainMVP.View view) {
        this.view = view;
    }

    @Override
    public void notificationSwitchClicked() {
        if (view != null) {
            boolean notificationsSwitchChecked = view.notificationsSwitchChecked();

            Timber.i("Notifications switch checked: %s.",
                    String.valueOf(notificationsSwitchChecked));

            if (notificationsSwitchChecked) {
                stopNotificationScheduler();
            } else {
                setNotificationScheduler();
            }
        }
    }

    @Override
    public void updateSwitchClicked() {
        if (view != null) {
            boolean updateSwitchChecked = view.updateSwitchChecked();
            Timber.i("Update switch checked: %s.", String.valueOf(updateSwitchChecked));

            if (updateSwitchChecked) {
                stopUpdateScheduler();
            } else {
                setUpdateScheduler();
            }
        }
    }

    @Override
    public void aboutClicked() {
        if (view != null) {
            view.showAboutDialog();
        }
    }

    @Override
    public void developerClicked() {
        if (view != null) {
            view.showDeveloperDialog();
        }
    }

    @Override
    public void checkUpdateClicked() {
        if (view != null) {
            boolean isUpdateAvailable = model.getUpdateAvailable(view.getSharedPreferences());
            Timber.i("Update available: %s.", String.valueOf(isUpdateAvailable));

            if (isUpdateAvailable) {
                boolean storagePermissionGranted = view.isStoragePermissionGranted();

                Timber.i("Storage permission granted: %s.",
                        String.valueOf(storagePermissionGranted));

                if (storagePermissionGranted) {
                    view.showDownloadConfirmDialog();
                } else {
                    view.requestStoragePermission();
                }
            } else {
                view.showCheckingUpdatesDialog();

                //
                // Check for update.
                //
            }
        }
    }

    @Override
    public void errorClicked() {
        if (view != null) {
            view.hideErrorTv();
            view.showSwipeLayout();
            mLoadingItems = true;
            view.showSwipeLoading();

            //
            // Get movies.
            //

            view.clearMovies();
            firstPage = 1;
            moviesDisposable = model.
                    getMovies(model.getMoviesLastFetchTime(view.getSharedPreferences()),
                            view.getDatabase(), firstPage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResult, this::handleError);
        }
    }

    @Override
    public void loadMovies() {
        if (view != null) {
            mLoadingItems = true;
            view.showSwipeLoading();

            //
            // Get movies.
            //

            view.clearMovies();
            firstPage = 1;
            moviesDisposable = model
                    .getMovies(model.getMoviesLastFetchTime(view.getSharedPreferences()),
                            view.getDatabase(), firstPage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResult, this::handleError);
        }
    }

    @Override
    public void setSchedulers() {
        Timber.i("setSchedulers");

        if (view != null) {
            boolean runBefore = model.getRunBefore(view.getSharedPreferences());
            Timber.i("App run before: %s.", String.valueOf(runBefore));

            if (runBefore) {
                boolean notificationEnabled = model
                        .getNotificationsEnabled(view.getSharedPreferences());

                Timber.i("Notifications enabled: %s.",
                        String.valueOf(notificationEnabled));

                if (notificationEnabled) {
                    setNotificationScheduler();
                } else {
                    stopNotificationScheduler();
                }

                boolean updateEnabled = model.getUpdateEnabled(view.getSharedPreferences());
                Timber.i("Auto update enabled: %s.", String.valueOf(updateEnabled));

                if (updateEnabled) {
                    setUpdateScheduler();
                } else {
                    stopUpdateScheduler();
                }
            } else {
                setNotificationScheduler();
                setUpdateScheduler();
                model.saveRunBefore(view.getSharedPreferences());
            }
        }
    }

    @Override
    public void checkUpdate() {
        if (view != null) {
            if (model.getUpdateAvailable(view.getSharedPreferences())
                    && view.isOnline()
                    && ((new Date().getTime() -
                    model.getLastCheckUpdateTime(view.getSharedPreferences())
                    >= (24 * 60 * 60 * 1000))
                    || model.getFromNotification(view.getSharedPreferences()))) {
                if (view.isStoragePermissionGranted()) {
                    view.showDownloadConfirmDialog();
                } else {
                    view.requestStoragePermission();
                }
            }
        }
    }

    @Override
    public void recyclerScrolled(int mOnScreenItems, int mTotalItemsInList, int mFirstVisibleItem) {
        if (view != null) {
            int mVisibleThreshold = 1;

            if (!mLoadingItems &&
                    (mTotalItemsInList - mOnScreenItems) <=
                            (mFirstVisibleItem + mVisibleThreshold)) {
                mLoadingItems = true;
                view.showSwipeLoading();

                //
                // Get more movies.
                //

                firstPage++;
                moviesDisposable = model.getMovies(model
                                .getMoviesLastFetchTime(view.getSharedPreferences()),
                        view.getDatabase(), firstPage)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleResult, this::handleError);
            }
        }
    }

    @Override
    public void refreshMovies() {
        if (view != null) {
            mLoadingItems = true;
            view.showSwipeLoading();

            //
            // Get movies.
            //

            view.clearMovies();
            firstPage = 1;
            moviesDisposable = model.
                    getMovies(model.getMoviesLastFetchTime(view.getSharedPreferences()),
                            view.getDatabase(), firstPage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResult, this::handleError);
        }
    }

    @Override
    public void bannerAdLoaded() {
        if (view != null) {
            view.showAdView();
            view.setMainPadding(0, 16, 0, 0);
        }
    }

    @Override
    public void bannerAdFailed() {
        if (view != null) {
            view.hideAdView();
            view.setMainPadding(0, 16, 0, 16);
        }
    }

    @Override
    public void bannerClicked() {
        FabricEvents.logAdClickedEvent("Banner");
    }

    @Override
    public void interstitialClicked() {
        FabricEvents.logAdClickedEvent("Interstitial");
    }

    @Override
    public void interstitialClosed() {
        if (view != null) {
            view.openDetails(movieToOpen);
        }
    }

    @Override
    public void permissionCallback(boolean granted) {
        if (view != null) {
            if (granted) {
                view.showDownloadConfirmDialog();
            } else {
                view.showPermissionErrorSnackBar();
            }
        }
    }

    @Override
    public void backClicked() {
        if (view != null) {
            if (view.getDrawerOpened()) {
                view.closeDrawer();
            } else {
                view.closeApp();
            }
        }
    }

    @Override
    public void onPaused() {
        //
        // Pause ad view if not null.
        //

        if (view != null) {
            if (!view.isAdViewNull()) {
                view.pauseAdView();
            }
        }
    }

    @Override
    public void onResumed() {
        //
        // Resume ad view if not null.
        //

        if (view != null) {
            if (!view.isAdViewNull()) {
                view.resumeAdView();
            }
        }
    }

    @Override
    public void onDestroyed() {
        //
        // Unsubscribe from rx callbacks.
        //

        rxUnsubscribe();

        //
        // Destroy ad view if not null.
        //

        if (view != null) {
            if (!view.isAdViewNull()) {
                view.destroyAdView();
            }
        }
    }

    @Override
    public void movieClicked(MovieViewModel movie) {
        if (view != null) {
            if (view.getInterstitialLoaded()) {
                movieToOpen = movie;
                view.showInterstitialAd();
            } else {
                view.openDetails(movie);
            }
        }
    }

    private void setNotificationScheduler() {
        Timber.i("setNotificationScheduler");

        if (view != null) {
            //stopNotificationScheduler();
            model.saveNotificationsEnabled(view.getSharedPreferences(), true);
            view.enableNotificationsSwitch();
        }
    }

    private void stopNotificationScheduler() {
        Timber.i("stopNotificationScheduler");

        if (view != null) {
            //stopNotificationScheduler();
            model.saveNotificationsEnabled(view.getSharedPreferences(), false);
            view.disableNotificationsSwitch();
        }
    }

    private void setUpdateScheduler() {
        Timber.i("setUpdateScheduler");

        if (view != null) {
            //stopNotificationScheduler();
            model.saveUpdateEnabled(view.getSharedPreferences(), true);
            view.enableUpdateSwitch();
        }
    }

    private void stopUpdateScheduler() {
        Timber.i("stopUpdateScheduler");

        if (view != null) {
            //stopNotificationScheduler();
            model.saveUpdateEnabled(view.getSharedPreferences(), false);
            view.disableUpdateSwitch();
        }
    }

    private void rxUnsubscribe() {
        if (moviesDisposable != null && moviesDisposable.isDisposed()) {
            moviesDisposable.dispose();
        }
    }

    private void handleResult(Movie movie) {
        mLoadingItems = false;
        if (view != null) {
            view.hideSwipeLoading();

            //
            // Convert movie object to movie view model object.
            //

            Timber.i("Movie to be converted: %s.", movie.toString());
            MovieViewModel movieViewModel = new MovieViewModel();
            movieViewModel.setImdbCode(movie.getImdbCode());
            movieViewModel.setTitle(movie.getTitle());
            movieViewModel.setYear(movie.getYear());
            movieViewModel.setRating(movie.getRating());
            movieViewModel.setGenres(movie.getGenres());
            movieViewModel.setSynopsis(movie.getSynopsis());
            movieViewModel.setBackgroundImage(movie.getBackgroundImage());
            movieViewModel.setMediumCoverImage(movie.getMediumCoverImage());
            List<TorrentViewModel> torrentViewModels = new ArrayList<>();

            if (movie.getTorrents() != null && movie.getTorrents().size() > 0) {
                for (Torrent torrent :
                        movie.getTorrents()) {
                    Timber.i("Torrent to be converted: %s.", torrent.toString());
                    TorrentViewModel torrentViewModel = new TorrentViewModel();
                    torrentViewModel.setUrl(torrent.getUrl());
                    torrentViewModel.setQuality(torrent.getQuality());
                    torrentViewModel.setSize(torrent.getSize());

                    Timber.i("Converted torrent view model: %s.",
                            torrentViewModel.toString());

                    torrentViewModels.add(torrentViewModel);
                }
            }

            movieViewModel.setTorrents(torrentViewModels);
            Timber.i("Converted movie view model: %s.", movieViewModel.toString());

            //
            // Update view.
            //

            view.updateMovies(movieViewModel);
        }
    }

    private void handleError(Throwable throwable) {
        Timber.e(throwable);
        mLoadingItems = false;

        if (view != null) {
            if (firstPage == 1) {
                if (model.getMoviesLastFetchTime(view.getSharedPreferences()) == 0) {
                    //
                    // Show text view error.
                    //

                    showTvError(throwable);
                } else {
                    //
                    // Get movies offline.
                    //

                    List<Movie> offlineMovies = new ArrayList<>();

                    moviesDisposable = model
                            .getMoviesOffline(view.getDatabase())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnNext(offlineMovies::add)
                            .doOnError(throwable1 -> showTvError(throwable))
                            .doOnComplete(() -> {
                                if (offlineMovies.size() > 0) {
                                    showSnackBarError(throwable);
                                } else {
                                    showTvError(throwable);
                                }
                            })
                            .subscribe(this::handleResult);
                }
            } else {
                //
                // Show snack bar error.
                //
                firstPage--;
                showSnackBarError(throwable);
            }
        }
    }

    private void showTvError(Throwable throwable) {
        if (view != null) {
            if (throwable instanceof ConnectException
                    || throwable instanceof SocketTimeoutException) {
                view.setInternetError();
            } else {
                view.setGetMoviesError();
            }

            view.hideSwipeLoading();
            view.hideSwipeLayout();
            view.showErrorTv();
        }
    }

    private void showSnackBarError(Throwable throwable) {
        if (view != null) {
            view.hideSwipeLoading();

            if (throwable instanceof ConnectException
                    || throwable instanceof SocketTimeoutException) {
                view.showInternetErrorSnackBar();
            } else {
                view.showGetMoviesErrorSnackBar();
            }
        }
    }
}
