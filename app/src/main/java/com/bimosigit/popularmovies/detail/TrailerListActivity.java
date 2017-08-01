package com.bimosigit.popularmovies.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.bimosigit.popularmovies.R;
import com.bimosigit.popularmovies.adapter.TrailerAdapter;
import com.bimosigit.popularmovies.model.Trailer;
import com.bimosigit.popularmovies.model.source.local.MoviesContract;
import com.bimosigit.popularmovies.utilities.ScreenUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailerListActivity extends AppCompatActivity implements TrailerAdapter.ListItemClickListener {

    @BindView(R.id.rv_movie_trailers)
    RecyclerView mMovieTrailersRecyclerView;
    private ArrayList<Trailer> mTrailers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer_list);

        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.trailer) + " " + getIntent().getStringExtra(MoviesContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE));
        }
        mTrailers = new ArrayList<>();
        mTrailers = (ArrayList<Trailer>) getIntent().getSerializableExtra(getString(R.string.trailer_list_extra));
        TrailerAdapter mAdapter = new TrailerAdapter(mTrailers, this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(TrailerListActivity.this, ScreenUtils.numberOfColumns(this));

        mMovieTrailersRecyclerView.setLayoutManager(gridLayoutManager);
        mMovieTrailersRecyclerView.setHasFixedSize(true);

        mMovieTrailersRecyclerView.setAdapter(mAdapter);

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
        Trailer trailer = mTrailers.get(clickedItemIndex);
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.youtube_link) + trailer.getKey())));
    }

}
