package elmeniawy.eslam.ytsag.screens.main;

import android.support.annotation.Nullable;

/**
 * MainPresenter
 * <p>
 * Created by Eslam El-Meniawy on 11-Mar-2018.
 * CITC - Mansoura University
 */

public class MainPresenter implements MainMVP.Presenter {
    private int mPreviousTotal = 0, mVisibleThreshold = 1;
    private boolean mLoadingItems = true;
    private int getPage = 1;

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

    }

    @Override
    public void updateSwitchClicked() {

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

    }

    @Override
    public void errorClicked() {

    }

    @Override
    public void loadMovies() {

    }

    @Override
    public void setSchedulers() {

    }

    @Override
    public void checkUpdate() {

    }

    @Override
    public void recyclerScrolled(int mOnScreenItems, int mTotalItemsInList, int mFirstVisibleItem) {
        if (mLoadingItems) {
            if (mTotalItemsInList > mPreviousTotal + 1) {
                mLoadingItems = false;
                mPreviousTotal = mTotalItemsInList;
            }
        }

        if (!mLoadingItems && (mTotalItemsInList - mOnScreenItems) <= (mFirstVisibleItem + mVisibleThreshold)) {
            mLoadingItems = true;

            //
            // Get more movies.
            //
        }
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
    public void interstitialClicked() {

    }

    @Override
    public void interstitialClosed() {

    }

    @Override
    public void permissionCallback(boolean granted) {

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

    }

    private void setNotificationScheduler() {

    }

    private void stopNotificationScheduler() {

    }

    private void setUpdateScheduler() {

    }

    private void stopUpdateScheduler() {

    }

    private void rxUnsubscribe() {

    }
}
