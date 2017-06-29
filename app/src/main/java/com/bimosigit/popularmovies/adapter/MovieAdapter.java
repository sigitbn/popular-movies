package com.bimosigit.popularmovies.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bimosigit.popularmovies.R;
import com.bimosigit.popularmovies.model.Movie;
import com.bimosigit.popularmovies.viewholder.MovieViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sigitbn on 6/14/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    ArrayList<Movie> mMovies;

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public MovieAdapter(ArrayList<Movie> movies, ListItemClickListener listener) {
        mOnClickListener = listener;
        mMovies = movies;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_movie, parent, false);

        return new MovieViewHolder(view, mOnClickListener);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        Movie movie = mMovies.get(position);
        Uri uri = Uri.parse("http://image.tmdb.org/t/p/w185/" + movie.getPosterPath());
        Picasso.with(context).load(uri).into(holder.posterImageView);

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }
}
