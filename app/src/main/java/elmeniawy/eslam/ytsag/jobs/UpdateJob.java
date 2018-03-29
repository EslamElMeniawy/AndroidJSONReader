package elmeniawy.eslam.ytsag.jobs;

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
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * UpdateJob
 * <p>
 * Created by Eslam El-Meniawy on 20-Mar-2018.
 * CITC - Mansoura University
 */

public class UpdateJob extends Job {
    public static final String TAG = "update_job_tag";
    private Disposable disposable = null;

    @Inject
    UpdateApiService updateApiService;

    @Inject
    MySharedPreferences sharedPreferences;

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        Timber.i("onRunJob");

        //
        // Inject job.
        //

        ((MyApplication) getContext().getApplicationContext()).getComponent().inject(this);

        //
        // Check for updates.
        //

        disposable = updateApiService
                .checkUpdate(System.currentTimeMillis())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(this::handleUpdateResult);

        return Result.SUCCESS;
    }

    @Override
    protected void onCancel() {
        super.onCancel();

        if (disposable != null && disposable.isDisposed()) {
            disposable.dispose();
        }
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

    private void handleUpdateResult(UpdateResponse updateResponse) {
        if (updateResponse.getSuccess()) {
            try {
                PackageInfo pInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
                int verCode = pInfo.versionCode;

                if (updateResponse.getVersion() > verCode) {
                    sharedPreferences.putBoolean(PreferencesUtils.KEY_UPDATE_AVAILABLE, true);
                    
                    NotificationsHelper.showNotification(getContext(), 2,
                            getContext()
                                    .getResources()
                                    .getString(R.string.update_notification_alert_title),
                            getContext()
                                    .getResources()
                                    .getString(R.string.update_notification_alert_msg));
                }
            } catch (PackageManager.NameNotFoundException e) {
                Timber.e(e);
            }
        }
    }
}
