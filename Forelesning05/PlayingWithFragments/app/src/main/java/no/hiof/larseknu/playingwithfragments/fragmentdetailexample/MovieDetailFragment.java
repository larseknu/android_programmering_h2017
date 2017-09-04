package no.hiof.larseknu.playingwithfragments.fragmentdetailexample;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import no.hiof.larseknu.playingwithfragments.R;
import no.hiof.larseknu.playingwithfragments.model.Movie;

/**
 * Created by larseknu on 04.09.2017.
 */

public class MovieDetailFragment extends Fragment {
    private List<Movie> movieList;

    public final static String MOVIE_INDEX = "movieIndex";
    private static final int DEFAULT_MOVIE_INDEX = 1;

    private TextView movieTitleView;
    private TextView movieDescriptionView;
    private ImageView moviePosterImageView;
    private int movieIndex;

    /*public static MovieDetailFragment newInstance(int movieIndex) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putInt(MOVIE_INDEX, movieIndex);
        fragment.setArguments(args);
        return fragment;
    }*/

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        movieIndex = savedInstanceState == null? DEFAULT_MOVIE_INDEX : savedInstanceState.getInt(MOVIE_INDEX, DEFAULT_MOVIE_INDEX);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        movieList = Movie.getData();

        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        movieTitleView = fragmentView.findViewById(R.id.movie_title);
        moviePosterImageView = fragmentView.findViewById(R.id.movie_poster);

        setDisplayedDetail(movieIndex);

        return fragmentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(MOVIE_INDEX, movieIndex);
    }

    public void setDisplayedDetail(int movieDescriptionIndex) {
        movieIndex = movieDescriptionIndex;

        Movie movie = movieList.get(movieIndex);

        movieTitleView.setText(movie.getTitle());

        Drawable imagePoster = ContextCompat.getDrawable(getActivity(), movie.getImageId());
        if (imagePoster != null)
            moviePosterImageView.setImageDrawable(imagePoster);
    }
}
