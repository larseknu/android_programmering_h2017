package no.hiof.larseknu.playingwithfragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import no.hiof.larseknu.playingwithfragments.buttonhandlingexample.ButtonHandlingActivity;
import no.hiof.larseknu.playingwithfragments.fragmentdetailexample.MovieActivity;
import no.hiof.larseknu.playingwithfragments.fragmenttabsexample.TabbedMovieActivity;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getFragmentManager();
    }


    public void addHappyFragment(View view) {
        HappyFragment happyFragment = new HappyFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.content, happyFragment, "Happy");
        fragmentTransaction.addToBackStack("Happy");
        fragmentTransaction.commit();
    }

    public void addSadFragment(View view) {
        SadFragment sadFragment = new SadFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.content, sadFragment, "Sad");
        fragmentTransaction.addToBackStack("Sad");
        fragmentTransaction.commit();
    }

    public void removeHappyFragment(View view) {
        Fragment fragment = fragmentManager.findFragmentByTag("Happy");

        if (fragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        }
    }

    public void removeSadFragment(View view) {
        Fragment fragment = fragmentManager.findFragmentByTag("Sad");

        if (fragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        }
    }

    public void replaceWithHappyFragment(View view) {
        HappyFragment happyFragment = new HappyFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.content, happyFragment, "HappyReplaced");

        fragmentTransaction.commit();
    }

    public void replaceWithSadFragment(View view) {
        SadFragment sadFragment = new SadFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.content, sadFragment, "SadReplaced");

        fragmentTransaction.commit();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_button_handling) {
            startActivity(new Intent(this, ButtonHandlingActivity.class));
            return true;
        }
        else if (id == R.id.action_tabbed_movie) {
            startActivity(new Intent(this, TabbedMovieActivity.class));
            return true;
        }
        else if (id == R.id.action_movie) {
            startActivity(new Intent(this, MovieActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
