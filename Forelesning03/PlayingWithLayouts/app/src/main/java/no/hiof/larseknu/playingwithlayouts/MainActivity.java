package no.hiof.larseknu.playingwithlayouts;

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

    public void startLayoutBasics(View view) {
        startActivity(new Intent(this, LayoutBasicsActivity.class));
    }

    public void startLinearLayout(View view) {
        startActivity(new Intent(this, LinearLayoutActivity.class));
    }

    public void startRelativeLayout(View view) {
        startActivity(new Intent(this, RelativeLayoutActivity.class));
    }

    public void startFrameLayout(View view) {
        startActivity(new Intent(this, FrameLayoutActivity.class));
    }

    public void startTableLayout(View view) {
        startActivity(new Intent(this, TableLayoutActivity.class));
    }

    public void startHierarchyViewer(View view) {
        startActivity(new Intent(this, HierarchyViewerActivity.class));
    }
}
