package com.bimosigit.popularmovies.main;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements MainContract.View, MovieAdapter.ListItemClickListener {


    private MainContract.Presenter mPresenter;
    private RecyclerView mMoviesList;
    private ProgressBar mProgressBar;
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
        setHasOptionsMenu(true);
        mMoviesList = (RecyclerView) view.findViewById(R.id.rv_movies);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_loading_indicator);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
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
        intent.putExtra(Movie.MOVIE_ID, movie.getId());
        intent.putExtra(Movie.MOVIE_ORIGINAL_TITLE, movie.getOriginal_title());
        intent.putExtra(Movie.MOVIE_OVERVIEW, movie.getOverview());
        intent.putExtra(Movie.MOVIE_POSTER_PATH, movie.getPoster_path());
        intent.putExtra(Movie.MOVIE_RELEASE_DATE, movie.getRelease_date());
        intent.putExtra(Movie.MOVIE_VOTE_AVERAGE, movie.getVote_average());
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
        }
        mPresenter.loadMovies();
        return super.onOptionsItemSelected(item);
    }
}
