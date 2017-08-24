package no.hiof.larseknu.playingwithintents;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
    private int counter = 0;

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

    public void implicitlyStartOtherActivity(View view) {
        // Call OtherActivity with an implicit intent
        Intent intent = new Intent("no.hiof.larseknu.playingwithintents.action.OTHER_ACTIVITY");
        startActivity(intent);
    }

    public void showTime(View view) {
        // Show date with an implicit intent
        Intent intent = new Intent("no.hiof.larseknu.playingwithintents.action.SHOW_TIME");
        // Sets a flag that the opened activity should be deleted from the history stack when the user navigates away from it
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void showDate(View view) {
        // Show time with an implicit intent
        Intent intent = new Intent("no.hiof.larseknu.playingwithintents.action.SHOW_DATE");
        startActivity(intent);
    }

    public void openWebsite(View view) {
        // Create an intent that we want to view something
        Intent intent = new Intent("android.intent.action.VIEW");
        // This is what we want to view
        Uri uri = Uri.parse("http://www.hiof.no");
        intent.setData(uri);
        startActivity(intent);
    }

    public void runServiceJob(View view) {
        // We want to send a different number to the service each time
        counter++;
        // We want to produce Android clones... or kittens

        String product = "Android clones";
        if (counter % 2 == 0)
            product = "Kittens";

        // Start our service
        MyIntentService.startActionProduce(this, product, counter);
    }
}