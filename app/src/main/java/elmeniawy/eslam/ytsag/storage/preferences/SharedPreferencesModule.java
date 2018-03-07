package elmeniawy.eslam.ytsag.storage.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import elmeniawy.eslam.ytsag.utils.PreferencesUtils;

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
        return context.getSharedPreferences(PreferencesUtils.PREF_FILE_NAME,
                Context.MODE_PRIVATE);
    }
}
