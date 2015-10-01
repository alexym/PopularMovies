package alexym.com.popularmovies;

import android.app.ActivityOptions;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import alexym.com.popularmovies.Data.MovieContract;
import alexym.com.popularmovies.Rest.Movie;
import alexym.com.popularmovies.Utils.FetchMovieTask;
import alexym.com.popularmovies.Utils.OnTaskCompleted;
import alexym.com.popularmovies.Utils.RecyclerItemClickListener;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements OnTaskCompleted{

    public static final String TAG = "MainActivityFragment";

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private final String MOVIE_LIST_KEY = "movielist";
    private final String SORT_KEY = "stringsort";
    private String sortOrderGeneral="hola";
    private List items = new ArrayList<Movie>();
    private RecyclerView recycler;
    private ProgressBar progressBar;
    private MovieAdapter adapter;



    public static MainActivityFragment newInstance(Bundle arguments){
        MainActivityFragment f = new MainActivityFragment();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }


    public MainActivityFragment(){}

    @Override
    public void onStart(){
        Log.i(LOG_TAG, "onStart");
        orderSortEvaluate();
        super.onStart();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIE_LIST_KEY, (ArrayList<? extends Parcelable>) items);
        outState.putString(SORT_KEY, sortOrderGeneral);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(LOG_TAG, "estamos en el onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        recycler = (RecyclerView) rootView.findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);
        //StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL); // (int spanCount, int orientation)
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(mLayoutManager);
        adapter = new MovieAdapter(items);
        recycler.setAdapter(adapter);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);


        recycler.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recycler, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(getActivity(), DetailActivity.class);
                Movie movieObj = (Movie) items.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("my object", movieObj);

                i.putExtras(bundle);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    View sharedView = view.findViewById(R.id.imagen_view_card);
                    String transitionName = getString(R.string.image_card_animation);
                    ActivityOptions transitionActivityOptions;
                    transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), sharedView, transitionName);
                    getActivity().startActivity(i, transitionActivityOptions.toBundle());
                } else {
                    startActivity(i);
                }


            }

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
            }
        }));
        if(savedInstanceState !=null){
            items = savedInstanceState.getParcelableArrayList(MOVIE_LIST_KEY);
            sortOrderGeneral = savedInstanceState.getString(SORT_KEY);
            refreshDataScreen(items);
        }else{
            orderSortEvaluate();
        }
        return rootView;
    }

    private void orderSortEvaluate(){
        //Obtiene el tipo de orden que esta en el Shared preferences y evalua si ha cambiado,
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = prefs.getString(getString(R.string.pref_sort_order_key), getString((R.string.pref_sort_order_most_popular)));
        if(!sortOrderGeneral.equals(sortOrder)) {
            sortOrderGeneral = sortOrder;
            if(sortOrderGeneral.equals(getString(R.string.pref_sort_order_favorite))){
                updateMoviesDB();
            }else{
                updateMovies();
            }

        }
    }

    public void updateMovies(){
            if(isNetworkAvailable()) {
                new FetchMovieTask(this).execute(sortOrderGeneral);
            }
            else{
                Toast.makeText(getActivity(),"Es necesario conectarse a internet",Toast.LENGTH_SHORT).show();
            }

    }
    public void refreshDataScreen(List items){
        adapter.updateList(items);
        progressBar.setVisibility(View.GONE);
    }
    public void updateMoviesDB(){
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cursor = cr.query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
        List result = new ArrayList<Movie>();
        while (cursor.moveToNext()) {


            result.add(new Movie(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE))
                    , cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_IMAGE_THUMBNAIL))
                    , cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW))
                    , cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_USER_RATING))
                    , cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE))
                    , cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID_MOVIE))));

        }
        items.clear();
        items.addAll(result);
        refreshDataScreen(result);
    }
    @Override
    public void updateView(List result) {
        if(result !=null) {
            items.clear();
            items.addAll(result);
            refreshDataScreen(result);
        }

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
