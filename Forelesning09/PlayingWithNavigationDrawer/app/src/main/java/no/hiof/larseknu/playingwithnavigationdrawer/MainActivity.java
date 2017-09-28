package no.hiof.larseknu.playingwithnavigationdrawer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import no.hiof.larseknu.playingwithnavigationdrawer.fragment.NavigationDrawerFragment;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    NavigationDrawerFragment navigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Hello, I'm a snackbar!", Snackbar.LENGTH_LONG)
                        .setAction("Action", new MyActionListener()).show();
            }
        });

        setUpDrawer();
    }

    private void setUpDrawer() {
        navigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer_fragment);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationDrawerFragment.setUpDrawer(drawerLayout, toolbar, R.id.nav_shows);
    }

    @Override
    protected void onStart() {
        navigationDrawerFragment.updateCheckedItem(R.id.nav_shows);

        super.onStart();
    }

    private class MyActionListener  implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            // Code to do the desired Action
            Snackbar.make(v, "I'm dead! =(", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }
}
