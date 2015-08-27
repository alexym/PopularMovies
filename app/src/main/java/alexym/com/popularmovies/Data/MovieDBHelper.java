package alexym.com.popularmovies.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alexym on 23/08/15.
 */
public class MovieDBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "movie.db";

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.MovieEntry.COLUMN_ID_MOVIE + " INTEGER UNIQUE NOT NULL," +
                MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_IMAGE_THUMBNAIL + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_USER_RATING + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL " +
                ");";
        final String SQL_CREATE_TRAILERS_TABLE = "CREATE TABLE " + MovieContract.TrailerEntry.TABLE_NAME + " (" +
                MovieContract.TrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.TrailerEntry.COLUMN_ID_MOVIE + " INTEGER NOT NULL," +
                MovieContract.TrailerEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                MovieContract.TrailerEntry.COLUMN_SOURCE + " TEXT NOT NULL, " +
                //se asigna el id de movies como llave foranea
                " FOREIGN KEY (" + MovieContract.TrailerEntry.COLUMN_ID_MOVIE + ") REFERENCES " +
                MovieContract.MovieEntry.TABLE_NAME + " (" + MovieContract.MovieEntry._ID + ") " +
                ");";

        final String SQL_CREATE_REVIEWS_TABLE = "CREATE TABLE " + MovieContract.ReviewsEntry.TABLE_NAME + " (" +
                MovieContract.ReviewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.ReviewsEntry.COLUMN_ID_MOVIE + " INTEGER NOT NULL," +
                MovieContract.ReviewsEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                MovieContract.ReviewsEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
                " FOREIGN KEY (" + MovieContract.ReviewsEntry.COLUMN_ID_MOVIE + ") REFERENCES " +
                MovieContract.MovieEntry.TABLE_NAME + " (" + MovieContract.MovieEntry._ID + ") " +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRAILERS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEWS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +  MovieContract.MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +  MovieContract.TrailerEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +  MovieContract.ReviewsEntry.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}