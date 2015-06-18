/*
 * Copyright (c) 2015.
 *
 * Dear maintainer:
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * So, good luck maintaining the code :D
 *
 * Eslam El-Meniawy
 * 04-Jun-2015 21:02
 */

package elmeniawy.eslam.yts.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import elmeniawy.eslam.yts.R;
import elmeniawy.eslam.yts.adapters.MoviesListAdapter;
import elmeniawy.eslam.yts.fragments.NavigationDrawerFragment;
import elmeniawy.eslam.yts.network.VolleySingleton;
import elmeniawy.eslam.yts.objects.Movie;

import static elmeniawy.eslam.yts.extras.UrlEndpoints.*;
import static elmeniawy.eslam.yts.extras.Keys.EndpointsMoviesList.*;


public class MainActivity extends AppCompatActivity {

    public static final String PREF_FILE_NAME = "YTSPref";
    private Toolbar toolbar;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private ArrayList<Movie> listMovies = new ArrayList<>();
    private MoviesListAdapter moviesListAdapter;
    private RecyclerView listMoviesRecycler;
    private DrawerLayout drawerLayout;
    private View containerView;
    private CheckedTextView errorView;
    private ProgressBar progressBar;
    private AdView mAdView;
    private int getPage = 1, moviesLimit = 15;
    private LinearLayoutManager linearLayoutManager;
    private boolean mLoadingItems = true;
    private int mOnScreenItems, mTotalItemsInList, mFirstVisibleItem, mPreviousTotal = 0, mVisibleThreshold = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        containerView = findViewById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, drawerLayout, toolbar);
        listMoviesRecycler = (RecyclerView) findViewById(R.id.list_movies_recycler);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        listMoviesRecycler.setLayoutManager(linearLayoutManager);
        moviesListAdapter = new MoviesListAdapter(MainActivity.this);
        errorView = (CheckedTextView) findViewById(R.id.error_view);
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendJSONRequest();
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listMoviesRecycler.setAdapter(moviesListAdapter);
        listMoviesRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mOnScreenItems = listMoviesRecycler.getChildCount();
                mTotalItemsInList = linearLayoutManager.getItemCount();
                mFirstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                if (mLoadingItems) {
                    if (mTotalItemsInList > mPreviousTotal+1) {
                        mLoadingItems = false;
                        mPreviousTotal = mTotalItemsInList;
                    }
                }
                if (!mLoadingItems && (mTotalItemsInList - mOnScreenItems) <= (mFirstVisibleItem + mVisibleThreshold)) {
                    listMovies.add(null);
                    moviesListAdapter.notifyItemInserted(listMovies.size());
                    getPage ++;
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getRequestUrl(moviesLimit, getPage), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            ArrayList<Movie> listMoreMovies = parseJSONResponse(response, false);
                            listMovies.remove(listMovies.size() - 1);
                            moviesListAdapter.notifyItemRemoved(listMovies.size());
                            for (int i = 0; i < listMoreMovies.size(); i++) {
                                Movie movie = listMoreMovies.get(i);
                                listMovies.add(movie);
                                moviesListAdapter.notifyItemInserted(listMovies.size());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            listMovies.remove(listMovies.size() - 1);
                            moviesListAdapter.notifyItemRemoved(listMovies.size());
                        }
                    });
                    requestQueue.add(request);
                    mLoadingItems = true;
                }
            }
        });
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        sendJSONRequest();
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(containerView)) {
            drawerLayout.closeDrawer(containerView);
        } else {
            super.onBackPressed();
        }
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
        errorView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getRequestUrl(moviesLimit, getPage), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                listMoviesRecycler.setVisibility(View.VISIBLE);
                listMovies = parseJSONResponse(response, true);
                SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("firstId", listMovies.get(0).getId());
                editor.apply();
                moviesListAdapter.setMoviesList(listMovies);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                listMoviesRecycler.setVisibility(View.GONE);
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    errorView.setText(R.string.no_internet_error);
                } else {
                    errorView.setText(R.string.get_movies_json_error);
                }
            }
        });
        requestQueue.add(request);
    }

    public static String getRequestUrl(int limit, int page) {
        return URL_LIST_MOVIES + URL_CHAR_QUESTION + URL_PARAM_LIMIT + limit + URL_CHAR_AMPERSAND + URL_PARAM_PAGE + page;
    }

    private ArrayList<Movie> parseJSONResponse(JSONObject response, boolean firstLoad) {
        ArrayList<Movie> listMovies = new ArrayList<>();
        if (response != null && response.length() > 0) {
            try {
                if (response.has(KEY_STATUS) && response.getString(KEY_STATUS).equals("ok")) {
                    JSONArray arrayMovies = response.getJSONObject(KEY_DATA).getJSONArray(KEY_MOVIES);
                    for (int i = 0; i < arrayMovies.length(); i++) {
                        JSONObject currentMovie = arrayMovies.getJSONObject(i);
                        long id = -1;
                        if (currentMovie.has(KEY_ID) && !currentMovie.isNull(KEY_ID)) {
                            id = currentMovie.getLong(KEY_ID);
                        }
                        String imdbCode = "";
                        if (currentMovie.has(KEY_IMDB_CODE) && !currentMovie.isNull(KEY_IMDB_CODE)) {
                            imdbCode = currentMovie.getString(KEY_IMDB_CODE);
                        }
                        String title = "No Data Available";
                        if (currentMovie.has(KEY_TITLE) && !currentMovie.isNull(KEY_TITLE)) {
                            title = currentMovie.getString(KEY_TITLE);
                        }
                        String year = "";
                        if (currentMovie.has(KEY_YEAR) && !currentMovie.isNull(KEY_YEAR)) {
                            year = String.valueOf(currentMovie.getInt(KEY_YEAR));
                        }
                        double rating = -1;
                        if (currentMovie.has(KEY_RATING) && !currentMovie.isNull(KEY_RATING)) {
                            rating = currentMovie.getDouble(KEY_RATING);
                        }
                        String genres = "";
                        if (currentMovie.has(KEY_GENRES) && !currentMovie.isNull(KEY_GENRES)) {
                            JSONArray arrayGenres = currentMovie.getJSONArray(KEY_GENRES);
                            for (int j = 0; j < arrayGenres.length(); j++) {
                                genres += arrayGenres.getString(j);
                                if (j < (arrayGenres.length() - 1)) {
                                    genres += "\t";
                                }
                            }
                        }
                        String backgroundImage = null;
                        if (currentMovie.has(KEY_BACKGROUND_IMAGE) && !currentMovie.isNull(KEY_BACKGROUND_IMAGE)) {
                            backgroundImage = currentMovie.getString(KEY_BACKGROUND_IMAGE);
                        }
                        String smallCoverImage = null;
                        if (currentMovie.has(KEY_SMALL_COVER_IMAGE) && !currentMovie.isNull(KEY_SMALL_COVER_IMAGE)) {
                            smallCoverImage = currentMovie.getString(KEY_SMALL_COVER_IMAGE);
                        }
                        String mediumCoverImage = null;
                        if (currentMovie.has(KEY_MEDIUM_COVER_IMAGE) && !currentMovie.isNull(KEY_MEDIUM_COVER_IMAGE)) {
                            mediumCoverImage = currentMovie.getString(KEY_MEDIUM_COVER_IMAGE);
                        }
                        String[] url = new String[0];
                        if (currentMovie.has(KEY_TORRENTS) && !currentMovie.isNull(KEY_TORRENTS)) {
                            JSONArray arrayTorrents = currentMovie.getJSONArray(KEY_TORRENTS);
                            url = new String[arrayTorrents.length()];
                            for (int k = 0; k < arrayTorrents.length(); k++) {
                                url[k] = arrayTorrents.getJSONObject(k).getString(KEY_URL);
                            }
                        }
                        String[] quality = new String[0];
                        if (currentMovie.has(KEY_TORRENTS) && !currentMovie.isNull(KEY_TORRENTS)) {
                            JSONArray arrayTorrents = currentMovie.getJSONArray(KEY_TORRENTS);
                            quality = new String[arrayTorrents.length()];
                            for (int k = 0; k < arrayTorrents.length(); k++) {
                                quality[k] = arrayTorrents.getJSONObject(k).getString(KEY_QUALITY);
                            }
                        }
                        String[] size = new String[0];
                        if (currentMovie.has(KEY_TORRENTS) && !currentMovie.isNull(KEY_TORRENTS)) {
                            JSONArray arrayTorrents = currentMovie.getJSONArray(KEY_TORRENTS);
                            size = new String[arrayTorrents.length()];
                            for (int k = 0; k < arrayTorrents.length(); k++) {
                                size[k] = arrayTorrents.getJSONObject(k).getString(KEY_SIZE);
                            }
                        }
                        Movie movie = new Movie();
                        movie.setImdbCode(imdbCode);
                        movie.setId(id);
                        movie.setTitle(title);
                        movie.setYear(year);
                        movie.setRating(rating);
                        movie.setGenres(genres);
                        movie.setBackgroundImage(backgroundImage);
                        movie.setSmallCoverImage(smallCoverImage);
                        movie.setMediumCoverImage(mediumCoverImage);
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
                        progressBar.setVisibility(View.GONE);
                        listMoviesRecycler.setVisibility(View.GONE);
                        errorView.setText(R.string.get_movies_json_error);
                    } else {
                        this.listMovies.remove(this.listMovies.size() - 1);
                        moviesListAdapter.notifyItemRemoved(this.listMovies.size());
                    }
                }
            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
        return listMovies;
    }
}
