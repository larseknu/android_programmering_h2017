package no.hiof.larseknu.playingwithintents;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by larseknu on 23/08/2017.
 */

public class OtherActivity extends Activity {
    // We want a reference to the request we send
    static final int PICK_PICTURE_REQUEST = 0;
    private Bitmap bitmap;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        // Get the ImageView
        imageView = (ImageView) findViewById(R.id.image_view);
    }

    public void getPicture(View View) {
        Intent intent = new Intent();
        // We only want images of any kind
        intent.setType("image/*");
        // We want to get some content (images as specified by the type)
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // We want to open the image in our activity
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        startActivityForResult(intent, PICK_PICTURE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        InputStream stream = null;
        // We only want to do something if we got a good result back
        if (requestCode == PICK_PICTURE_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                // Recyle unused bitmaps
                if (bitmap != null) {
                    bitmap.recycle();
                }
                // Gets the input stream from the intentdata
                stream = getContentResolver().openInputStream(data.getData());
                // Decodes the stream into a bitmap
                bitmap = BitmapFactory.decodeStream(stream);
                // Shows the bitmap in our ImageView
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // Always clean up
            finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
