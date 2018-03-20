package elmeniawy.eslam.ytsag.screens.details;

import java.util.List;

import elmeniawy.eslam.ytsag.screens.main.MovieViewModel;
import elmeniawy.eslam.ytsag.screens.main.TorrentViewModel;

/**
 * DetailsMVP
 * <p>
 * Created by Eslam El-Meniawy on 20-Mar-2018.
 * CITC - Mansoura University
 */

public interface DetailsMVP {
    interface View {
        void hideLoading();

        void showData();

        void showError();

        void setTitle(String title);

        void setYear(String year);

        void setGenres(String genres);

        void setTorrents(List<TorrentViewModel> torrents);

        void setSynopsis(String synopsis);

        void loadMovieImage(String imageUrl);

        void loadMovieBackground(String backgroundUrl);

        void openTorrentWebPage(String torrentUrl);

        void openMovieWebPage(String imdbUrl);

        void setMainPadding(int bottom);

        void showAdView();

        void hideAdView();

        boolean isAdViewNull();

        void pauseAdView();

        void resumeAdView();

        void destroyAdView();
    }

    interface Presenter {
        void setView(DetailsMVP.View view);

        void setMovie(MovieViewModel movie);

        void setTorrents(List<TorrentViewModel> torrents);

        void fillData();

        void torrentClicked(int position);

        void bannerAdLoaded();

        void bannerAdFailed();

        void bannerClicked();

        void onPaused();

        void onResumed();

        void onDestroyed();
    }

    interface Model {
    }
}
