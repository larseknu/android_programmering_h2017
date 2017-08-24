package no.hiof.larseknu.playingwithintentsotherapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GetTimeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_time);

        TextView textView = (TextView) findViewById(R.id.text_view_time);

        Intent intent = getIntent();
        String action = intent.getAction();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);

        if(action.equalsIgnoreCase("no.hiof.larseknu.playingwithintents.action.SHOW_TIME"))
            dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);

        long now = (new Date()).getTime();
        textView.setText(dateFormat.format(now));
    }
}
