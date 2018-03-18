package elmeniawy.eslam.ytsag.screens.search;

import elmeniawy.eslam.ytsag.api.model.Movie;
import io.reactivex.Observable;

/**
 * SearchMVP
 * <p>
 * Created by Eslam El-Meniawy on 18-Mar-2018.
 * CITC - Mansoura University
 */

public interface SearchMVP {
    interface View {
    }

    interface Presenter {
        void setView(SearchMVP.View view);
    }

    interface Model {
        Observable<Movie> getMovies(String searchQuery, int firstPage);
    }
}
