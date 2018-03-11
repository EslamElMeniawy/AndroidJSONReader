package elmeniawy.eslam.ytsag.storage.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Locale;

import elmeniawy.eslam.ytsag.utils.DatabaseUtils;

/**
 * MovieEntity
 * <p>
 * Created by Eslam El-Meniawy on 11-Mar-2018.
 * CITC - Mansoura University
 */

@Entity(tableName = DatabaseUtils.TABLE_MOVIES)
public class MovieEntity {
    //
    // Attributes.
    //

    @PrimaryKey
    private Long id;

    @ColumnInfo(name = DatabaseUtils.COLUMN_IMDB_CODE)
    private String imdbCode;

    @ColumnInfo(name = DatabaseUtils.COLUMN_TITLE)
    private String title;

    @ColumnInfo(name = DatabaseUtils.COLUMN_YEAR)
    private Long year;

    @ColumnInfo(name = DatabaseUtils.COLUMN_RATING)
    private Double rating;

    @ColumnInfo(name = DatabaseUtils.COLUMN_GENRES)
    private String genres;

    @ColumnInfo(name = DatabaseUtils.COLUMN_SYNOPSIS)
    private String synopsis;

    @ColumnInfo(name = DatabaseUtils.COLUMN_BACKGROUND_IMAGE)
    private String backgroundImage;

    @ColumnInfo(name = DatabaseUtils.COLUMN_COVER_IMAGE)
    private String mediumCoverImage;

    //
    // Setters and getters.
    //

    public void setId(Long id) {
        this.id = id;
    }

    public void setImdbCode(String imdbCode) {
        this.imdbCode = imdbCode;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public void setMediumCoverImage(String mediumCoverImage) {
        this.mediumCoverImage = mediumCoverImage;
    }

    public Long getId() {
        return id;
    }

    public String getImdbCode() {
        return imdbCode;
    }

    public String getTitle() {
        return title;
    }

    public Long getYear() {
        return year;
    }

    public Double getRating() {
        return rating;
    }

    public String getGenres() {
        return genres;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public String getMediumCoverImage() {
        return mediumCoverImage;
    }

    //
    // Convert object to string for logging.
    //

    @Override
    public String toString() {
        return String.format(Locale.getDefault(),
                "id: %d, imdbCode: %s, title: %s, year: %d, rating: %f, genres: %s," +
                        " synopsis: %s, backgroundImage: %s, mediumCoverImage: %s",
                id, imdbCode, title, year, rating, genres, synopsis, backgroundImage,
                mediumCoverImage);
    }
}
