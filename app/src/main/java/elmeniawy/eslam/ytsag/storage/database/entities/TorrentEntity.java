package elmeniawy.eslam.ytsag.storage.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.Locale;

import elmeniawy.eslam.ytsag.utils.DatabaseUtils;

/**
 * TorrentEntity
 * <p>
 * Created by Eslam El-Meniawy on 11-Mar-2018.
 * CITC - Mansoura University
 */

@Entity(tableName = DatabaseUtils.TABLE_TORRENTS,
        foreignKeys = @ForeignKey(entity = MovieEntity.class,
                parentColumns = "id",
                childColumns = DatabaseUtils.COLUMN_MOVIE_ID,
                onDelete = ForeignKey.CASCADE))
public class TorrentEntity {
    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo(name = DatabaseUtils.COLUMN_URL)
    private String url;

    @ColumnInfo(name = DatabaseUtils.COLUMN_QUALITY)
    private String quality;

    @ColumnInfo(name = DatabaseUtils.COLUMN_SIZE)
    private String size;

    @ColumnInfo(name = DatabaseUtils.COLUMN_MOVIE_ID)
    private Long movieId;

    //
    // Setters and getters.
    //

    public void setId(Long id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getQuality() {
        return quality;
    }

    public String getSize() {
        return size;
    }

    public Long getMovieId() {
        return movieId;
    }

    //
    // Convert object to string for logging.
    //

    @Override
    public String toString() {
        return String.format(Locale.getDefault(),
                "id: %d, url: %s, quality: %s, size: %s, movieId: %d",
                id, url, quality, size, movieId);
    }
}
