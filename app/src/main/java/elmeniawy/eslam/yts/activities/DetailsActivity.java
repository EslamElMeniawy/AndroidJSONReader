/*
 * Copyright (c) 2015.
 *
 * Dear maintainer:
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * So, good luck maintaining the code :D
 *
 * Eslam El-Meniawy
 * 05-Jun-2015 00:22
 */

package elmeniawy.eslam.yts.activities;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONException;
import org.json.JSONObject;

import elmeniawy.eslam.yts.R;
import elmeniawy.eslam.yts.network.VolleySingleton;
import elmeniawy.eslam.yts.objects.Movie;

import static elmeniawy.eslam.yts.extras.Keys.EndpointsMoviesList.*;


public class DetailsActivity extends AppCompatActivity {

    private Movie movie;
    private CheckedTextView title, year, genres, rate, qualityText1, qualityText2, qualityText3, qualitySize1, qualitySize2, qualitySize3, synopsis, error;
    private RippleView imdbLink, quality1, quality2, quality3;
    private LinearLayout qualityLayout1, qualityLayout2, qualityLayout3;
    private ScrollView scrollView;
    private ImageView image;
    private RelativeLayout background;
    private AdView mAdView;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = (CheckedTextView) findViewById(R.id.movie_tittle);
        year = (CheckedTextView) findViewById(R.id.movie_year);
        genres = (CheckedTextView) findViewById(R.id.movie_genres);
        rate = (CheckedTextView) findViewById(R.id.movie_imdb_rate);
        qualityText1 = (CheckedTextView) findViewById(R.id.movie_quality_1_text);
        qualityText2 = (CheckedTextView) findViewById(R.id.movie_quality_2_text);
        qualityText3 = (CheckedTextView) findViewById(R.id.movie_quality_3_text);
        qualitySize1 = (CheckedTextView) findViewById(R.id.movie_quality_1_size);
        qualitySize2 = (CheckedTextView) findViewById(R.id.movie_quality_2_size);
        qualitySize3 = (CheckedTextView) findViewById(R.id.movie_quality_3_size);
        synopsis = (CheckedTextView) findViewById(R.id.movie_synopsis);
        error = (CheckedTextView) findViewById(R.id.error_view);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        imdbLink = (RippleView) findViewById(R.id.movie_imdb_link);
        quality1 = (RippleView) findViewById(R.id.movie_quality_1);
        quality2 = (RippleView) findViewById(R.id.movie_quality_2);
        quality3 = (RippleView) findViewById(R.id.movie_quality_3);
        image = (ImageView) findViewById(R.id.movie_medium_image);
        qualityLayout1 = (LinearLayout) findViewById(R.id.movie_quality_1_layout);
        qualityLayout2 = (LinearLayout) findViewById(R.id.movie_quality_2_layout);
        qualityLayout3 = (LinearLayout) findViewById(R.id.movie_quality_3_layout);
        background = (RelativeLayout) findViewById(R.id.background);
        Bundle bundle = DetailsActivity.this.getIntent().getExtras();
        if(bundle!=null) {
            movie = bundle.getParcelable("movie");
            volleySingleton = VolleySingleton.getInstance();
            imageLoader = volleySingleton.getImageLoader();
            imageLoader.get(movie.getMediumCoverImage(), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    image.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            if (background!=null) {
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
            }
            requestQueue = volleySingleton.getRequestQueue();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://yts.ag/api/v2/movie_details.json?movie_id=" + movie.getId(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (response != null && response.length() > 0) {
                        try {
                            if (response.has(KEY_STATUS) && response.getString(KEY_STATUS).equals("ok")) {
                                if (response.has(KEY_DATA) && !response.isNull(KEY_DATA)) {
                                    JSONObject details = response.getJSONObject(KEY_DATA);
                                    if (details.has(KEY_SYNOPSIS) && !details.isNull(KEY_SYNOPSIS)) {
                                        synopsis.setText(details.getString(KEY_SYNOPSIS));
                                    } else {
                                        synopsis.setText(R.string.get_movie_synopsis_error);
                                    }
                                } else {
                                    synopsis.setText(R.string.get_movie_synopsis_error);
                                }
                            } else {
                                synopsis.setText(R.string.get_movie_synopsis_error);
                            }
                        } catch (JSONException e) {
                            synopsis.setText(R.string.get_movie_synopsis_error);
                        }
                    } else {
                        synopsis.setText(R.string.get_movie_synopsis_error);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    synopsis.setText(R.string.get_movie_synopsis_error);
                }
            });
            requestQueue.add(request);
            title.setText(movie.getTitle());
            year.setText(movie.getYear());
            genres.setText(movie.getGenres());
            imdbLink.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                @Override
                public void onComplete(RippleView rippleView) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.imdb.com/title/" + movie.getImdbCode()));
                    startActivity(browserIntent);
                }
            });
            rate.setText(String.valueOf(movie.getRating()));
            for (int i = 0; i < movie.getTorrentsQuality().length && i < movie.getTorrentsUrl().length && i < movie.getTorrentsSize().length; i++) {
                if (i == 0) {
                    qualityText1.setText(movie.getTorrentsQuality()[0]);
                    qualitySize1.setText(movie.getTorrentsSize()[0]);
                    quality1.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                        @Override
                        public void onComplete(RippleView rippleView) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getTorrentsUrl()[0]));
                            startActivity(browserIntent);
                        }
                    });
                    qualityLayout1.setVisibility(View.VISIBLE);
                }
                if (i == 1) {
                    qualityText2.setText(movie.getTorrentsQuality()[1]);
                    qualitySize2.setText(movie.getTorrentsSize()[1]);
                    quality2.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                        @Override
                        public void onComplete(RippleView rippleView) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getTorrentsUrl()[1]));
                            startActivity(browserIntent);
                        }
                    });
                    qualityLayout2.setVisibility(View.VISIBLE);
                }
                if (i == 2) {
                    qualityText3.setText(movie.getTorrentsQuality()[2]);
                    qualitySize3.setText(movie.getTorrentsSize()[2]);
                    quality3.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                        @Override
                        public void onComplete(RippleView rippleView) {
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

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
        DetailsActivity.this.finish();
    }
}
