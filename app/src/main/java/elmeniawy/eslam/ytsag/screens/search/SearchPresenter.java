package elmeniawy.eslam.ytsag.screens.search;

import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import elmeniawy.eslam.ytsag.api.model.Movie;
import elmeniawy.eslam.ytsag.api.model.Torrent;
import elmeniawy.eslam.ytsag.screens.main.MovieViewModel;
import elmeniawy.eslam.ytsag.screens.main.TorrentViewModel;
import elmeniawy.eslam.ytsag.utils.FabricEvents;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * SearchPresenter
 * <p>
 * Created by Eslam El-Meniawy on 18-Mar-2018.
 * CITC - Mansoura University
 */

public class SearchPresenter implements SearchMVP.Presenter {
    private boolean mLoadingItems = true;
    private MovieViewModel movieToOpen = null;
    private int firstPage = 1;
    private String queryTerm = "";
    private Gson gson = new Gson();

    private Disposable moviesOnlineDisposable = null;

    @Nullable
    private SearchMVP.View view;

    private SearchMVP.Model model;

    SearchPresenter(SearchMVP.Model model) {
        this.model = model;
    }

    @Override
    public void setView(@Nullable SearchMVP.View view) {
        this.view = view;
    }

    @Override
    public void errorClicked() {
        if (view != null) {
            view.hideError();
            view.showSwipeLayout();


            //
            // Get movies.
            //

            loadFirstTimeMovies();
        }
    }

    @Override
    public void setQuery(String queryTerm) {
        this.queryTerm = queryTerm;
    }

    @Override
    public void searchMovies() {
        if (view != null) {
            //
            // Get movies.
            //

            loadFirstTimeMovies();
        }
    }

    @Override
    public void recyclerScrolled(int mOnScreenItems, int mTotalItemsInList, int mFirstVisibleItem) {
        if (view != null && !queryTerm.isEmpty() && model.getMoviesCount() > 20 * firstPage) {
            int mVisibleThreshold = 1;

            if (!mLoadingItems &&
                    (mTotalItemsInList - mOnScreenItems) <=
                            (mFirstVisibleItem + mVisibleThreshold)) {
                mLoadingItems = true;
                view.showSwipeLoading();

                //
                // Get more movies.
                //

                firstPage += 1;
                moviesOnlineDisposable = model
                        .getMovies(queryTerm, firstPage)
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

    private void rxUnsubscribe() {
        if (moviesOnlineDisposable != null && !moviesOnlineDisposable.isDisposed()) {
            moviesOnlineDisposable.dispose();
        }
    }

    private void loadFirstTimeMovies() {
        if (view != null && !queryTerm.isEmpty()) {
            mLoadingItems = true;
            view.showSwipeLoading();
            view.clearMovies();
            firstPage = 1;

            moviesOnlineDisposable = model
                    .getMovies(queryTerm, firstPage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete(() -> {
                        if (model.getMoviesCount() == 0) {
                            mLoadingItems = false;
                            view.hideSwipeLoading();
                            view.hideSwipeLayout();
                            view.showNoResults();
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
                //
                // Show text view error.
                //

                showTvError(throwable);
            } else {
                //
                // Show snack bar error.
                //
                firstPage -= 1;
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
            view.showError();
        }
    }
}
