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
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    public
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

    private InterstitialAd mInterstitialAd;
    private GridLayoutManager gridLayoutManager;
    private MoviesListAdapter moviesListAdapter;
    private List<MovieViewModel> moviesList = new ArrayList<>();

    //
    // Bind strings.
    //

    @BindString(R.string.interstitial_ad_unit_id)
    String interstitialAdUnitId;

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
            presenter.refreshMovies();
        });

        //
        // Set toolbar.
        //

        setSupportActionBar(toolbar);

        //
        // Show up button in toolbar.
        //

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

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

                presenter.recyclerScrolled(recyclerViewMovies.getChildCount(),
                        gridLayoutManager.getItemCount(),
                        gridLayoutManager.findFirstVisibleItemPosition());
            }
        });

        //
        // Load ads.
        //

        loadAds();

        //
        // Set search query.
        //

        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
            presenter.setQuery(getIntent().getStringExtra(SearchManager.QUERY));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //
        // Set view.
        //

        presenter.setView(this);

        //
        // Search movies.
        //

        presenter.searchMovies();

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
    protected void onNewIntent(Intent intent) {
        //
        // Set search query.
        //

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            presenter.setQuery(intent.getStringExtra(SearchManager.QUERY));
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
    public boolean getInterstitialLoaded() {
        return mInterstitialAd.isLoaded();
    }

    @Override
    public void showInterstitialAd() {
        mInterstitialAd.show();
    }

    @Override
    public void openDetails(MovieViewModel movie, List<TorrentViewModel> torrentsList) {
        startActivity(new Intent(SearchActivity.this,
                DetailsActivity.class)
                .putExtra(ConstantUtils.INTENT_KEY_MOVIE, movie)
                .putExtra(ConstantUtils.INTENT_KEY_TORRENTS, new ArrayList<>(torrentsList)));
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
}