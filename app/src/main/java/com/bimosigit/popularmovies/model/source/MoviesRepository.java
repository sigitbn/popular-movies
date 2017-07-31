package com.bimosigit.popularmovies.model.source;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.bimosigit.popularmovies.main.MovieFilterType;
import com.bimosigit.popularmovies.model.Movie;
import com.bimosigit.popularmovies.model.source.local.MoviesContract;
import com.bimosigit.popularmovies.model.source.local.MoviesDbHelper;
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
    private LoadMoviesCallback mCallBack;
    private MoviesDbHelper mDbHelper;

    private MoviesRepository(Context context) {
        mDbHelper = new MoviesDbHelper(context);
    }

    public static MoviesRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MoviesRepository(context);
        }
        return INSTANCE;
    }

    @Override
    public void fetchMovies(MovieFilterType filterType, @NonNull LoadMoviesCallback callback) {
        mCallBack = callback;
        String query = getQuery(filterType);

        if (filterType.equals(MovieFilterType.FAVORITES)) {
            getFavorites();
        } else {
            URL moviesURL = NetworkUtils.buildURI(query);
            new MoviesQueryTask().execute(moviesURL);
        }
    }

    private void getFavorites() {
        List<Movie> movies = new ArrayList<Movie>();
        SQLiteDatabase sqLiteDatabase = mDbHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(MoviesContract.MovieEntry.TABLE_NAME, null, null, null, null, null, MoviesContract.MovieEntry.COLUMN_MOVIE_FAVORITE);
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
        sqLiteDatabase.close();

        if (movies.isEmpty()) {
            mCallBack.onDataNotAvailable();
        } else {
            mCallBack.onMoviesLoaded(movies);
        }

    }


    @Override
    public void addToFavorites(Movie movie) {

        SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_ID, movie.getMovieID());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE, movie.getOriginalTitle());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, movie.getVoteAverage());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_FAVORITE, true);

        sqLiteDatabase.insert(MoviesContract.MovieEntry.TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
    }

    @Override
    public void addToFavorites(Integer movieID) {

    }

    @Override
    public boolean isFavorites(Integer movieID) {
        SQLiteDatabase sqLiteDatabase = mDbHelper.getReadableDatabase();
        String sql = "SELECT "
                + MoviesContract.MovieEntry.COLUMN_MOVIE_ID + " FROM "
                + MoviesContract.MovieEntry.TABLE_NAME + " WHERE "
                + MoviesContract.MovieEntry.COLUMN_MOVIE_ID + "="
                + movieID;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        boolean favorite = cursor.getCount() > 0;
        cursor.close();

        return favorite;
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
                        movie.setMovieID(movieObject.getInt(MoviesContract.MovieEntry.COLUMN_MOVIE_ID));
                        movie.setOriginalTitle(movieObject.getString(MoviesContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE));
                        movie.setOverview(movieObject.getString(MoviesContract.MovieEntry.COLUMN_MOVIE_OVERVIEW));
                        movie.setPosterPath(movieObject.getString(MoviesContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH));
                        movie.setReleaseDate(movieObject.getString(MoviesContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE));
                        movie.setVoteAverage(movieObject.getDouble(MoviesContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE));

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
