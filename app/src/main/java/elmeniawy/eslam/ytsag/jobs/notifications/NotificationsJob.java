package elmeniawy.eslam.ytsag.jobs.notifications;

import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

import javax.inject.Inject;

import elmeniawy.eslam.ytsag.R;
import elmeniawy.eslam.ytsag.api.MoviesApiService;
import elmeniawy.eslam.ytsag.helpers.NotificationsHelper;
import elmeniawy.eslam.ytsag.root.MyApplication;
import elmeniawy.eslam.ytsag.storage.database.ApplicationDatabase;
import elmeniawy.eslam.ytsag.utils.SchedulingUtils;
import timber.log.Timber;

/**
 * NotificationsJob
 * <p>
 * Created by Eslam El-Meniawy on 20-Mar-2018.
 * CITC - Mansoura University
 */

public class NotificationsJob extends Job implements NotificationsMVP.View {
    public static final String TAG = "notification_job_tag";

    @Inject
    MoviesApiService moviesApiService;

    @Inject
    ApplicationDatabase database;

    @Inject
    NotificationsMVP.Presenter presenter;

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        Timber.i("onRunJob");

        //
        // Inject job.
        //

        ((MyApplication) getContext().getApplicationContext()).getComponent().inject(this);

        //
        // Set view & call run job.
        //

        presenter.setView(this);
        presenter.runJob();

        //
        // Return job success.
        //

        return Result.SUCCESS;
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        presenter.rxUnsubscribe();
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
    public ApplicationDatabase getDatabase() {
        return database;
    }

    @Override
    public String getTitle() {
        return getContext().getResources().getString(R.string.notification_alert_title);
    }

    @Override
    public String getMessage() {
        return getContext().getResources().getString(R.string.notification_alert_msg);
    }

    @Override
    public void showNotification(int id, String title, String message) {
        NotificationsHelper.showNotification(getContext(), id, title, message);
    }
}
