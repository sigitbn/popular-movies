package com.bimosigit.popularmovies.main;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bimosigit.popularmovies.R;
import com.bimosigit.popularmovies.adapter.MovieAdapter;
import com.bimosigit.popularmovies.detail.DetailActivity;
import com.bimosigit.popularmovies.model.Movie;
import com.bimosigit.popularmovies.model.source.local.MoviesContract;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements MainContract.View, MovieAdapter.ListItemClickListener {


    @BindView(R.id.rv_movies)
    RecyclerView mMoviesList;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mProgressBar;

    Unbinder unbinder;
    private MainContract.Presenter mPresenter;
    private ArrayList<Movie> mMovies;
    private MovieAdapter mAdapter;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);

        setHasOptionsMenu(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), numberOfColumns());
        mMoviesList.setLayoutManager(gridLayoutManager);
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
            Toast.makeText(activity, R.string.cannot_connect_server, Toast.LENGTH_SHORT).show();
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

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }
}
