package elmeniawy.eslam.ytsag.jobs;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

import javax.inject.Inject;

import elmeniawy.eslam.ytsag.R;
import elmeniawy.eslam.ytsag.api.MoviesApiService;
import elmeniawy.eslam.ytsag.api.model.MovieResponse;
import elmeniawy.eslam.ytsag.root.MyApplication;
import elmeniawy.eslam.ytsag.screens.main.MainActivity;
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
                .getLatestMovie()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(this::checkIfMovieNew);

        return Result.SUCCESS;
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
                                showNotification();
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe();
            }
        }
    }

    private void showNotification() {
        //
        // Create notifications builder.
        //

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 1,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat
                .Builder(getContext(), getContext().getResources().getString(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getContext()
                        .getResources().getString(R.string.notification_alert_title))
                .setContentText(getContext()
                        .getResources().getString(R.string.notification_alert_msg))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(getContext()
                                .getResources().getString(R.string.notification_alert_msg)))
                .setSound(defaultSoundUri)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library.
            //

            NotificationChannel channel = new NotificationChannel(
                    getContext().getResources().getString(R.string.app_name),
                    getContext().getResources().getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT);

            //
            // Register the channel with the system.
            //

            NotificationManager notificationManager = (NotificationManager) getContext()
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        //
        // Show notification.
        //

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
        notificationManager.notify(1, mBuilder.build());
    }
}
