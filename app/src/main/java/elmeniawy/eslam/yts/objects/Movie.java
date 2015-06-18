/*
 * Copyright (c) 2015.
 *
 * Dear maintainer:
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * So, good luck maintaining the code :D
 *
 * Eslam El-Meniawy
 * 06-Jun-2015 11:39
 */

package elmeniawy.eslam.yts.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private long id;
    private String imdbCode;
    private String title;
    private String year;
    private double rating;
    private String genres;
    private String backgroundImage;
    private String smallCoverImage;
    private String mediumCoverImage;
    private String[] torrentsUrl;
    private String[] torrentsQuality;
    private String[] torrentsSize;

    public Movie() {
    }

    public Movie(long id, String imdbCode, String title, String year, double rating, String genres, String backgroundImage, String smallCoverImage, String mediumCoverImage, String[] torrentsUrl, String[] torrentsQuality, String[] torrentsSize) {
        this.id = id;
        this.imdbCode = imdbCode;
        this.title = title;
        this.year = year;
        this.rating = rating;
        this.genres = genres;
        this.backgroundImage = backgroundImage;
        this.smallCoverImage = smallCoverImage;
        this.mediumCoverImage = mediumCoverImage;
        this.torrentsUrl = torrentsUrl;
        this.torrentsQuality = torrentsQuality;
        this.torrentsSize = torrentsSize;
    }

    public Movie(Parcel parcel) {
        id = parcel.readLong();
        imdbCode = parcel.readString();
        title = parcel.readString();
        year = parcel.readString();
        rating = parcel.readDouble();
        genres = parcel.readString();
        backgroundImage = parcel.readString();
        smallCoverImage = parcel.readString();
        mediumCoverImage = parcel.readString();
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

    public double getRating() {
        return rating;
    }

    public String getGenres() {
        return genres;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public String getSmallCoverImage() {
        return smallCoverImage;
    }

    public String getMediumCoverImage() {
        return mediumCoverImage;
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

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public void setSmallCoverImage(String smallCoverImage) {
        this.smallCoverImage = smallCoverImage;
    }

    public void setMediumCoverImage(String mediumCoverImage) {
        this.mediumCoverImage = mediumCoverImage;
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
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(imdbCode);
        parcel.writeString(title);
        parcel.writeString(year);
        parcel.writeDouble(rating);
        parcel.writeString(genres);
        parcel.writeString(backgroundImage);
        parcel.writeString(smallCoverImage);
        parcel.writeString(mediumCoverImage);
        parcel.writeStringArray(torrentsUrl);
        parcel.writeStringArray(torrentsQuality);
        parcel.writeStringArray(torrentsSize);
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
