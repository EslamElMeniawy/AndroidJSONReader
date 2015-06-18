/*
 * Copyright (c) 2015.
 *
 * Dear maintainer:
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * So, good luck maintaining the code :D
 *
 * Eslam El-Meniawy
 * 12-Jun-2015 15:41
 */

package elmeniawy.eslam.yts.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import elmeniawy.eslam.yts.R;
import elmeniawy.eslam.yts.activities.SplashActivity;
import elmeniawy.eslam.yts.application.MyApplication;
import elmeniawy.eslam.yts.network.VolleySingleton;
import elmeniawy.eslam.yts.receivers.AlarmReceiver;

import static elmeniawy.eslam.yts.extras.Keys.EndpointsMoviesList.*;

public class SchedulingService extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    public SchedulingService() {
        super("SchedulingService");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://yts.to/api/v2/list_movies.json?limit=1&page=1", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null && response.length() > 0) {
                    try {
                        if (response.has(KEY_STATUS) && response.getString(KEY_STATUS).equals("ok")) {
                            if (response.has(KEY_DATA) && !response.isNull(KEY_DATA)) {
                                JSONObject details = response.getJSONObject(KEY_DATA);
                                if (details.has(KEY_MOVIES) && !details.isNull(KEY_MOVIES)) {
                                    JSONObject movie = details.getJSONArray(KEY_MOVIES).getJSONObject(0);
                                    if (movie.has(KEY_ID) && !movie.isNull(KEY_ID)) {
                                        SharedPreferences sharedPreferences = MyApplication.getAppContext().getSharedPreferences("YTSPref", Context.MODE_PRIVATE);
                                        long id = sharedPreferences.getLong("firstId", -1);
                                        if (id != -1 && id < movie.getLong(KEY_ID)) {
                                            sendNotification();
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putLong("firstId", movie.getLong(KEY_ID));
                                            editor.apply();
                                            AlarmReceiver.completeWakefulIntent(intent);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
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
                .setContentTitle(getString(R.string.notification_alert_title))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.notification_alert_msg)))
                .setContentText(getString(R.string.notification_alert_msg))
                .setAutoCancel(true)
                .setSound(alarmSound);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}
