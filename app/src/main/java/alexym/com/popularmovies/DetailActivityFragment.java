package alexym.com.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import alexym.com.popularmovies.Rest.ReviewsAndTrailers;
import alexym.com.popularmovies.Rest.ReviewsAndTrailers.Result;
import alexym.com.popularmovies.Rest.ReviewsAndTrailers.Reviews;
import alexym.com.popularmovies.Rest.ReviewsAndTrailers.Trailers;
import alexym.com.popularmovies.Rest.ReviewsAndTrailers.Youtube;
import alexym.com.popularmovies.Rest.MovieService;
import alexym.com.popularmovies.Utils.Movie;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    private Movie myObject;

    LinearLayout linearLayoutTrailers, linearLayoutReviews;
    TextView runtime;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.fragment_detail, container, false);
        myObject = (Movie) this.getArguments().getParcelable("my object");
        try {
            initUI(V, myObject);
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
    }


    private String getReadableDateString(String time) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date theDate = format.parse(time);

        Calendar myCal = new GregorianCalendar();
        myCal.setTime(theDate);

        return String.valueOf(myCal.get(Calendar.YEAR));
    }

    private void updateInfoMovie() {
        MovieService movieService = new MovieService();
        MovieService.MovieServiceInterface movieServiceInterface = movieService.getmMovieServiceInterface();
        movieServiceInterface.getTrailerAndReviews(myObject.getId(), new Callback<ReviewsAndTrailers>() {

            @Override
            public void success(ReviewsAndTrailers reviewsAndTrailers, Response response) {
                runtime.setText(String.valueOf(reviewsAndTrailers.getRuntime()) + "min.");
                Trailers trailers = reviewsAndTrailers.getTrailers();
                List<Youtube> youtubeList = trailers.getYoutube();
                createlinearlayoutTrailers(youtubeList);

                Reviews reviews = reviewsAndTrailers.getReviews();
                List<Result> results = reviews.getResults();
                createlinearlayoutReviews(results);

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
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
            contentTv.setText(result.getContent());
            linearLayoutReviews.addView(child);
        }
    }
}
