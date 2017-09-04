package no.hiof.larseknu.playingwithfragments.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import no.hiof.larseknu.playingwithfragments.R;
import no.hiof.larseknu.playingwithfragments.model.Movie;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    private List<Movie> movieData;
    private LayoutInflater inflater;
    private OnMovieSelectedListener movieSelectedListener;
    private RecycleAdapterListener recycleAdapterListener;

    public MovieRecyclerAdapter(Context context, List<Movie> data, final RecycleAdapterListener recycleAdapterListener) {
        this.movieData = data;
        this.inflater = LayoutInflater.from(context);

        this.recycleAdapterListener = recycleAdapterListener;

        movieSelectedListener = new OnMovieSelectedListener() {
            @Override
            public void movieSelected(int position) {
                Movie movie = movieData.get(position);
                recycleAdapterListener.movieSelected(movie);
            }
        };
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.movie_list_item, parent, false);
        MovieViewHolder holder = new MovieViewHolder(view);

        view.setOnClickListener(holder);

        return holder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie currentObj = movieData.get(position);
        holder.bind(currentObj, movieSelectedListener);
    }

    @Override
    public int getItemCount() {
        return movieData.size();
    }
}
