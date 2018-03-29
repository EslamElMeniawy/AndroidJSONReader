package elmeniawy.eslam.ytsag.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import elmeniawy.eslam.ytsag.jobs.notifications.NotificationsJob;
import elmeniawy.eslam.ytsag.jobs.update.UpdateJob;
import elmeniawy.eslam.ytsag.root.MyApplication;
import elmeniawy.eslam.ytsag.storage.preferences.MySharedPreferences;
import elmeniawy.eslam.ytsag.utils.PreferencesUtils;

/**
 * Created by Eslam El-Meniawy on 02-Jan-16.
 */
@SuppressWarnings("DefaultFileTemplate")
public class BootReceiver extends BroadcastReceiver {
    @Inject
    MySharedPreferences mySharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        //
        // Inject receiver.
        //

        ((MyApplication) context.getApplicationContext()).getComponent().inject(this);

        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                //
                // Start jobs.
                //

                if (mySharedPreferences.getBoolean(PreferencesUtils.KEY_NOTIFICATION_ENABLED)) {
                    NotificationsJob.scheduleJob();
                }

                if (mySharedPreferences.getBoolean(PreferencesUtils.KEY_UPDATE_ENABLED)) {
                    UpdateJob.scheduleJob();
                }
            }
        }
    }
}
