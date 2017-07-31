package com.bimosigit.popularmovies.detail;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bimosigit.popularmovies.R;
import com.bimosigit.popularmovies.model.Review;
import com.bimosigit.popularmovies.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements DetailContract.View, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.iv_poster)
    ImageView mPosterImageView;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mProgressBar;

    @BindView(R.id.tv_original_title)
    TextView mOriginalTitleTextView;

    @BindView(R.id.tv_rating)
    TextView mRatingTextView;

    @BindView(R.id.tv_release)
    TextView mReleaseTextView;

    @BindView(R.id.tv_overview)
    TextView mOverviewTextView;

    @BindView(R.id.tb_detail_favorite)
    ToggleButton mFavoriteToggleButton;

    @BindView(R.id.iv_detail_a_trailer)
    ImageView mDetailTrailerAImageView;

    @BindView(R.id.iv_detail_b_trailer)
    ImageView mDetailTrailerBImageView;

    @BindView(R.id.tv_detail_review_author_a)
    TextView mDetailReviewAuthorATextView;

    @BindView(R.id.tv_detail_review_content_a)
    TextView mDetailReviewContentATextView;

    @BindView(R.id.tv_detail_review_author_b)
    TextView mDetailReviewAuthorBTextView;

    @BindView(R.id.tv_detail_review_content_b)
    TextView mDetailReviewContentBTextView;

    @BindView(R.id.btn_detail_show_more_trailer)
    Button mDetailShowMoreTrailerButton;

    @BindView(R.id.btn_detail_show_more_review)
    Button mDetailShowMoreReviewButton;

    @BindView(R.id.tv_detail_trailer)
    TextView mDetailTrailerTextView;

    @BindView(R.id.tv_detail_review)
    TextView mDetailReviewTextView;

    private Unbinder unbinder;
    private DetailContract.Presenter mPresenter;
    private boolean loadingMovies, loadingReviews, loadingTrailer;


    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        unbinder = ButterKnife.bind(this, view);

        loadingMovies = loadingReviews = loadingTrailer = true;
        loadingMovies = false;
//
//        movie = getMovieFromIntent();
//        initUI(movie);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (loadingMovies || loadingReviews || loadingTrailer) {
            active = true;
        }
        mProgressBar.setVisibility(active ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showTitle(String title) {
        mOriginalTitleTextView.setText(title);
    }

    @Override
    public void showReleaseDate(String releaseDate) {
        String releaseText = getString(R.string.release_on) + releaseDate;
        mReleaseTextView.setText(releaseText);
    }

    @Override
    public void showMoviePoster(String imageUri) {
        Uri uri = Uri.parse("http://image.tmdb.org/t/p/w185/" + imageUri);
        Picasso.with(getActivity()).load(uri).into(mPosterImageView);
    }

    @Override
    public void showVoteAverage(Double voteAverage) {
        String ratingText = getString(R.string.rating) + voteAverage + "/10";
        mRatingTextView.setText(ratingText);
    }

    @Override
    public void showOverview(String overview) {
        mOverviewTextView.setText(overview);
    }

    @Override
    public void showTrailerVideos(List<Trailer> trailers) {
        loadingTrailer = false;
        setLoadingIndicator(false);
        int trailerCount = trailers.size();
        mDetailTrailerTextView.setText(getString(R.string.trailer) + " (" + trailerCount + ")");
        for (int i = 0; i < trailerCount; i++) {
            switch (i) {
                case 0:
                    Picasso.with(getActivity()).load(trailers.get(i).getThumnailUrl()).into(mDetailTrailerAImageView);
                    break;

                case 1:
                    Picasso.with(getActivity()).load(trailers.get(i).getThumnailUrl()).into(mDetailTrailerBImageView);
                    break;

                default:
                    mDetailShowMoreTrailerButton.setVisibility(View.VISIBLE);
                    break;
            }
        }

    }

    @Override
    public void showUserReviews(List<Review> reviews) {
        loadingReviews = false;
        setLoadingIndicator(false);
        int reviewCount = reviews.size();
        mDetailReviewTextView.setText(getString(R.string.review) + " (" + reviewCount + ")");
        for (int i = 0; i < reviews.size(); i++) {
            switch (i) {
                case 0:
                    mDetailReviewAuthorATextView.setText(reviews.get(i).getAuthor());
                    mDetailReviewContentATextView.setText(reviews.get(i).getContent());
                    break;
                case 1:
                    mDetailReviewAuthorBTextView.setText(reviews.get(i).getAuthor());
                    mDetailReviewContentBTextView.setText(reviews.get(i).getContent());
                    break;
                default:
                    mDetailShowMoreReviewButton.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    @Override
    public void launchTrailer(Trailer trailer) {

    }

    @Override
    public void setFavorite(boolean favorite) {
        mFavoriteToggleButton.setChecked(favorite);
        mFavoriteToggleButton.setOnCheckedChangeListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
            Toast.makeText(getActivity(), "Added to Favorites", Toast.LENGTH_SHORT).show();
            mPresenter.addToFavorites();
        } else {
            Toast.makeText(getActivity(), "Removed from Favorites", Toast.LENGTH_SHORT).show();
            mPresenter.removeFromFavorites();
        }
    }

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }
}
