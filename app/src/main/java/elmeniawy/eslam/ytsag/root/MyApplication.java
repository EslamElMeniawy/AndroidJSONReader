package elmeniawy.eslam.ytsag.root;

import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

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

        component = DaggerApplicationComponent.builder()
                //.loginModule(new LoginModule())
                //.topMoviesModule(new TopMoviesModule())
                .build();

        component.inject(this);
    }

    public ApplicationComponent getComponent() {
        return component;
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
