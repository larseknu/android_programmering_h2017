package no.hiof.larseknu.playingwithmaterialdesign;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import no.hiof.larseknu.playingwithmaterialdesign.model.Landscape;

public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle(this.getTitle());

        //setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.main_menu);
        toolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.colors:
                break;
            case R.id.animals:
                startActivity(new Intent(this, AnimalActivity.class));
                break;
            case R.id.landscape:
                startActivity(new Intent(this, LandscapeActivity.class));
                break;
        }

        return true;
    }
}
