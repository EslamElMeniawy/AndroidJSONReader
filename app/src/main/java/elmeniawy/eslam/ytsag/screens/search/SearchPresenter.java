package elmeniawy.eslam.ytsag.screens.search;

import android.support.annotation.Nullable;

import elmeniawy.eslam.ytsag.screens.main.MovieViewModel;

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

    @Nullable
    private SearchMVP.View view;

    private SearchMVP.Model model;

    SearchPresenter(SearchMVP.Model model) {
        this.model = model;
    }

    @Override
    public void setView(SearchMVP.View view) {
        this.view = view;
    }

    @Override
    public void errorClicked() {

    }

    @Override
    public void setQuery(String queryTerm) {

    }

    @Override
    public void searchMovies() {

    }

    @Override
    public void recyclerScrolled(int mOnScreenItems, int mTotalItemsInList, int mFirstVisibleItem) {

    }

    @Override
    public void refreshMovies() {

    }

    @Override
    public void bannerAdLoaded() {

    }

    @Override
    public void bannerAdFailed() {

    }

    @Override
    public void bannerClicked() {

    }

    @Override
    public void onPaused() {

    }

    @Override
    public void onResumed() {

    }

    @Override
    public void onDestroyed() {

    }

    @Override
    public void movieClicked(MovieViewModel movie) {

    }
}
