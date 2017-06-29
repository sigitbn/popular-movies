package com.bimosigit.popularmovies.main;

import com.bimosigit.popularmovies.BasePresenter;
import com.bimosigit.popularmovies.BaseView;
import com.bimosigit.popularmovies.model.Movie;

import java.util.List;

/**
 * Created by sigitbn on 6/20/17.
 */

public interface MainContract {
    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showMovies(List<Movie> movies);

        void showMovieDetailsUi(Movie movie);

        void showLoadingMoviesError();
    }

    interface Presenter extends BasePresenter {
        void loadMovies();

        void setFiltering(MovieFilterType filterType);

        MovieFilterType getFiltering();
    }
}
