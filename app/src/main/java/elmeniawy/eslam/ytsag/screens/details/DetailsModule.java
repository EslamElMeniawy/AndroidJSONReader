package elmeniawy.eslam.ytsag.screens.details;

import dagger.Module;
import dagger.Provides;

/**
 * DetailsModule
 * <p>
 * Created by Eslam El-Meniawy on 20-Mar-2018.
 * CITC - Mansoura University
 */

@Module
public class DetailsModule {
    @Provides
    DetailsMVP.Presenter providedDetailsPresenter() {
        return new DetailsPresenter();
    }
}
