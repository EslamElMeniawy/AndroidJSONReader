package elmeniawy.eslam.ytsag;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String PREF_FILE_NAME = "YTSPref";
    private static final String TAG = MainActivity.class.getName();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SwitchCompat notifications, update;
    private boolean notificationsEnabled, updateEnabled;
    private RequestQueue requestQueue;
    private ArrayList<Movie> listMovies = new ArrayList<>();
    private MoviesListAdapter moviesListAdapter;
    private SwipeRefreshLayout moviesSwipe;
    private RecyclerView listMoviesRecycler;
    private TextView errorView;
    private AdView mAdView;
    private int getPage = 1;
    private GridLayoutManager gridLayoutManager;
    private boolean mLoadingItems = true;
    private int mOnScreenItems, mTotalItemsInList, mFirstVisibleItem, mPreviousTotal = 0, mVisibleThreshold = 1;
    private RelativeLayout main;
    private UpdateReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        sharedPreferences = MainActivity.this.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        notificationsEnabled = sharedPreferences.getBoolean("notificationsEnabled", true);
        updateEnabled = sharedPreferences.getBoolean("updateEnabled", true);

        final AlarmReceiver alarm = new AlarmReceiver();
        receiver = new UpdateReceiver();

        notifications = (SwitchCompat) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_notifications)).findViewById(R.id.notifications_switch);
        notifications.setChecked(notificationsEnabled);
        if (notificationsEnabled) {
            alarm.cancelAlarm(MainActivity.this);
            alarm.setAlarm(MainActivity.this);
        }

        update = (SwitchCompat) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_auto_update)).findViewById(R.id.update_switch);
        if (updateEnabled) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                updateEnabled = false;
                editor.putBoolean("updateEnabled", false);
                editor.apply();
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            } else {
                receiver.cancelAlarm(MainActivity.this);
                receiver.setAlarm(MainActivity.this);
            }
        } else {
            receiver.cancelAlarm(MainActivity.this);
        }
        update.setChecked(updateEnabled);

        moviesSwipe = (SwipeRefreshLayout) findViewById(R.id.MoviesSwipeRefresh);
        listMoviesRecycler = (RecyclerView) findViewById(R.id.MoviesRecycler);
        errorView = (TextView) findViewById(R.id.error_view);
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moviesSwipe.setVisibility(View.VISIBLE);
                moviesSwipe.setRefreshing(true);
                sendJSONRequest();
            }
        });

        DisplayMetrics displayMetrics = MainActivity.this.getResources().getDisplayMetrics();
        int spanCount = Math.max(1, (displayMetrics.widthPixels * 160 / displayMetrics.densityDpi) / 230);
        gridLayoutManager = new GridLayoutManager(MainActivity.this, spanCount);
        listMoviesRecycler.setLayoutManager(gridLayoutManager);
        moviesListAdapter = new MoviesListAdapter(MainActivity.this);
        listMoviesRecycler.setAdapter(moviesListAdapter);
        VolleySingleton volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        moviesSwipe.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
        moviesSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                moviesSwipe.setRefreshing(true);
                sendJSONRequest();
            }
        });
        listMoviesRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mOnScreenItems = listMoviesRecycler.getChildCount();
                mTotalItemsInList = gridLayoutManager.getItemCount();
                mFirstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();
                if (mLoadingItems) {
                    if (mTotalItemsInList > mPreviousTotal + 1) {
                        mLoadingItems = false;
                        mPreviousTotal = mTotalItemsInList;
                    }
                }
                if (!mLoadingItems && (mTotalItemsInList - mOnScreenItems) <= (mFirstVisibleItem + mVisibleThreshold)) {
                    moviesSwipe.setRefreshing(true);
                    getPage++;
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://yts.ag/api/v2/list_movies.json?limit=20&page=" + getPage, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            ArrayList<Movie> listMoreMovies = parseJSONResponse(response, false);
                            for (int i = 0; i < listMoreMovies.size(); i++) {
                                Movie movie = listMoreMovies.get(i);
                                listMovies.add(movie);
                                moviesListAdapter.notifyItemInserted(listMovies.size());
                            }
                            moviesSwipe.setRefreshing(false);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            getPage--;
                            moviesSwipe.setRefreshing(false);
                        }
                    });
                    RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    request.setRetryPolicy(policy);
                    request.setTag(TAG);
                    requestQueue.add(request);
                    mLoadingItems = true;
                }
            }
        });
        moviesSwipe.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        moviesSwipe.setRefreshing(true);

        sendJSONRequest();

        notifications.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(View v) {
                notificationsEnabled = !notificationsEnabled;
                editor.putBoolean("notificationsEnabled", notificationsEnabled);
                editor.apply();
                notifications.setChecked(notificationsEnabled);
                if (notificationsEnabled) {
                    alarm.setAlarm(MainActivity.this);
                    Snackbar.make(MainActivity.this.findViewById(R.id.nav_view), getResources().getText(R.string.notifications_enabled), Snackbar.LENGTH_LONG).show();
                } else {
                    alarm.cancelAlarm(MainActivity.this);
                    Snackbar.make(MainActivity.this.findViewById(R.id.nav_view), getResources().getText(R.string.notifications_disabled), Snackbar.LENGTH_LONG).show();
                }
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateEnabled) {
                    updateEnabled = !updateEnabled;
                    editor.putBoolean("updateEnabled", updateEnabled);
                    editor.apply();
                    update.setChecked(updateEnabled);
                    receiver.cancelAlarm(MainActivity.this);
                } else {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        update.setChecked(false);
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                    } else {
                        updateEnabled = !updateEnabled;
                        editor.putBoolean("updateEnabled", updateEnabled);
                        editor.apply();
                        update.setChecked(updateEnabled);
                        receiver.setAlarm(MainActivity.this);
                    }
                }
            }
        });

        main = (RelativeLayout) findViewById(R.id.main);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                if (mAdView.getVisibility() == View.VISIBLE) {
                    mAdView.setVisibility(View.GONE);
                    main.setPadding(0, 16, 0, 16);
                }
            }

            @Override
            public void onAdLoaded() {
                if (mAdView.getVisibility() == View.GONE) {
                    mAdView.setVisibility(View.VISIBLE);
                    main.setPadding(0, 16, 0, 0);
                }
            }
        });

        if (sharedPreferences.getBoolean("updateAvailable", false) && isOnline() && ((new Date().getTime() - sharedPreferences.getLong("lastCheck", new Date().getTime()) >= (24 * 60 * 60 * 1000)) || sharedPreferences.getBoolean("fromNotification", false))) {
            downloadUpdate();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        return true;
    }

    @SuppressLint("InflateParams")
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = MainActivity.this.getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.dialog_about_app, null));
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (id == R.id.nav_developer) {
            FragmentManager fm = getSupportFragmentManager();
            DialogFragment overlay = new FragmentDialogDeveloper();
            overlay.show(fm, "FragmentDialog");
        } else if (id == R.id.nav_check_update) {
            if (sharedPreferences.getBoolean("updateAvailable", false)) {
                downloadUpdate();
            } else {
                final ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this, null, getString(R.string.checking_update), true);
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://raw.githubusercontent.com/EslamEl-Meniawy/AndroidJSONReader/master/AppData.json", new Response.Listener<JSONObject>() {
                    @SuppressWarnings("ConstantConditions")
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        if (response != null && response.length() > 0) {
                            try {
                                if (response.has("success") && response.getBoolean("success") && response.has("version") && !response.isNull("version")) {
                                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                                    int verCode = pInfo.versionCode;
                                    if (response.getInt("version") > verCode) {
                                        editor.putBoolean("updateAvailable", true);
                                        editor.apply();
                                        downloadUpdate();
                                    } else {
                                        editor.putBoolean("updateAvailable", false);
                                        editor.apply();
                                        Snackbar.make(MainActivity.this.findViewById(R.id.nav_view), getResources().getText(R.string.no_update), Snackbar.LENGTH_LONG).show();
                                    }
                                } else {
                                    Snackbar.make(MainActivity.this.findViewById(R.id.nav_view), getResources().getText(R.string.update_error), Snackbar.LENGTH_LONG).show();
                                }
                            } catch (JSONException | PackageManager.NameNotFoundException e) {
                                Snackbar.make(MainActivity.this.findViewById(R.id.nav_view), getResources().getText(R.string.update_error), Snackbar.LENGTH_LONG).show();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @SuppressWarnings("ConstantConditions")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Snackbar.make(MainActivity.this.findViewById(R.id.nav_view), getResources().getText(R.string.update_error), Snackbar.LENGTH_LONG).show();
                    }
                });
                RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                request.setRetryPolicy(policy);
                request.setTag(TAG);
                requestQueue.add(request);
            }
        }

        return true;
    }

    @Override
    protected void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            listMovies.clear();
            moviesListAdapter.notifyDataSetChanged();
            String savedMovies = sharedPreferences.getString("moviesList", "");
            if (!savedMovies.equals("")) {
                listMovies = parseJSONResponse(new JSONObject(savedMovies), true);
                moviesListAdapter.setMoviesList(listMovies);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        listMovies.clear();
        moviesListAdapter.notifyDataSetChanged();
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateEnabled = true;
                    editor.putBoolean("updateEnabled", true);
                    editor.apply();
                    receiver.cancelAlarm(MainActivity.this);
                    receiver.setAlarm(MainActivity.this);
                    update.setChecked(updateEnabled);
                } else {
                    Snackbar.make(MainActivity.this.findViewById(R.id.nav_view), R.string.write_permission, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.allow, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                                }
                            })
                            .show();
                }
                break;
        }
    }

    private void sendJSONRequest() {
        getPage = 1;
        mPreviousTotal = 0;
        errorView.setVisibility(View.GONE);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://yts.ag/api/v2/list_movies.json?limit=20&page=1", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                moviesSwipe.setVisibility(View.VISIBLE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("moviesList", response.toString());
                editor.apply();
                listMovies = parseJSONResponse(response, true);
                editor.putLong("firstId", listMovies.get(0).getId());
                editor.apply();
                moviesListAdapter.notifyDataSetChanged();
                moviesListAdapter.setMoviesList(listMovies);
                moviesSwipe.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onErrorResponse(VolleyError error) {
                moviesSwipe.setRefreshing(false);
                String strJson = sharedPreferences.getString("moviesList", "");
                if (error instanceof NoConnectionError) {
                    errorView.setText(R.string.no_internet_error);
                } else {
                    errorView.setText(R.string.get_movies_json_error);
                }
                if (strJson.equals("")) {
                    errorView.setVisibility(View.VISIBLE);
                    moviesSwipe.setVisibility(View.GONE);
                } else {
                    if (error instanceof NoConnectionError) {
                        Snackbar.make(MainActivity.this.findViewById(R.id.MainCoordinatorLayout), getResources().getText(R.string.no_internet), Snackbar.LENGTH_LONG).show();
                    } else {
                        Snackbar.make(MainActivity.this.findViewById(R.id.MainCoordinatorLayout), getResources().getText(R.string.get_movies_error), Snackbar.LENGTH_LONG).show();
                    }
                    try {
                        JSONObject jsonData = new JSONObject(strJson);
                        listMovies = parseJSONResponse(jsonData, true);
                        moviesListAdapter.setMoviesList(listMovies);
                    } catch (JSONException e) {
                        errorView.setVisibility(View.VISIBLE);
                        moviesSwipe.setVisibility(View.GONE);
                    }
                }
            }
        });
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        request.setTag(TAG);
        requestQueue.add(request);
    }

    private ArrayList<Movie> parseJSONResponse(JSONObject response, boolean firstLoad) {
        ArrayList<Movie> listMovies = new ArrayList<>();
        if (response != null && response.length() > 0) {
            try {
                if (response.has("status") && response.getString("status").equals("ok")) {
                    JSONArray arrayMovies = response.getJSONObject("data").getJSONArray("movies");
                    for (int i = 0; i < arrayMovies.length(); i++) {
                        JSONObject currentMovie = arrayMovies.getJSONObject(i);
                        long id = -1;
                        if (currentMovie.has("id") && !currentMovie.isNull("id")) {
                            id = currentMovie.getLong("id");
                        }
                        String imdbCode = "";
                        if (currentMovie.has("imdb_code") && !currentMovie.isNull("imdb_code")) {
                            imdbCode = currentMovie.getString("imdb_code");
                        }
                        String title = "No Data Available";
                        if (currentMovie.has("title") && !currentMovie.isNull("title")) {
                            title = currentMovie.getString("title");
                        }
                        String year = "";
                        if (currentMovie.has("year") && !currentMovie.isNull("year")) {
                            year = String.valueOf(currentMovie.getInt("year"));
                        }
                        String genres = "";
                        if (currentMovie.has("genres") && !currentMovie.isNull("genres")) {
                            JSONArray arrayGenres = currentMovie.getJSONArray("genres");
                            for (int j = 0; j < arrayGenres.length(); j++) {
                                genres += arrayGenres.getString(j);
                                if (j < (arrayGenres.length() - 1)) {
                                    genres += "\t";
                                }
                            }
                        }
                        String backgroundImage = null;
                        if (currentMovie.has("background_image") && !currentMovie.isNull("background_image")) {
                            backgroundImage = currentMovie.getString("background_image");
                        }
                        String mediumCoverImage = null;
                        if (currentMovie.has("medium_cover_image") && !currentMovie.isNull("medium_cover_image")) {
                            mediumCoverImage = currentMovie.getString("medium_cover_image");
                        }
                        String synopsis = "";
                        if (currentMovie.has("synopsis") && !currentMovie.isNull("synopsis")) {
                            synopsis = currentMovie.getString("synopsis");
                        }
                        float rating = -1;
                        if (currentMovie.has("rating") && !currentMovie.isNull("rating")) {
                            rating = (float) currentMovie.getDouble("rating");
                        }
                        String[] url = new String[0];
                        if (currentMovie.has("torrents") && !currentMovie.isNull("torrents")) {
                            JSONArray arrayTorrents = currentMovie.getJSONArray("torrents");
                            url = new String[arrayTorrents.length()];
                            for (int k = 0; k < arrayTorrents.length(); k++) {
                                url[k] = arrayTorrents.getJSONObject(k).getString("url");
                            }
                        }
                        String[] quality = new String[0];
                        if (currentMovie.has("torrents") && !currentMovie.isNull("torrents")) {
                            JSONArray arrayTorrents = currentMovie.getJSONArray("torrents");
                            quality = new String[arrayTorrents.length()];
                            for (int k = 0; k < arrayTorrents.length(); k++) {
                                quality[k] = arrayTorrents.getJSONObject(k).getString("quality");
                            }
                        }
                        String[] size = new String[0];
                        if (currentMovie.has("torrents") && !currentMovie.isNull("torrents")) {
                            JSONArray arrayTorrents = currentMovie.getJSONArray("torrents");
                            size = new String[arrayTorrents.length()];
                            for (int k = 0; k < arrayTorrents.length(); k++) {
                                size[k] = arrayTorrents.getJSONObject(k).getString("size");
                            }
                        }
                        Movie movie = new Movie();
                        movie.setId(id);
                        movie.setImdbCode(imdbCode);
                        movie.setTitle(title);
                        movie.setYear(year);
                        movie.setGenres(genres);
                        movie.setBackgroundImage(backgroundImage);
                        movie.setMediumCoverImage(mediumCoverImage);
                        movie.setSynopsis(synopsis);
                        movie.setRating(rating);
                        movie.setTorrentsUrl(url);
                        movie.setTorrentsQuality(quality);
                        movie.setTorrentsSize(size);
                        if (id != -1 && !title.equals("No Data Available")) {
                            listMovies.add(movie);
                        }
                    }
                } else {
                    if (firstLoad) {
                        errorView.setVisibility(View.VISIBLE);
                        moviesSwipe.setVisibility(View.GONE);
                        errorView.setText(R.string.get_movies_json_error);
                    } else {
                        moviesSwipe.setRefreshing(false);
                    }
                }
            } catch (JSONException e) {
                errorView.setVisibility(View.VISIBLE);
                moviesSwipe.setVisibility(View.GONE);
                errorView.setText(R.string.get_movies_json_error);
            }
        }
        return listMovies;
    }

    private void downloadUpdate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(R.string.update_notification_alert_title)
                .setTitle(R.string.update);
        builder.setPositiveButton(R.string.download, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                UpdateApp updateApp = new UpdateApp();
                updateApp.setContext(getApplicationContext());
                updateApp.execute("https://raw.githubusercontent.com/EslamEl-Meniawy/AndroidJSONReader/master/app/app-release.apk");
            }
        });
        builder.setNegativeButton(R.string.remind_later, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                editor.putLong("lastCheck", new Date().getTime());
                editor.apply();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public class UpdateApp extends AsyncTask<String, Void, Void> {
        private Context context;
        private ProgressDialog progressDialog;

        public void setContext(Context contextf) {
            context = contextf;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivity.this, null, getString(R.string.downloading), true);
        }

        @SuppressWarnings({"ResultOfMethodCallIgnored", "ConstantConditions"})
        @Override
        protected Void doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();

                String PATH = Environment.getExternalStorageDirectory() + "/YTS/";
                File file = new File(PATH);
                file.mkdirs();
                File outputFile = new File(file, "YTS.apk");
                if (outputFile.exists()) {
                    outputFile.delete();
                }
                FileOutputStream fos = new FileOutputStream(outputFile);

                InputStream is = c.getInputStream();

                byte[] buffer = new byte[1024];
                int len1;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                fos.close();
                is.close();

                editor.putBoolean("updateAvailable", false);
                editor.apply();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/YTS/YTS.apk")), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (Exception e) {
                Snackbar.make(MainActivity.this.findViewById(R.id.nav_view), getResources().getText(R.string.update_error), Snackbar.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}
