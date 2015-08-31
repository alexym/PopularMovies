package alexym.com.popularmovies;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import alexym.com.popularmovies.Data.MovieContract;
import alexym.com.popularmovies.Data.MovieProvider;
import alexym.com.popularmovies.Rest.Movie;
import alexym.com.popularmovies.Rest.MovieService;
import alexym.com.popularmovies.Rest.Result;
import alexym.com.popularmovies.Rest.ReviewsAndTrailers;
import alexym.com.popularmovies.Rest.ReviewsAndTrailers.Reviews;
import alexym.com.popularmovies.Rest.ReviewsAndTrailers.Trailers;
import alexym.com.popularmovies.Rest.Youtube;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    private Movie mMyObject;
    List<Youtube> mYoutubeList;
    List<Result> mResultList;

    LinearLayout linearLayoutTrailers, linearLayoutReviews;
    TextView runtime;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.fragment_detail, container, false);
        mMyObject = (Movie) this.getArguments().getParcelable("my object");
        try {
            initUI(V, mMyObject);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        updateInfoMovie();
        return V;
    }

    public void initUI(View v, Movie obj) throws ParseException {
        ImageView poster_iv = (ImageView) v.findViewById(R.id.poster_iv);
        Picasso.with(getActivity()).load(obj.getMoviePosterImageThumbnail()).into(poster_iv);

        TextView title_tv = (TextView) v.findViewById(R.id.title_tv);
        title_tv.setText(obj.getOriginalTitle());

        TextView year_tv = (TextView) v.findViewById(R.id.year_tv);
        year_tv.setText(getReadableDateString(obj.getReleaseDate()));

        TextView rate_tv = (TextView) v.findViewById(R.id.rate_tv);
        rate_tv.setText(obj.getUserRating() + "/10");

        TextView overview_tv = (TextView) v.findViewById(R.id.overview_tv);
        overview_tv.setText(obj.getOverview());

        runtime = (TextView) v.findViewById(R.id.time_tv);
        linearLayoutTrailers = (LinearLayout) v.findViewById(R.id.videos_ll);
        linearLayoutReviews = (LinearLayout) v.findViewById(R.id.reviews_ll);

        Button fav_btn = (Button) v.findViewById(R.id.favorite_btn);
        fav_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveMovieDB();
            }
        });

    }


    private String getReadableDateString(String time) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date theDate = format.parse(time);

        Calendar myCal = new GregorianCalendar();
        myCal.setTime(theDate);

        return String.valueOf(myCal.get(Calendar.YEAR));
    }

    private void updateInfoMovie() {
        ContentResolver cr = getActivity().getContentResolver();
        Uri uriBase = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, mMyObject.getId());
        Cursor cursor = cr.query(uriBase, null, null, null, null);
        if (cursor.moveToFirst()) {
            Log.i(LOG_TAG,"si hay registros guardados");
            Cursor cursorTraielers = cr.query(
                    MovieContract.TrailerEntry.CONTENT_URI,
                    null,
                    MovieProvider.selectionId,
                    new String[]{String.valueOf(mMyObject.getId())},
                    null);
            if(cursorTraielers.moveToFirst()){
                Log.i(LOG_TAG,"si si si entro al cursortrailers");
            }else{
                Log.i(LOG_TAG,"no hay nada");
            }
            while (cursorTraielers.moveToNext()) {
                Log.i(LOG_TAG,"si si ");

                Youtube youtubeOb = new Youtube("hola","hola");
                youtubeOb.setName(cursorTraielers.getString(cursorTraielers.getColumnIndex(MovieContract.TrailerEntry.COLUMN_NAME)));
                Log.i(LOG_TAG,"essss is "+youtubeOb);
            }

        } else {
            MovieService movieService = new MovieService();
            MovieService.MovieServiceInterface movieServiceInterface = movieService.getmMovieServiceInterface();
                movieServiceInterface.getTrailerAndReviews(mMyObject.getId(), new Callback<ReviewsAndTrailers>() {

                @Override
                public void success(ReviewsAndTrailers reviewsAndTrailers, Response response) {
                    runtime.setText(String.valueOf(reviewsAndTrailers.getRuntime()) + "min.");
                    Trailers trailers = reviewsAndTrailers.getTrailers();
                    mYoutubeList = trailers.getYoutube();
                    createlinearlayoutTrailers(mYoutubeList);

                    Reviews reviews = reviewsAndTrailers.getReviews();
                    mResultList = reviews.getResults();
                    createlinearlayoutReviews(mResultList);

                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }
    }

    public void createlinearlayoutTrailers(List<Youtube> youtubelistm) {
        for (Youtube youtube : youtubelistm) {
            View child = getActivity().getLayoutInflater().inflate(R.layout.trailer_row, null);
            TextView tv = (TextView) child.findViewById(R.id.title_video_tv);
            tv.setText(youtube.getName());
            child.setOnClickListener(buttonClick);
            TextView tvhidden = (TextView) child.findViewById(R.id.hidden_value);
            tvhidden.setText(youtube.getSource());
            linearLayoutTrailers.addView(child);
        }
    }

    View.OnClickListener buttonClick = new View.OnClickListener() {
        public void onClick(View v) {
            TextView tvhidden = (TextView) v.findViewById(R.id.hidden_value);
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + tvhidden.getText())));
        }
    };

    public void createlinearlayoutReviews(List<Result> resultListm) {
        for (Result result : resultListm) {
            View child = getActivity().getLayoutInflater().inflate(R.layout.review_row, null);
            TextView authorTv = (TextView) child.findViewById(R.id.author_tv);
            authorTv.setText("By " + result.getAuthor());

            TextView contentTv = (TextView) child.findViewById(R.id.content_tv);
            contentTv.setText("\"" + result.getContent() + "\"");
            linearLayoutReviews.addView(child);
        }
    }

    public int saveMovieDB() {
        ContentResolver cr = getActivity().getContentResolver();
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.COLUMN_ID_MOVIE, mMyObject.getId());
        cv.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, mMyObject.getOriginalTitle());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_IMAGE_THUMBNAIL, mMyObject.getMoviePosterImageThumbnail());
        cv.put(MovieContract.MovieEntry.COLUMN_USER_RATING, mMyObject.getUserRating());
        cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, mMyObject.getReleaseDate());
        cv.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, mMyObject.getOverview());

        Uri resultInsUri = cr.insert(MovieContract.MovieEntry.CONTENT_URI, cv);
        int resutInsId = Integer.parseInt(resultInsUri.getLastPathSegment());
        Log.i(LOG_TAG, "se inserto " + resultInsUri.getLastPathSegment());
        saveTrailersAndReviews(resutInsId);
        return 0;
    }

    public boolean saveTrailersAndReviews(int resultInsId) {
        Log.i(LOG_TAG, "el result es " + resultInsId);
        ContentResolver cr = getActivity().getContentResolver();
        saveTrailers(resultInsId, cr);
        saveReviews(resultInsId, cr);
        return false;
    }

    public void saveTrailers(int resultInsId, ContentResolver cv) {
        if (mYoutubeList.size() > 0) {
            ArrayList<ContentValues> cvArray = new ArrayList<ContentValues>();

            for (Youtube youtube : mYoutubeList) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(MovieContract.TrailerEntry.COLUMN_ID_MOVIE, resultInsId);
                contentValues.put(MovieContract.TrailerEntry.COLUMN_NAME, youtube.getName());
                contentValues.put(MovieContract.TrailerEntry.COLUMN_SOURCE, youtube.getSource());
                cvArray.add(contentValues);
            }

            int bulk = cv.bulkInsert(MovieContract.TrailerEntry.CONTENT_URI, cvArray.toArray(new ContentValues[cvArray.size()]));
            Log.i(LOG_TAG, "El valor de bulk trailers " + bulk);
        }
    }

    public void saveReviews(int resultInsId, ContentResolver cv) {
        if (mResultList.size() > 0) {
            ArrayList<ContentValues> cvArray = new ArrayList<ContentValues>();

            for (Result result : mResultList) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(MovieContract.ReviewsEntry.COLUMN_ID_MOVIE, resultInsId);
                contentValues.put(MovieContract.ReviewsEntry.COLUMN_AUTHOR, result.getAuthor());
                contentValues.put(MovieContract.ReviewsEntry.COLUMN_CONTENT, result.getContent());
                cvArray.add(contentValues);
            }

            int bulk = cv.bulkInsert(MovieContract.ReviewsEntry.CONTENT_URI, cvArray.toArray(new ContentValues[cvArray.size()]));
            Log.i(LOG_TAG, "El valor de bulk reviews " + bulk);
        }
    }
}
