package elmeniawy.eslam.ytsag;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Eslam El-Meniawy on 23-Jan-16.
 */
public class UpdateService extends IntentService {
    public static final int NOTIFICATION_ID = 2;

    public UpdateService() {
        super("YTSUpdateService");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        VolleySingleton volleySingleton = VolleySingleton.getInstance();
        RequestQueue requestQueue = volleySingleton.getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://raw.githubusercontent.com/EslamEl-Meniawy/AndroidJSONReader/master/AppData.json", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null && response.length() > 0) {
                    try {
                        if (response.has("success") && response.getBoolean("success") && response.has("version") && !response.isNull("version") && response.has("url") && !response.isNull("url")) {
                            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                            int verCode = pInfo.versionCode;
                            if (response.getInt("version") > verCode) {
                                SharedPreferences sharedPreferences = MyApplication.getAppContext().getSharedPreferences("YTSPref", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("updateAvailable", true);
                                sendNotification();
                                AlarmReceiver.completeWakefulIntent(intent);
                            }
                        }
                    } catch (JSONException e) {
                    } catch (PackageManager.NameNotFoundException e) {
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(request);
    }

    private void sendNotification() {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, SplashActivity.class), 0);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.update_notification_alert_title))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.update_notification_alert_msg)))
                .setContentText(getString(R.string.update_notification_alert_msg))
                .setAutoCancel(true)
                .setSound(alarmSound);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}