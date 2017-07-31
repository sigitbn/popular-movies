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
    private MoviesRepository mMoviesRepository;
    private MovieFilterType mCurrentFiltering = MovieFilterType.TOP_RATED;

    public MainPresenter(MainContract.View mainFragment, MoviesRepository moviesRepository) {

        mMoviesRepository = moviesRepository;
        mMoviesView = mainFragment;
        mMoviesView.setPresenter(this);
    }

    @Override
    public void start() {
        loadMovies();
    }

    @Override
    public void loadMovies() {
        mMoviesView.setLoadingIndicator(true);

        mMoviesRepository.fetchMovies(mCurrentFiltering, new MoviesDataSource.LoadMoviesCallback() {
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

    @Override
    public void setFiltering(MovieFilterType filterType) {
        mCurrentFiltering = filterType;
    }

    @Override
    public MovieFilterType getFiltering() {
        return mCurrentFiltering;
    }
}
