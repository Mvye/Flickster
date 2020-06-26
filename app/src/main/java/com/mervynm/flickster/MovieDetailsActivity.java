package com.mervynm.flickster;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.mervynm.flickster.databinding.ActivityMovieDetailsBinding;
import com.mervynm.flickster.models.Movie;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;

    TextView movieActivityTitle;
    TextView movieActivityOverview;
    RatingBar movieAverageRating;
    ImageView movieBackdrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMovieDetailsBinding binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        movieActivityTitle = binding.movieActivityTitle;
        movieActivityOverview = binding.movieActivityOverview;
        movieAverageRating = binding.movieAverageRating;
        movieBackdrop = binding.movieBackdrop;

        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        movieActivityTitle.setText(movie.getTitle());
        movieActivityOverview.setText(movie.getOverview());

        float movieRating = movie.getVoteAverage().floatValue();
        movieAverageRating.setRating(movieRating = movieRating > 0 ? movieRating / 2.0f : movieRating);

        Glide.with(this).load(movie.getBackdropPath())
                .placeholder(R.drawable.flicks_backdrop_placeholder)
                .transform(new RoundedCornersTransformation(30,10))
                .into(movieBackdrop);
    }
}
