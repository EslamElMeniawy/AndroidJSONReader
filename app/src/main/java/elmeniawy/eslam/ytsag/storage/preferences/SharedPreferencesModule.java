package elmeniawy.eslam.ytsag.storage.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * SharedPreferencesModule
 * <p>
 * Created by Eslam El-Meniawy on 06-Mar-2018.
 * CITC - Mansoura University
 */

@Module
public class SharedPreferencesModule {
    @Provides
    @Singleton
    @Inject
    SharedPreferences provideSharedPreferences(Context context) {
        return context.getSharedPreferences("elmeniawy.eslam.ytsag.YTSPref",
                Context.MODE_PRIVATE);
    }
}
