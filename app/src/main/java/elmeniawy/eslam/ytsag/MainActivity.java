package elmeniawy.eslam.ytsag;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    public static final String PREF_FILE_NAME = "YTSPref";
    private SharedPreferences sharedPreferences;
    private SwitchCompat notifications;
    private boolean notificationsEnabled;
    private RequestQueue requestQueue;
    private ArrayList<Movie> listMovies = new ArrayList<>();
    private MoviesListAdapter moviesListAdapter;
    private SwipeRefreshLayout moviesSwipe;
    private RecyclerView listMoviesRecycler;
    private TextView errorView;
    private AdView mAdView;
    private int getPage = 1;
    private GridLayoutManager gridLayoutManager;
    private boolean mLoadingItems = true, stop = false;
    private int mOnScreenItems, mTotalItemsInList, mFirstVisibleItem, mPreviousTotal = 0, mVisibleThreshold = 1;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        sharedPreferences = MainActivity.this.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);

        notificationsEnabled = sharedPreferences.getBoolean("notificationsEnabled", true);

        notifications = (SwitchCompat) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_notifications)).findViewById(R.id.notifications_switch);
        notifications.setChecked(notificationsEnabled);

        moviesSwipe = (SwipeRefreshLayout) findViewById(R.id.MoviesSwipeRefresh);
        listMoviesRecycler = (RecyclerView) findViewById(R.id.MoviesRecycler);
        errorView = (TextView) findViewById(R.id.error_view);
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    getPage ++;
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
                            getPage --;
                            moviesSwipe.setRefreshing(false);
                        }
                    });
                    requestQueue.add(request);
                    mLoadingItems = true;
                }
            }
        });
        moviesSwipe.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        moviesSwipe.setRefreshing(true);
        sendJSONRequest();
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            mSearchView.setIconified(false);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_notifications) {
            final SharedPreferences.Editor editor = sharedPreferences.edit();
            notificationsEnabled = !notificationsEnabled;
            editor.putBoolean("notificationsEnabled", notificationsEnabled);
            editor.apply();
            notifications.setChecked(notificationsEnabled);

            View actionView = MenuItemCompat.getActionView(item);
            actionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notificationsEnabled = !notificationsEnabled;
                    editor.putBoolean("notificationsEnabled", notificationsEnabled);
                    editor.apply();
                    notifications.setChecked(notificationsEnabled);
                }
            });
        } else if (id == R.id.nav_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = MainActivity.this.getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.dialog_about_app, null));
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (id == R.id.nav_developer) {
            FragmentManager fm = getSupportFragmentManager();
            DialogFragment overlay = new FragmentDialogDeveloper();
            overlay.show(fm, "FragmentDialog");
        }

        return true;
    }

    @Override
    protected void onPause() {
        stop = true;
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    private void sendJSONRequest() {
        getPage = 1;
        mPreviousTotal = 0;
        errorView.setVisibility(View.GONE);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://yts.ag/api/v2/list_movies.json?limit=20&page=1", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (!stop) {
                    moviesSwipe.setVisibility(View.VISIBLE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("moviesList", response.toString());
                    editor.apply();
                    listMovies = parseJSONResponse(response, true);
                    editor.putLong("firstId", listMovies.get(0).getId());
                    editor.apply();
                    moviesListAdapter.setMoviesList(listMovies);
                    moviesSwipe.setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (!stop) {
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
            }
        });
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
                            rating = (float)currentMovie.getDouble("rating");
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("movie", query);
        intent.putExtras(bundle);
        MainActivity.this.startActivity(intent);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
