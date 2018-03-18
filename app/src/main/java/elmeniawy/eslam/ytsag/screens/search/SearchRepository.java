package elmeniawy.eslam.ytsag.screens.search;

import elmeniawy.eslam.ytsag.api.MoviesApiService;
import elmeniawy.eslam.ytsag.api.model.Movie;
import elmeniawy.eslam.ytsag.api.model.MovieResponse;
import io.reactivex.Observable;

/**
 * SearchRepository
 * <p>
 * Created by Eslam El-Meniawy on 18-Mar-2018.
 * CITC - Mansoura University
 */

public class SearchRepository implements Repository {
    private MoviesApiService moviesApiService;

    SearchRepository(MoviesApiService moviesApiService) {
        this.moviesApiService = moviesApiService;
    }

    @Override
    public Observable<Movie> getMovies(String searchQuery, int firstPage) {
        Observable<MovieResponse> movieObservable = moviesApiService
                .searchMovies(searchQuery, firstPage)
                .concatWith(moviesApiService.searchMovies(searchQuery, firstPage + 1));

        return movieObservable
                .concatMap(movieResponse -> Observable
                        .fromIterable(movieResponse.getData().getMovies()));
    }
}
