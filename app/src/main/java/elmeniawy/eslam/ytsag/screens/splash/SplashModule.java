package elmeniawy.eslam.ytsag.screens.splash;

import dagger.Module;
import dagger.Provides;

/**
 * SplashModule
 * <p>
 * Created by Eslam El-Meniawy on 07-Mar-2018.
 * CITC - Mansoura University
 */

@Module
public class SplashModule {
    @Provides
    SplashMVP.Presenter providedSplashPresenter(SplashMVP.Model model) {
        return new SplashPresenter(model);
    }

    @Provides
    SplashMVP.Model provideSplashModel(Repository repository) {
        return new SplashModel(repository);
    }

    @Provides
    Repository provideSplashRepository() {
        return new SplashRepository();
    }
}
