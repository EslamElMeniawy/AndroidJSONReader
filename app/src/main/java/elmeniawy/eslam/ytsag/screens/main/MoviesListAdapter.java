package elmeniawy.eslam.ytsag.screens.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import elmeniawy.eslam.ytsag.R;
import elmeniawy.eslam.ytsag.screens.search.SearchActivity;
import timber.log.Timber;

/**
 * Created by Eslam El-Meniawy on 02-Jan-16.
 */
@SuppressWarnings("DefaultFileTemplate")
public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.ViewHolderMoviesList> {
    private List<MovieViewModel> list;
    private Context context;

    public MoviesListAdapter(List<MovieViewModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderMoviesList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);

        return new ViewHolderMoviesList(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMoviesList holder, int position) {
        MovieViewModel currentMovie = list.get(position);
        Timber.i("Load image at: %s.", currentMovie.getMediumCoverImage());

        Picasso
                .get()
                .load(currentMovie.getMediumCoverImage())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.movieImage);

        Timber.i("Title: %s.", currentMovie.getTitle());
        holder.movieTitle.setText(currentMovie.getTitle());
        Timber.i("Year: %d.", currentMovie.getYear());
        holder.movieYear.setText(String.valueOf(currentMovie.getYear()));
        Timber.i("Rating: %f.", currentMovie.getRating());

        holder.movieRating
                .setText(String.format(Locale.getDefault(), "%.1f/10",
                        currentMovie.getRating()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolderMoviesList extends RecyclerView.ViewHolder {
        @BindView(R.id.movie_image)
        AppCompatImageView movieImage;

        @BindView(R.id.movie_tittle)
        AppCompatTextView movieTitle;

        @BindView(R.id.movie_year)
        AppCompatTextView movieYear;

        @BindView(R.id.movie_rating)
        AppCompatTextView movieRating;

        ViewHolderMoviesList(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            movieTitle.setHeight(movieTitle.getLineHeight() * 3);
            movieTitle.setMaxLines(2);
            movieTitle.setEllipsize(TextUtils.TruncateAt.END);
        }

        @OnClick
        void itemClicked() {
            Timber.i("Item clicked at position: %d.", getLayoutPosition());

            if (context instanceof MainActivity) {
                Timber.i("Context is from main activity.");
                ((MainActivity) context).presenter.movieClicked(list.get(getLayoutPosition()));
            } else if (context instanceof SearchActivity) {
                Timber.i("Context is from search activity.");
                ((SearchActivity) context).presenter.movieClicked(list.get(getLayoutPosition()));
            }
        }
    }
}
