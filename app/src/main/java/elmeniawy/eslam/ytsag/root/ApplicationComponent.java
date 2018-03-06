package elmeniawy.eslam.ytsag.root;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import elmeniawy.eslam.ytsag.DetailsActivity;
import elmeniawy.eslam.ytsag.MainActivity;
import elmeniawy.eslam.ytsag.SearchActivity;
import elmeniawy.eslam.ytsag.SplashActivity;

/**
 * ApplicationComponent
 * <p>
 * Created by Eslam El-Meniawy on 06-Mar-2018.
 * CITC - Mansoura University
 */

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class, ApplicationModule.class})
public interface ApplicationComponent extends AndroidInjector<MyApplication> {
    void inject(SplashActivity target);
    void inject(MainActivity target);
    void inject(DetailsActivity target);
    void inject(SearchActivity target);
}
