package elmeniawy.eslam.ytsag.screens.search;

import elmeniawy.eslam.ytsag.api.model.Movie;
import io.reactivex.Observable;

/**
 * SearchModel
 * <p>
 * Created by Eslam El-Meniawy on 18-Mar-2018.
 * CITC - Mansoura University
 */

public class SearchModel implements SearchMVP.Model {
    private Repository repository;

    SearchModel(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<Movie> getMovies(String searchQuery, int firstPage) {
        return repository.getMovies(searchQuery, firstPage);
    }

    @Override
    public long getMoviesCount() {
        return repository.getMoviesCount();
    }
}
