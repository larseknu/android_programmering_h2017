package no.hiof.larseknu.playingwithnotifications;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class PictureActivity extends AppCompatActivity {
    public static final String TITLE_EXTRA = "title_extra";
    public static final String IMAGE_TEXT_EXTRA = "image_text_extra";
    public static final String IMAGE_RESOURCE_ID_EXTRA = "image_resource_id_extra";

    public static final int IMAGE_RESOURCE_ID_NOT_SET = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        Intent createIntent = getIntent();
        String title = createIntent.getStringExtra(TITLE_EXTRA);
        String imageText = createIntent.getStringExtra(IMAGE_TEXT_EXTRA);
        int imageResourceId =
                createIntent.getIntExtra(IMAGE_RESOURCE_ID_EXTRA, IMAGE_RESOURCE_ID_NOT_SET);

        ImageView imageView = findViewById(R.id.imageView);

        if(title != null)
            setTitle(title);
        if(imageText != null)
        {
            ((TextView)findViewById(R.id.imageTextView)).setText(imageText);
            imageView.setContentDescription(imageText);
        }
        if(imageResourceId != IMAGE_RESOURCE_ID_NOT_SET)
            imageView.setImageResource(imageResourceId);
    }
}

