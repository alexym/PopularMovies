package alexym.com.popularmovies.Rest;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alexym on 12/07/15.
 */
public class Movie implements Parcelable {
    private String originalTitle;
    private String moviePosterImageThumbnail;
    private String overview;
    private String userRating;
    private String releaseDate;
    private int id;


    public Movie(String originalTitle, String moviePosterImageThumbnail, String overview, String userRating, String releaseDate, int id) {
        this.originalTitle = originalTitle;
        this.moviePosterImageThumbnail = moviePosterImageThumbnail;
        this.overview = overview;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getMoviePosterImageThumbnail() {
        return moviePosterImageThumbnail;
    }

    public void setMoviePosterImageThumbnail(String moviePosterImageThumbnail) {
        this.moviePosterImageThumbnail = moviePosterImageThumbnail;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    protected Movie(Parcel in) {
        originalTitle = in.readString();
        moviePosterImageThumbnail = in.readString();
        overview = in.readString();
        userRating = in.readString();
        releaseDate = in.readString();
        id = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originalTitle);
        dest.writeString(moviePosterImageThumbnail);
        dest.writeString(overview);
        dest.writeString(userRating);
        dest.writeString(releaseDate);
        dest.writeInt(id);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}