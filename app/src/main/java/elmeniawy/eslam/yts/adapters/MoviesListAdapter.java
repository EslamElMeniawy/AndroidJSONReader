/*
 * Copyright (c) 2015.
 *
 * Dear maintainer:
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * So, good luck maintaining the code :D
 *
 * Eslam El-Meniawy
 * 06-Jun-2015 18:05
 */

package elmeniawy.eslam.yts.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;

import com.andexert.library.RippleView;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;

import elmeniawy.eslam.yts.R;
import elmeniawy.eslam.yts.activities.DetailsActivity;
import elmeniawy.eslam.yts.network.VolleySingleton;
import elmeniawy.eslam.yts.objects.Movie;

public class MoviesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private static ArrayList<Movie> listMovies = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    private static Context context;

    public MoviesListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        volleySingleton = VolleySingleton.getInstance();
        imageLoader = volleySingleton.getImageLoader();
        this.context = context;
    }

    public void setMoviesList(ArrayList<Movie> listMovies) {
        this.listMovies = listMovies;
        notifyItemRangeChanged(0, listMovies.size());
    }

    @Override
    public int getItemViewType(int position) {
        return listMovies.get(position)!=null? VIEW_ITEM: VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if(viewType==VIEW_ITEM) {
            View view = layoutInflater.inflate(R.layout.list_movies_row, parent, false);
            viewHolder = new ViewHolderMoviesList(view);
        } else {
            View view = layoutInflater.inflate(R.layout.list_movies_row_progress, parent, false);
            viewHolder = new ProgressViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder hold, int position) {
        if(hold instanceof ViewHolderMoviesList) {
            Movie currentMovie = listMovies.get(position);
            final ViewHolderMoviesList holder = (ViewHolderMoviesList) hold;
            holder.movieTitle.setText(currentMovie.getTitle());
            holder.movieYear.setText(currentMovie.getYear());
            holder.movieGenre.setText(currentMovie.getGenres());
            for (int i = 0; i < currentMovie.getTorrentsQuality().length; i++) {
                if (i == 0) {
                    holder.movieQuality1.setText(currentMovie.getTorrentsQuality()[0]);
                }
                if (i == 1) {
                    holder.movieQuality2.setText(currentMovie.getTorrentsQuality()[1]);
                }
                if (i == 2) {
                    holder.movieQuality3.setText(currentMovie.getTorrentsQuality()[2]);
                }
            }
            if ((int) currentMovie.getRating() == -1) {
                holder.movieRating.setRating(0.0F);
                AlphaAnimation alpha = new AlphaAnimation(0.5F, 0.5F);
                alpha.setDuration(0);
                alpha.setFillAfter(true);
                holder.movieRating.startAnimation(alpha);
            } else {
                holder.movieRating.setRating((float) (currentMovie.getRating() / 2));
            }
            String movieSmallImageUrl = currentMovie.getSmallCoverImage();
            if (movieSmallImageUrl != null && !movieSmallImageUrl.equals("")) {
                imageLoader.get(movieSmallImageUrl, new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        holder.movieSmallImage.setImageBitmap(response.getBitmap());
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
            }
        } else {
            ((ProgressViewHolder)hold).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return listMovies.size();
    }

    static class ViewHolderMoviesList extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView movieSmallImage;
        private CheckedTextView movieTitle;
        private CheckedTextView movieYear;
        private RatingBar movieRating;
        private CheckedTextView movieGenre;
        private CheckedTextView movieQuality1;
        private CheckedTextView movieQuality2;
        private CheckedTextView movieQuality3;
        private InterstitialAd mInterstitialAd;

        public ViewHolderMoviesList(View itemView) {
            super(itemView);
            movieSmallImage = (ImageView) itemView.findViewById(R.id.movie_small_image);
            movieTitle = (CheckedTextView) itemView.findViewById(R.id.movie_tittle);
            movieYear = (CheckedTextView) itemView.findViewById(R.id.movie_year);
            movieRating = (RatingBar) itemView.findViewById(R.id.movie_rating);
            LayerDrawable stars = (LayerDrawable) movieRating.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(context.getResources().getColor(R.color.accentColor), PorterDuff.Mode.SRC_ATOP);
            movieGenre = (CheckedTextView) itemView.findViewById(R.id.movie_genres);
            movieQuality1 = (CheckedTextView) itemView.findViewById(R.id.movie_quality_1);
            movieQuality2 = (CheckedTextView) itemView.findViewById(R.id.movie_quality_2);
            movieQuality3 = (CheckedTextView) itemView.findViewById(R.id.movie_quality_3);
            itemView.setOnClickListener(this);
            mInterstitialAd = new InterstitialAd(context);
            mInterstitialAd.setAdUnitId(context.getString(R.string.interstitial_ad_unit_id));
            requestNewInterstitial();

        }

        @Override
        public void onClick(View view) {
            RippleView rippleView = (RippleView) view;
            rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                @Override
                public void onComplete(RippleView rippleView) {
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            Intent intent = new Intent(context, DetailsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("movie", listMovies.get(getLayoutPosition()));
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                            listMovies.clear();
                        }
                    });
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Intent intent = new Intent(context, DetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("movie", listMovies.get(getLayoutPosition()));
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                        listMovies.clear();
                    }
                }
            });
        }
        private void requestNewInterstitial() {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
        }
    }

    static class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar progressBar;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar)itemView.findViewById(R.id.progressBarDown);
        }
    }
}
