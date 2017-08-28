package no.hiof.larseknu.playingwithlayouts;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

public class HierarchyViewerActivity extends Activity {
    private View smileys;
    private View smileysViewStub;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hierarchy_viewer);

        smileys = findViewById(R.id.happySmileys);
    }

    public void toggleSmileys(View view) {
        if (smileysViewStub == null)
        {
            smileysViewStub = ((ViewStub)findViewById(R.id.happySmileysViewStub)).inflate();
        }

        boolean visibleViewStub = (smileysViewStub.getVisibility() == View.VISIBLE);
        smileysViewStub.setVisibility(visibleViewStub ? View.GONE : View.VISIBLE);

        boolean visible = (smileys.getVisibility() == View.VISIBLE);
        //smileys.setVisibility(visible ? View.GONE : View.VISIBLE);
    }
}
