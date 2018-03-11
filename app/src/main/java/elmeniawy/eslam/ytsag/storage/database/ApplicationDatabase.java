package elmeniawy.eslam.ytsag.storage.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import elmeniawy.eslam.ytsag.storage.database.daos.MovieDao;
import elmeniawy.eslam.ytsag.storage.database.daos.TorrentDao;
import elmeniawy.eslam.ytsag.storage.database.entities.MovieEntity;
import elmeniawy.eslam.ytsag.storage.database.entities.TorrentEntity;
import elmeniawy.eslam.ytsag.utils.DatabaseUtils;

/**
 * ApplicationDatabase
 * <p>
 * Created by Eslam El-Meniawy on 11-Mar-2018.
 * CITC - Mansoura University
 */

@Database(entities = {MovieEntity.class, TorrentEntity.class},
        version = DatabaseUtils.DATABASE_VERSION)
public abstract class ApplicationDatabase extends RoomDatabase {
    //
    // Movies DAOs.
    //

    public abstract MovieDao movieDao();

    //
    // Torrents DAOs.
    //

    public abstract TorrentDao torrentDao();
}
