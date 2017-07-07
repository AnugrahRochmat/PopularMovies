package com.example.android.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.myapplication.R;
import com.example.android.myapplication.model.Review;

import java.util.List;

/**
 * Created by Anugrah on 7/8/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> reviews;
    private Context context;

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        public final TextView reviewAuthor;
        public final TextView reviewContent;

        public ReviewViewHolder(View view) {
            super(view);
            reviewAuthor = view.findViewById(R.id.tv_review_author);
            reviewContent = view.findViewById(R.id.tv_review_content);
        }
    }

    public ReviewAdapter(List<Review> reviews, Context context) {
        this.reviews = reviews;
        this.context = context;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutInflaterReview = R.layout.review_movie;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutInflaterReview, viewGroup, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, final int position) {
        Review review = reviews.get(position);
        holder.reviewAuthor.setText(review.getAuthor());
        holder.reviewContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void setReviewsData(List<Review> reviews){
        this.reviews = reviews;
        notifyDataSetChanged();
    }
}
