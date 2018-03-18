package elmeniawy.eslam.ytsag.screens.search;

import dagger.Module;
import dagger.Provides;
import elmeniawy.eslam.ytsag.api.MoviesApiService;

/**
 * SearchModule
 * <p>
 * Created by Eslam El-Meniawy on 18-Mar-2018.
 * CITC - Mansoura University
 */

@Module
public class SearchModule {
    @Provides
    SearchMVP.Presenter providedSearchPresenter(SearchMVP.Model model) {
        return new SearchPresenter(model);
    }

    @Provides
    SearchMVP.Model provideSearchModel(Repository repository) {
        return new SearchModel(repository);
    }

    @Provides
    Repository provideSearchRepository(MoviesApiService moviesApiService) {
        return new SearchRepository(moviesApiService);
    }
}
