package elmeniawy.eslam.ytsag.screens.details;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import elmeniawy.eslam.ytsag.screens.main.MovieViewModel;
import elmeniawy.eslam.ytsag.screens.main.TorrentViewModel;
import elmeniawy.eslam.ytsag.utils.FabricEvents;

/**
 * DetailsPresenter
 * <p>
 * Created by Eslam El-Meniawy on 20-Mar-2018.
 * CITC - Mansoura University
 */

public class DetailsPresenter implements DetailsMVP.Presenter {
    private MovieViewModel movie = null;
    private List<TorrentViewModel> torrents = new ArrayList<>();

    @Nullable
    private DetailsMVP.View view;

    @Override
    public void setView(@Nullable DetailsMVP.View view) {
        this.view = view;
    }

    @Override
    public void setMovie(MovieViewModel movie) {
        this.movie = movie;
    }

    @Override
    public void setTorrents(List<TorrentViewModel> torrents) {
        this.torrents = torrents;
    }

    @Override
    public void fillData() {
        if (view != null) {
            if (movie == null) {
                view.showError();
            } else {
                view.setTitle(movie.getTitle());
                view.setYear(String.valueOf(movie.getYear()));
                view.setGenres(TextUtils.join("\t", movie.getGenres()));

                if (torrents.size() > 0) {
                    view.setTorrents(torrents);
                }

                view.setSynopsis(movie.getSynopsis());
                view.loadMovieImage(movie.getMediumCoverImage());
                view.loadMovieBackground(movie.getBackgroundImage());

                //
                // Show the movie data.
                //

                view.showData();
            }

            view.hideLoading();
        }
    }

    @Override
    public void imdbClicked() {
        if (view != null) {
            view.openMovieWebPage("https://www.imdb.com/title/" + movie.getImdbCode());
        }
    }

    @Override
    public void torrentClicked(int position) {
        if (view != null && torrents.size() > 0) {
            view.openTorrentWebPage(torrents.get(position).getUrl());
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
        // Destroy ad view if not null.
        //

        if (view != null) {
            if (!view.isAdViewNull()) {
                view.destroyAdView();
            }
        }
    }
}
