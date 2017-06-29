package com.bimosigit.popularmovies.model.source;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.bimosigit.popularmovies.main.MovieFilterType;
import com.bimosigit.popularmovies.model.Movie;
import com.bimosigit.popularmovies.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sigitbn on 6/20/17.
 */

public class MoviesRepository implements MoviesDataSource {

    private LoadMoviesCallback mCallBack;

    public MoviesRepository() {
    }

    @Override
    public void fetchMovies(MovieFilterType filterType, @NonNull LoadMoviesCallback callback) {
        mCallBack = callback;
        String query = getQuery(filterType);
        URL moviesURL = NetworkUtils.buildURI(query);
        new MoviesQueryTask().execute(moviesURL);
    }

    private String getQuery(MovieFilterType filterType) {
        switch (filterType) {
            case POPULAR_MOVIE:
                return "popular";
            case TOP_RATED:
                return "top_rated";
            default:
                return "top_rated";
        }
    }

    private class MoviesQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL moviesUrl = urls[0];
            String response = null;
            try {
                response = NetworkUtils.getResponseFromHttpUrl(moviesUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            List<Movie> movies = new ArrayList<Movie>();
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject movieObject = jsonArray.getJSONObject(i);
                        Movie movie = new Movie();
                        movie.setMovieID(movieObject.getInt(Movie.MOVIE_ID));
                        movie.setOriginalTitle(movieObject.getString(Movie.MOVIE_ORIGINAL_TITLE));
                        movie.setOverview(movieObject.getString(Movie.MOVIE_OVERVIEW));
                        movie.setPosterPath(movieObject.getString(Movie.MOVIE_POSTER_PATH));
                        movie.setReleaseDate(movieObject.getString(Movie.MOVIE_RELEASE_DATE));
                        movie.setVoteAverage(movieObject.getDouble(Movie.MOVIE_VOTE_AVERAGE));

                        movies.add(movie);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mCallBack.onMoviesLoaded(movies);
            } else {
                mCallBack.onDataNotAvailable();
            }
        }
    }
}
