package no.hiof.larseknu.playingwithintents;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startOtherActivity(View view) {
        // Call OtherActivity with an explicit intent
        Intent intent = new Intent(this, OtherActivity.class);
        startActivity(intent);
    }
}
