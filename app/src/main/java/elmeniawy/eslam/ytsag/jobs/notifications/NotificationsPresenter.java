package elmeniawy.eslam.ytsag.jobs.notifications;

import android.support.annotation.Nullable;

import elmeniawy.eslam.ytsag.api.model.MovieResponse;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * NotificationsPresenter
 * <p>
 * Created by Eslam El-Meniawy on 29-Mar-2018.
 * CITC - Mansoura University
 */

public class NotificationsPresenter implements NotificationsMVP.Presenter {
    private Disposable onlineDisposable = null,
            databaseDisposable = null;

    @Nullable
    private NotificationsMVP.View view;

    private NotificationsMVP.Model model;

    NotificationsPresenter(NotificationsMVP.Model model) {
        this.model = model;
    }

    @Override
    public void setView(@Nullable NotificationsMVP.View view) {
        this.view = view;
    }

    @Override
    public void runJob() {
        onlineDisposable = model
                .getLatestMovie(System.currentTimeMillis())
                .subscribe(this::checkIfMovieNew);
    }

    @Override
    public void rxUnsubscribe() {
        if (onlineDisposable != null && !onlineDisposable.isDisposed()) {
            onlineDisposable.dispose();
        }

        if (databaseDisposable != null && !databaseDisposable.isDisposed()) {
            databaseDisposable.dispose();
        }
    }

    private void checkIfMovieNew(MovieResponse movieResponse) {
        if (view != null) {
            if (movieResponse.getStatus().equals("ok")) {
                if (movieResponse.getData() != null
                        && movieResponse.getData().getMovies() != null
                        && movieResponse.getData().getMovies().size() > 0) {
                    long movieId = movieResponse.getData().getMovies().get(0).getId();
                    Timber.i("Online movie id: %d.", movieId);

                    databaseDisposable = model
                            .getLatestId(view.getDatabase())
                            .subscribe(databaseId -> {
                                Timber.i("Database movie id: %d.", databaseId);

                                if (movieId > databaseId) {
                                    view.showNotification(1,
                                            view.getTitle(),
                                            view.getMessage());
                                }
                            });
                }
            }
        }
    }
}
