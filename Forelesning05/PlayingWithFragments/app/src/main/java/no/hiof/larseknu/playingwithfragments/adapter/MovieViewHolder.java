package no.hiof.larseknu.playingwithfragments.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import no.hiof.larseknu.playingwithfragments.R;
import no.hiof.larseknu.playingwithfragments.model.Movie;

/**
 * Created by larseknu on 04/09/2017.
 */

public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView title;
    ImageView poster;
    OnMovieSelectedListener onMovieSelectedListener;

    public MovieViewHolder(View itemView) {
        super(itemView);
        //title       = itemView.findViewById(R.id.movie_title);
        poster    = itemView.findViewById(R.id.movie_poster);
    }

    public void bind(Movie movie, OnMovieSelectedListener listener) {
        //this.title.setText(current.getTitle());
        this.poster.setImageResource(movie.getImageId());
        this.onMovieSelectedListener = listener;
    }

    @Override
    public void onClick(View view) {
        onMovieSelectedListener.movieSelected(getAdapterPosition());
    }
}
