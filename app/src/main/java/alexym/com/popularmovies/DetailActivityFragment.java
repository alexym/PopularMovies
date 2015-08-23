package alexym.com.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import alexym.com.popularmovies.Model.POJOs.ReviewsAndTrailers;
import alexym.com.popularmovies.Model.POJOs.ReviewsAndTrailers.Trailers;
import alexym.com.popularmovies.Model.POJOs.ReviewsAndTrailers.Youtube;
import alexym.com.popularmovies.Service.MovieService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    private Movie myObject;
    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.fragment_detail, container, false);
        myObject = (Movie)  this.getArguments().getParcelable("my object");
        Log.i(LOG_TAG,myObject.getOriginalTitle());
        try {
            initUI(V,myObject);
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
        rate_tv.setText(obj.getUserRating()+"/10");

        TextView overview_tv = (TextView) v.findViewById(R.id.overview_tv);
        overview_tv.setText(obj.getOverview());
        Log.i(LOG_TAG, obj.toString());
    }


    private String getReadableDateString(String time) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date theDate = format.parse(time);

        Calendar myCal = new GregorianCalendar();
        myCal.setTime(theDate);

        return String.valueOf(myCal.get(Calendar.YEAR));
    }

    private void updateInfoMovie(){
        MovieService movieService = new MovieService();
        MovieService.MovieServiceInterface movieServiceInterface =movieService.getmMovieServiceInterface();
        movieServiceInterface.getTrailerAndReviews(myObject.getId(), new Callback<ReviewsAndTrailers>() {


            @Override
            public void success(ReviewsAndTrailers reviewsAndTrailers, Response response) {
                Trailers trailers = reviewsAndTrailers.getTrailers();
                List<Youtube> youtube = trailers.getYoutube();
                for(Youtube youtube1 : youtube){
                    Log.i(LOG_TAG,"es s "+youtube1.getName());
                }

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
