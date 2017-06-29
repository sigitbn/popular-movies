package com.bimosigit.popularmovies.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bimosigit.popularmovies.R;
import com.bimosigit.popularmovies.model.source.MoviesRepository;
import com.bimosigit.popularmovies.utilities.ActivityUtils;

public class MainActivity extends AppCompatActivity {

    private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";
    private MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (mainFragment == null) {
            mainFragment = MainFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mainFragment, R.id.content_frame);
        }

        MoviesRepository moviesRepository = new MoviesRepository();
        mPresenter = new MainPresenter(mainFragment, moviesRepository);
        if (savedInstanceState != null) {
            MovieFilterType currentFilterType = (MovieFilterType) savedInstanceState.getSerializable(CURRENT_FILTERING_KEY);
            mPresenter.setFiltering(currentFilterType);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CURRENT_FILTERING_KEY, mPresenter.getFiltering());
        super.onSaveInstanceState(outState);
    }
}
