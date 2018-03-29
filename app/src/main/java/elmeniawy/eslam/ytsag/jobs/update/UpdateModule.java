package elmeniawy.eslam.ytsag.jobs.update;

import dagger.Module;
import dagger.Provides;
import elmeniawy.eslam.ytsag.api.UpdateApiService;

/**
 * UpdateModule
 * <p>
 * Created by Eslam El-Meniawy on 29-Mar-2018.
 * CITC - Mansoura University
 */

@Module
public class UpdateModule {
    @Provides
    UpdateMVP.Presenter providedUpdatePresenter(UpdateMVP.Model model) {
        return new UpdatePresenter(model);
    }

    @Provides
    UpdateMVP.Model provideUpdateModel(Repository repository) {
        return new UpdateModel(repository);
    }

    @Provides
    Repository provideUpdateRepository(UpdateApiService updateApiService) {
        return new UpdateRepository(updateApiService);
    }
}
