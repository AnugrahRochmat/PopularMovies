package com.example.android.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.myapplication.R;
import com.example.android.myapplication.activity.MovieDetailActivity;
import com.example.android.myapplication.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Anugrah on 6/16/17.
 */

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterViewHolder> {
    /**
     * Variable Declaration
     */
    private List<Movie> movies;
    private Context context;

    /**
     * ViewHolder to contain a view for list item movie
     */
    public class PosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView posterImage;

        public PosterViewHolder(View view) {
            super(view);
            posterImage = view.findViewById(R.id.poster_image);
            view.setOnClickListener(this);
        }

        /**
         * Handle movie item from a movie list that was clicked
         * @param v
         */
        @Override
        public void onClick(View v) {
            Movie movie = movies.get(getAdapterPosition());

            Intent intentToStartDetailActivity = new Intent(v.getContext(), MovieDetailActivity.class);
            intentToStartDetailActivity.putExtra("movie", movie);

            v.getContext().startActivity(intentToStartDetailActivity);
        }
    }

    /**
     * PosterAdapter class constructor
     * @param movies
     * @param context
     */
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
        Movie movie = movies.get(position);
        Picasso.with(context).load(movie.getPosterPath()).into(holder.posterImage);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setMoviesData(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

}
