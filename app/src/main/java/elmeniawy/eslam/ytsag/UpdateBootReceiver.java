package elmeniawy.eslam.ytsag;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by Eslam El-Meniawy on 23-Jan-16.
 */
@SuppressWarnings("DefaultFileTemplate")
public class UpdateBootReceiver extends BroadcastReceiver {
    UpdateReceiver receiver = new UpdateReceiver();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("YTSPref", Context.MODE_PRIVATE);
            if (sharedPreferences.getBoolean("updateEnabled", true)) {
                receiver.setAlarm(context);
            }
        }
    }
}
