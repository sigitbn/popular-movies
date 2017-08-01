package com.bimosigit.popularmovies.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bimosigit.popularmovies.R;
import com.bimosigit.popularmovies.adapter.TrailerAdapter;

/**
 * Created by sigitbn on 6/14/17.
 */

public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView trailerImageView;
    final private TrailerAdapter.ListItemClickListener mOnClickListener;

    public TrailerViewHolder(View itemView, TrailerAdapter.ListItemClickListener listener) {
        super(itemView);

        trailerImageView = (ImageView) itemView.findViewById(R.id.iv_detail_trailer);
        mOnClickListener = listener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int clickedPosition = getAdapterPosition();
        mOnClickListener.onListItemClick(clickedPosition);
    }
}
