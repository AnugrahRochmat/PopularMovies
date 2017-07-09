package com.example.android.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.myapplication.R;
import com.example.android.myapplication.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Anugrah on 7/10/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private List<Trailer> trailers;
    private Context context;

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView trailerThumbnail;

        public TrailerViewHolder(View view) {
            super(view);
            trailerThumbnail = view.findViewById(R.id.trailer_thumbnail);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Trailer trailer = trailers.get(getAdapterPosition());

            Intent intentToStartTrailer = new Intent(Intent.ACTION_VIEW);
            intentToStartTrailer.setData(Uri.parse(trailer.getVideoUrl()));

            v.getContext().startActivity(intentToStartTrailer);
        }
    }

    public TrailerAdapter(List<Trailer> trailers, Context context) {
        this.trailers = trailers;
        this.context = context;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutInflaterReview = R.layout.list_trailer_movie;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutInflaterReview, viewGroup, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, final int position) {
        Trailer trailer = trailers.get(position);
        Picasso.with(context).load(trailer.getVideoThumbnail()).into(holder.trailerThumbnail);
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public void setTrailersData(List<Trailer> trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }
}
