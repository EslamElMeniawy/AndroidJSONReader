package elmeniawy.eslam.ytsag.screens.main;

//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.app.ProgressDialog;
//import android.app.SearchManager;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.net.Uri;
//import android.os.Environment;
//import android.support.annotation.NonNull;
//import android.support.design.widget.NavigationView;
//import android.support.design.widget.Snackbar;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.DialogFragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.content.FileProvider;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.view.MenuItemCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.SearchView;
//import android.support.v7.widget.SwitchCompat;
//import android.support.v7.widget.Toolbar;
//import android.util.DisplayMetrics;
//import android.util.TypedValue;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.android.volley.Cache;
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.NetworkResponse;
//import com.android.volley.NoConnectionError;
//import com.android.volley.ParseError;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.RetryPolicy;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.HttpHeaderParser;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.tonyodev.fetch.Fetch;
//import com.tonyodev.fetch.listener.FetchListener;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.Date;
//
//import elmeniawy.eslam.ytsag.AlarmReceiver;
//import elmeniawy.eslam.ytsag.screens.main.FragmentDialogDeveloper;
//import elmeniawy.eslam.ytsag.Movie;
//import elmeniawy.eslam.ytsag.MoviesListAdapter;
//import elmeniawy.eslam.ytsag.R;
//import elmeniawy.eslam.ytsag.SpacesItemDecoration;
//import elmeniawy.eslam.ytsag.UpdateReceiver;
//import elmeniawy.eslam.ytsag.VolleySingleton;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import elmeniawy.eslam.ytsag.R;
import elmeniawy.eslam.ytsag.root.MyApplication;
import elmeniawy.eslam.ytsag.storage.database.ApplicationDatabase;
import elmeniawy.eslam.ytsag.storage.preferences.MySharedPreferences;
import elmeniawy.eslam.ytsag.utils.FabricEvents;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements MainMVP.View,
        NavigationView.OnNavigationItemSelectedListener {
    //    public static final String PREF_FILE_NAME = "YTSPref";
//    private static final String TAG = MainActivity.class.getName();
//    private SharedPreferences sharedPreferences;
//    private SharedPreferences.Editor editor;
//    private SwitchCompat notifications, update;
//    private boolean notificationsEnabled, updateEnabled;
//    private RequestQueue requestQueue;
//    private ArrayList<Movie> listMovies = new ArrayList<>();
//    private MoviesListAdapter moviesListAdapter;
//    private SwipeRefreshLayout moviesSwipe;
//    private RecyclerView listMoviesRecycler;
//    private TextView errorView;
//    private AdView mAdView;
//    private int getPage = 1;
//    private GridLayoutManager gridLayoutManager;
//    private boolean mLoadingItems = true;
//    private int mOnScreenItems, mTotalItemsInList, mFirstVisibleItem, mPreviousTotal = 0, mVisibleThreshold = 1;
//    private RelativeLayout main;
//    private UpdateReceiver receiver;
//
//    private long downloadId = -1;
//    private Fetch fetch;
//    private ProgressDialog progressDialog;
//    private long downloadStartTime;
//
//    @SuppressLint("CommitPrefEdits")
    private static final String TAG = MainActivity.class.getSimpleName();

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
    private static final int PERMISSION_CODE = 0;

    //
    // Bind strings.
    //

    @BindString(R.string.interstitial_ad_unit_id)
    String interstitialAdUnitId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        // Inject activity.
        //

        ((MyApplication) getApplication()).getComponent().inject(this);

        //
        // Set timber tag.
        //

        Timber.tag(TAG);

        //
        // Initialize butter knife.
        //

        ButterKnife.bind(this);

        //
        // Log fabric content view event only once.
        //

        if (savedInstanceState == null) {
            FabricEvents.logContentViewEvent(TAG);
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
        // Set recycler view spacing.
        //

        recyclerViewMovies.addItemDecoration(new SpacesItemDecoration(MainActivity.this,
                spacingPx));

        //
        // Set recycler view layout manager.
        //

        gridLayoutManager = new GridLayoutManager(MainActivity.this, spanCount);
        recyclerViewMovies.setLayoutManager(gridLayoutManager);

        //
        // Set recycler view adapter.
        //

        moviesListAdapter = new MoviesListAdapter(MainActivity.this);
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
        // Load movies.
        //

        presenter.loadMovies();

        //
        // Load ads.
        //

        loadAds();

        //
        // Set schedulers.
        //

        presenter.setSchedulers();

        //
        // Check Update.
        //

        presenter.checkUpdate();


//
//        notificationsEnabled = sharedPreferences.getBoolean("notificationsEnabled", true);
//        updateEnabled = sharedPreferences.getBoolean("updateEnabled", true);
//
//        final AlarmReceiver alarm = new AlarmReceiver();
//        receiver = new UpdateReceiver();
//
//        notifications = (SwitchCompat) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_notifications)).findViewById(R.id.notifications_switch);
//        notifications.setChecked(notificationsEnabled);
//        if (notificationsEnabled) {
//            alarm.cancelAlarm(MainActivity.this);
//            alarm.setAlarm(MainActivity.this);
//        }
//
//        update = (SwitchCompat) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_auto_update)).findViewById(R.id.update_switch);
//        if (updateEnabled) {
//            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                updateEnabled = false;
//                editor.putBoolean("updateEnabled", false);
//                editor.apply();
//                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
//            } else {
//                receiver.cancelAlarm(MainActivity.this);
//                receiver.setAlarm(MainActivity.this);
//            }
//        } else {
//            receiver.cancelAlarm(MainActivity.this);
//        }
//        update.setChecked(updateEnabled);
//
//        moviesSwipe = (SwipeRefreshLayout) findViewById(R.id.MoviesSwipeRefresh);
//        listMoviesRecycler = (RecyclerView) findViewById(R.id.MoviesRecycler);
//        errorView = (TextView) findViewById(R.id.error_view);
//        errorView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                moviesSwipe.setVisibility(View.VISIBLE);
//                moviesSwipe.setRefreshing(true);
//                sendJSONRequest();
//            }
//        });
//
//        DisplayMetrics displayMetrics = MainActivity.this.getResources().getDisplayMetrics();
//        int itemWithDp = 201;
//        int totalWidthDp = (displayMetrics.widthPixels * 160 / displayMetrics.densityDpi);
//        int spanCount = Math.max(1, totalWidthDp / itemWithDp);
//        int totalSpacingDp = totalWidthDp - (spanCount * itemWithDp);
//        int spacingDp = totalSpacingDp / (spanCount * 2);
//        int spacingPx = spacingDp * (displayMetrics.densityDpi / 160);
//
//        gridLayoutManager = new GridLayoutManager(MainActivity.this, spanCount);
//        listMoviesRecycler.addItemDecoration(new SpacesItemDecoration(MainActivity.this, spacingPx));
//        listMoviesRecycler.setLayoutManager(gridLayoutManager);
//        moviesListAdapter = new MoviesListAdapter(MainActivity.this);
//        listMoviesRecycler.setAdapter(moviesListAdapter);
//        VolleySingleton volleySingleton = VolleySingleton.getInstance();
//        requestQueue = volleySingleton.getRequestQueue();
//        moviesSwipe.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
//        moviesSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                moviesSwipe.setRefreshing(true);
//                sendJSONRequest();
//            }
//        });
//        listMoviesRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                mOnScreenItems = listMoviesRecycler.getChildCount();
//                mTotalItemsInList = gridLayoutManager.getItemCount();
//                mFirstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();
//                if (mLoadingItems) {
//                    if (mTotalItemsInList > mPreviousTotal + 1) {
//                        mLoadingItems = false;
//                        mPreviousTotal = mTotalItemsInList;
//                    }
//                }
//                if (!mLoadingItems && (mTotalItemsInList - mOnScreenItems) <= (mFirstVisibleItem + mVisibleThreshold)) {
//                    moviesSwipe.setRefreshing(true);
//                    getPage++;
//                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://yts.am/api/v2/list_movies.json?limit=20&page=" + getPage, new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            ArrayList<Movie> listMoreMovies = parseJSONResponse(response, false);
//                            for (int i = 0; i < listMoreMovies.size(); i++) {
//                                Movie movie = listMoreMovies.get(i);
//                                listMovies.add(movie);
//                                moviesListAdapter.notifyItemInserted(listMovies.size());
//                            }
//                            moviesSwipe.setRefreshing(false);
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            getPage--;
//                            moviesSwipe.setRefreshing(false);
//                        }
//                    });
//                    RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//                    request.setRetryPolicy(policy);
//                    request.setTag(TAG);
//                    requestQueue.add(request);
//                    mLoadingItems = true;
//                }
//            }
//        });
//        moviesSwipe.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
//        moviesSwipe.setRefreshing(true);
//
//        sendJSONRequest();
//
//        notifications.setOnClickListener(new View.OnClickListener() {
//            @SuppressWarnings("ConstantConditions")
//            @Override
//            public void onClick(View v) {
//                notificationsEnabled = !notificationsEnabled;
//                editor.putBoolean("notificationsEnabled", notificationsEnabled);
//                editor.apply();
//                notifications.setChecked(notificationsEnabled);
//                if (notificationsEnabled) {
//                    alarm.setAlarm(MainActivity.this);
//                    Snackbar.make(MainActivity.this.findViewById(R.id.nav_view), getResources().getText(R.string.notifications_enabled), Snackbar.LENGTH_LONG).show();
//                } else {
//                    alarm.cancelAlarm(MainActivity.this);
//                    Snackbar.make(MainActivity.this.findViewById(R.id.nav_view), getResources().getText(R.string.notifications_disabled), Snackbar.LENGTH_LONG).show();
//                }
//            }
//        });
//        update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (updateEnabled) {
//                    updateEnabled = !updateEnabled;
//                    editor.putBoolean("updateEnabled", updateEnabled);
//                    editor.apply();
//                    update.setChecked(updateEnabled);
//                    receiver.cancelAlarm(MainActivity.this);
//                } else {
//                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                        update.setChecked(false);
//                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
//                    } else {
//                        updateEnabled = !updateEnabled;
//                        editor.putBoolean("updateEnabled", updateEnabled);
//                        editor.apply();
//                        update.setChecked(updateEnabled);
//                        receiver.setAlarm(MainActivity.this);
//                    }
//                }
//            }
//        });
//
//        main = (RelativeLayout) findViewById(R.id.main);
//
//        mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
//        mAdView.setAdListener(new AdListener() {
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                if (mAdView.getVisibility() == View.VISIBLE) {
//                    mAdView.setVisibility(View.GONE);
//                    main.setPadding(0, 16, 0, 16);
//                }
//            }
//
//            @Override
//            public void onAdLoaded() {
//                if (mAdView.getVisibility() == View.GONE) {
//                    mAdView.setVisibility(View.VISIBLE);
//                    main.setPadding(0, 16, 0, 0);
//                }
//            }
//        });
//
//        if (sharedPreferences.getBoolean("updateAvailable", false) && isOnline() && ((new Date().getTime() - sharedPreferences.getLong("lastCheck", new Date().getTime()) >= (24 * 60 * 60 * 1000)) || sharedPreferences.getBoolean("fromNotification", false))) {
//            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//            } else {
//                downloadUpdate();
//            }
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //
        // Set view.
        //

        presenter.setView(this);
        presenter.onResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPaused();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    public void setError(String error) {
        Timber.i("Setting error message to: %s.", error);
        tvError.setText(error);
    }

    @Override
    public void clearError() {
        Timber.i("Clear error message.");
        tvError.setText("");
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

    }

    @Override
    public void showSnackBar(String message) {

    }

    @Override
    public void setMainPadding() {
        main.setPadding(0, 16, 0, 0);
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

    }

    @Override
    public void showDownloadConfirmDialog() {

    }

    @Override
    public void showDownloadingDialog() {

    }

    @Override
    public void setDownloadProgress(int progress) {

    }

    @Override
    public void setDownloadMaxSize(int max) {

    }

    @Override
    public void cancelDownloadDialog() {

    }

    @Override
    public void showInstallDialog() {

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
    public void openDetails(MovieViewModel movie) {

    }

    @Override
    public void closeApp() {
        MainActivity.this.finish();
    }

    @OnClick(R.id.error_view)
    void errorClicked() {
        Timber.i("errorClicked");
        presenter.errorClicked();
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

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        assert drawer != null;
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main, menu);
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setIconifiedByDefault(false);
//        return true;
//    }

//    @SuppressLint("InflateParams")
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.nav_about) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//            LayoutInflater inflater = MainActivity.this.getLayoutInflater();
//            builder.setView(inflater.inflate(R.layout.dialog_about_app, null));
//            AlertDialog dialog = builder.create();
//            dialog.show();
//        } else if (id == R.id.nav_developer) {
//            FragmentManager fm = getSupportFragmentManager();
//            DialogFragment overlay = new FragmentDialogDeveloper();
//            overlay.show(fm, "FragmentDialog");
//        } else if (id == R.id.nav_check_update) {
//            if (sharedPreferences.getBoolean("updateAvailable", false)) {
//                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                } else {
//                    downloadUpdate();
//                }
//            } else {
//                final ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this, null, getString(R.string.checking_update), true);
//                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://raw.githubusercontent.com/EslamEl-Meniawy/AndroidJSONReader/master/AppData.json#" + System.currentTimeMillis(), new Response.Listener<JSONObject>() {
//                    @SuppressWarnings("ConstantConditions")
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        progressDialog.dismiss();
//                        if (response != null && response.length() > 0) {
//                            try {
//                                if (response.has("success") && response.getBoolean("success") && response.has("version") && !response.isNull("version")) {
//                                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
//                                    int verCode = pInfo.versionCode;
//                                    if (response.getInt("version") > verCode) {
//                                        editor.putBoolean("updateAvailable", true);
//                                        editor.apply();
//                                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                                        } else {
//                                            downloadUpdate();
//                                        }
//                                    } else {
//                                        editor.putBoolean("updateAvailable", false);
//                                        editor.apply();
//                                        Snackbar.make(MainActivity.this.findViewById(R.id.nav_view), getResources().getText(R.string.no_update), Snackbar.LENGTH_LONG).show();
//                                    }
//                                } else {
//                                    Snackbar.make(MainActivity.this.findViewById(R.id.nav_view), getResources().getText(R.string.update_error), Snackbar.LENGTH_LONG).show();
//                                }
//                            } catch (JSONException | PackageManager.NameNotFoundException e) {
//                                Snackbar.make(MainActivity.this.findViewById(R.id.nav_view), getResources().getText(R.string.update_error), Snackbar.LENGTH_LONG).show();
//                            }
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @SuppressWarnings("ConstantConditions")
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
//                        Snackbar.make(MainActivity.this.findViewById(R.id.nav_view), getResources().getText(R.string.update_error), Snackbar.LENGTH_LONG).show();
//                    }
//                });
//                RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//                request.setRetryPolicy(policy);
//                request.setTag(TAG);
//                requestQueue.add(request);
//            }
//        }
//
//        return true;
//    }
//
//    @Override
//    protected void onPause() {
//        if (mAdView != null) {
//            mAdView.pause();
//        }
//
//        if (downloadId != -1 && fetch != null) {
//            fetch.removeFetchListener(this);
//        }
//
//        super.onPause();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        try {
//            listMovies.clear();
//            moviesListAdapter.notifyDataSetChanged();
//            String savedMovies = sharedPreferences.getString("moviesList", "");
//            if (!savedMovies.equals("")) {
//                listMovies = parseJSONResponse(new JSONObject(savedMovies), true);
//                moviesListAdapter.setMoviesList(listMovies);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        if (mAdView != null) {
//            mAdView.resume();
//        }
//
//        if (downloadId != -1 && fetch != null) {
//            fetch.addFetchListener(MainActivity.this);
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        listMovies.clear();
//        moviesListAdapter.notifyDataSetChanged();
//
//        if (mAdView != null) {
//            mAdView.destroy();
//        }
//
//        if (downloadId != -1 && fetch != null) {
//            fetch.release();
//        }
//
//        super.onDestroy();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (requestQueue != null) {
//            requestQueue.cancelAll(TAG);
//        }
//    }
//
//    @SuppressWarnings("ConstantConditions")
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case 0:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Snackbar.make(mAdView, "", Snackbar.LENGTH_SHORT).addCallback(new Snackbar.Callback() {
//                        @Override
//                        public void onShown(Snackbar snackbar) {
//                            snackbar.dismiss();
//                        }
//                    }).show();
//                    updateEnabled = true;
//                    editor.putBoolean("updateEnabled", true);
//                    editor.apply();
//                    receiver.cancelAlarm(MainActivity.this);
//                    receiver.setAlarm(MainActivity.this);
//                    update.setChecked(updateEnabled);
//                } else {
//                    Snackbar.make(MainActivity.this.findViewById(R.id.nav_view), R.string.write_permission, Snackbar.LENGTH_INDEFINITE)
//                            .setAction(R.string.allow, new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
//                                }
//                            })
//                            .show();
//                }
//                break;
//            case 1:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Snackbar.make(mAdView, "", Snackbar.LENGTH_SHORT).addCallback(new Snackbar.Callback() {
//                        @Override
//                        public void onShown(Snackbar snackbar) {
//                            snackbar.dismiss();
//                        }
//                    }).show();
//                    downloadUpdate();
//                } else {
//                    Snackbar.make(MainActivity.this.findViewById(R.id.nav_view), R.string.write_permission, Snackbar.LENGTH_INDEFINITE)
//                            .setAction(R.string.allow, new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                                }
//                            })
//                            .show();
//                }
//                break;
//        }
//    }
//
//    private void sendJSONRequest() {
//        getPage = 1;
//        mPreviousTotal = 0;
//        errorView.setVisibility(View.GONE);
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://yts.am/api/v2/list_movies.json?limit=20&page=1", new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                moviesSwipe.setVisibility(View.VISIBLE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("moviesList", response.toString());
//                editor.apply();
//                listMovies = parseJSONResponse(response, true);
//                editor.putLong("firstId", listMovies.get(0).getId());
//                editor.apply();
//                moviesListAdapter.notifyDataSetChanged();
//                moviesListAdapter.setMoviesList(listMovies);
//                moviesSwipe.setRefreshing(false);
//            }
//        }, new Response.ErrorListener() {
//            @SuppressWarnings("ConstantConditions")
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                moviesSwipe.setRefreshing(false);
//                String strJson = sharedPreferences.getString("moviesList", "");
//                if (error instanceof NoConnectionError) {
//                    errorView.setText(R.string.no_internet_error);
//                } else {
//                    errorView.setText(R.string.get_movies_json_error);
//                }
//                if (strJson.equals("")) {
//                    errorView.setVisibility(View.VISIBLE);
//                    moviesSwipe.setVisibility(View.GONE);
//                } else {
//                    if (error instanceof NoConnectionError) {
//                        Snackbar.make(MainActivity.this.findViewById(R.id.MainCoordinatorLayout), getResources().getText(R.string.no_internet), Snackbar.LENGTH_LONG).show();
//                    } else {
//                        Snackbar.make(MainActivity.this.findViewById(R.id.MainCoordinatorLayout), getResources().getText(R.string.get_movies_error), Snackbar.LENGTH_LONG).show();
//                    }
//                    try {
//                        JSONObject jsonData = new JSONObject(strJson);
//                        listMovies = parseJSONResponse(jsonData, true);
//                        moviesListAdapter.setMoviesList(listMovies);
//                    } catch (JSONException e) {
//                        errorView.setVisibility(View.VISIBLE);
//                        moviesSwipe.setVisibility(View.GONE);
//                    }
//                }
//            }
//        }) {
//            @Override
//            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
//                try {
//                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
//                    if (cacheEntry == null) {
//                        cacheEntry = new Cache.Entry();
//                    }
//                    final long cacheHitButRefreshed = 3 * 60 * 1000;
//                    final long cacheExpired = 24 * 60 * 60 * 1000;
//                    long now = System.currentTimeMillis();
//                    final long softExpire = now + cacheHitButRefreshed;
//                    final long ttl = now + cacheExpired;
//                    cacheEntry.data = response.data;
//                    cacheEntry.softTtl = softExpire;
//                    cacheEntry.ttl = ttl;
//                    String headerValue;
//                    headerValue = response.headers.get("Date");
//                    if (headerValue != null) {
//                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
//                    }
//                    headerValue = response.headers.get("Last-Modified");
//                    if (headerValue != null) {
//                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
//                    }
//                    cacheEntry.responseHeaders = response.headers;
//                    final String jsonString = new String(response.data,
//                            HttpHeaderParser.parseCharset(response.headers));
//                    return Response.success(new JSONObject(jsonString), cacheEntry);
//                } catch (UnsupportedEncodingException e) {
//                    return Response.error(new ParseError(e));
//                } catch (JSONException e) {
//                    return Response.error(new ParseError(e));
//                }
//            }
//
//            @Override
//            protected void deliverResponse(JSONObject response) {
//                super.deliverResponse(response);
//            }
//
//            @Override
//            public void deliverError(VolleyError error) {
//                super.deliverError(error);
//            }
//
//            @Override
//            protected VolleyError parseNetworkError(VolleyError volleyError) {
//                return super.parseNetworkError(volleyError);
//            }
//        };
//        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        request.setRetryPolicy(policy);
//        request.setTag(TAG);
//        requestQueue.add(request);
//    }
//
//    @SuppressWarnings("ConstantConditions")
//    private ArrayList<Movie> parseJSONResponse(JSONObject response, boolean firstLoad) {
//        ArrayList<Movie> listMovies = new ArrayList<>();
//        if (response != null && response.length() > 0) {
//            try {
//                if (response.has("status") && response.getString("status").equals("ok")) {
//                    JSONArray arrayMovies = response.getJSONObject("data").getJSONArray("movies");
//                    for (int i = 0; i < arrayMovies.length(); i++) {
//                        JSONObject currentMovie = arrayMovies.getJSONObject(i);
//                        long id = -1;
//                        if (currentMovie.has("id") && !currentMovie.isNull("id")) {
//                            id = currentMovie.getLong("id");
//                        }
//                        String imdbCode = "";
//                        if (currentMovie.has("imdb_code") && !currentMovie.isNull("imdb_code")) {
//                            imdbCode = currentMovie.getString("imdb_code");
//                        }
//                        String title = "No Data Available";
//                        if (currentMovie.has("title") && !currentMovie.isNull("title")) {
//                            title = currentMovie.getString("title");
//                        }
//                        String year = "";
//                        if (currentMovie.has("year") && !currentMovie.isNull("year")) {
//                            year = String.valueOf(currentMovie.getInt("year"));
//                        }
//                        String genres = "";
//                        if (currentMovie.has("genres") && !currentMovie.isNull("genres")) {
//                            JSONArray arrayGenres = currentMovie.getJSONArray("genres");
//                            for (int j = 0; j < arrayGenres.length(); j++) {
//                                genres += arrayGenres.getString(j);
//                                if (j < (arrayGenres.length() - 1)) {
//                                    genres += "\t";
//                                }
//                            }
//                        }
//                        String backgroundImage = null;
//                        if (currentMovie.has("background_image") && !currentMovie.isNull("background_image")) {
//                            backgroundImage = currentMovie.getString("background_image");
//                        }
//                        String mediumCoverImage = null;
//                        if (currentMovie.has("medium_cover_image") && !currentMovie.isNull("medium_cover_image")) {
//                            mediumCoverImage = currentMovie.getString("medium_cover_image");
//                        }
//                        String synopsis = "";
//                        if (currentMovie.has("synopsis") && !currentMovie.isNull("synopsis")) {
//                            synopsis = currentMovie.getString("synopsis");
//                        }
//                        float rating = -1;
//                        if (currentMovie.has("rating") && !currentMovie.isNull("rating")) {
//                            rating = (float) currentMovie.getDouble("rating");
//                        }
//                        String[] url = new String[0];
//                        if (currentMovie.has("torrents") && !currentMovie.isNull("torrents")) {
//                            JSONArray arrayTorrents = currentMovie.getJSONArray("torrents");
//                            url = new String[arrayTorrents.length()];
//                            for (int k = 0; k < arrayTorrents.length(); k++) {
//                                url[k] = arrayTorrents.getJSONObject(k).getString("url");
//                            }
//                        }
//                        String[] quality = new String[0];
//                        if (currentMovie.has("torrents") && !currentMovie.isNull("torrents")) {
//                            JSONArray arrayTorrents = currentMovie.getJSONArray("torrents");
//                            quality = new String[arrayTorrents.length()];
//                            for (int k = 0; k < arrayTorrents.length(); k++) {
//                                quality[k] = arrayTorrents.getJSONObject(k).getString("quality");
//                            }
//                        }
//                        String[] size = new String[0];
//                        if (currentMovie.has("torrents") && !currentMovie.isNull("torrents")) {
//                            JSONArray arrayTorrents = currentMovie.getJSONArray("torrents");
//                            size = new String[arrayTorrents.length()];
//                            for (int k = 0; k < arrayTorrents.length(); k++) {
//                                size[k] = arrayTorrents.getJSONObject(k).getString("size");
//                            }
//                        }
//                        Movie movie = new Movie();
//                        movie.setId(id);
//                        movie.setImdbCode(imdbCode);
//                        movie.setTitle(title);
//                        movie.setYear(year);
//                        movie.setGenres(genres);
//                        movie.setBackgroundImage(backgroundImage);
//                        movie.setMediumCoverImage(mediumCoverImage);
//                        movie.setSynopsis(synopsis);
//                        movie.setRating(rating);
//                        movie.setTorrentsUrl(url);
//                        movie.setTorrentsQuality(quality);
//                        movie.setTorrentsSize(size);
//                        if (id != -1 && !title.equals("No Data Available")) {
//                            listMovies.add(movie);
//                        }
//                    }
//                } else {
//                    if (firstLoad) {
//                        errorView.setVisibility(View.VISIBLE);
//                        moviesSwipe.setVisibility(View.GONE);
//                        errorView.setText(R.string.get_movies_json_error);
//                    } else {
//                        moviesSwipe.setRefreshing(false);
//                    }
//                }
//            } catch (JSONException e) {
//                errorView.setVisibility(View.VISIBLE);
//                moviesSwipe.setVisibility(View.GONE);
//                errorView.setText(R.string.get_movies_json_error);
//            }
//        }
//        return listMovies;
//    }
//
//    private void downloadUpdate() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        builder.setMessage(R.string.update_notification_alert_title)
//                .setTitle(R.string.update);
//        builder.setPositiveButton(R.string.download, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                downloadStartTime = System.currentTimeMillis();
//
//                String PATH = Environment.getExternalStorageDirectory() + "/YTS/";
//                String fileName = "YTS" + downloadStartTime + ".apk";
//
//                try {
//                    File fetchDir = new File(PATH);
//                    deleteFileAndContents(fetchDir);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Snackbar.make(MainActivity.this.findViewById(R.id.nav_view), getResources().getText(R.string.update_error), Snackbar.LENGTH_LONG).show();
//                    return;
//                }
//
//                fetch = Fetch.getInstance(MainActivity.this);
//
//                String downloadUrl = "https://raw.githubusercontent.com/EslamEl-Meniawy/AndroidJSONReader/master/app/app-release.apk#" + downloadStartTime;
//                com.tonyodev.fetch.request.Request request = new com.tonyodev.fetch.request.Request(downloadUrl,
//                        PATH, fileName);
//                downloadId = fetch.enqueue(request);
//                fetch.addFetchListener(MainActivity.this);
//
//                progressDialog = new ProgressDialog(MainActivity.this);
//                progressDialog.setMessage(getString(R.string.downloading));
//                progressDialog.setIndeterminate(false);
//                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                progressDialog.setCancelable(true);
//                progressDialog.setProgressNumberFormat("%1d/%2d KB");
//
//                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialogInterface) {
//                        fetch.removeFetchListener(MainActivity.this);
//                        fetch.release();
//                        downloadId = -1;
//                    }
//                });
//
//                progressDialog.show();
//            }
//        });
//        builder.setNegativeButton(R.string.remind_later, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                editor.putLong("lastCheck", new Date().getTime());
//                editor.apply();
//                dialog.dismiss();
//            }
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
//
//    private boolean isOnline() {
//        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo netInfo = cm.getActiveNetworkInfo();
//        return netInfo != null && netInfo.isConnected();
//    }
//
//    @Override
//    public void onUpdate(long id, int status, int progress, long downloadedBytes, long fileSize, int error) {
//        if (downloadId == id) {
//            if (status == Fetch.STATUS_ERROR) {
//                progressDialog.dismiss();
//
//                fetch.removeFetchListener(MainActivity.this);
//                fetch.release();
//                downloadId = -1;
//
//                Snackbar.make(MainActivity.this.findViewById(R.id.nav_view), getResources().getText(R.string.update_error), Snackbar.LENGTH_LONG).show();
//            } else if (status == Fetch.STATUS_DONE) {
//                progressDialog.dismiss();
//
//                fetch.removeFetchListener(MainActivity.this);
//                fetch.release();
//                downloadId = -1;
//
//                if (android.os.Build.VERSION.SDK_INT >= 24) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider",
//                            new File(Environment.getExternalStorageDirectory() + "/YTS/YTS" + downloadStartTime + ".apk")));
//                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                } else {
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/YTS/YTS" + downloadStartTime + ".apk")),
//                            "application/vnd.android.package-archive");
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                }
//            } else {
//                progressDialog.setMax((int) (fileSize / 1024));
//                progressDialog.setProgress((int) (downloadedBytes / 1024));
//            }
//        }
//    }
//
//    private void deleteFileAndContents(File file) throws Exception {
//        if (file == null) {
//            return;
//        }
//
//        if (file.exists()) {
//            if (file.isDirectory()) {
//                File[] contents = file.listFiles();
//                if (contents != null) {
//                    for (File content : contents) {
//                        deleteFileAndContents(content);
//                    }
//                }
//            }
//            //noinspection ResultOfMethodCallIgnored
//            file.delete();
//        }
//    }

}
