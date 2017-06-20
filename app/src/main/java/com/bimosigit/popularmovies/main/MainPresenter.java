package com.bimosigit.popularmovies.main;

import com.bimosigit.popularmovies.model.Movie;
import com.bimosigit.popularmovies.model.source.MoviesDataSource;
import com.bimosigit.popularmovies.model.source.MoviesRepository;

import java.util.List;

/**
 * Created by sigitbn on 6/20/17.
 */

public class MainPresenter implements MainContract.Presenter {


    private final MainContract.View mMoviesView;
    private final String mQuery;
    private MoviesRepository mMoviesRepository;

    public MainPresenter(MainFragment mainFragment, String query, MoviesRepository moviesRepository) {

        mMoviesRepository = moviesRepository;
        mMoviesView = mainFragment;
        mQuery = query;
        mMoviesView.setPresenter(this);
    }

    @Override
    public void start() {
        loadMovies(mQuery);
    }

    @Override
    public void loadMovies(String query) {
        mMoviesView.setLoadingIndicator(true);

        mMoviesRepository.fetchMovies(query, new MoviesDataSource.LoadMoviesCallback() {
            @Override
            public void onMoviesLoaded(List<Movie> movies) {
                mMoviesView.showMovies(movies);
            }

            @Override
            public void onDataNotAvailable() {
                mMoviesView.showLoadingMoviesError();
            }
        });
    }
}
