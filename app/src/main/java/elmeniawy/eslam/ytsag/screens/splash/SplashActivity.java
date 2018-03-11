package elmeniawy.eslam.ytsag.screens.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import javax.inject.Inject;

import elmeniawy.eslam.ytsag.R;
import elmeniawy.eslam.ytsag.root.MyApplication;
import elmeniawy.eslam.ytsag.screens.main.MainActivity;
import elmeniawy.eslam.ytsag.storage.preferences.MySharedPreferences;
import elmeniawy.eslam.ytsag.utils.PreferencesUtils;
import timber.log.Timber;

public class SplashActivity extends AppCompatActivity implements SplashMVP.View {
    @Inject
    MySharedPreferences mySharedPreferences;

    @Inject
    SplashMVP.Presenter presenter;

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
    }

    @Override
    protected void onResume() {
        super.onResume();

        //
        // Set view, check from notification & start wait;
        //

        presenter.setView(this);
        presenter.saveFromNotification();
        presenter.startWait();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.rxUnsubscribe();
    }

    @Override
    public MySharedPreferences getSharedPreferences() {
        return mySharedPreferences;
    }

    @Override
    public Boolean getFromNotification() {
        Bundle extras = getIntent().getExtras();
        Timber.i("extras: %s.", extras);

        if (extras != null) {
            Timber.i("Extras available.");
            Boolean fromNotification = extras.getBoolean(PreferencesUtils.KEY_FROM_NOTIFICATION);
            Timber.i("fromNotification: %s.", String.valueOf(fromNotification));
            return fromNotification;
        }

        return false;
    }

    @Override
    public void openMainActivity() {
        Intent openMainActivity = new Intent(SplashActivity.this,
                MainActivity.class);

        startActivity(openMainActivity);
        SplashActivity.this.finish();
    }
}
