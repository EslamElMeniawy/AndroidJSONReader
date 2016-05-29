package elmeniawy.eslam.ytsag;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    String query = "";
    private RequestQueue requestQueue;
    private ArrayList<Movie> listMovies = new ArrayList<>();
    private MoviesListAdapter moviesListAdapter;
    private SwipeRefreshLayout moviesSwipe;
    private RecyclerView listMoviesRecycler;
    private TextView errorView, noResultsView;
    private AdView mAdView;
    private int getPage = 1;
    private GridLayoutManager gridLayoutManager;
    private boolean mLoadingItems = true;
    private int mOnScreenItems, mTotalItemsInList, mFirstVisibleItem, mPreviousTotal = 0, mVisibleThreshold = 1;
    private RelativeLayout main;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        noResultsView = (TextView) findViewById(R.id.no_results_view);

        DisplayMetrics displayMetrics = SearchActivity.this.getResources().getDisplayMetrics();
        int spanCount = Math.max(1, (displayMetrics.widthPixels * 160 / displayMetrics.densityDpi) / 230);
        gridLayoutManager = new GridLayoutManager(SearchActivity.this, spanCount);
        listMoviesRecycler.setLayoutManager(gridLayoutManager);
        moviesListAdapter = new MoviesListAdapter(SearchActivity.this);
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
                    try {
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://yts.ag/api/v2/list_movies.json?limit=20&query_term=" + URLEncoder.encode(query, "UTF-8") + "&page=" + getPage, new Response.Listener<JSONObject>() {
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
                        requestQueue.add(request);
                        mLoadingItems = true;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        moviesSwipe.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

        handleIntent(getIntent());

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
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            moviesSwipe.setRefreshing(true);
            sendJSONRequest();
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
        listMovies.clear();
        moviesListAdapter.notifyDataSetChanged();
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    private void sendJSONRequest() {
        getPage = 1;
        mPreviousTotal = 0;
        errorView.setVisibility(View.GONE);
        try {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://yts.ag/api/v2/list_movies.json?limit=20&query_term=" + URLEncoder.encode(query, "UTF-8") + "&page=1", new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    listMovies = parseJSONResponse(response, true);
                    if (listMovies.isEmpty()) {
                        noResultsView.setVisibility(View.VISIBLE);
                        moviesSwipe.setVisibility(View.GONE);
                        errorView.setVisibility(View.GONE);
                    } else {
                        moviesSwipe.setVisibility(View.VISIBLE);
                        moviesListAdapter.notifyDataSetChanged();
                        moviesListAdapter.setMoviesList(listMovies);
                        listMoviesRecycler.setVisibility(View.VISIBLE);
                    }
                    moviesSwipe.setRefreshing(false);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    moviesSwipe.setRefreshing(false);
                    if (error instanceof NoConnectionError) {
                        errorView.setText(R.string.no_internet_error);
                    } else {
                        errorView.setText(R.string.get_movies_json_error);
                    }
                    errorView.setVisibility(View.VISIBLE);
                    moviesSwipe.setVisibility(View.GONE);
                }
            });
            requestQueue.add(request);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
                if (firstLoad) {
                    errorView.setVisibility(View.VISIBLE);
                    moviesSwipe.setVisibility(View.GONE);
                    errorView.setText(R.string.get_movies_json_error);
                } else {
                    moviesSwipe.setRefreshing(false);
                }
            }
        }
        return listMovies;
    }
}
