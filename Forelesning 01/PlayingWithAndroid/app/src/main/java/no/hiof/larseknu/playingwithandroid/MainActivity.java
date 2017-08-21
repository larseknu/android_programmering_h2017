package no.hiof.larseknu.playingwithandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {
    private final String tag = "Livssyklus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(tag, "Inne i onCreate()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(tag, "Inne i onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(tag, "Inne i onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(tag, "Inne i onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(tag, "Inne i onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(tag, "Inne i onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(tag, "Inne i onDestroy()");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == R.id.do_something_menu_item) {
            Toast.makeText(this, "Did something!", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, OtherActivity.class);

            startActivity(intent);

            return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }

}
