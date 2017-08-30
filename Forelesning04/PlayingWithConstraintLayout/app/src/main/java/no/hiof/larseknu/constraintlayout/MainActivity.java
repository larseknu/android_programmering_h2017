package no.hiof.larseknu.constraintlayout;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_constraint_layout_basics);
        //setContentView(R.layout.activity_constraint_layout_chains);
        //setContentView(R.layout.activity_spotify);
        //setContentView(R.layout.activity_plex);
        //setContentView(R.layout.activity_plex_generated_constraints);
    }
}
