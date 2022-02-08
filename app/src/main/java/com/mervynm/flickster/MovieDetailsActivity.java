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
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.mervynm.flickster.databinding.ActivityMovieDetailsBinding;
import com.mervynm.flickster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class MovieDetailsActivity extends YouTubeBaseActivity {

    public static final String MOVIE_VIDEO_URL = "https://api.themoviedb.org/3/movie/";
    public static final String API_URL = "/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final Double AUTOPLAY_MINIMUM_SCORE = 7.5;

    Movie movie;
    String movieURL;

    TextView movieActivityTitle;
    TextView movieActivityOverview;
    RatingBar movieAverageRating;
    ImageView movieBackdrop;

    YouTubePlayerView youTubePlayerView;

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
        youTubePlayerView = binding.youtubePlayer;

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

        movieURL = MOVIE_VIDEO_URL + movie.getMovieID() + API_URL;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(movieURL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    if (results.length() == 0) {
                        youTubePlayerView.setVisibility(View.GONE);
                        movieBackdrop.setVisibility(View.VISIBLE);
                        return;
                    }
                    movieBackdrop.setVisibility(View.GONE);
                    youTubePlayerView.setVisibility(View.VISIBLE);
                    String youtubeKey = results.getJSONObject(0).getString("key");
                    initializeYoutube(youtubeKey);

                } catch (JSONException e) {
                    Log.e("MovieDetailsActivity", "Hit json exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        });
    }

    private void initializeYoutube(String youtubeKey) {
        youTubePlayerView.initialize(getResources().getString(R.string.YOUTUBE_API_KEY), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d("MovieDetailsActivity","onInitializationSuccess");
                Log.d("MovieDetailActivity", "Voter Average: " + movie.getVoteAverage().toString());
                if (movie.getVoteAverage() > AUTOPLAY_MINIMUM_SCORE) {
                    youTubePlayer.loadVideo(youtubeKey);
                }
                else {
                    youTubePlayer.cueVideo(youtubeKey);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d("MovieDetailsActivity","onInitializationFailure");
            }
        });
    }
}
