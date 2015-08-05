package Utils;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import alexym.com.popularmovies.Movie;

/**
 * Created by alexym on 16/07/15.
 */
public class FetchMovieTask extends AsyncTask<String, String, List<Movie> > {
    private final String LOG_TAG_FETCH = FetchMovieTask.class.getSimpleName();
    private OnTaskCompleted listener;

    public FetchMovieTask(OnTaskCompleted listener){
        this.listener=listener;
    }

    protected List<Movie> doInBackground(String... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        List items = new ArrayList<Movie>();
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;
        String API_KEY = "eabd29e5dd012c8df5fb7a073ae22063";
        //Español
        //String LANGUAGE = "es";
        String LANGUAGE = "en";


        try {
            //http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=eabd29e5dd012c8df5fb7a073ae22063&language=es
            //http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=eabd29e5dd012c8df5fb7a073ae22063&language=es
            final String SCHEME_PARAM = "http";
            final String AUTHORITY_PARAM = "api.themoviedb.org";
            final String PATH1_PARAM = "3";
            final String PATH2_PARAM = "discover";
            final String PATH3_PARAM = "movie";

            final String SORT_BY_PARAM = "sort_by";
            final String API_KEY_PARAM = "api_key";
            final String LANGUAGE_PARAM = "language";

            Uri.Builder builder = new Uri.Builder();
            builder.scheme(SCHEME_PARAM)
                    .authority(AUTHORITY_PARAM)
                    .appendPath(PATH1_PARAM)
                    .appendPath(PATH2_PARAM)
                    .appendPath(PATH3_PARAM)
                    .appendQueryParameter(SORT_BY_PARAM, params[0])
                    .appendQueryParameter(API_KEY_PARAM, API_KEY)
                    .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE);
            String myUrl = builder.build().toString();
            URL url = new URL(myUrl);
            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.

                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.

                return null;
            }
            movieJsonStr = buffer.toString();
            items.clear();
            items = getMovieDataFromJson(movieJsonStr);

        } catch (IOException e) {
            Log.e(LOG_TAG_FETCH, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }


        return items;
    }

        /* The date/time conversion code is going to be moved outside the asynctask later,
         * so for convenience we're breaking it out into its own method now.
         */


    private List<Movie> getMovieDataFromJson(String MovieJsonStr) throws JSONException {
        JSONObject moviesJson = new JSONObject(MovieJsonStr);
        List itemsJSON = new ArrayList<Movie>();
        final String OWM_RESULTS = "results";
        final String OWM_ID = "id";
        final String OWM_POSTER = "poster_path";
        final String OWM_TITLE = "title";
        final String OWM_OVERVIEW = "overview";
        final String OWM_DATE = "release_date";
        final String OWN_VOTE_AVARAGE = "vote_average";

        final String URL_IMAGE_BASE = "http://image.tmdb.org/t/p/";
        final String SIZE_IMAGE = "w185" + "/";


        JSONArray moviesArray = moviesJson.getJSONArray(OWM_RESULTS);
        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject movieJson = moviesArray.getJSONObject(i);
            itemsJSON.add(new Movie(movieJson.getString(OWM_TITLE)
                    , URL_IMAGE_BASE + SIZE_IMAGE + movieJson.getString(OWM_POSTER)
                    , movieJson.getString(OWM_OVERVIEW)
                    , movieJson.getString(OWN_VOTE_AVARAGE)
                    , movieJson.getString(OWM_DATE)
                    , movieJson.getInt(OWM_ID)));
        }


        return itemsJSON;
    }

    protected void onPostExecute(List result) {
        listener.updateView(result);
    }
}
