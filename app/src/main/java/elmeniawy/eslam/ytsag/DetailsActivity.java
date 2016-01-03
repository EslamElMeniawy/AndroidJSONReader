package elmeniawy.eslam.ytsag;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class DetailsActivity extends AppCompatActivity {
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        TextView error = (TextView) findViewById(R.id.error_view);

        Bundle bundle = DetailsActivity.this.getIntent().getExtras();
        if (bundle != null) {
            final Movie movie = bundle.getParcelable("movie");

            TextView title = (TextView) findViewById(R.id.movie_tittle);
            TextView year = (TextView) findViewById(R.id.movie_year);
            TextView genres = (TextView) findViewById(R.id.movie_genres);
            TextView rate = (TextView) findViewById(R.id.movie_imdb_rate);
            TextView qualityText1 = (TextView) findViewById(R.id.movie_quality_1_text);
            TextView qualityText2 = (TextView) findViewById(R.id.movie_quality_2_text);
            TextView qualityText3 = (TextView) findViewById(R.id.movie_quality_3_text);
            TextView qualitySize1 = (TextView) findViewById(R.id.movie_quality_1_size);
            TextView qualitySize2 = (TextView) findViewById(R.id.movie_quality_2_size);
            TextView qualitySize3 = (TextView) findViewById(R.id.movie_quality_3_size);
            TextView synopsis = (TextView) findViewById(R.id.movie_synopsis);
            ImageView imdbLink = (ImageView) findViewById(R.id.movie_imdb_link);
            LinearLayout quality1 = (LinearLayout) findViewById(R.id.movie_quality_1);
            LinearLayout quality2 = (LinearLayout) findViewById(R.id.movie_quality_2);
            LinearLayout quality3 = (LinearLayout) findViewById(R.id.movie_quality_3);
            final ImageView image = (ImageView) findViewById(R.id.movie_medium_image);
            LinearLayout qualityLayout1 = (LinearLayout) findViewById(R.id.movie_quality_1_layout);
            LinearLayout qualityLayout2 = (LinearLayout) findViewById(R.id.movie_quality_2_layout);
            LinearLayout qualityLayout3 = (LinearLayout) findViewById(R.id.movie_quality_3_layout);
            final RelativeLayout background = (RelativeLayout) findViewById(R.id.background);

            VolleySingleton volleySingleton = VolleySingleton.getInstance();
            ImageLoader imageLoader = volleySingleton.getImageLoader();

            imageLoader.get(movie.getMediumCoverImage(), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    image.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            imageLoader.get(movie.getBackgroundImage(), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if (android.os.Build.VERSION.SDK_INT >= 16) {
                        background.setBackground(new BitmapDrawable(DetailsActivity.this.getResources(), response.getBitmap()));
                    } else {
                        background.setBackgroundDrawable(new BitmapDrawable(response.getBitmap()));
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            title.setText(movie.getTitle());
            year.setText(movie.getYear());
            genres.setText(movie.getGenres());
            rate.setText(String.valueOf(movie.getRating()));
            synopsis.setText(movie.getSynopsis());
            imdbLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.imdb.com/title/" + movie.getImdbCode()));
                    startActivity(browserIntent);
                }
            });
            for (int i = 0; i < movie.getTorrentsQuality().length && i < movie.getTorrentsUrl().length && i < movie.getTorrentsSize().length; i++) {
                if (i == 0) {
                    qualityText1.setText(movie.getTorrentsQuality()[0]);
                    qualitySize1.setText(movie.getTorrentsSize()[0]);
                    quality1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getTorrentsUrl()[0]));
                            startActivity(browserIntent);
                        }
                    });
                    qualityLayout1.setVisibility(View.VISIBLE);
                }
                if (i == 1) {
                    qualityText2.setText(movie.getTorrentsQuality()[1]);
                    qualitySize2.setText(movie.getTorrentsSize()[1]);
                    quality2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getTorrentsUrl()[1]));
                            startActivity(browserIntent);
                        }
                    });
                    qualityLayout2.setVisibility(View.VISIBLE);
                }
                if (i == 2) {
                    qualityText3.setText(movie.getTorrentsQuality()[2]);
                    qualitySize3.setText(movie.getTorrentsSize()[2]);
                    quality3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getTorrentsUrl()[2]));
                            startActivity(browserIntent);
                        }
                    });
                    qualityLayout3.setVisibility(View.VISIBLE);
                }
            }
        } else {
            scrollView.setVisibility(View.GONE);
            error.setVisibility(View.VISIBLE);
        }
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
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
}
