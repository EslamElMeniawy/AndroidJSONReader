package elmeniawy.eslam.ytsag.screens.search;

import elmeniawy.eslam.ytsag.api.model.Movie;
import io.reactivex.Observable;

/**
 * Repository
 * <p>
 * Created by Eslam El-Meniawy on 18-Mar-2018.
 * CITC - Mansoura University
 */

public interface Repository {
    Observable<Movie> getMovies(String searchQuery, int firstPage);

    long getMoviesCount();
}
