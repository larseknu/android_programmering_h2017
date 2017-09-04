package no.hiof.larseknu.playingwithfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import no.hiof.larseknu.playingwithfragments.fragmenttabs.CollectionFragment;
import no.hiof.larseknu.playingwithfragments.fragmenttabs.DiscoverFragment;
import no.hiof.larseknu.playingwithfragments.model.Movie;

/**
 * Created by larseknu on 04/09/2017.
 */

public class MovieActivity extends AppCompatActivity implements CollectionFragment.OnMovieFragmentInteractionListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        CollectionFragment collectionFragment = new CollectionFragment();
        fragmentTransaction.add(R.id.movielistcontainer, collectionFragment);


        if (getResources().getBoolean(R.bool.twoPaneMode)) {
            findViewById(R.id.moviedetailscontainer).setVisibility(View.VISIBLE);
            DiscoverFragment discoverFragment = new DiscoverFragment();
            fragmentTransaction.add(R.id.moviedetailscontainer, discoverFragment);
        }

        fragmentTransaction.commit();
    }

    @Override
    public void onMovieSelected(Movie movie) {

        if (getResources().getBoolean(R.bool.twoPaneMode)) {

        }
        else {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            DiscoverFragment discoverFragment = new DiscoverFragment();
            fragmentTransaction.replace(R.id.movielistcontainer, discoverFragment, "MovieDetail");
            fragmentTransaction.commit();
        }

    }

}
