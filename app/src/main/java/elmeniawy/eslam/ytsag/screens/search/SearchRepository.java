package elmeniawy.eslam.ytsag.screens.search;

import elmeniawy.eslam.ytsag.api.MoviesApiService;
import elmeniawy.eslam.ytsag.api.model.Movie;
import io.reactivex.Observable;
import timber.log.Timber;

/**
 * SearchRepository
 * <p>
 * Created by Eslam El-Meniawy on 18-Mar-2018.
 * CITC - Mansoura University
 */

public class SearchRepository implements Repository {
    private MoviesApiService moviesApiService;
    private long movieCount = 0;

    SearchRepository(MoviesApiService moviesApiService) {
        this.moviesApiService = moviesApiService;
    }

    @Override
    public Observable<Movie> getMovies(String searchQuery, int firstPage) {
        return moviesApiService
                .searchMovies(searchQuery, firstPage)
                .concatMap(movieResponse -> {
                    Timber.i("Movie count: %d.", movieResponse.getData().getMovieCount());
                    movieCount = movieResponse.getData().getMovieCount();

                    if (movieResponse.getData().getMovies() != null
                            && movieResponse.getData().getMovies().size() > 0) {
                        return Observable.fromIterable(movieResponse.getData().getMovies());
                    } else {
                        return Observable.empty();
                    }
                });
    }

    @Override
    public long getMoviesCount() {
        return movieCount;
    }
}
