package elmeniawy.eslam.ytsag.root;

import android.app.Activity;
import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.evernote.android.job.JobManager;
import com.google.firebase.FirebaseApp;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import elmeniawy.eslam.ytsag.BuildConfig;
import elmeniawy.eslam.ytsag.jobs.YtsJobCreator;
import elmeniawy.eslam.ytsag.jobs.notifications.NotificationsModule;
import elmeniawy.eslam.ytsag.jobs.update.UpdateModule;
import elmeniawy.eslam.ytsag.root.timber.CrashReportingTree;
import elmeniawy.eslam.ytsag.screens.details.DetailsModule;
import elmeniawy.eslam.ytsag.screens.main.MainModule;
import elmeniawy.eslam.ytsag.screens.search.SearchModule;
import elmeniawy.eslam.ytsag.screens.splash.SplashModule;
import elmeniawy.eslam.ytsag.storage.database.ApplicationDatabaseModule;
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
                .applicationDatabaseModule(new ApplicationDatabaseModule())
                .splashModule(new SplashModule())
                .mainModule(new MainModule())
                .searchModule(new SearchModule())
                .detailsModule(new DetailsModule())
                .notificationsModule(new NotificationsModule())
                .updateModule(new UpdateModule())
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

        //
        // Initialize firebase
        //

        FirebaseApp.initializeApp(this);

        //
        // Initialize job manager.
        //

        JobManager.create(this).addJobCreator(new YtsJobCreator());
    }

    public ApplicationComponent getComponent() {
        return component;
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
