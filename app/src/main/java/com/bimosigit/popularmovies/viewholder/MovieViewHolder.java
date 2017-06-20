package com.bimosigit.popularmovies.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bimosigit.popularmovies.R;
import com.bimosigit.popularmovies.adapter.MovieAdapter;

/**
 * Created by sigitbn on 6/14/17.
 */

public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView posterImageView;
    final private MovieAdapter.ListItemClickListener mOnClickListener;

    public MovieViewHolder(View itemView, MovieAdapter.ListItemClickListener listener) {
        super(itemView);

        posterImageView = (ImageView) itemView.findViewById(R.id.iv_poster);
        mOnClickListener = listener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int clickedPosition = getAdapterPosition();
        mOnClickListener.onListItemClick(clickedPosition);
    }
}
