package elmeniawy.eslam.ytsag.root;

import android.app.Activity;
import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import elmeniawy.eslam.ytsag.BuildConfig;
import elmeniawy.eslam.ytsag.root.timber.CrashReportingTree;
import elmeniawy.eslam.ytsag.screens.splash.SplashModule;
import elmeniawy.eslam.ytsag.storage.preferences.SharedPreferencesModule;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * Created by Eslam El-Meniawy on 02-Jan-16.
 */
@SuppressWarnings("DefaultFileTemplate")
public class MyApplication extends Application implements HasActivityInjector {
    private ApplicationComponent component;

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        //
        // Initialize application component.
        //

        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .sharedPreferencesModule(new SharedPreferencesModule())
                .splashModule(new SplashModule())
                .build();

        //
        // Initialize timber.
        //

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }

        //
        // Initialize fabric if not initialized and in production mode.
        //

        if (!Fabric.isInitialized()) {
            Fabric.with(this, new Crashlytics.Builder()
                    .core(new CrashlyticsCore.Builder()
                            .disabled(BuildConfig.DEBUG)
                            .build())
                    .build());
        }
    }

    public ApplicationComponent getComponent() {
        return component;
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
