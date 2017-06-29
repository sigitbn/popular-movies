package com.bimosigit.popularmovies.model.source;

import android.support.annotation.NonNull;

import com.bimosigit.popularmovies.main.MovieFilterType;
import com.bimosigit.popularmovies.model.Movie;

import java.util.List;

/**
 * Created by sigitbn on 6/20/17.
 */

public interface MoviesDataSource {

    interface LoadMoviesCallback {

        void onMoviesLoaded(List<Movie> movies);

        void onDataNotAvailable();
    }

    void fetchMovies(MovieFilterType filterType, @NonNull LoadMoviesCallback callback);
}
