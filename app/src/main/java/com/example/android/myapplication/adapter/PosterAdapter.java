package com.example.android.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.myapplication.R;
import com.example.android.myapplication.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Anugrah on 6/16/17.
 */

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterViewHolder> {
    /**
     * Declaration variable
     */
    private List<Movie> movies;
    private Context context;

    public static class PosterViewHolder extends RecyclerView.ViewHolder {
        ImageView posterImage;

        public PosterViewHolder(View view) {
            super(view);
            posterImage = (ImageView) view.findViewById(R.id.poster_image);
        }
    }

    public PosterAdapter(List<Movie> movies, Context context) {
        this.movies = movies;
        this.context = context;
    }

    @Override
    public PosterAdapter.PosterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutInflaterPoster = R.layout.list_poster_movie;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutInflaterPoster, viewGroup, false);
        return new PosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PosterViewHolder holder, final int position) {
        Picasso.with(context).load(movies.get(position).getPosterPath()).into(holder.posterImage);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
