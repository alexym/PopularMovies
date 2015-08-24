package alexym.com.popularmovies;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

import alexym.com.popularmovies.Data.MovieContract;
import alexym.com.popularmovies.Data.MovieDBHelper;

/**
 * Created by Cloudco on 24/08/15.
 */
public class TestDB extends AndroidTestCase {
    public static final String LOG_TAG = TestDB.class.getSimpleName();
    void deleteTheDatabase() {
        mContext.deleteDatabase(MovieDBHelper.DATABASE_NAME);
    }
    public void setUp() {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(MovieContract.MovieEntry.TABLE_NAME);
        tableNameHashSet.add(MovieContract.TrailerEntry.TABLE_NAME);
        tableNameHashSet.add(MovieContract.ReviewsEntry.TABLE_NAME);

        mContext.deleteDatabase(MovieDBHelper.DATABASE_NAME);
        SQLiteDatabase db = new MovieDBHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + MovieContract.MovieEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> locationColumnHashSet = new HashSet<String>();
        HashSet<String> locationColumn = new HashSet<String>();
        locationColumnHashSet.add(MovieContract.MovieEntry._ID);
        locationColumnHashSet.add(MovieContract.MovieEntry.COLUMN_ID_MOVIE);
        locationColumnHashSet.add(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE);
        locationColumnHashSet.add(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_IMAGE_THUMBNAIL);
        locationColumnHashSet.add(MovieContract.MovieEntry.COLUMN_OVERVIEW);
        locationColumnHashSet.add(MovieContract.MovieEntry.COLUMN_USER_RATING);
        locationColumnHashSet.add(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);


        int columnNameIndex = c.getColumnIndex("name");
        int nm=0;
        do {
            String columnName = c.getString(columnNameIndex);
            locationColumn.add(columnName);
            locationColumnHashSet.remove(columnName);
            nm++;
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns "+locationColumn.toString(),
                locationColumnHashSet.isEmpty());

        c = db.rawQuery("PRAGMA table_info(" + MovieContract.TrailerEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        HashSet<String> locationColumnHashSetTrailer = new HashSet<String>();
        //HashSet<String> locationColumn = new HashSet<String>();
        locationColumnHashSetTrailer.add(MovieContract.TrailerEntry._ID);
        locationColumnHashSetTrailer.add(MovieContract.TrailerEntry.COLUMN_ID_MOVIE);
        locationColumnHashSetTrailer.add(MovieContract.TrailerEntry.COLUMN_NAME);
        locationColumnHashSetTrailer.add(MovieContract.TrailerEntry.COLUMN_SOURCE);


        int columnNameIndextr = c.getColumnIndex("name");

        do {
            String columnName = c.getString(columnNameIndextr);
            //locationColumn.add(columnName);
            locationColumnHashSet.remove(columnName);
            nm++;
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns "+locationColumn.toString(),
                locationColumnHashSet.isEmpty());
        db.close();
    }
    public void testInsertDB() throws Throwable {
        MovieDBHelper weatherDbHelper = new MovieDBHelper(mContext);
        SQLiteDatabase db = weatherDbHelper.getWritableDatabase();
        ContentValues testValues = createMovieCV();
        long locationRowId;
        locationRowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert North Pole Location Values", locationRowId != -1);

    }
    public ContentValues createMovieCV() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.MovieEntry.COLUMN_ID_MOVIE, 10988);
        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_IMAGE_THUMBNAIL, "56iadsfsd");
        testValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, "Hola");
        testValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, "sip overview");
        testValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "realesedate");
        testValues.put(MovieContract.MovieEntry.COLUMN_USER_RATING, "sip user rating");

        return testValues;
    }
}
