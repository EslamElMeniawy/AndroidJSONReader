package elmeniawy.eslam.ytsag.screens.main;

import dagger.Module;
import dagger.Provides;
import elmeniawy.eslam.ytsag.api.MoviesApiService;
import elmeniawy.eslam.ytsag.api.UpdateApiService;

/**
 * MainModule
 * <p>
 * Created by Eslam El-Meniawy on 11-Mar-2018.
 * CITC - Mansoura University
 */

@Module
public class MainModule {
    @Provides
    MainMVP.Presenter providedMainPresenter(MainMVP.Model model) {
        return new MainPresenter(model);
    }

    @Provides
    MainMVP.Model provideMainModel(Repository repository) {
        return new MainModel(repository);
    }

    @Provides
    Repository provideMainRepository(MoviesApiService moviesApiService,
                                     UpdateApiService updateApiService) {
        return new MainRepository(moviesApiService, updateApiService);
    }
}
