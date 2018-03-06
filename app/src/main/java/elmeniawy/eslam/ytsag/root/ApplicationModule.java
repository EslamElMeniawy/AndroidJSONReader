package elmeniawy.eslam.ytsag.root;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * ApplicationModule
 * <p>
 * Created by Eslam El-Meniawy on 06-Mar-2018.
 * CITC - Mansoura University
 */

@Module
public class ApplicationModule {
    @Provides
    @Singleton
    public Context provideContext(MyApplication application) {
        return application.getApplicationContext();
    }
}
