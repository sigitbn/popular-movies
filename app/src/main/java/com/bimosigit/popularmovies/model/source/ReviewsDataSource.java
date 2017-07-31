package com.bimosigit.popularmovies.model.source;

import android.support.annotation.NonNull;

import com.bimosigit.popularmovies.model.Review;

import java.util.List;

/**
 * Created by sigitbn on 6/20/17.
 */

public interface ReviewsDataSource {

    interface LoadReviewsCallback {

        void onReviewsLoaded(List<Review> reviews);

        void onDataNotAvailable();
    }

    void fetchReviews(int movieID, @NonNull LoadReviewsCallback callback);
}
