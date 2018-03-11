package elmeniawy.eslam.ytsag.api.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Movie {
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("imdb_code")
    @Expose
    private String imdbCode;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("year")
    @Expose
    private Long year;

    @SerializedName("rating")
    @Expose
    private Double rating;

    @SerializedName("genres")
    @Expose
    private List<String> genres = null;

    @SerializedName("synopsis")
    @Expose
    private String synopsis;

    @SerializedName("background_image")
    @Expose
    private String backgroundImage;

    @SerializedName("medium_cover_image")
    @Expose
    private String mediumCoverImage;

    @SerializedName("torrents")
    @Expose
    private List<Torrent> torrents = null;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImdbCode() {
        return imdbCode;
    }

    public void setImdbCode(String imdbCode) {
        this.imdbCode = imdbCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getMediumCoverImage() {
        return mediumCoverImage;
    }

    public void setMediumCoverImage(String mediumCoverImage) {
        this.mediumCoverImage = mediumCoverImage;
    }

    public List<Torrent> getTorrents() {
        return torrents;
    }

    public void setTorrents(List<Torrent> torrents) {
        this.torrents = torrents;
    }
}
