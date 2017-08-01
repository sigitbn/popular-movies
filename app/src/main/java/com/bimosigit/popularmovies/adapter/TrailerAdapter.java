package com.bimosigit.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bimosigit.popularmovies.R;
import com.bimosigit.popularmovies.model.Trailer;
import com.bimosigit.popularmovies.viewholder.TrailerViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sigitbn on 6/14/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerViewHolder> {

    ArrayList<Trailer> mTrailers;

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public TrailerAdapter(ArrayList<Trailer> movies, ListItemClickListener listener) {
        mOnClickListener = listener;
        mTrailers = movies;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_trailer, parent, false);

        return new TrailerViewHolder(view, mOnClickListener);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        Picasso.with(context).load(mTrailers.get(position).getThumnailUrl()).into(holder.trailerImageView);

    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }
}
