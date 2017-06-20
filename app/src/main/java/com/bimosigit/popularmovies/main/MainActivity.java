package com.bimosigit.popularmovies.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import com.bimosigit.popularmovies.R;
import com.bimosigit.popularmovies.adapter.MovieAdapter;
import com.bimosigit.popularmovies.model.Movie;
import com.bimosigit.popularmovies.model.source.MoviesRepository;
import com.bimosigit.popularmovies.utilities.ActivityUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {

    private RecyclerView mMoviesList;
    private MovieAdapter mAdapter;
    private ProgressBar mProgressBar;
    private ArrayList<Movie> mMovies;
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
        mPresenter = new MainPresenter(mainFragment, "top_rated", moviesRepository);

    }

}
