package elmeniawy.eslam.ytsag.screens.main;

import android.support.annotation.Nullable;

import java.util.Date;

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
            if (view.notificationsSwitchChecked()) {
                stopNotificationScheduler();
            } else {
                setNotificationScheduler();
            }
        }
    }

    @Override
    public void updateSwitchClicked() {
        if (view != null) {
            if (view.updateSwitchChecked()) {
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
            if (view.isStoragePermissionGranted()) {
                view.showCheckingUpdatesDialog();

                //
                // Check for update.
                //
            } else {
                view.requestStoragePermission();
            }
        }
    }

    @Override
    public void errorClicked() {

    }

    @Override
    public void loadMovies() {
        if (view != null) {
            mLoadingItems = true;
            view.showSwipeLoading();

            //
            // Get movies.
            //
        }
    }

    @Override
    public void setSchedulers() {
        Timber.i("setSchedulers");

        if (view != null) {
            Timber.i("App run before: %s.",
                    String.valueOf(model.getRunBefore(view.getSharedPreferences())));

            if (model.getRunBefore(view.getSharedPreferences())) {
                Timber.i("Notifications enabled: %s.",
                        String.valueOf(model.getNotificationsEnabled(view.getSharedPreferences())));

                if (model.getNotificationsEnabled(view.getSharedPreferences())) {
                    setNotificationScheduler();
                } else {
                    stopNotificationScheduler();
                }

                Timber.i("Auto update enabled: %s.",
                        String.valueOf(model.getUpdateEnabled(view.getSharedPreferences())));

                if (model.getUpdateEnabled(view.getSharedPreferences())) {
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
                    (mTotalItemsInList - mOnScreenItems) <= (mFirstVisibleItem + mVisibleThreshold)) {
                mLoadingItems = true;
                view.showSwipeLoading();

                //
                // Get more movies.
                //
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
        }
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

    }
}
