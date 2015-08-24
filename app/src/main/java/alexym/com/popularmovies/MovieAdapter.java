package alexym.com.popularmovies;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import alexym.com.popularmovies.Utils.Movie;

/**
 * Created by alexym on 12/07/15.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{
    private final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private List<Movie> items;
    private Context context;

    public MovieAdapter(List items){
        this.items = items;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_movie, viewGroup, false);
        return new MovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder movieViewHolder, int i) {

        Picasso.with(context).load(items.get(i).getMoviePosterImageThumbnail()).into(movieViewHolder.imagen);
        movieViewHolder.nombre.setText(items.get(i).getOriginalTitle());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public ImageView imagen;
        public TextView nombre;


        public MovieViewHolder(View v) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.imagen_view_card);
            nombre = (TextView) v.findViewById(R.id.nombre);

        }
    }
    public void updateList(List<Movie> data) {
        items = data;
        final MovieAdapter adapter = this;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }
}
