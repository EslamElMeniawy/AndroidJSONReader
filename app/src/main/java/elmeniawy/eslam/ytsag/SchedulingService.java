package elmeniawy.eslam.ytsag;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Eslam El-Meniawy on 02-Jan-16.
 */
@SuppressWarnings("DefaultFileTemplate")
public class SchedulingService extends IntentService {
    public static final int NOTIFICATION_ID = 1;

    public SchedulingService() {
        super("YTSSchedulingService");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        VolleySingleton volleySingleton = VolleySingleton.getInstance();
        RequestQueue requestQueue = volleySingleton.getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://yts.ag/api/v2/list_movies.json?limit=1&page=1", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null && response.length() > 0) {
                    try {
                        if (response.has("status") && response.getString("status").equals("ok")) {
                            if (response.has("data") && !response.isNull("data")) {
                                JSONObject details = response.getJSONObject("data");
                                if (details.has("movies") && !details.isNull("movies")) {
                                    JSONObject movie = details.getJSONArray("movies").getJSONObject(0);
                                    if (movie.has("id") && !movie.isNull("id")) {
                                        SharedPreferences sharedPreferences = MyApplication.getAppContext().getSharedPreferences("YTSPref", Context.MODE_PRIVATE);
                                        long id = sharedPreferences.getLong("firstId", -1);
                                        if (id != -1 && id != movie.getLong("id")) {
                                            sendNotification();
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putLong("firstId", movie.getLong("id"));
                                            editor.apply();
                                            AlarmReceiver.completeWakefulIntent(intent);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (JSONException ignored) {
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        requestQueue.add(request);
    }

    private void sendNotification() {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, SplashActivity.class), 0);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.notification_alert_title))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.notification_alert_msg)))
                .setContentText(getString(R.string.notification_alert_msg))
                .setAutoCancel(true)
                .setSound(alarmSound);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

        VolleySingleton.getInstance().getRequestQueue().getCache().clear();
    }
}
