package elmeniawy.eslam.ytsag.screens.splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import elmeniawy.eslam.ytsag.R;
import elmeniawy.eslam.ytsag.screens.main.MainActivity;

public class SplashActivity extends AppCompatActivity {
    public static final String PREF_FILE_NAME = "YTSPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            SharedPreferences sharedPreferences = SplashActivity.this.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("fromNotification", extras.getBoolean("fromNotification"));
            editor.apply();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent openMainActivity = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(openMainActivity);
                SplashActivity.this.finish();
            }
        }, 3000);
    }
}
