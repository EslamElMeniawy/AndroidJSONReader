package elmeniawy.eslam.ytsag;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;

/**
 * Created by Eslam El-Meniawy on 02-Jan-16.
 */
public class MoviesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static ArrayList<Movie> listMovies = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private ImageLoader imageLoader;
    private static Context context;

    public MoviesListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        VolleySingleton volleySingleton = VolleySingleton.getInstance();
        imageLoader = volleySingleton.getImageLoader();
        this.context = context;
    }

    public void setMoviesList(ArrayList<Movie> listMovies) {
        this.listMovies = listMovies;
        notifyItemRangeChanged(0, listMovies.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = layoutInflater.inflate(R.layout.movie_item, parent, false);
        viewHolder = new ViewHolderMoviesList(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder hold, int position) {
        Movie currentMovie = listMovies.get(position);
        final ViewHolderMoviesList holder = (ViewHolderMoviesList) hold;
        holder.movieTitle.setText(currentMovie.getTitle());
        holder.movieYear.setText(currentMovie.getYear());
        for (int i = 0; i < currentMovie.getTorrentsQuality().length; i++) {
            if (i == 0) {
                holder.movieQuality1.setVisibility(View.VISIBLE);
                holder.movieQuality1.setText(currentMovie.getTorrentsQuality()[0]);
            }
            if (i == 1) {
                holder.movieQuality2.setVisibility(View.VISIBLE);
                holder.movieQuality2.setText(currentMovie.getTorrentsQuality()[1]);
            }
            if (i == 2) {
                holder.movieQuality3.setVisibility(View.VISIBLE);
                holder.movieQuality3.setText(currentMovie.getTorrentsQuality()[2]);
            }
        }
        /*if ((int) currentMovie.getRating() == -1) {
            holder.movieRating.setRating(0.0F);
            AlphaAnimation alpha = new AlphaAnimation(0.5F, 0.5F);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            holder.movieRating.startAnimation(alpha);
        } else {
            //holder.movieRating.setRating((float) currentMovie.getRating());
            holder.movieRating.setRating(currentMovie.getRating());
        }*/
        holder.movieRating.setText(String.valueOf(currentMovie.getRating()) + "/10");
        String movieImageUrl = currentMovie.getMediumCoverImage();
        if (movieImageUrl != null && !movieImageUrl.equals("")) {
            imageLoader.get(movieImageUrl, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    holder.movieImage.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listMovies.size();
    }

    static class ViewHolderMoviesList extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView movieImage;
        private TextView movieTitle, movieYear, movieRating, movieQuality1, movieQuality2, movieQuality3;
        //private RatingBar movieRating;
        private InterstitialAd mInterstitialAd;

        public ViewHolderMoviesList(View itemView) {
            super(itemView);
            movieImage = (ImageView) itemView.findViewById(R.id.movie_image);
            movieTitle = (TextView) itemView.findViewById(R.id.movie_tittle);
            movieYear = (TextView) itemView.findViewById(R.id.movie_year);
            movieRating = (TextView) itemView.findViewById(R.id.movie_rating);
            /*LayerDrawable stars = (LayerDrawable) movieRating.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            DrawableCompat.setTint(stars, ContextCompat.getColor(context, R.color.colorAccent));*/
            movieQuality1 = (TextView) itemView.findViewById(R.id.movie_quality_1);
            movieQuality2 = (TextView) itemView.findViewById(R.id.movie_quality_2);
            movieQuality3 = (TextView) itemView.findViewById(R.id.movie_quality_3);
            itemView.setOnClickListener(this);
            mInterstitialAd = new InterstitialAd(context);
            mInterstitialAd.setAdUnitId(context.getString(R.string.interstitial_ad_unit_id));
            requestNewInterstitial();
        }

        @Override
        public void onClick(View view) {
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    Intent intent = new Intent(context, DetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("movie", listMovies.get(getLayoutPosition()));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
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
            }
        }

        private void requestNewInterstitial() {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
        }
    }
}
