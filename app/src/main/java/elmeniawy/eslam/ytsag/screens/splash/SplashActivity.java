package elmeniawy.eslam.ytsag.screens.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import javax.inject.Inject;

import elmeniawy.eslam.ytsag.R;
import elmeniawy.eslam.ytsag.root.MyApplication;
import elmeniawy.eslam.ytsag.screens.main.MainActivity;
import elmeniawy.eslam.ytsag.storage.preferences.MySharedPreferences;
import timber.log.Timber;

public class SplashActivity extends AppCompatActivity {
    @Inject
    MySharedPreferences mySharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //
        // Set full screen.
        //

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //
        // Inject activity.
        //

        ((MyApplication) getApplication()).getComponent().inject(this);

        //
        // Set timber tag.
        //

        Timber.tag(SplashActivity.class.getSimpleName());

        //
        // Check if extras available.
        // Then save "fromNotification" to preferences.
        //

        Bundle extras = getIntent().getExtras();
        Timber.i("extras: %s.", extras);

        if (extras != null) {
            Timber.i("Extras available.");

            Timber.i("fromNotification: %s.",
                    String.valueOf(extras.getBoolean("fromNotification")));

            mySharedPreferences.putData("fromNotification",
                    extras.getBoolean("fromNotification"));
        }

        //
        // Wait for 3 seconds and move to main activity.
        //

        new Handler().postDelayed(() -> {
            Intent openMainActivity = new Intent(SplashActivity.this,
                    MainActivity.class);

            startActivity(openMainActivity);
            SplashActivity.this.finish();
        }, 3000);
    }
}
