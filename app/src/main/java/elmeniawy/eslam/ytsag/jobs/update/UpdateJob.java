package elmeniawy.eslam.ytsag.jobs.update;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

import javax.inject.Inject;

import elmeniawy.eslam.ytsag.R;
import elmeniawy.eslam.ytsag.api.UpdateApiService;
import elmeniawy.eslam.ytsag.api.model.UpdateResponse;
import elmeniawy.eslam.ytsag.helpers.NotificationsHelper;
import elmeniawy.eslam.ytsag.root.MyApplication;
import elmeniawy.eslam.ytsag.storage.preferences.MySharedPreferences;
import elmeniawy.eslam.ytsag.utils.PreferencesUtils;
import elmeniawy.eslam.ytsag.utils.SchedulingUtils;
import timber.log.Timber;

/**
 * UpdateJob
 * <p>
 * Created by Eslam El-Meniawy on 20-Mar-2018.
 * CITC - Mansoura University
 */

public class UpdateJob extends Job implements UpdateMVP.View {
    public static final String TAG = "update_job_tag";

    @Inject
    UpdateApiService updateApiService;

    @Inject
    MySharedPreferences sharedPreferences;

    @Inject
    UpdateMVP.Presenter presenter;

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

        new JobRequest.Builder(UpdateJob.TAG)
                .setPeriodic(SchedulingUtils.SCHEDULE_INTERVAL, SchedulingUtils.SCHEDULE_FLEX)
                .setUpdateCurrent(true)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .build()
                .schedule();
    }

    @Override
    public MySharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    @Override
    public int getVersionCode() {
        try {
            PackageInfo pInfo = getContext()
                    .getPackageManager()
                    .getPackageInfo(getContext().getPackageName(), 0);

            return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e);
        }


        return 0;
    }

    @Override
    public String getTitle() {
        return getContext().getResources().getString(R.string.update_notification_alert_title);
    }

    @Override
    public String getMessage() {
        return getContext().getResources().getString(R.string.update_notification_alert_msg);
    }

    @Override
    public void showNotification(int id, String title, String message) {
        NotificationsHelper.showNotification(getContext(), id, title, message);
    }
}
