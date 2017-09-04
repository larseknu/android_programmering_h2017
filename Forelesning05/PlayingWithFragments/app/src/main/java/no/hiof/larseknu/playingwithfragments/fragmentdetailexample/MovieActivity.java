package no.hiof.larseknu.playingwithfragments.fragmentdetailexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import no.hiof.larseknu.playingwithfragments.R;
import no.hiof.larseknu.playingwithfragments.model.Movie;

/**
 * Created by larseknu on 04/09/2017.
 */

public class MovieActivity extends AppCompatActivity implements MovieListFragment.OnMovieFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
    }

    @Override
    public void onMovieSelected(Movie movie) {

        if (getResources().getBoolean(R.bool.twoPaneMode)) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            MovieDetailFragment movieDetailFragment = (MovieDetailFragment) fragmentManager.findFragmentById(R.id.movie_detail_fragment);

            movieDetailFragment.setDisplayedDetail(movie.getId());
        }
        else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra("movie_id", movie.getId());
            startActivity(intent);
        }
    }
}
