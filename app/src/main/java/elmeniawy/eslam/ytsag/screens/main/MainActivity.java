package elmeniawy.eslam.ytsag.screens.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.support.v7.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2rx.RxFetch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import elmeniawy.eslam.ytsag.BuildConfig;
import elmeniawy.eslam.ytsag.R;
import elmeniawy.eslam.ytsag.helpers.BootReceiver;
import elmeniawy.eslam.ytsag.root.MyApplication;
import elmeniawy.eslam.ytsag.screens.details.DetailsActivity;
import elmeniawy.eslam.ytsag.storage.database.ApplicationDatabase;
import elmeniawy.eslam.ytsag.storage.preferences.MySharedPreferences;
import elmeniawy.eslam.ytsag.utils.ConstantUtils;
import elmeniawy.eslam.ytsag.utils.FabricEvents;
import elmeniawy.eslam.ytsag.utils.NetworkUtils;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements MainMVP.View,
        NavigationView.OnNavigationItemSelectedListener {

    @Inject
    MySharedPreferences mySharedPreferences;

    @Inject
    ApplicationDatabase database;

    @Inject
    MainMVP.Presenter presenter;

    //
    // Bind views.
    //

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.MoviesSwipeRefresh)
    SwipeRefreshLayout swipeRefreshLayoutMovies;

    @BindView(R.id.MoviesRecycler)
    RecyclerView recyclerViewMovies;

    @BindView(R.id.error_view)
    TextView tvError;

    @BindView(R.id.main)
    RelativeLayout main;

    @BindView(R.id.adView)
    AdView adView;

    private SwitchCompat switchNotifications, switchUpdate;
    private InterstitialAd mInterstitialAd;
    private GridLayoutManager gridLayoutManager;
    private MoviesListAdapter moviesListAdapter;
    private List<MovieViewModel> moviesList = new ArrayList<>();
    private static final int PERMISSION_CODE = 0;

    //
    // Bind strings.
    //

    @BindString(R.string.interstitial_ad_unit_id)
    String interstitialAdUnitId;

    @BindString(R.string.no_internet_error)
    String noInternetRefresh;

    @BindString(R.string.no_internet)
    String noInternet;

    @BindString(R.string.get_movies_json_error)
    String errorGetRefresh;

    @BindString(R.string.get_movies_error)
    String errorGet;

    @BindString(R.string.write_permission)
    String noPermission;

    @BindString(R.string.update_error)
    String errorUpdate;

    @BindString(R.string.no_update)
    String noUpdate;

    //
    // Dialogs.
    //

    private ProgressDialog dialogCheckingUpdate;
    private ProgressDialog dialogDownloading;

    //
    // Fetch.
    //

    private RxFetch rxFetch;
    private FetchListener fetchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        // Inject activity.
        //

        ((MyApplication) getApplication()).getComponent().inject(this);

        //
        // Initialize butter knife.
        //

        ButterKnife.bind(this);

        //
        // Log fabric content view event only once.
        //

        if (savedInstanceState == null) {
            FabricEvents.logContentViewEvent(MainActivity.class.getSimpleName());
        }

        //
        // Get drawer switches.
        //

        switchNotifications = navigationView
                .getMenu()
                .findItem(R.id.nav_notifications)
                .getActionView()
                .findViewById(R.id.notifications_switch);

        switchUpdate = navigationView
                .getMenu()
                .findItem(R.id.nav_auto_update)
                .getActionView()
                .findViewById(R.id.update_switch);

        //
        // Set swipe refresh layout refreshing colors & offset.
        //

        swipeRefreshLayoutMovies.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);

        swipeRefreshLayoutMovies.setProgressViewOffset(false,
                0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        24,
                        getResources().getDisplayMetrics()));

        //
        // Set swipe refresh layout refresh listener.
        //

        swipeRefreshLayoutMovies.setOnRefreshListener(() -> {
            Timber.i("OnRefreshListener");
            presenter.refreshMovies();
        });

        //
        // Set toolbar.
        //

        setSupportActionBar(toolbar);

        //
        // Add drawer toggle.
        //

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //
        // Set navigation select listener.
        //

        navigationView.setNavigationItemSelectedListener(this);

        //
        // Calculate item counts & spacing.
        //

        DisplayMetrics displayMetrics = MainActivity.this.getResources().getDisplayMetrics();
        int itemWithDp = 201;
        int totalWidthDp = (displayMetrics.widthPixels * 160 / displayMetrics.densityDpi);
        int spanCount = Math.max(1, totalWidthDp / itemWithDp);
        Timber.i("Number of items in row: %d", spanCount);
        int totalSpacingDp = totalWidthDp - (spanCount * itemWithDp);
        int spacingDp = totalSpacingDp / (spanCount * 2);
        Timber.i("Spacing between items in dp: %d", spacingDp);
        int spacingPx = spacingDp * (displayMetrics.densityDpi / 160);
        Timber.i("Spacing between items in px: %d", spacingPx);

        //
        // Set recycler view spacing & animator.
        //

        recyclerViewMovies.addItemDecoration(new SpacesItemDecoration(MainActivity.this,
                spacingPx));

        recyclerViewMovies.setItemAnimator(new DefaultItemAnimator());

        //
        // Set recycler view layout manager.
        //

        gridLayoutManager = new GridLayoutManager(MainActivity.this, spanCount);
        recyclerViewMovies.setLayoutManager(gridLayoutManager);

        //
        // Set recycler view adapter.
        //

        moviesListAdapter = new MoviesListAdapter(moviesList, MainActivity.this);
        recyclerViewMovies.setAdapter(moviesListAdapter);

        //
        // Set recycler view scroll detector.
        //

        recyclerViewMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Timber.i("Recycler view onScrolled.");

                presenter.recyclerScrolled(recyclerViewMovies.getChildCount(),
                        gridLayoutManager.getItemCount(),
                        gridLayoutManager.findFirstVisibleItemPosition());
            }
        });

        //
        // Load ads.
        //

        loadAds();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //
        // Set view.
        //

        presenter.setView(this);

        //
        // Load movies.
        //

        presenter.loadMovies();

        //
        // Set schedulers.
        //

        presenter.setSchedulers();

        //
        // Check Update.
        //

        presenter.checkUpdate();

        //
        // Call presenter resumed method to handle ad resume.
        //

        presenter.onResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();

        //
        // Call presenter paused method to handle ad pause.
        //

        presenter.onPaused();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //
        // Call presenter destroyed method to handle ad destroy & rx unsubscribe.
        //

        presenter.onDestroyed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //
        // Inflate the options menu from XML.
        //

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        //
        // Get the SearchView and set the searchable configuration.
        //

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        //
        // Assumes current activity is the searchable activity.
        //

        assert searchManager != null;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        //
        // Enable submit button.
        //

        searchView.setSubmitButtonEnabled(true);

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Timber.i("Selected menu item with title: %s.", item.getTitle());
        int id = item.getItemId();

        if (id == R.id.nav_notifications) {
            Timber.i("nav_notifications");
            presenter.notificationSwitchClicked();
        } else if (id == R.id.nav_about) {
            Timber.i("nav_about");
            presenter.aboutClicked();
        } else if (id == R.id.nav_developer) {
            Timber.i("nav_developer");
            presenter.developerClicked();
        } else if (id == R.id.nav_check_update) {
            Timber.i("nav_check_update");
            presenter.checkUpdateClicked();
        } else if (id == R.id.nav_auto_update) {
            Timber.i("nav_auto_update");
            presenter.updateSwitchClicked();
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        Timber.i("onBackPressed");
        presenter.backClicked();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Timber.i("onRequestPermissionsResult");
        Timber.i("requestCode: %d.", requestCode);

        if (requestCode == PERMISSION_CODE) {
            presenter.permissionCallback(grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED);
        }
    }

    @Override
    public MySharedPreferences getSharedPreferences() {
        return mySharedPreferences;
    }

    @Override
    public ApplicationDatabase getDatabase() {
        return database;
    }

    @Override
    public boolean notificationsSwitchChecked() {
        return switchNotifications.isChecked();
    }

    @Override
    public void enableNotificationsSwitch() {
        switchNotifications.setChecked(true);
    }

    @Override
    public void disableNotificationsSwitch() {
        switchNotifications.setChecked(false);
    }

    @Override
    public boolean updateSwitchChecked() {
        return switchUpdate.isChecked();
    }

    @Override
    public void enableUpdateSwitch() {
        switchUpdate.setChecked(true);
    }

    @Override
    public void disableUpdateSwitch() {
        switchUpdate.setChecked(false);
    }

    @SuppressLint("InflateParams")
    @Override
    public void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_about_app, null));
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void showDeveloperDialog() {
        FragmentManager fm = getSupportFragmentManager();
        DialogFragment overlay = new FragmentDialogDeveloper();
        overlay.show(fm, "FragmentDialogDeveloper");
    }

    @Override
    public void setInternetError() {
        tvError.setText(noInternetRefresh);
    }

    @Override
    public void setGetMoviesError() {
        tvError.setText(errorGetRefresh);
    }

    @Override
    public void showErrorTv() {
        tvError.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideErrorTv() {
        tvError.setVisibility(View.GONE);
    }

    @Override
    public void showSwipeLayout() {
        swipeRefreshLayoutMovies.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSwipeLayout() {
        swipeRefreshLayoutMovies.setVisibility(View.GONE);
    }

    @Override
    public void showSwipeLoading() {
        swipeRefreshLayoutMovies.setRefreshing(true);
    }

    @Override
    public void hideSwipeLoading() {
        swipeRefreshLayoutMovies.setRefreshing(false);
    }

    @Override
    public void updateMovies(MovieViewModel movie) {
        moviesList.add(movie);

        if (moviesList.isEmpty()) {
            moviesListAdapter.notifyItemInserted(0);
        } else {
            moviesListAdapter.notifyItemInserted(moviesList.size() - 1);
        }

        Timber.i("updateMovies: %d.", moviesList.size());
    }

    @Override
    public void clearMovies() {
        Timber.i("Clearing movies.");
        moviesList.clear();
        moviesListAdapter.notifyDataSetChanged();
    }

    @Override
    public void showInternetErrorSnackBar() {
        showSnackBar(noInternet);
    }

    @Override
    public void showGetMoviesErrorSnackBar() {
        showSnackBar(errorGet);
    }

    @Override
    public void showPermissionErrorSnackBar() {
        showSnackBar(noPermission);
    }

    @Override
    public void showUpdateErrorSnackBar() {
        showSnackBar(errorUpdate);
    }

    @Override
    public void showNoUpdateSnackBar() {
        showSnackBar(noUpdate);
    }

    @Override
    public void setMainPadding(int bottom) {
        main.setPadding(0, 16, 0, bottom);
    }

    @Override
    public void showAdView() {
        adView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAdView() {
        adView.setVisibility(View.GONE);
    }

    @Override
    public boolean isAdViewNull() {
        return adView == null;
    }

    @Override
    public void pauseAdView() {
        adView.pause();
    }

    @Override
    public void resumeAdView() {
        adView.resume();
    }

    @Override
    public void destroyAdView() {
        adView.destroy();
    }

    @Override
    public boolean getDrawerOpened() {
        return drawer.isDrawerOpen(GravityCompat.START);
    }

    @Override
    public void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean isStoragePermissionGranted() {
        return ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void requestStoragePermission() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_CODE);
    }

    @Override
    public void showCheckingUpdatesDialog() {
        dialogCheckingUpdate = ProgressDialog.show(MainActivity.this, null,
                getString(R.string.checking_update), true);
    }

    @Override
    public void cancelCheckingUpdatesDialog() {
        if (dialogCheckingUpdate != null) {
            dialogCheckingUpdate.dismiss();
        }
    }

    @Override
    public void showDownloadConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage(R.string.update_notification_alert_title)
                .setTitle(R.string.update);

        builder.setPositiveButton(R.string.download, (dialog, id) -> {
            dialog.dismiss();
            presenter.downloadConfirmed();
        });

        builder.setNegativeButton(R.string.remind_later, (dialog, id) -> {
            presenter.downloadRefused();
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void showDownloadingDialog() {
        dialogDownloading = new ProgressDialog(MainActivity.this);
        dialogDownloading.setMessage(getString(R.string.downloading));
        dialogDownloading.setIndeterminate(false);
        dialogDownloading.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialogDownloading.setCancelable(true);
        dialogDownloading.setProgressNumberFormat("%1d/%2d KB");
        dialogDownloading.setOnCancelListener(dialog -> presenter.downloadCanceled());
        dialogDownloading.show();
    }

    @Override
    public void setDownloadProgress(int progress) {
        if (dialogDownloading != null) {
            dialogDownloading.setProgress(progress);
        }
    }

    @Override
    public void setDownloadMaxSize(int max) {
        if (dialogDownloading != null) {
            dialogDownloading.setMax(max);
        }
    }

    @Override
    public void cancelDownloadDialog() {
        if (dialogDownloading != null) {
            dialogDownloading.dismiss();
        }
    }

    @Override
    public void showInstallDialog(String path) {
        File file = new File(path);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri downloaded_apk = FileProvider.getUriForFile(MainActivity.this,
                    getApplicationContext().getPackageName() + ".provider",
                    file);

            Intent intent = new Intent(Intent.ACTION_VIEW).setDataAndType(downloaded_apk,
                    "application/vnd.android.package-archive");

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);

            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public boolean getInterstitialLoaded() {
        return mInterstitialAd.isLoaded();
    }

    @Override
    public void showInterstitialAd() {
        mInterstitialAd.show();
    }

    @Override
    public boolean isOnline() {
        return NetworkUtils.isOnline(this);
    }

    @Override
    public void openDetails(MovieViewModel movie, List<TorrentViewModel> torrentsList) {
        startActivity(new Intent(MainActivity.this,
                DetailsActivity.class)
                .putExtra(ConstantUtils.INTENT_KEY_MOVIE, movie)
                .putExtra(ConstantUtils.INTENT_KEY_TORRENTS, new ArrayList<>(torrentsList)));
    }

    @Override
    public void closeApp() {
        MainActivity.this.finish();
    }

    @Override
    public void startBootReceiver() {
        Timber.i("startBootReceiver");
        ComponentName receiver = new ComponentName(this, BootReceiver.class);
        PackageManager pm = getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    @Override
    public void stopBootReceiver() {
        Timber.i("stopBootReceiver");
        ComponentName receiver = new ComponentName(this, BootReceiver.class);
        PackageManager pm = getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    @Override
    public int getVersionCode() {
        try {
            PackageInfo pInfo = getPackageManager()
                    .getPackageInfo(getPackageName(), 0);

            return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e);
        }


        return 0;
    }

    @Override
    public RxFetch getRxFetch() {
        if (rxFetch == null) {
            rxFetch = new RxFetch.Builder(MainActivity.this,
                    MainActivity.class.getSimpleName())
                    .setDownloadConcurrentLimit(1)
                    .enableLogging(BuildConfig.DEBUG)
                    .build();
        }

        return rxFetch;
    }

    @Override
    public FetchListener getFetchListener() {
        if (fetchListener == null) {
            fetchListener = new FetchListener() {
                @Override
                public void onQueued(Download download) {

                }

                @Override
                public void onCompleted(Download download) {
                    presenter.downloadComplete(download.getId());
                }

                @Override
                public void onError(Download download) {
                    presenter.downloadError(download.getId());
                }

                @Override
                public void onProgress(Download download, long l, long l1) {
                    presenter.downloadProgress(download.getId(), download.getDownloaded(),
                            download.getTotal());
                }

                @Override
                public void onPaused(Download download) {

                }

                @Override
                public void onResumed(Download download) {

                }

                @Override
                public void onCancelled(Download download) {

                }

                @Override
                public void onRemoved(Download download) {

                }

                @Override
                public void onDeleted(Download download) {

                }
            };
        }

        return fetchListener;
    }

    @Override
    public void addFetchListener(FetchListener fetchListener) {
        rxFetch.addListener(fetchListener);
    }

    @Override
    public void removeFetchListener(FetchListener fetchListener) {
        rxFetch.removeListener(fetchListener);
    }

    @Override
    public String getApkDirectoryPath() {
        return Environment.getExternalStorageDirectory() + "/YTS/";
    }

    @Override
    public String getApkPath(long downloadStartTime) {
        return Environment.getExternalStorageDirectory()
                + "/YTS/YTS" + downloadStartTime + ".apk";
    }

    @OnClick(R.id.error_view)
    void errorClicked() {
        Timber.i("errorClicked");
        presenter.errorClicked();
    }

    private void showSnackBar(String message) {
        Snackbar.make(main, message, Snackbar.LENGTH_LONG).show();
    }

    private void loadAds() {
        loadBannerAd();
        loadInterstitialAd();
    }

    private void loadBannerAd() {
        Timber.i("loadBannerAd");
        adView.loadAd(new AdRequest.Builder().build());

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                Timber.i("onAdFailedToLoad");
                presenter.bannerAdFailed();
            }

            @Override
            public void onAdLoaded() {
                Timber.i("onAdLoaded");
                presenter.bannerAdLoaded();
            }

            @Override
            public void onAdClicked() {
                Timber.i("onAdClicked");
                super.onAdClicked();
                presenter.bannerClicked();
            }
        });
    }

    private void loadInterstitialAd() {
        Timber.i("loadInterstitialAd");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(interstitialAdUnitId);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                Timber.i("onAdClicked");
                super.onAdClicked();
                presenter.interstitialClicked();
            }

            @Override
            public void onAdClosed() {
                Timber.i("onAdClosed");
                super.onAdClosed();
                presenter.interstitialClosed();
            }
        });
    }

}
