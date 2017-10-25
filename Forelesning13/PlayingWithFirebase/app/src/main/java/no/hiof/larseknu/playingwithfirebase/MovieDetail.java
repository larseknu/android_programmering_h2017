package no.hiof.larseknu.playingwithfirebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import no.hiof.larseknu.playingwithfirebase.model.Movie;

public class MovieDetail extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference movieDatabaseReference;
    private DatabaseReference ratingDatabaseReference;

    private TextView movieTitleTextView;
    private TextView descriptionTextView;
    private TextView movieReleaseDateTextView;
    private ImageView moviePosterImageView;
    private RatingBar movieRatingBar;

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent receivedIntent = getIntent();
        final String movieUid = receivedIntent.getStringExtra("movieUid");

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        movieDatabaseReference = firebaseDatabase.getReference().child("movies").child(movieUid);
        ratingDatabaseReference = firebaseDatabase.getReference().child("ratings").child(firebaseAuth.getUid());

        movieTitleTextView = findViewById(R.id.movieTitle);
        descriptionTextView = findViewById(R.id.description);
        movieReleaseDateTextView = findViewById(R.id.movieReleaseDate);
        moviePosterImageView = findViewById(R.id.moviePoster);
        movieRatingBar = findViewById(R.id.movieRatingBar);

        movieRatingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    float touchPositionX = event.getX();
                    float width = movieRatingBar.getWidth();
                    float starsf = (touchPositionX / width) * 5.0f;
                    int stars = (int)starsf + 1;

                    ratingDatabaseReference.child(movie.getUid()).setValue(stars);
                    v.setPressed(false);
                }

                return true;
            }
        });

        movieDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                movie = dataSnapshot.getValue(Movie.class);
                movie.setUid(dataSnapshot.getKey());

                movieTitleTextView.setText(movie.getTitle());
                descriptionTextView.setText(movie.getDescription());
                movieReleaseDateTextView.setText(movie.getReleaseDate());

                ratingDatabaseReference.child(firebaseAuth.getUid()).child(movie.getUid());

                Glide.with(MovieDetail.this)
                        .load(movie.getPosterUrl())
                        .into(moviePosterImageView);

                ratingDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (movie.getUid() != null && dataSnapshot.child(movie.getUid()).exists()) {
                            long rating = (long)dataSnapshot.child(movie.getUid()).getValue();
                            movieRatingBar.setRating(rating);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
