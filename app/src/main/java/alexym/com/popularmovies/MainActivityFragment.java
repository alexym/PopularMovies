package alexym.com.popularmovies;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import Utils.FetchMovieTask;
import Utils.OnTaskCompleted;
import Utils.RecyclerItemClickListener;


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
    public void onStart(){
        super.onStart();
        updateMovies();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIE_LIST_KEY, (ArrayList<? extends Parcelable>) items);
        outState.putString(SORT_KEY, sortOrderGeneral);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            items = savedInstanceState.getParcelableArrayList(MOVIE_LIST_KEY);
            sortOrderGeneral = savedInstanceState.getString(SORT_KEY);
            refreshDataScreen(items);
        }
    }

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

        return rootView;
    }

    public void updateMovies(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = prefs.getString(getString(R.string.pref_sort_order_key), getString((R.string.pref_sort_order_most_popular)));
        if(!sortOrderGeneral.equals(sortOrder)) {
            new FetchMovieTask(this).execute(sortOrder);
            sortOrderGeneral = sortOrder;
        }
    }

    @Override
    public void updateView(List result) {
        items.clear();
        items.addAll(result);
        refreshDataScreen(result);

    }
    public void refreshDataScreen(List items){
        adapter.updateList(items);
        progressBar.setVisibility(View.GONE);

    }
}