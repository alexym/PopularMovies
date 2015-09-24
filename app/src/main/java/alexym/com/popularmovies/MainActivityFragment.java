package alexym.com.popularmovies;

import android.app.ActivityOptions;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import alexym.com.popularmovies.Rest.Movie;
import alexym.com.popularmovies.Utils.FetchMovieTask;
import alexym.com.popularmovies.Utils.OnTaskCompleted;
import alexym.com.popularmovies.Utils.RecyclerItemClickListener;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements OnTaskCompleted{

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final String MOVIE_LIST_KEY = "movielist";
    private final String SORT_KEY = "stringsort";
    String sortOrderGeneral="hola";

    List items = new ArrayList<Movie>();

    private RecyclerView recycler;
    private ProgressBar progressBar;
    MovieAdapter adapter;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.sortOrder) {
            sortOrderDiag();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart(){
        super.onStart();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIE_LIST_KEY, (ArrayList<? extends Parcelable>) items);
        outState.putString(SORT_KEY, sortOrderGeneral);
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        if (savedInstanceState != null) {
//            items = savedInstanceState.getParcelableArrayList(MOVIE_LIST_KEY);
//            sortOrderGeneral = savedInstanceState.getString(SORT_KEY);
//            refreshDataScreen(items);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
            updateMovies();
        }
        return rootView;
    }

    public void updateMovies(){
        //Obtiene el tipo de orden que esta en el Shared preferences y evalua si ha cambiado,
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = prefs.getString(getString(R.string.pref_sort_order_key), getString((R.string.pref_sort_order_most_popular)));
        if(!sortOrderGeneral.equals(sortOrder)) {
            if(isNetworkAvailable()) {
                new FetchMovieTask(this).execute(sortOrder);
                sortOrderGeneral = sortOrder;
            }
            else{
                Toast.makeText(getActivity(),"Es necesario conectarse a internet",Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void updateView(List result) {
        if(result !=null) {
            items.clear();
            items.addAll(result);
            refreshDataScreen(result);
        }

    }
    public void refreshDataScreen(List items){
        adapter.updateList(items);
        progressBar.setVisibility(View.GONE);

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
    private void sortOrderDiag(){
        DialogFragment newFragment = new SortOrderDialog();
        newFragment.show(getActivity().getFragmentManager(),"sortOrderDialog");
    }

}
