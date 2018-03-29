package elmeniawy.eslam.ytsag.helpers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import elmeniawy.eslam.ytsag.R;
import elmeniawy.eslam.ytsag.screens.main.MainActivity;

/**
 * NotificationsHelper
 * <p>
 * Created by Eslam El-Meniawy on 29-Mar-2018.
 * CITC - Mansoura University
 */

public class NotificationsHelper {
    public static void showNotification(Context context, int id, String title, String message) {
        //
        // Create notifications builder.
        //

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, id,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat
                .Builder(context, context.getResources().getString(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
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
                    context.getResources().getString(R.string.app_name),
                    context.getResources().getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT);

            //
            // Register the channel with the system.
            //

            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        //
        // Show notification.
        //

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(id, mBuilder.build());
    }
}
