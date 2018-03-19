package elmeniawy.eslam.ytsag.api.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Data {
    @SerializedName("movie_count")
    @Expose
    private Long movieCount;

    @SerializedName("movies")
    @Expose
    private List<Movie> movies = null;

    public Long getMovieCount() {
        return movieCount;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public void setMovieCount(Long movieCount) {
        this.movieCount = movieCount;
    }
}
