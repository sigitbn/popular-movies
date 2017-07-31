package com.bimosigit.popularmovies.model.source;

import android.support.annotation.NonNull;

import com.bimosigit.popularmovies.model.Trailer;

import java.util.List;

/**
 * Created by sigitbn on 6/20/17.
 */

public interface TrailerDataSource {

    interface LoadTrailersCallback {

        void onTrailerLoaded(List<Trailer> trailers);

        void onDataNotAvailable();
    }

    void fetchTrailers(int movieID, @NonNull LoadTrailersCallback callback);
}
