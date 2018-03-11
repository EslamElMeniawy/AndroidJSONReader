package elmeniawy.eslam.ytsag.utils;

/**
 * DatabaseUtils
 * <p>
 * Created by Eslam El-Meniawy on 11-Mar-2018.
 * CITC - Mansoura University
 */

public class DatabaseUtils {
    //
    // General database.
    //

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "yts_movies_db";

    //
    // Movies table.
    //

    public static final String TABLE_MOVIES = "movies";
    public static final String COLUMN_IMDB_CODE = "imdb_code";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_YEAR= "year";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_GENRES= "genres";
    public static final String COLUMN_SYNOPSIS = "synopsis";
    public static final String COLUMN_BACKGROUND_IMAGE= "background_image";
    public static final String COLUMN_COVER_IMAGE = "medium_cover_image";

    //
    // Torrents table.
    //

    public static final String TABLE_TORRENTS= "torrents";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_QUALITY = "quality";
    public static final String COLUMN_SIZE = "size";
    public static final String COLUMN_MOVIE_ID = "movie_id";
}
