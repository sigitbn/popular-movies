package com.bimosigit.popularmovies.detail;

import com.bimosigit.popularmovies.model.Movie;
import com.bimosigit.popularmovies.model.Review;
import com.bimosigit.popularmovies.model.Trailer;
import com.bimosigit.popularmovies.model.source.MoviesRepository;
import com.bimosigit.popularmovies.model.source.ReviewsDataSource;
import com.bimosigit.popularmovies.model.source.ReviewsRepository;
import com.bimosigit.popularmovies.model.source.TrailerDataSource;
import com.bimosigit.popularmovies.model.source.TrailerRepository;

import java.util.List;

/**
 * Created by sigitbn on 7/30/17.
 */

public class DetailPresenter implements DetailContract.Presenter {

    private final DetailContract.View mView;
    private Movie mMovie;
    private ReviewsRepository mReviewsRepository;
    private TrailerRepository mTrailerRepository;
    private MoviesRepository mMoviesRepository;

    public DetailPresenter(Movie movie, ReviewsRepository reviewsRepository, TrailerRepository trailerRepository, MoviesRepository moviesRepository, DetailContract.View view) {
        mMovie = movie;
        mReviewsRepository = reviewsRepository;
        mTrailerRepository = trailerRepository;
        mMoviesRepository = moviesRepository;

        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.setLoadingIndicator(true);
        loadMovie();
        loadReviews();
        loadTrailers();
    }

    @Override
    public void addToFavorites() {
        mMoviesRepository.addToFavorites(mMovie);
    }

    @Override
    public void removeFromFavorites() {
        mMoviesRepository.removeFromFavorites(mMovie.getMovieID());
    }

    @Override
    public void loadMovie() {
        mView.showTitle(mMovie.getOriginalTitle());
        mView.showVoteAverage(mMovie.getVoteAverage());
        mView.showOverview(mMovie.getOverview());
        mView.showReleaseDate(mMovie.getReleaseDate());
        mView.showMoviePoster(mMovie.getPosterPath());
        mView.setFavorite(mMoviesRepository.isFavorites(mMovie.getMovieID()));

    }

    @Override
    public void loadReviews() {
        mReviewsRepository.fetchReviews(mMovie.getMovieID(), new ReviewsDataSource.LoadReviewsCallback() {
            @Override
            public void onReviewsLoaded(List<Review> reviews) {
                mView.showUserReviews(reviews);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });

    }

    @Override
    public void loadTrailers() {
        mTrailerRepository.fetchTrailers(mMovie.getMovieID(), new TrailerDataSource.LoadTrailersCallback() {
            @Override
            public void onTrailerLoaded(List<Trailer> trailers) {
                mView.showTrailerVideos(trailers);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }
}
