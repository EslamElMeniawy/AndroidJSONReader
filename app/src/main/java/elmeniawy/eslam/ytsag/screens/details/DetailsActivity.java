package elmeniawy.eslam.ytsag.screens.details;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import elmeniawy.eslam.ytsag.R;
import elmeniawy.eslam.ytsag.helpers.CustomRelativeLayout;
import elmeniawy.eslam.ytsag.helpers.ExpandableHeightGridView;
import elmeniawy.eslam.ytsag.root.MyApplication;
import elmeniawy.eslam.ytsag.screens.main.MovieViewModel;
import elmeniawy.eslam.ytsag.screens.main.TorrentViewModel;
import elmeniawy.eslam.ytsag.utils.ConstantUtils;
import elmeniawy.eslam.ytsag.utils.FabricEvents;
import timber.log.Timber;

public class DetailsActivity extends AppCompatActivity implements DetailsMVP.View {
    @Inject
    DetailsMVP.Presenter presenter;

    //
    // Bind views.
    //

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.loading)
    ProgressBar loading;

    @BindView(R.id.scrollView)
    ScrollView scrollData;

    @BindView(R.id.error_view)
    TextView tvError;

    @BindView(R.id.main)
    RelativeLayout main;

    @BindView(R.id.adView)
    AdView adView;

    @BindView(R.id.background)
    CustomRelativeLayout background;

    @BindView(R.id.movie_medium_image)
    ImageView image;

    @BindView(R.id.movie_tittle)
    TextView title;

    @BindView(R.id.movie_year)
    TextView year;

    @BindView(R.id.movie_genres)
    TextView genres;

    @BindView(R.id.movie_imdb_rate)
    TextView rate;

    @BindView(R.id.grid_torrents)
    ExpandableHeightGridView torrents;

    @BindView(R.id.movie_synopsis)
    TextView synopsis;

    //
    // Bind colors.
    //

    @BindColor(R.color.colorAccent)
    int accentColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

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
            FabricEvents.logContentViewEvent(DetailsActivity.class.getSimpleName());
        }

        //
        // Set progress bar color.
        //

        loading.getIndeterminateDrawable().setColorFilter(accentColor, PorterDuff.Mode.SRC_IN);

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
        // Expand torrents grid.
        //

        torrents.setExpanded(true);

        Bundle bundle = DetailsActivity.this.getIntent().getExtras();
        Timber.i("Bundle: %s.", bundle);

        if (bundle != null) {
            MovieViewModel movie = bundle.getParcelable(ConstantUtils.INTENT_KEY_MOVIE);
            Timber.i("Movie: %s.", movie);
            presenter.setMovie(movie);

            List<TorrentViewModel> torrents = bundle
                    .getParcelableArrayList(ConstantUtils.INTENT_KEY_TORRENTS);

            Timber.i("Torrents: %s.", torrents);
            presenter.setTorrents(torrents);
        }

        //
        // Load ads.
        //

        loadAds();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
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
    protected void onResume() {
        super.onResume();

        //
        // Set view.
        //

        presenter.setView(this);

        //
        // Fill movie data.
        //

        presenter.fillData();

        //
        // Call presenter resumed method to handle ad resume.
        //

        presenter.onResumed();
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
    public void hideLoading() {
        loading.setVisibility(View.GONE);
    }

    @Override
    public void showData() {
        scrollData.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError() {
        tvError.setVisibility(View.VISIBLE);
    }

    @Override
    public void setTitle(String title) {
        this.title.setText(title);
    }

    @Override
    public void setYear(String year) {
        this.year.setText(year);
    }

    @Override
    public void setGenres(String genres) {
        this.genres.setText(genres);
    }

    @Override
    public void setRate(String rate) {
        this.rate.setText(rate);
    }

    @Override
    public void setTorrents(List<TorrentViewModel> torrents) {
        this.torrents.setAdapter(new TorrentsAdapter(this, torrents));

        this.torrents.setOnItemClickListener((parent, view, position, id) ->
                presenter.torrentClicked(position));
    }

    @Override
    public void setSynopsis(String synopsis) {
        this.synopsis.setText(synopsis);
    }

    @Override
    public void loadMovieImage(String imageUrl) {
        Picasso
                .get()
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(image);
    }

    @Override
    public void loadMovieBackground(String backgroundUrl) {
        Picasso
                .get()
                .load(backgroundUrl)
                .into(background);
    }

    @Override
    public void openTorrentWebPage(String torrentUrl) {
        openBrowser(torrentUrl);
    }

    @Override
    public void openMovieWebPage(String imdbUrl) {
        openBrowser(imdbUrl);
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

    @OnClick(R.id.movie_imdb_link)
    void imdbClicked() {
        presenter.imdbClicked();
    }

    private void loadAds() {
        loadBannerAd();
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

    private void openBrowser(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
}
