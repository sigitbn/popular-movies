package com.bimosigit.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bimosigit.popularmovies.R;
import com.bimosigit.popularmovies.model.Review;
import com.bimosigit.popularmovies.viewholder.ReviewViewHolder;

import java.util.ArrayList;

/**
 * Created by sigitbn on 6/14/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {

    ArrayList<Review> mReviews;

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public ReviewAdapter(ArrayList<Review> reviews, ListItemClickListener listener) {
        mOnClickListener = listener;
        mReviews = reviews;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_review, parent, false);

        return new ReviewViewHolder(view, mOnClickListener);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.authorTextView.setText(mReviews.get(position).getAuthor());
        holder.contentTextView.setText(mReviews.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }
}
