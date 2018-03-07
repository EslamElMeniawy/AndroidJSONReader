package elmeniawy.eslam.ytsag.api;

import elmeniawy.eslam.ytsag.api.model.MovieResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * ApiService
 * <p>
 * Created by Eslam El-Meniawy on 07-Mar-2018.
 * CITC - Mansoura University
 */

public interface ApiService {
    @GET("list_movies.json")
    Observable<MovieResponse> getMovies(@Query("page") Integer page);

    @GET("list_movies.json")
    Observable<MovieResponse> searchMovies(@Query("query_term") String queryTerm,
                                           @Query("page") Integer page);
}
