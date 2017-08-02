package com.bimosigit.popularmovies.main;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bimosigit.popularmovies.R;
import com.bimosigit.popularmovies.adapter.MovieAdapter;
import com.bimosigit.popularmovies.detail.DetailActivity;
import com.bimosigit.popularmovies.model.Movie;
import com.bimosigit.popularmovies.model.source.local.MoviesContract;
import com.bimosigit.popularmovies.utilities.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements MainContract.View, MovieAdapter.ListItemClickListener {


    private static final String LIST_STATE_KEY = "LIST_STATE_KEY";
    @BindView(R.id.rv_movies)
    RecyclerView mMoviesList;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mProgressBar;

    Unbinder unbinder;
    @BindView(R.id.no_movies_text)
    TextView noMoviesTextView;
    @BindView(R.id.no_movies_views)
    LinearLayout noMoviesViews;

    private MainContract.Presenter mPresenter;
    private ArrayList<Movie> mMovies;
    private MovieAdapter mAdapter;
    private Parcelable mListState;
    private LinearLayoutManager layoutManager;
    private View view;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovies = new ArrayList<>();
        mAdapter = new MovieAdapter(mMovies, this);

        layoutManager = new GridLayoutManager(getContext(), ScreenUtils.numberOfColumns(getActivity()));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mListState = mMoviesList.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, mListState);
        outState.putSerializable(LIST_STATE_KEY, mMovies);
        super.onSaveInstanceState(outState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);

        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);
            mMovies = (ArrayList<Movie>) savedInstanceState.getSerializable(LIST_STATE_KEY);
            mAdapter = new MovieAdapter(mMovies, this);
            layoutManager.onRestoreInstanceState(mListState);
        }
        mMoviesList.setLayoutManager(layoutManager);
        mMoviesList.setHasFixedSize(true);
        mMoviesList.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        setLoadingIndicator(false);
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mProgressBar.setVisibility(active ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showMovies(List<Movie> movies) {
        setLoadingIndicator(false);
        showEmptyScreen(false, getString(R.string.favorites_is_empty));
        mMovies.clear();
        mMovies.addAll(movies);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMovieDetailsUi(Movie movie) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(MoviesContract.MovieEntry.COLUMN_MOVIE_ID, movie.getMovieID());
        intent.putExtra(MoviesContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE, movie.getOriginalTitle());
        intent.putExtra(MoviesContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        intent.putExtra(MoviesContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
        intent.putExtra(MoviesContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
        intent.putExtra(MoviesContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, movie.getVoteAverage());
        startActivity(intent);
    }

    @Override
    public void showLoadingMoviesError() {
        setLoadingIndicator(false);
        Activity activity = getActivity();
        if (activity != null) {
            if (mPresenter.getFiltering().equals(MovieFilterType.FAVORITES)) {
                showEmptyScreen(true, getString(R.string.favorites_is_empty));
                mAdapter.notifyDataSetChanged();
                Toast.makeText(activity, R.string.favorites_is_empty, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, R.string.cannot_connect_server, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showEmptyScreen(boolean visibility, String message) {
        if (visibility) {
            noMoviesTextView.setText(message);
            noMoviesViews.setVisibility(View.VISIBLE);

            mMoviesList.setVisibility(View.GONE);
        } else {
            noMoviesViews.setVisibility(View.GONE);
            mMoviesList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Movie movie = mMovies.get(clickedItemIndex);
        showMovieDetailsUi(movie);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.top_rated:
                mPresenter.setFiltering(MovieFilterType.TOP_RATED);
                break;
            case R.id.popular:
                mPresenter.setFiltering(MovieFilterType.POPULAR_MOVIE);
                break;
            case R.id.favorites:
                mPresenter.setFiltering(MovieFilterType.FAVORITES);
                break;
        }
        mPresenter.loadMovies();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
