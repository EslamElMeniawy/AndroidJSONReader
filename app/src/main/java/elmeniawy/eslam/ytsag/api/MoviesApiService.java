package elmeniawy.eslam.ytsag.api;

import elmeniawy.eslam.ytsag.api.model.MovieResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * MoviesApiService
 * <p>
 * Created by Eslam El-Meniawy on 07-Mar-2018.
 * CITC - Mansoura University
 */

public interface MoviesApiService {
    @GET("list_movies.json?limit=20")
    Observable<MovieResponse> getMovies(@Query("page") Integer page);

    @GET("list_movies.json?limit=20")
    Observable<MovieResponse> searchMovies(@Query("query_term") String queryTerm,
                                           @Query("page") Integer page);

    @GET("list_movies.json?limit=1&page=1#{current_time}")
    Observable<MovieResponse> getLatestMovie(@Path("current_time") long currentTime);
}
