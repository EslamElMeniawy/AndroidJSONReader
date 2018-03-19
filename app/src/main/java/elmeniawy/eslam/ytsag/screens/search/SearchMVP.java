package elmeniawy.eslam.ytsag.screens.search;

import java.util.List;

import elmeniawy.eslam.ytsag.api.model.Movie;
import elmeniawy.eslam.ytsag.screens.main.MovieViewModel;
import elmeniawy.eslam.ytsag.screens.main.TorrentViewModel;
import io.reactivex.Observable;

/**
 * SearchMVP
 * <p>
 * Created by Eslam El-Meniawy on 18-Mar-2018.
 * CITC - Mansoura University
 */

public interface SearchMVP {
    interface View {
        void showNoResults();

        void setInternetError();

        void setGetMoviesError();

        void showError();

        void hideError();

        void showSwipeLayout();

        void hideSwipeLayout();

        void showSwipeLoading();

        void hideSwipeLoading();

        void updateMovies(MovieViewModel movie);

        void clearMovies();

        void setMainPadding(int bottom);

        void showAdView();

        void hideAdView();

        boolean isAdViewNull();

        void pauseAdView();

        void resumeAdView();

        void destroyAdView();

        boolean getInterstitialLoaded();

        void showInterstitialAd();

        void openDetails(MovieViewModel movie, List<TorrentViewModel> torrentsList);
    }

    interface Presenter {
        void setView(SearchMVP.View view);

        void errorClicked();

        void setQuery(String queryTerm);

        void searchMovies();

        void recyclerScrolled(int mOnScreenItems, int mTotalItemsInList, int mFirstVisibleItem);

        void refreshMovies();

        void bannerAdLoaded();

        void bannerAdFailed();

        void bannerClicked();

        void interstitialClicked();

        void interstitialClosed();

        void onPaused();

        void onResumed();

        void onDestroyed();

        void movieClicked(MovieViewModel movie);
    }

    interface Model {
        Observable<Movie> getMovies(String searchQuery, int firstPage);

        long getMoviesCount();
    }
}
