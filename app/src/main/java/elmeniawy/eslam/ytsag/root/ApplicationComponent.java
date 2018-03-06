package elmeniawy.eslam.ytsag.root;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import elmeniawy.eslam.ytsag.screens.details.DetailsActivity;
import elmeniawy.eslam.ytsag.screens.main.MainActivity;
import elmeniawy.eslam.ytsag.screens.search.SearchActivity;
import elmeniawy.eslam.ytsag.screens.splash.SplashActivity;
import elmeniawy.eslam.ytsag.storage.preferences.SharedPreferencesModule;

/**
 * ApplicationComponent
 * <p>
 * Created by Eslam El-Meniawy on 06-Mar-2018.
 * CITC - Mansoura University
 */

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class, ApplicationModule.class,
        SharedPreferencesModule.class})
public interface ApplicationComponent extends AndroidInjector<MyApplication> {
    void inject(SplashActivity target);
    void inject(MainActivity target);
    void inject(DetailsActivity target);
    void inject(SearchActivity target);
}
