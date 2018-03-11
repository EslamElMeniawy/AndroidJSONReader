package elmeniawy.eslam.ytsag.screens.main;

import dagger.Module;
import dagger.Provides;

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
    Repository provideMainRepository() {
        return new MainRepository();
    }
}
