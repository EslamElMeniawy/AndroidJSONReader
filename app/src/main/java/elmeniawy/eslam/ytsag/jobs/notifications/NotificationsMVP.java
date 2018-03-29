package elmeniawy.eslam.ytsag.jobs.notifications;

import elmeniawy.eslam.ytsag.api.model.MovieResponse;
import elmeniawy.eslam.ytsag.storage.database.ApplicationDatabase;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * NotificationsMVP
 * <p>
 * Created by Eslam El-Meniawy on 29-Mar-2018.
 * CITC - Mansoura University
 */

public interface NotificationsMVP {
    interface View {
        ApplicationDatabase getDatabase();

        String getTitle();

        String getMessage();

        void showNotification(int id, String title, String message);
    }

    interface Presenter {
        void setView(NotificationsMVP.View view);

        void runJob();

        void rxUnsubscribe();
    }

    interface Model {
        Observable<MovieResponse> getLatestMovie(long currentTime);

        Single<Long> getLatestId(ApplicationDatabase database);
    }
}
