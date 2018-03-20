package elmeniawy.eslam.ytsag.screens.details;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import elmeniawy.eslam.ytsag.R;
import elmeniawy.eslam.ytsag.screens.main.MovieViewModel;
import elmeniawy.eslam.ytsag.screens.main.TorrentViewModel;
import elmeniawy.eslam.ytsag.utils.ConstantUtils;
import timber.log.Timber;

public class DetailsActivity extends AppCompatActivity {
//    private AdView mAdView;
//    private RelativeLayout main;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Bundle bundle = DetailsActivity.this.getIntent().getExtras();

        if (bundle != null) {
            MovieViewModel movie = bundle.getParcelable(ConstantUtils.INTENT_KEY_MOVIE);
            Timber.i("Movie: %s.", movie);

            List<TorrentViewModel> torrents = bundle
                    .getParcelableArrayList(ConstantUtils.INTENT_KEY_TORRENTS);

            Timber.i("Torrents: %s.", torrents);

            //gridView.setExpanded(true);
        }

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
//        TextView error = (TextView) findViewById(R.id.error_view);
//
//        Bundle bundle = DetailsActivity.this.getIntent().getExtras();
//        if (bundle != null) {
//            final Movie movie = bundle.getParcelable("movie");
//
//            TextView title = (TextView) findViewById(R.id.movie_tittle);
//            TextView year = (TextView) findViewById(R.id.movie_year);
//            TextView genres = (TextView) findViewById(R.id.movie_genres);
//            TextView rate = (TextView) findViewById(R.id.movie_imdb_rate);
//            TextView qualityText1 = (TextView) findViewById(R.id.movie_quality_1_text);
//            TextView qualityText2 = (TextView) findViewById(R.id.movie_quality_2_text);
//            TextView qualityText3 = (TextView) findViewById(R.id.movie_quality_3_text);
//            TextView qualitySize1 = (TextView) findViewById(R.id.movie_quality_1_size);
//            TextView qualitySize2 = (TextView) findViewById(R.id.movie_quality_2_size);
//            TextView qualitySize3 = (TextView) findViewById(R.id.movie_quality_3_size);
//            TextView synopsis = (TextView) findViewById(R.id.movie_synopsis);
//            ImageView imdbLink = (ImageView) findViewById(R.id.movie_imdb_link);
//            LinearLayout quality1 = (LinearLayout) findViewById(R.id.movie_quality_1);
//            LinearLayout quality2 = (LinearLayout) findViewById(R.id.movie_quality_2);
//            LinearLayout quality3 = (LinearLayout) findViewById(R.id.movie_quality_3);
//            final ImageView image = (ImageView) findViewById(R.id.movie_medium_image);
//            LinearLayout qualityLayout1 = (LinearLayout) findViewById(R.id.movie_quality_1_layout);
//            LinearLayout qualityLayout2 = (LinearLayout) findViewById(R.id.movie_quality_2_layout);
//            LinearLayout qualityLayout3 = (LinearLayout) findViewById(R.id.movie_quality_3_layout);
//            final RelativeLayout background = (RelativeLayout) findViewById(R.id.background);
//
//            Picasso.with(DetailsActivity.this)
//                    .load(movie.getMediumCoverImage())
//                    .placeholder(R.drawable.placeholder)
//                    .error(R.drawable.placeholder)
//                    .into(image);
//            Picasso.with(DetailsActivity.this)
//                    .load(movie.getBackgroundImage())
//                    .into(new Target() {
//                        @Override
//                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                            if (android.os.Build.VERSION.SDK_INT >= 16) {
//                                background.setBackground(new BitmapDrawable(DetailsActivity.this.getResources(), bitmap));
//                            } else {
//                                //noinspection deprecation
//                                background.setBackgroundDrawable(new BitmapDrawable(bitmap));
//                            }
//                        }
//
//                        @Override
//                        public void onBitmapFailed(Drawable errorDrawable) {
//                        }
//
//                        @Override
//                        public void onPrepareLoad(Drawable placeHolderDrawable) {
//                        }
//                    });
//            title.setText(movie.getTitle());
//            year.setText(movie.getYear());
//            genres.setText(movie.getGenres());
//            rate.setText(String.valueOf(movie.getRating()));
//            synopsis.setText(movie.getSynopsis());
//            imdbLink.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.imdb.com/title/" + movie.getImdbCode()));
//                    startActivity(browserIntent);
//                }
//            });
//            for (int i = 0; i < movie.getTorrentsQuality().length && i < movie.getTorrentsUrl().length && i < movie.getTorrentsSize().length; i++) {
//                if (i == 0) {
//                    qualityText1.setText(movie.getTorrentsQuality()[0]);
//                    qualitySize1.setText(movie.getTorrentsSize()[0]);
//                    quality1.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getTorrentsUrl()[0]));
//                            startActivity(browserIntent);
//                        }
//                    });
//                    qualityLayout1.setVisibility(View.VISIBLE);
//                }
//                if (i == 1) {
//                    qualityText2.setText(movie.getTorrentsQuality()[1]);
//                    qualitySize2.setText(movie.getTorrentsSize()[1]);
//                    quality2.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getTorrentsUrl()[1]));
//                            startActivity(browserIntent);
//                        }
//                    });
//                    qualityLayout2.setVisibility(View.VISIBLE);
//                }
//                if (i == 2) {
//                    qualityText3.setText(movie.getTorrentsQuality()[2]);
//                    qualitySize3.setText(movie.getTorrentsSize()[2]);
//                    quality3.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getTorrentsUrl()[2]));
//                            startActivity(browserIntent);
//                        }
//                    });
//                    qualityLayout3.setVisibility(View.VISIBLE);
//                }
//            }
//        } else {
//            scrollView.setVisibility(View.GONE);
//            error.setVisibility(View.VISIBLE);
//        }
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
    protected void onPause() {
//        if (mAdView != null) {
//            mAdView.pause();
//        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (mAdView != null) {
//            mAdView.resume();
//        }
    }

    @Override
    protected void onDestroy() {
//        if (mAdView != null) {
//            mAdView.destroy();
//        }
        super.onDestroy();
    }
}
