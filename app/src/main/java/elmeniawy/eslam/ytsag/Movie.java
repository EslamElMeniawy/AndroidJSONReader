package elmeniawy.eslam.ytsag;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eslam El-Meniawy on 02-Jan-16.
 */
@SuppressWarnings("DefaultFileTemplate")
class Movie implements Parcelable {
    private long id;
    private String imdbCode, title, year, genres, backgroundImage, mediumCoverImage, synopsis;
    private float rating;
    private String[] torrentsUrl, torrentsQuality, torrentsSize;

    Movie() {
    }

    private Movie(Parcel parcel) {
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

    String getImdbCode() {
        return imdbCode;
    }

    public String getTitle() {
        return title;
    }

    String getYear() {
        return year;
    }

    String getGenres() {
        return genres;
    }

    String getBackgroundImage() {
        return backgroundImage;
    }

    String getMediumCoverImage() {
        return mediumCoverImage;
    }

    String getSynopsis() {
        return synopsis;
    }

    float getRating() {
        return rating;
    }

    String[] getTorrentsUrl() {
        return torrentsUrl;
    }

    String[] getTorrentsQuality() {
        return torrentsQuality;
    }

    String[] getTorrentsSize() {
        return torrentsSize;
    }

    public void setId(long id) {
        this.id = id;
    }

    void setImdbCode(String imdbCode) {
        this.imdbCode = imdbCode;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    void setYear(String year) {
        this.year = year;
    }

    void setGenres(String genres) {
        this.genres = genres;
    }

    void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    void setMediumCoverImage(String mediumCoverImage) {
        this.mediumCoverImage = mediumCoverImage;
    }

    void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    void setRating(float rating) {
        this.rating = rating;
    }

    void setTorrentsUrl(String[] torrentsUrl) {
        this.torrentsUrl = torrentsUrl;
    }

    void setTorrentsQuality(String[] torrentsQuality) {
        this.torrentsQuality = torrentsQuality;
    }

    void setTorrentsSize(String[] torrentsSize) {
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
