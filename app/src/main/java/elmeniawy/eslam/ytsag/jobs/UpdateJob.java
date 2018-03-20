package elmeniawy.eslam.ytsag.jobs;

import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

import elmeniawy.eslam.ytsag.utils.SchedulingUtils;
import timber.log.Timber;

/**
 * UpdateJob
 * <p>
 * Created by Eslam El-Meniawy on 20-Mar-2018.
 * CITC - Mansoura University
 */

public class UpdateJob extends Job {
    public static final String TAG = "update_job_tag";

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        Timber.i("onRunJob");
        return Result.SUCCESS;
    }

    public static void scheduleJob() {
        Timber.i("scheduleJob");

        new JobRequest.Builder(UpdateJob.TAG)
                .setPeriodic(SchedulingUtils.SCHEDULE_INTERVAL, SchedulingUtils.SCHEDULE_FLEX)
                .setUpdateCurrent(true)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .build()
                .schedule();
    }
}
