package no.hiof.larseknu.playingwithfragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
