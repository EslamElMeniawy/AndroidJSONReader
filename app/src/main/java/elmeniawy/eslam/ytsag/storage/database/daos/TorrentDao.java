package elmeniawy.eslam.ytsag.storage.database.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import elmeniawy.eslam.ytsag.storage.database.entities.TorrentEntity;
import elmeniawy.eslam.ytsag.utils.DatabaseUtils;
import io.reactivex.Maybe;

/**
 * TorrentDao
 * <p>
 * Created by Eslam El-Meniawy on 11-Mar-2018.
 * CITC - Mansoura University
 */

@Dao
public interface TorrentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertData(List<TorrentEntity> torrentsList);

    @Query("SELECT * FROM " + DatabaseUtils.TABLE_TORRENTS + " WHERE "
            + DatabaseUtils.COLUMN_MOVIE_ID + " = :movieId")
    Maybe<List<TorrentEntity>> getData(Long movieId);
}
