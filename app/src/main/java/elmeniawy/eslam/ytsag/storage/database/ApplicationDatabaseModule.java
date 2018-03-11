package elmeniawy.eslam.ytsag.storage.database;

import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import elmeniawy.eslam.ytsag.utils.DatabaseUtils;

/**
 * ApplicationDatabaseModule
 * <p>
 * Created by Eslam El-Meniawy on 11-Mar-2018.
 * CITC - Mansoura University
 */

@Module
public class ApplicationDatabaseModule {
    @Provides
    @Singleton
    @Inject
    ApplicationDatabase provideDatabase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(),
                ApplicationDatabase.class, DatabaseUtils.DATABASE_NAME)
                .build();
    }
}
