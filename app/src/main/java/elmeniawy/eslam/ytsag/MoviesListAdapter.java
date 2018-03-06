package elmeniawy.eslam.ytsag;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import elmeniawy.eslam.ytsag.screens.details.DetailsActivity;

/**
 * Created by Eslam El-Meniawy on 02-Jan-16.
 */
@SuppressWarnings("DefaultFileTemplate")
class MoviesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static ArrayList<Movie> listMovies = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private LayoutInflater layoutInflater;

    MoviesListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        MoviesListAdapter.context = context;
    }

    void setMoviesList(ArrayList<Movie> listMovies) {
        MoviesListAdapter.listMovies = listMovies;
        notifyItemRangeChanged(0, listMovies.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view = layoutInflater.inflate(R.layout.movie_item, parent, false);
        viewHolder = new ViewHolderMoviesList(view);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
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
        holder.movieRating.setText(String.valueOf(currentMovie.getRating()) + "/10");
        String movieImageUrl = currentMovie.getMediumCoverImage();
        Picasso.with(context)
                .load(movieImageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.movieImage);
    }

    @Override
    public int getItemCount() {
        return listMovies.size();
    }

    private static class ViewHolderMoviesList extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView movieImage;
        private TextView movieTitle, movieYear, movieRating, movieQuality1, movieQuality2, movieQuality3;
        private InterstitialAd mInterstitialAd;

        ViewHolderMoviesList(View itemView) {
            super(itemView);
            movieImage = (ImageView) itemView.findViewById(R.id.movie_image);
            movieTitle = (TextView) itemView.findViewById(R.id.movie_tittle);
            movieYear = (TextView) itemView.findViewById(R.id.movie_year);
            movieRating = (TextView) itemView.findViewById(R.id.movie_rating);
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
