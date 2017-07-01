package com.bimosigit.popularmovies.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bimosigit.popularmovies.R;
import com.bimosigit.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.iv_poster)
    ImageView mPosterImageView;
    @BindView(R.id.tv_original_title)
    TextView mOriginalTitleTextView;
    @BindView(R.id.tv_rating)
    TextView mRatingTextView;
    @BindView(R.id.tv_release)
    TextView mReleaseTextView;
    @BindView(R.id.tv_overview)
    TextView mOverviewTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initUI(getMovieFromIntent());

    }

    private void initUI(Movie movie) {
        String ratingText = getString(R.string.rating) + movie.getVoteAverage() + "/10";
        String releaseText = getString(R.string.release_on) + movie.getReleaseDate();

        mOriginalTitleTextView.setText(movie.getOriginalTitle());
        mRatingTextView.setText(ratingText);
        mOverviewTextView.setText(movie.getOverview());
        mReleaseTextView.setText(releaseText);

        Uri uri = Uri.parse("http://image.tmdb.org/t/p/w185/" + movie.getPosterPath());
        Picasso.with(this).load(uri).into(mPosterImageView);
    }

    private Movie getMovieFromIntent() {

        Movie movie = new Movie();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            movie.setMovieID(intent.getIntExtra(Movie.MOVIE_ID, 0));
            movie.setOriginalTitle(intent.getStringExtra(Movie.MOVIE_ORIGINAL_TITLE));
            movie.setVoteAverage(intent.getDoubleExtra(Movie.MOVIE_VOTE_AVERAGE, 0));
            movie.setReleaseDate(intent.getStringExtra(Movie.MOVIE_RELEASE_DATE));
            movie.setPosterPath(intent.getStringExtra(Movie.MOVIE_POSTER_PATH));
            movie.setOverview(intent.getStringExtra(Movie.MOVIE_OVERVIEW));
        }
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
