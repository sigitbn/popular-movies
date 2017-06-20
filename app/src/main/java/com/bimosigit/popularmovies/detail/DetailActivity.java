package com.bimosigit.popularmovies.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bimosigit.popularmovies.R;
import com.bimosigit.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private TextView releaseTextView;
    private TextView overviewTextView;
    private TextView ratingTextView;
    private TextView titleTextView;
    private ImageView posterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        posterImageView = (ImageView) findViewById(R.id.iv_poster);
        titleTextView = (TextView) findViewById(R.id.tv_original_title);
        ratingTextView = (TextView) findViewById(R.id.tv_rating);
        overviewTextView = (TextView) findViewById(R.id.tv_overview);
        releaseTextView = (TextView) findViewById(R.id.tv_release);

        initUI(getMovieFromIntent());

    }

    private void initUI(Movie movie) {
        String ratingText = "Rating : " + movie.getVote_average() + "/10";
        String releaseText = "Release on " + movie.getRelease_date();

        titleTextView.setText(movie.getOriginal_title());
        ratingTextView.setText(ratingText);
        overviewTextView.setText(movie.getOverview());
        releaseTextView.setText(releaseText);

        Uri uri = Uri.parse("http://image.tmdb.org/t/p/w185/" + movie.getPoster_path());
        Picasso.with(this).load(uri).into(posterImageView);
    }

    private Movie getMovieFromIntent() {
        Intent intent = getIntent();
        Movie movie = new Movie();
        movie.setId(intent.getIntExtra(Movie.MOVIE_ID, 0));
        movie.setOriginal_title(intent.getStringExtra(Movie.MOVIE_ORIGINAL_TITLE));
        movie.setVote_average(intent.getDoubleExtra(Movie.MOVIE_VOTE_AVERAGE, 0));
        movie.setRelease_date(intent.getStringExtra(Movie.MOVIE_RELEASE_DATE));
        movie.setPoster_path(intent.getStringExtra(Movie.MOVIE_POSTER_PATH));
        movie.setOverview(intent.getStringExtra(Movie.MOVIE_OVERVIEW));
        return movie;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
