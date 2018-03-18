package elmeniawy.eslam.ytsag.screens.search;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import elmeniawy.eslam.ytsag.R;
import elmeniawy.eslam.ytsag.root.MyApplication;
import elmeniawy.eslam.ytsag.screens.details.DetailsActivity;
import elmeniawy.eslam.ytsag.screens.main.MovieViewModel;
import elmeniawy.eslam.ytsag.screens.main.MoviesListAdapter;
import elmeniawy.eslam.ytsag.screens.main.SpacesItemDecoration;
import elmeniawy.eslam.ytsag.screens.main.TorrentViewModel;
import elmeniawy.eslam.ytsag.utils.ConstantUtils;
import elmeniawy.eslam.ytsag.utils.FabricEvents;
import timber.log.Timber;

public class SearchActivity extends AppCompatActivity implements SearchMVP.View {
    @Inject
    SearchMVP.Presenter presenter;

    //
    // Bind views.
    //

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.MoviesSwipeRefresh)
    SwipeRefreshLayout swipeRefreshLayoutMovies;

    @BindView(R.id.MoviesRecycler)
    RecyclerView recyclerViewMovies;

    @BindView(R.id.error_view)
    TextView tvError;

    @BindView(R.id.no_results_view)
    TextView tvNoResults;

    @BindView(R.id.main)
    RelativeLayout main;

    @BindView(R.id.adView)
    AdView adView;

    private GridLayoutManager gridLayoutManager;
    private MoviesListAdapter moviesListAdapter;
    private List<MovieViewModel> moviesList = new ArrayList<>();

    //
    // Bind strings.
    //

    @BindString(R.string.no_internet_error)
    String noInternetRefresh;

    @BindString(R.string.get_movies_json_error)
    String errorGetRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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
            FabricEvents.logContentViewEvent(SearchActivity.class.getSimpleName());
        }

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
            //presenter.refreshMovies();
        });

        //
        // Set toolbar.
        //

        setSupportActionBar(toolbar);

        //
        // Calculate item counts & spacing.
        //

        DisplayMetrics displayMetrics = SearchActivity.this.getResources().getDisplayMetrics();
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

        recyclerViewMovies.addItemDecoration(new SpacesItemDecoration(SearchActivity.this,
                spacingPx));

        recyclerViewMovies.setItemAnimator(new DefaultItemAnimator());

        //
        // Set recycler view layout manager.
        //

        gridLayoutManager = new GridLayoutManager(SearchActivity.this, spanCount);
        recyclerViewMovies.setLayoutManager(gridLayoutManager);

        //
        // Set recycler view adapter.
        //

        moviesListAdapter = new MoviesListAdapter(moviesList, SearchActivity.this);
        recyclerViewMovies.setAdapter(moviesListAdapter);

        //
        // Set recycler view scroll detector.
        //

        recyclerViewMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Timber.i("Recycler view onScrolled.");

                /*presenter.recyclerScrolled(recyclerViewMovies.getChildCount(),
                        gridLayoutManager.getItemCount(),
                        gridLayoutManager.findFirstVisibleItemPosition());*/
            }
        });

        //
        // Load ads.
        //

        loadAds();

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
//        noResultsView = (TextView) findViewById(R.id.no_results_view);
//
//        DisplayMetrics displayMetrics = SearchActivity.this.getResources().getDisplayMetrics();
//        int itemWithDp = 201;
//        int totalWidthDp = (displayMetrics.widthPixels * 160 / displayMetrics.densityDpi);
//        int spanCount = Math.max(1, totalWidthDp / itemWithDp);
//        int totalSpacingDp = totalWidthDp - (spanCount * itemWithDp);
//        int spacingDp = totalSpacingDp / (spanCount * 2);
//        int spacingPx = spacingDp * (displayMetrics.densityDpi / 160);
//
//        gridLayoutManager = new GridLayoutManager(SearchActivity.this, spanCount);
//        listMoviesRecycler.addItemDecoration(new SpacesItemDecoration(SearchActivity.this, spacingPx));
//        listMoviesRecycler.setLayoutManager(gridLayoutManager);
//        moviesListAdapter = new MoviesListAdapter(SearchActivity.this);
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
//                    try {
//                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://yts.am/api/v2/list_movies.json?limit=20&query_term=" + URLEncoder.encode(query, "UTF-8") + "&page=" + getPage, new Response.Listener<JSONObject>() {
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                ArrayList<Movie> listMoreMovies = parseJSONResponse(response, false);
//                                for (int i = 0; i < listMoreMovies.size(); i++) {
//                                    Movie movie = listMoreMovies.get(i);
//                                    listMovies.add(movie);
//                                    moviesListAdapter.notifyItemInserted(listMovies.size());
//                                }
//                                moviesSwipe.setRefreshing(false);
//                            }
//                        }, new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                getPage--;
//                                moviesSwipe.setRefreshing(false);
//                            }
//                        });
//                        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//                        request.setRetryPolicy(policy);
//                        request.setTag(TAG);
//                        requestQueue.add(request);
//                        mLoadingItems = true;
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//        moviesSwipe.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
//
//        handleIntent(getIntent());
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
    }

    @Override
    protected void onResume() {
        super.onResume();

        //
        // Set view.
        //

        presenter.setView(this);

        //
        // Call presenter resumed method to handle ad resume.
        //

        //presenter.onResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();

        //
        // Call presenter paused method to handle ad pause.
        //

        //presenter.onPaused();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //
        // Call presenter destroyed method to handle ad destroy & rx unsubscribe.
        //

        //presenter.onDestroyed();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            //presenter.searchMovies(intent.getStringExtra(SearchManager.QUERY));
        }
    }

    @Override
    public void showNoResults() {
        tvNoResults.setVisibility(View.VISIBLE);
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
    public void showError() {
        tvError.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideError() {
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
    public void openDetails(MovieViewModel movie, List<TorrentViewModel> torrentsList) {
        startActivity(new Intent(SearchActivity.this,
                DetailsActivity.class)
                .putExtra(ConstantUtils.INTENT_KEY_MOVIE, movie)
                .putExtra(ConstantUtils.INTENT_KEY_TORRENTS, new ArrayList<>(torrentsList)));
    }

    private void loadAds() {
        Timber.i("loadBannerAd");
        adView.loadAd(new AdRequest.Builder().build());

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                Timber.i("onAdFailedToLoad");
                //presenter.bannerAdFailed();
            }

            @Override
            public void onAdLoaded() {
                Timber.i("onAdLoaded");
                //presenter.bannerAdLoaded();
            }

            @Override
            public void onAdClicked() {
                Timber.i("onAdClicked");
                super.onAdClicked();
                //presenter.bannerClicked();
            }
        });
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        handleIntent(intent);
//    }
//
//    private void handleIntent(Intent intent) {
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            query = intent.getStringExtra(SearchManager.QUERY);
//            moviesSwipe.setRefreshing(true);
//            sendJSONRequest();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        if (mAdView != null) {
//            mAdView.pause();
//        }
//        super.onPause();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (mAdView != null) {
//            mAdView.resume();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        listMovies.clear();
//        moviesListAdapter.notifyDataSetChanged();
//        if (mAdView != null) {
//            mAdView.destroy();
//        }
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
//    private void sendJSONRequest() {
//        getPage = 1;
//        mPreviousTotal = 0;
//        errorView.setVisibility(View.GONE);
//        try {
//            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://yts.am/api/v2/list_movies.json?limit=20&query_term=" + URLEncoder.encode(query, "UTF-8") + "&page=1", new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    listMovies = parseJSONResponse(response, true);
//                    if (listMovies.isEmpty()) {
//                        noResultsView.setVisibility(View.VISIBLE);
//                        moviesSwipe.setVisibility(View.GONE);
//                        errorView.setVisibility(View.GONE);
//                    } else {
//                        moviesSwipe.setVisibility(View.VISIBLE);
//                        moviesListAdapter.notifyDataSetChanged();
//                        moviesListAdapter.setMoviesList(listMovies);
//                        listMoviesRecycler.setVisibility(View.VISIBLE);
//                    }
//                    moviesSwipe.setRefreshing(false);
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    moviesSwipe.setRefreshing(false);
//                    if (error instanceof NoConnectionError) {
//                        errorView.setText(R.string.no_internet_error);
//                    } else {
//                        errorView.setText(R.string.get_movies_json_error);
//                    }
//                    errorView.setVisibility(View.VISIBLE);
//                    moviesSwipe.setVisibility(View.GONE);
//                }
//            }) {
//                @Override
//                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
//                    try {
//                        Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
//                        if (cacheEntry == null) {
//                            cacheEntry = new Cache.Entry();
//                        }
//                        final long cacheHitButRefreshed = 3 * 60 * 1000;
//                        final long cacheExpired = 24 * 60 * 60 * 1000;
//                        long now = System.currentTimeMillis();
//                        final long softExpire = now + cacheHitButRefreshed;
//                        final long ttl = now + cacheExpired;
//                        cacheEntry.data = response.data;
//                        cacheEntry.softTtl = softExpire;
//                        cacheEntry.ttl = ttl;
//                        String headerValue;
//                        headerValue = response.headers.get("Date");
//                        if (headerValue != null) {
//                            cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
//                        }
//                        headerValue = response.headers.get("Last-Modified");
//                        if (headerValue != null) {
//                            cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
//                        }
//                        cacheEntry.responseHeaders = response.headers;
//                        final String jsonString = new String(response.data,
//                                HttpHeaderParser.parseCharset(response.headers));
//                        return Response.success(new JSONObject(jsonString), cacheEntry);
//                    } catch (UnsupportedEncodingException e) {
//                        return Response.error(new ParseError(e));
//                    } catch (JSONException e) {
//                        return Response.error(new ParseError(e));
//                    }
//                }
//
//                @Override
//                protected void deliverResponse(JSONObject response) {
//                    super.deliverResponse(response);
//                }
//
//                @Override
//                public void deliverError(VolleyError error) {
//                    super.deliverError(error);
//                }
//
//                @Override
//                protected VolleyError parseNetworkError(VolleyError volleyError) {
//                    return super.parseNetworkError(volleyError);
//                }
//            };
//            RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//            request.setRetryPolicy(policy);
//            request.setTag(TAG);
//            requestQueue.add(request);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
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
//                if (firstLoad) {
//                    errorView.setVisibility(View.VISIBLE);
//                    moviesSwipe.setVisibility(View.GONE);
//                    errorView.setText(R.string.get_movies_json_error);
//                } else {
//                    moviesSwipe.setRefreshing(false);
//                }
//            }
//        }
//        return listMovies;
//    }
}
