package com.bimosigit.popularmovies.detail;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.bimosigit.popularmovies.R;
import com.bimosigit.popularmovies.adapter.ReviewAdapter;
import com.bimosigit.popularmovies.model.Review;
import com.bimosigit.popularmovies.model.source.local.MoviesContract;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewListActivity extends AppCompatActivity implements ReviewAdapter.ListItemClickListener {

    @BindView(R.id.rv_movie_reviews)
    RecyclerView mMovieReviewsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.review) + " " + getIntent().getStringExtra(MoviesContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE));
        }
        ArrayList<Review> mReviews = new ArrayList<>();
        mReviews = (ArrayList<Review>) getIntent().getSerializableExtra(getString(R.string.review_list_extra));
        ReviewAdapter mAdapter = new ReviewAdapter(mReviews, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ReviewListActivity.this);
        mMovieReviewsRecyclerView.setLayoutManager(layoutManager);
        mMovieReviewsRecyclerView.setHasFixedSize(true);

        mMovieReviewsRecyclerView.setAdapter(mAdapter);

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

    @Override
    public void onListItemClick(int clickedItemIndex) {

    }

}
