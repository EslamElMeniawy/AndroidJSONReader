package elmeniawy.eslam.ytsag;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eslam El-Meniawy on 02-Jan-16.
 */
public class Movie implements Parcelable {
    private long id;
    private String imdbCode, title, year, genres, backgroundImage, mediumCoverImage, synopsis;
    private float rating;
    private String[] torrentsUrl, torrentsQuality, torrentsSize;

    public Movie() {
    }

    public Movie(long id, String imdbCode, String title, String year, String genres, String backgroundImage, String mediumCoverImage, String synopsis, float rating, String[] torrentsUrl, String[] torrentsQuality, String[] torrentsSize) {
        this.id = id;
        this.imdbCode = imdbCode;
        this.title = title;
        this.year = year;
        this.genres = genres;
        this.backgroundImage = backgroundImage;
        this.mediumCoverImage = mediumCoverImage;
        this.synopsis = synopsis;
        this.rating = rating;
        this.torrentsUrl = torrentsUrl;
        this.torrentsQuality = torrentsQuality;
        this.torrentsSize = torrentsSize;
    }

    public Movie(Parcel parcel) {
        id = parcel.readLong();
        imdbCode = parcel.readString();
        title = parcel.readString();
        year = parcel.readString();
        genres = parcel.readString();
        backgroundImage = parcel.readString();
        mediumCoverImage = parcel.readString();
        synopsis = parcel.readString();
        rating = parcel.readFloat();
        torrentsUrl = parcel.createStringArray();
        torrentsQuality = parcel.createStringArray();
        torrentsSize = parcel.createStringArray();
    }

    public long getId() {
        return id;
    }

    public String getImdbCode() {
        return imdbCode;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getGenres() {
        return genres;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public String getMediumCoverImage() {
        return mediumCoverImage;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public float getRating() {
        return rating;
    }

    public String[] getTorrentsUrl() {
        return torrentsUrl;
    }

    public String[] getTorrentsQuality() {
        return torrentsQuality;
    }

    public String[] getTorrentsSize() {
        return torrentsSize;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setImdbCode(String imdbCode) {
        this.imdbCode = imdbCode;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public void setMediumCoverImage(String mediumCoverImage) {
        this.mediumCoverImage = mediumCoverImage;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setTorrentsUrl(String[] torrentsUrl) {
        this.torrentsUrl = torrentsUrl;
    }

    public void setTorrentsQuality(String[] torrentsQuality) {
        this.torrentsQuality = torrentsQuality;
    }

    public void setTorrentsSize(String[] torrentsSize) {
        this.torrentsSize = torrentsSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(imdbCode);
        dest.writeString(title);
        dest.writeString(year);
        dest.writeString(genres);
        dest.writeString(backgroundImage);
        dest.writeString(mediumCoverImage);
        dest.writeString(synopsis);
        dest.writeFloat(rating);
        dest.writeStringArray(torrentsUrl);
        dest.writeStringArray(torrentsQuality);
        dest.writeStringArray(torrentsSize);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
}
