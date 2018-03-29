package elmeniawy.eslam.ytsag.screens.main;

import android.support.annotation.Nullable;

import com.evernote.android.job.JobManager;
import com.google.gson.Gson;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

import elmeniawy.eslam.ytsag.api.model.Movie;
import elmeniawy.eslam.ytsag.api.model.Torrent;
import elmeniawy.eslam.ytsag.jobs.notifications.NotificationsJob;
import elmeniawy.eslam.ytsag.jobs.update.UpdateJob;
import elmeniawy.eslam.ytsag.storage.database.entities.MovieEntity;
import elmeniawy.eslam.ytsag.storage.database.entities.TorrentEntity;
import elmeniawy.eslam.ytsag.utils.FabricEvents;
import io.reactivex.Observable;
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
    private boolean mLoadingItems = true;
    private MovieViewModel movieToOpen = null;
    private int firstPage = 1;
    private Gson gson = new Gson();

    private Disposable moviesOnlineDisposable = null,
            moviesOfflineDisposable = null,
            torrentsDisposable = null,
            moviesDisposable = null;

    @Nullable
    private MainMVP.View view;

    private MainMVP.Model model;

    MainPresenter(MainMVP.Model model) {
        this.model = model;
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


            //
            // Get movies.
            //

            loadFirstTimeMovies();
        }
    }

    @Override
    public void loadMovies() {
        if (view != null) {
            //
            // Get movies.
            //

            loadFirstTimeMovies();
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

                firstPage += 2;
                moviesOnlineDisposable = model
                        .getMovies(model.getMoviesLastFetchTime(view.getSharedPreferences()),
                                firstPage)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleResult, this::handleError);
            }
        }
    }

    @Override
    public void refreshMovies() {
        if (view != null) {
            //
            // Get movies.
            //

            loadFirstTimeMovies();
        }
    }

    @Override
    public void bannerAdLoaded() {
        if (view != null) {
            view.showAdView();
            view.setMainPadding(0);
        }
    }

    @Override
    public void bannerAdFailed() {
        if (view != null) {
            view.hideAdView();
            view.setMainPadding(16);
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
            view.openDetails(movieToOpen, movieToOpen.getTorrents());
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
                view.openDetails(movie, movie.getTorrents());
            }
        }
    }

    private void setNotificationScheduler() {
        Timber.i("setNotificationScheduler");

        if (view != null) {
            view.startBootReceiver();
            NotificationsJob.scheduleJob();
            model.saveNotificationsEnabled(view.getSharedPreferences(), true);
            view.enableNotificationsSwitch();
        }
    }

    private void stopNotificationScheduler() {
        Timber.i("stopNotificationScheduler");

        if (view != null) {
            JobManager.instance().cancelAllForTag(NotificationsJob.TAG);

            if (!model.getUpdateEnabled(view.getSharedPreferences())) {
                view.stopBootReceiver();
            }

            model.saveNotificationsEnabled(view.getSharedPreferences(), false);
            view.disableNotificationsSwitch();
        }
    }

    private void setUpdateScheduler() {
        Timber.i("setUpdateScheduler");

        if (view != null) {
            view.startBootReceiver();
            UpdateJob.scheduleJob();
            model.saveUpdateEnabled(view.getSharedPreferences(), true);
            view.enableUpdateSwitch();
        }
    }

    private void stopUpdateScheduler() {
        Timber.i("stopUpdateScheduler");
        JobManager.instance().cancelAllForTag(UpdateJob.TAG);

        if (view != null) {
            if (!model.getNotificationsEnabled(view.getSharedPreferences())) {
                view.stopBootReceiver();
            }

            model.saveUpdateEnabled(view.getSharedPreferences(), false);
            view.disableUpdateSwitch();
        }
    }

    private void rxUnsubscribe() {
        if (moviesOnlineDisposable != null && moviesOnlineDisposable.isDisposed()) {
            moviesOnlineDisposable.dispose();
        }

        if (moviesOfflineDisposable != null && moviesOfflineDisposable.isDisposed()) {
            moviesOfflineDisposable.dispose();
        }

        if (torrentsDisposable != null && torrentsDisposable.isDisposed()) {
            torrentsDisposable.dispose();
        }

        if (moviesDisposable != null && moviesDisposable.isDisposed()) {
            moviesDisposable.dispose();
        }

        model.rxUnsubscribe();
    }

    private void loadFirstTimeMovies() {
        if (view != null) {
            mLoadingItems = true;
            view.showSwipeLoading();
            view.clearMovies();
            firstPage = 1;
            List<Movie> onlineMovies = new ArrayList<>();

            moviesOnlineDisposable = model
                    .getMovies(model.getMoviesLastFetchTime(view.getSharedPreferences()),
                            firstPage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(onlineMovies::add)
                    .doOnComplete(() -> {
                        Timber.i("Get movies complete.");
                        Timber.i("Movies count: %d.", onlineMovies.size());

                        if (!model
                                .isUpToDate(model
                                        .getMoviesLastFetchTime(view
                                                .getSharedPreferences()))) {
                            //
                            // Save movies for offline use.
                            //

                            model.saveMovies(view.getDatabase(), view.getSharedPreferences(),
                                    onlineMovies, System.currentTimeMillis());
                        }
                    })
                    .subscribe(this::handleResult, this::handleError);
        }
    }

    private void handleResult(Movie movie) {
        mLoadingItems = false;

        if (view != null) {
            view.hideSwipeLoading();

            //
            // Convert movie object to movie view model object.
            //

            Timber.i("Movie to be converted: %s.", gson.toJson(movie));
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
                    Timber.i("Torrent to be converted: %s.", gson.toJson(torrent));
                    TorrentViewModel torrentViewModel = new TorrentViewModel();
                    torrentViewModel.setUrl(torrent.getUrl());
                    torrentViewModel.setQuality(torrent.getQuality());
                    torrentViewModel.setSize(torrent.getSize());

                    Timber.i("Converted torrent view model: %s.",
                            torrentViewModel);

                    torrentViewModels.add(torrentViewModel);
                }
            }

            movieViewModel.setTorrents(torrentViewModels);
            Timber.i("Converted movie view model: %s.", movieViewModel);

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

                    getMoviesOffline(throwable);
                }
            } else {
                //
                // Show snack bar error.
                //
                firstPage -= 2;
                showSnackBarError(throwable);
            }
        }
    }

    private void getMoviesOffline(Throwable throwable) {
        if (view != null) {
            moviesOfflineDisposable = model
                    .getMoviesOffline(view.getDatabase())
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(movieEntityList -> {
                        Timber.i("Got entities: %s.", movieEntityList);
                        List<Movie> offlineMovies = new ArrayList<>();

                        if (movieEntityList != null && movieEntityList.size() > 0) {
                            for (MovieEntity movieEntity :
                                    movieEntityList) {
                                Timber.i("Current movie entity to convert: %s.",
                                        movieEntity.toString());
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
                                // Add movie to movies list.
                                //

                                offlineMovies.add(movie);
                            }

                            //
                            // Get torrents for the gotten movies.
                            //

                            getTorrentsOffline(offlineMovies, 0, throwable);
                        } else {
                            showTvError(throwable);
                        }
                    });
        }
    }

    private void getTorrentsOffline(List<Movie> movies, int index, Throwable throwable) {
        if (view != null) {
            if (index < movies.size()) {
                torrentsDisposable = model
                        .getMovieOfflineTorrents(view.getDatabase(), movies.get(index).getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(torrentEntityList -> {
                            Timber.i("Got entities: %s.", torrentEntityList);
                            List<Torrent> torrents = new ArrayList<>();

                            if (torrentEntityList != null && torrentEntityList.size() > 0) {
                                for (TorrentEntity torrentEntity :
                                        torrentEntityList) {
                                    //
                                    // Map torrent entity to torrent object.
                                    //

                                    Torrent torrent = new Torrent();
                                    torrent.setUrl(torrentEntity.getUrl());
                                    torrent.setQuality(torrentEntity.getQuality());
                                    torrent.setSize(torrentEntity.getSize());

                                    //
                                    // Add torrent to torrents list.
                                    //

                                    torrents.add(torrent);
                                }
                            }

                            Movie movie = movies.get(index);
                            movie.setTorrents(torrents);
                            movies.set(index, movie);
                            int nextIndex = index + 1;
                            getTorrentsOffline(movies, nextIndex, throwable);
                        });
            } else {
                showSnackBarError(throwable);

                moviesDisposable = Observable
                        .fromIterable(movies)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleResult);
            }
        }
    }

    private void showTvError(Throwable throwable) {
        if (view != null) {
            if (throwable instanceof ConnectException
                    || throwable instanceof SocketTimeoutException
                    || throwable instanceof UnknownHostException
                    || throwable instanceof TimeoutException) {
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
                    || throwable instanceof SocketTimeoutException
                    || throwable instanceof UnknownHostException
                    || throwable instanceof TimeoutException) {
                view.showInternetErrorSnackBar();
            } else {
                view.showGetMoviesErrorSnackBar();
            }
        }
    }
}
