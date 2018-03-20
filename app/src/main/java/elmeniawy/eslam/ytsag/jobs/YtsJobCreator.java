package elmeniawy.eslam.ytsag.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * YtsJobCreator
 * <p>
 * Created by Eslam El-Meniawy on 20-Mar-2018.
 * CITC - Mansoura University
 */

public class YtsJobCreator implements JobCreator {
    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        switch (tag) {
            case NotificationsJob.TAG:
                return new NotificationsJob();
            case UpdateJob.TAG:
                return new UpdateJob();
            default:
                return null;
        }
    }
}
