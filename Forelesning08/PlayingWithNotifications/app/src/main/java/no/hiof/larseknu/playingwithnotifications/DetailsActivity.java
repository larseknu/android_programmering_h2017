package no.hiof.larseknu.playingwithnotifications;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {
    public static String TITLE_EXTRA = "TITLE_EXTRA";
    public static String BODY_TEXT_EXTRA = "BODY_TEXT_EXTRA";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent createdIntent = getIntent();
        String title = createdIntent.getStringExtra(TITLE_EXTRA);
        String bodyText = createdIntent.getStringExtra(BODY_TEXT_EXTRA);

        if (title != null)
            setTitle(title);
        if (bodyText != null)
            ((TextView)findViewById(R.id.body_text)).setText(bodyText);

    }
}
