package com.bimosigit.popularmovies.model.source;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.bimosigit.popularmovies.model.Trailer;
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

public class TrailerRepository implements TrailerDataSource {

    private LoadTrailersCallback mCallBack;

    public TrailerRepository() {
    }

    @Override
    public void fetchTrailers(int movieID, @NonNull LoadTrailersCallback callback) {
        mCallBack = callback;
        URL url = NetworkUtils.buildURI(String.valueOf(movieID), "videos");
        new QueryTask().execute(url);
    }

    private class QueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String response = null;
            try {
                response = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            List<Trailer> trailers = new ArrayList<Trailer>();
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject trailerObject = jsonArray.getJSONObject(i);
                        Trailer trailer = new Trailer();
                        trailer.setKey(trailerObject.getString(Trailer.TRAILER_KEY));
                        trailer.setSite(trailerObject.getString(Trailer.TRAILER_SITE));
                        trailers.add(trailer);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mCallBack.onTrailerLoaded(trailers);
            } else {
                mCallBack.onDataNotAvailable();
            }
        }
    }
}
