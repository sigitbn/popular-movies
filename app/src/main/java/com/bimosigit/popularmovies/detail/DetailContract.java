package com.bimosigit.popularmovies.detail;

import com.bimosigit.popularmovies.BasePresenter;
import com.bimosigit.popularmovies.BaseView;
import com.bimosigit.popularmovies.model.Review;
import com.bimosigit.popularmovies.model.Trailer;

import java.util.List;

/**
 * Created by sigitbn on 7/30/17.
 */

public class DetailContract {
    interface View extends BaseView<Presenter> {

        void showTitle(String title);

        void showReleaseDate(String releaseDate);

        void showMoviePoster(String imageUri);

        void showVoteAverage(Double voteAverage);

        void showTrailerVideos(List<Trailer> trailers);

        void showUserReviews(List<Review> reviews);

        void launchTrailer(Trailer trailer);

        void setFavorite(boolean favorite);

        void showOverview(String overview);
    }

    interface Presenter extends BasePresenter {

        void addToFavorites();

        void removeFromFavorites();

        void loadMovie();

        void loadReviews();

        void loadTrailers();

    }
}
