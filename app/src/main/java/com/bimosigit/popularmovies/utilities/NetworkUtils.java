package com.bimosigit.popularmovies.utilities;

import android.net.Uri;

import com.bimosigit.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


/**
 * Created by sigitbn on 6/14/17.
 */

public class NetworkUtils {

    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String API_KEY = "api_key";

    public static URL buildURI(String query) {
        Uri builtURI = Uri.parse(BASE_URL).buildUpon()
                .appendPath(query)
                .appendQueryParameter(API_KEY, BuildConfig.TheMovieDbApiKey)
                .build();
        URL url = null;

        try {
            url = new URL(builtURI.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream inputStream = urlConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
