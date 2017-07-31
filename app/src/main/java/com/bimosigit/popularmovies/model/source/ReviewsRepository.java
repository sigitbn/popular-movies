package com.bimosigit.popularmovies.model.source;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.bimosigit.popularmovies.model.Review;
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

public class ReviewsRepository implements ReviewsDataSource {

    private ReviewsDataSource.LoadReviewsCallback mCallBack;

    public ReviewsRepository() {
    }

    @Override
    public void fetchReviews(int movieID, @NonNull LoadReviewsCallback callback) {
        mCallBack = callback;
        URL url = NetworkUtils.buildURI(String.valueOf(movieID), "reviews");
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
            List<Review> reviews = new ArrayList<Review>();
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject reviewObject = jsonArray.getJSONObject(i);
                        Review review = new Review();
                        review.setAuthor(reviewObject.getString(Review.REVIEW_AUTHOR));
                        review.setContent(reviewObject.getString(Review.REVIEW_CONTENT));
                        reviews.add(review);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mCallBack.onReviewsLoaded(reviews);
            } else {
                mCallBack.onDataNotAvailable();
            }
        }
    }
}
