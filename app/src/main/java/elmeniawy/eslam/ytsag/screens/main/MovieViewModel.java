package elmeniawy.eslam.ytsag.screens.main;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * MovieViewModel
 * <p>
 * Created by Eslam El-Meniawy on 11-Mar-2018.
 * CITC - Mansoura University
 */

public class MovieViewModel implements Parcelable {
    //
    // Attributes.
    //

    private String imdbCode;
    private String title;
    private Long year;
    private Double rating;
    private List<String> genres;
    private String synopsis;
    private String backgroundImage;
    private String mediumCoverImage;
    private List<TorrentViewModel> torrents;

    MovieViewModel() {
    }

    private MovieViewModel(Parcel in) {
        imdbCode = in.readString();
        title = in.readString();

        if (in.readByte() == 0) {
            year = null;
        } else {
            year = in.readLong();
        }

        if (in.readByte() == 0) {
            rating = null;
        } else {
            rating = in.readDouble();
        }

        genres = in.createStringArrayList();
        synopsis = in.readString();
        backgroundImage = in.readString();
        mediumCoverImage = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imdbCode);
        dest.writeString(title);

        if (year == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(year);
        }

        if (rating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(rating);
        }

        dest.writeStringList(genres);
        dest.writeString(synopsis);
        dest.writeString(backgroundImage);
        dest.writeString(mediumCoverImage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MovieViewModel> CREATOR = new Creator<MovieViewModel>() {
        @Override
        public MovieViewModel createFromParcel(Parcel in) {
            return new MovieViewModel(in);
        }

        @Override
        public MovieViewModel[] newArray(int size) {
            return new MovieViewModel[size];
        }
    };

    //
    // Setters and getters.
    //

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

    public void setGenres(List<String> genres) {
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

    public void setTorrents(List<TorrentViewModel> torrents) {
        this.torrents = torrents;
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

    public List<String> getGenres() {
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

    public List<TorrentViewModel> getTorrents() {
        return torrents;
    }
}
