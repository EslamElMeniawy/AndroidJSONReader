package elmeniawy.eslam.ytsag.jobs.notifications;

import dagger.Module;
import dagger.Provides;
import elmeniawy.eslam.ytsag.api.MoviesApiService;

/**
 * NotificationsModule
 * <p>
 * Created by Eslam El-Meniawy on 29-Mar-2018.
 * CITC - Mansoura University
 */

@Module
public class NotificationsModule {
    @Provides
    NotificationsMVP.Presenter providedNotificationsPresenter(NotificationsMVP.Model model) {
        return new NotificationsPresenter(model);
    }

    @Provides
    NotificationsMVP.Model provideNotificationsModel(Repository repository) {
        return new NotificationsModel(repository);
    }

    @Provides
    Repository provideNotificationsRepository(MoviesApiService moviesApiService) {
        return new NotificationsRepository(moviesApiService);
    }
}
