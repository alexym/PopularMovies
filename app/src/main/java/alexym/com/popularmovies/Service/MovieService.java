package alexym.com.popularmovies.Service;


import alexym.com.popularmovies.Model.POJOs.ReviewsAndTrailers;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by alexym on 23/08/15.
 */
public class MovieService {
    //http://api.themoviedb.org/3/movie/{87101}?api_key=eabd29e5dd012c8df5fb7a073ae22063&append_to_response=trailers,reviews
    private static final String WEB_SERVICE_BASE_URL = "http://api.themoviedb.org/3";
    private MovieServiceInterface mMovieServiceInterface;

    public MovieService(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(WEB_SERVICE_BASE_URL)
                .build();
        mMovieServiceInterface = restAdapter.create(MovieServiceInterface.class);

    }

    public interface MovieServiceInterface {
        @GET("/movie/{id}?api_key=eabd29e5dd012c8df5fb7a073ae22063&append_to_response=trailers,reviews")
        void getTrailerAndReviews(@Path("id") int id, Callback<ReviewsAndTrailers> cb);
    }

    public MovieServiceInterface getmMovieServiceInterface(){
        return mMovieServiceInterface;
    }
}
