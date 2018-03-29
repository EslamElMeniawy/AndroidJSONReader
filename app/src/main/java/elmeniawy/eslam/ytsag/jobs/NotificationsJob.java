package elmeniawy.eslam.ytsag.jobs;

import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

import javax.inject.Inject;

import elmeniawy.eslam.ytsag.R;
import elmeniawy.eslam.ytsag.api.MoviesApiService;
import elmeniawy.eslam.ytsag.api.model.MovieResponse;
import elmeniawy.eslam.ytsag.helpers.NotificationsHelper;
import elmeniawy.eslam.ytsag.root.MyApplication;
import elmeniawy.eslam.ytsag.storage.database.ApplicationDatabase;
import elmeniawy.eslam.ytsag.utils.SchedulingUtils;
import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * NotificationsJob
 * <p>
 * Created by Eslam El-Meniawy on 20-Mar-2018.
 * CITC - Mansoura University
 */

public class NotificationsJob extends Job {
    public static final String TAG = "notification_job_tag";

    private Disposable onlineDisposable = null,
            databaseDisposable = null;

    @Inject
    MoviesApiService moviesApiService;

    @Inject
    ApplicationDatabase database;

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        Timber.i("onRunJob");

        //
        // Inject job.
        //

        ((MyApplication) getContext().getApplicationContext()).getComponent().inject(this);

        //
        // Get latest movie.
        //

        onlineDisposable = moviesApiService
                .getLatestMovie(System.currentTimeMillis())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(this::checkIfMovieNew);

        return Result.SUCCESS;
    }

    @Override
    protected void onCancel() {
        super.onCancel();

        if (onlineDisposable != null && onlineDisposable.isDisposed()) {
            onlineDisposable.dispose();
        }

        if (databaseDisposable != null && databaseDisposable.isDisposed()) {
            databaseDisposable.dispose();
        }
    }

    public static void scheduleJob() {
        Timber.i("scheduleJob");

        new JobRequest.Builder(NotificationsJob.TAG)
                .setPeriodic(SchedulingUtils.SCHEDULE_INTERVAL, SchedulingUtils.SCHEDULE_FLEX)
                .setUpdateCurrent(true)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .build()
                .schedule();
    }

    private void checkIfMovieNew(MovieResponse movieResponse) {
        if (movieResponse.getStatus().equals("ok")) {
            if (movieResponse.getData() != null
                    && movieResponse.getData().getMovies() != null
                    && movieResponse.getData().getMovies().size() > 0) {
                long movieId = movieResponse.getData().getMovies().get(0).getId();
                Timber.i("Online movie id: %d.", movieId);

                databaseDisposable = Completable
                        .fromAction(() -> {
                            long databaseId = database.movieDao().getLatestId();
                            Timber.i("Database movie id: %d.", databaseId);

                            if (movieId > databaseId) {
                                NotificationsHelper.showNotification(getContext(), 1,
                                        getContext()
                                                .getResources()
                                                .getString(R.string.notification_alert_title),
                                        getContext()
                                                .getResources()
                                                .getString(R.string.notification_alert_msg));
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe();
            }
        }
    }
}
