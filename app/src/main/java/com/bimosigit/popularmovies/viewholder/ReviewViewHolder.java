package com.bimosigit.popularmovies.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bimosigit.popularmovies.R;
import com.bimosigit.popularmovies.adapter.ReviewAdapter;

/**
 * Created by sigitbn on 6/14/17.
 */

public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView authorTextView;
    public TextView contentTextView;

    final private ReviewAdapter.ListItemClickListener mOnClickListener;

    public ReviewViewHolder(View itemView, ReviewAdapter.ListItemClickListener listener) {
        super(itemView);

        authorTextView = (TextView) itemView.findViewById(R.id.tv_detail_review_author);
        contentTextView = (TextView) itemView.findViewById(R.id.tv_detail_review_content);
        mOnClickListener = listener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int clickedPosition = getAdapterPosition();
        mOnClickListener.onListItemClick(clickedPosition);
    }
}
