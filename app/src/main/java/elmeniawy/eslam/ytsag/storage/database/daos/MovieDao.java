package elmeniawy.eslam.ytsag.storage.database.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import elmeniawy.eslam.ytsag.storage.database.entities.MovieEntity;
import elmeniawy.eslam.ytsag.utils.DatabaseUtils;
import io.reactivex.Maybe;

/**
 * MovieDao
 * <p>
 * Created by Eslam El-Meniawy on 11-Mar-2018.
 * CITC - Mansoura University
 */

@Dao
public interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertData(List<MovieEntity> movieList);

    @Query("SELECT * FROM " + DatabaseUtils.TABLE_MOVIES + " ORDER BY id DESC")
    Maybe<List<MovieEntity>> getData();

    @Delete
    int deleteData(List<MovieEntity> movieList);

    @Query("SELECT max(id) FROM " + DatabaseUtils.TABLE_MOVIES)
    long getLatestId();
}
