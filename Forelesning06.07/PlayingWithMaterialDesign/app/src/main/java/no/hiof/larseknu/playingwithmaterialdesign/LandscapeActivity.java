package no.hiof.larseknu.playingwithmaterialdesign;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import no.hiof.larseknu.playingwithmaterialdesign.adapter.RecyclerAdapter;
import no.hiof.larseknu.playingwithmaterialdesign.model.Landscape;

public class LandscapeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landscape);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Landscapes");
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.landscape_recycler_view);
        RecyclerAdapter adapter = new RecyclerAdapter(this, Landscape.getData());
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManagerVertical = new LinearLayoutManager(this);
        linearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManagerVertical);
    }
}
