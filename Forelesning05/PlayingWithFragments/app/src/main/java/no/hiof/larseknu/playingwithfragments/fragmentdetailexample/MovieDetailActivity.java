package no.hiof.larseknu.playingwithfragments.fragmentdetailexample;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import no.hiof.larseknu.playingwithfragments.R;
import no.hiof.larseknu.playingwithfragments.fragmentdetailexample.MovieDetailFragment;

/**
 * Created by larseknu on 04.09.2017.
 */

public class MovieDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        int id = getIntent().getIntExtra("movie_id", 1);

        FragmentManager fragmentManager = getSupportFragmentManager();
        MovieDetailFragment movieDetailFragment = (MovieDetailFragment) fragmentManager.findFragmentById(R.id.movie_detail_fragment);

        movieDetailFragment.setDisplayedDetail(id);
    }
}
