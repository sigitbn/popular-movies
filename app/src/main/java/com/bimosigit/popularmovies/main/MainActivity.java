package com.bimosigit.popularmovies.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bimosigit.popularmovies.R;
import com.bimosigit.popularmovies.model.source.MoviesRepository;
import com.bimosigit.popularmovies.utilities.ActivityUtils;

public class MainActivity extends AppCompatActivity {

    private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";
    private static final String MAIN_FRAGMENT = "MAIN_FRAGMENT";
    private MainPresenter mPresenter;
    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        MovieFilterType currentFilterType = MovieFilterType.POPULAR_MOVIE;
        if (savedInstanceState != null) {
            currentFilterType = (MovieFilterType) savedInstanceState.getSerializable(CURRENT_FILTERING_KEY);
            mainFragment = (MainFragment) getSupportFragmentManager().getFragment(savedInstanceState, MAIN_FRAGMENT);
        } else {
            mainFragment = MainFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mainFragment, R.id.content_frame);
        }

        MoviesRepository moviesRepository = MoviesRepository.getInstance(getApplicationContext());
        mPresenter = new MainPresenter(mainFragment, moviesRepository);
        mPresenter.setFiltering(currentFilterType);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(CURRENT_FILTERING_KEY, mPresenter.getFiltering());
        getSupportFragmentManager().putFragment(outState, MAIN_FRAGMENT, mainFragment);
    }
}
