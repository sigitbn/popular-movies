package com.bimosigit.popularmovies.model.source;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.bimosigit.popularmovies.main.MovieFilterType;
import com.bimosigit.popularmovies.model.Movie;
import com.bimosigit.popularmovies.model.source.local.MoviesContract;
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

    private static MoviesRepository INSTANCE;
    private ContentResolver contentResolver;
    private LoadMoviesCallback mCallBack;

    private MoviesRepository(@NonNull Context context) {
        contentResolver = context.getContentResolver();
    }

    public static MoviesRepository getInstance(@NonNull Context context) {

        if (INSTANCE == null) {
            INSTANCE = new MoviesRepository(context);
        }
        return INSTANCE;
    }

    @Override
    public void fetchMovies(MovieFilterType filterType, @NonNull LoadMoviesCallback callback) {
        mCallBack = callback;
        if (filterType.equals(MovieFilterType.FAVORITES)) {
            getFavorites();
        } else {
            new MoviesQueryTask().execute(filterType);
        }
    }

    private void getFavorites() {
        List<Movie> movies = new ArrayList<>();

        Cursor cursor = contentResolver.query(MoviesContract.MovieEntry.CONTENT_URI, null, null, null, MoviesContract.MovieEntry.COLUMN_MOVIE_FAVORITE);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Movie movie = new Movie();
                movie.setMovieID(cursor.getInt(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_ID)));
                movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_OVERVIEW)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE)));
                movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE)));

                movies.add(movie);
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        if (movies.isEmpty()) {
            mCallBack.onDataNotAvailable();
        } else {
            mCallBack.onMoviesLoaded(movies);
        }

    }


    @Override
    public boolean addToFavorites(Movie movie) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_ID, movie.getMovieID());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE, movie.getOriginalTitle());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, movie.getVoteAverage());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_FAVORITE, true);

        Uri uri = contentResolver.insert(MoviesContract.MovieEntry.CONTENT_URI, contentValues);
        return uri != null;

    }

    @Override
    public void addToFavorites(Integer movieID) {

    }

    @Override
    public boolean removeFromFavorites(Integer movieID) {

        Uri uri = MoviesContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(String.valueOf(movieID)).build();

        int deleteRow = contentResolver.delete(uri, null, null);

        return deleteRow > 0;
    }

    @Override
    public boolean isFavorites(Integer movieID) {

        Uri uri = MoviesContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(String.valueOf(movieID)).build();

        Cursor cursor = contentResolver.query(uri, null, null, null, MoviesContract.MovieEntry.COLUMN_MOVIE_FAVORITE);

        cursor.close();

        return cursor.getCount() > 0;
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

    private class MoviesQueryTask extends AsyncTask<MovieFilterType, Void, String> {

        @Override
        protected String doInBackground(MovieFilterType... filterTypes) {
            MovieFilterType filterType = filterTypes[0];
            String response = null;
            if (filterType.equals(MovieFilterType.FAVORITES)) {
                getFavorites();
                response = "Done";
            } else {
                URL moviesURL = NetworkUtils.buildURI(getQuery(filterType));
                try {
                    response = NetworkUtils.getResponseFromHttpUrl(moviesURL);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            List<Movie> movies = new ArrayList<>();
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject movieObject = jsonArray.getJSONObject(i);
                        Movie movie = new Movie();
                        movie.setMovieID(movieObject.getInt(MoviesContract.MovieEntry.COLUMN_MOVIE_ID));
                        movie.setOriginalTitle(movieObject.getString(MoviesContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE));
                        movie.setOverview(movieObject.getString(MoviesContract.MovieEntry.COLUMN_MOVIE_OVERVIEW));
                        movie.setPosterPath(movieObject.getString(MoviesContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH));
                        movie.setReleaseDate(movieObject.getString(MoviesContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE));
                        movie.setVoteAverage(movieObject.getDouble(MoviesContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE));

                        movies.add(movie);
                    }
                    mCallBack.onMoviesLoaded(movies);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                mCallBack.onDataNotAvailable();
            }
        }
    }
}
