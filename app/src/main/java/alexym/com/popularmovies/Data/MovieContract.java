package alexym.com.popularmovies.Data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by alexym on 23/08/15.
 */
public class MovieContract {
    public static final String CONTENT_AUTHORITY = "alexym.com.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";
    public static final String PATH_TRAILER = "trailer";
    public static final String PATH_REVIEWS = "review";

    public static final class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_ORIGINAL_TITLE = "originalTitle";
        public static final String COLUMN_MOVIE_POSTER_IMAGE_THUMBNAIL = "moviePosterImageThumbnail";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_USER_RATING = "userRating";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_ID_MOVIE = "movie_id";


        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }

    public static final class TrailerEntry implements BaseColumns {
        public static final String TABLE_NAME = "trailer";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SIZE = "size";
        public static final String COLUMN_SOURCE = "source";
        public static final String COLUMN_TYPE = "type";

        public static final String COLUMN_ID_MOVIE = "movie_id";
    }

    public static final class ReviewsEntry implements BaseColumns {
        public static final String TABLE_NAME = "reviews";

        public static final String COLUMN_ID_REVIEW ="review_id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_URL ="url";
    }
}
