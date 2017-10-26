package no.hiof.larseknu.playingwithfirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import no.hiof.larseknu.playingwithfirebase.adapter.CollectionRecyclerAdapter;
import no.hiof.larseknu.playingwithfirebase.model.Movie;

public class MainActivity extends AppCompatActivity {
    public static final int RC_SIGN_IN = 1;

    private RecyclerView recyclerView;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference movieDatabaseReference;
    private CollectionRecyclerAdapter recyclerAdapter;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    private ChildEventListener childEventListener;

    private ArrayList<Movie> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        movieList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        movieDatabaseReference = firebaseDatabase.getReference().child("movies");

        setUpRecyclerView();

        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    attachDatabaseReadListener();
                }
                else {
                    onSignedOutCleanup();
                    // Not signed in
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void onSignedOutCleanup() {
        movieList.clear();
    }

    private void attachDatabaseReadListener() {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Movie movie = dataSnapshot.getValue(Movie.class);
                movie.setUid(dataSnapshot.getKey());

                if (!movieList.contains(movie)) {
                    movieList.add(movie);
                    recyclerAdapter.notifyItemInserted(movieList.size()-1);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Movie movie = dataSnapshot.getValue(Movie.class);
                movie.setUid(dataSnapshot.getKey());

                int position = movieList.indexOf(movie);

                movieList.set(position, movie);
                recyclerAdapter.notifyItemChanged(position);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Movie removedMovie = dataSnapshot.getValue(Movie.class);
                removedMovie.setUid(dataSnapshot.getKey());
                int position = movieList.indexOf(removedMovie);
                movieList.remove(removedMovie);
                recyclerAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        movieDatabaseReference.addChildEventListener(childEventListener);
    }

    private void detachDatabaseReadListener() {
        if (childEventListener != null) {
            movieDatabaseReference.removeEventListener(childEventListener);
            childEventListener = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(firebaseAuthStateListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (firebaseAuthStateListener != null)
            firebaseAuth.removeAuthStateListener(firebaseAuthStateListener);

        detachDatabaseReadListener();
        movieList.clear();
        recyclerAdapter.notifyDataSetChanged();
    }

    private void setUpRecyclerView() {

        recyclerView = findViewById(R.id.collectionRecyclerView);
        recyclerAdapter = new CollectionRecyclerAdapter(MainActivity.this, movieList);
        recyclerView.setAdapter(recyclerAdapter);

        recyclerAdapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = recyclerView.getChildAdapterPosition(v);

                Movie movie = movieList.get(position);

                Intent intent = new Intent(MainActivity.this, MovieDetail.class);
                intent.putExtra("movieUid", movie.getUid());
                startActivity(intent);
            }
        });

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 2); // (Context context, int spanCount)
        recyclerView.setLayoutManager(mGridLayoutManager);

    }

    private void generateTestData() {
        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie(null, "Iron Man 3", "When Tony Stark's world is torn apart by a formidable terrorist called the Mandarin, he starts an odyssey of rebuilding and retribution.", "2013-04-18", null));
        movies.add(new Movie(null, "Donnie Darko", "A troubled teenager is plagued by visions of a man in a large rabbit suit who manipulates him to commit a series of crimes, after he narrowly escapes a bizarre accident.", "2001-10-26", null));
        movies.add(new Movie(null, "Pulp Fiction", "A burger-loving hit man, his philosophical partner, a drug-addled gangster's moll and a washed-up boxer converge in this sprawling, comedic crime caper. Their adventures unfurl in three stories that ingeniously trip back and forth in time.", "1994-10-14", null));
        movies.add(new Movie(null, "Spirited Away", "During her family's move to the suburbs, a sullen 10-year-old girl wanders into a world ruled by gods, witches, and spirits, and where humans are changed into beasts.", "2001-06-20", null));
        movies.add(new Movie(null, "Star Wars: The Force Awakens", "Thirty years after defeating the Galactic Empire, Han Solo and his allies face a new threat from the evil Kylo Ren and his army of Stormtroopers.", "2015-12-14", null));
        movies.add(new Movie(null, "Up", "Seventy-eight year old Carl Fredricksen travels to Paradise Falls in his home equipped with balloons, inadvertently taking a young stowaway.", "2009-09-25", null));


        for (Movie movie:
             movies) {
            movieDatabaseReference.push().setValue(movie);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
