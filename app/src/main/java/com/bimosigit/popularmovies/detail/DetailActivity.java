package com.bimosigit.popularmovies.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.bimosigit.popularmovies.R;
import com.bimosigit.popularmovies.model.Movie;
import com.bimosigit.popularmovies.model.source.MoviesRepository;
import com.bimosigit.popularmovies.model.source.ReviewsRepository;
import com.bimosigit.popularmovies.model.source.TrailerRepository;
import com.bimosigit.popularmovies.model.source.local.MoviesContract;
import com.bimosigit.popularmovies.utilities.ActivityUtils;

import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.detail_content_frame);
        if (detailFragment == null) {
            detailFragment = DetailFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), detailFragment, R.id.detail_content_frame);
        }

        ReviewsRepository reviewsRepository = new ReviewsRepository();
        TrailerRepository trailerRepository = new TrailerRepository();
        MoviesRepository moviesRepository = MoviesRepository.getInstance(getApplicationContext());

        //Create presenter
        new DetailPresenter(getMovieFromIntent(), reviewsRepository, trailerRepository, moviesRepository, detailFragment);
//        initUI(getMovieFromIntent());

    }
//
//    private void initUI(Movie movie) {
//        String ratingText = getString(R.string.rating) + movie.getVoteAverage() + "/10";
//        String releaseText = getString(R.string.release_on) + movie.getReleaseDate();
//
//        mOriginalTitleTextView.setText(movie.getOriginalTitle());
//        mRatingTextView.setText(ratingText);
//        mOverviewTextView.setText(movie.getOverview());
//        mReleaseTextView.setText(releaseText);
//
//        Uri uri = Uri.parse("http://image.tmdb.org/t/p/w185/" + movie.getPosterPath());
//        Picasso.with(this).load(uri).into(mPosterImageView);
//    }

    private Movie getMovieFromIntent() {

        Movie movie = new Movie();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            movie.setMovieID(intent.getIntExtra(MoviesContract.MovieEntry.COLUMN_MOVIE_ID, 0));
            movie.setOriginalTitle(intent.getStringExtra(MoviesContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE));
            movie.setVoteAverage(intent.getDoubleExtra(MoviesContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, 0));
            movie.setReleaseDate(intent.getStringExtra(MoviesContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE));
            movie.setPosterPath(intent.getStringExtra(MoviesContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH));
            movie.setOverview(intent.getStringExtra(MoviesContract.MovieEntry.COLUMN_MOVIE_OVERVIEW));
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
