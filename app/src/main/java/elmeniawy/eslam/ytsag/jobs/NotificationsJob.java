package elmeniawy.eslam.ytsag.jobs;

import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

import elmeniawy.eslam.ytsag.utils.SchedulingUtils;
import timber.log.Timber;

/**
 * NotificationsJob
 * <p>
 * Created by Eslam El-Meniawy on 20-Mar-2018.
 * CITC - Mansoura University
 */

public class NotificationsJob extends Job {
    public static final String TAG = "notification_job_tag";

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        Timber.i("onRunJob");
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
}
