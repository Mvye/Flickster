package com.mervynm.flickster;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mervynm.flickster.models.Movie;

import org.parceler.Parcels;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;

    TextView movieActivityTitle;
    TextView movieActivityOverview;
    RatingBar movieAverageRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        movieActivityTitle = findViewById(R.id.movieActivityTitle);
        movieActivityOverview = findViewById(R.id.movieActivityOverview);
        movieAverageRating = findViewById(R.id.movieAverageRating);

        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        movieActivityTitle.setText(movie.getTitle());
        movieActivityOverview.setText(movie.getOverview());

        float movieRating = movie.getVoteAverage().floatValue();
        movieAverageRating.setRating(movieRating = movieRating > 0 ? movieRating / 2.0f : movieRating);
    }
}
