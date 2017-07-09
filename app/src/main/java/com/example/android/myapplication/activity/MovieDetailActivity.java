package com.example.android.myapplication.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.myapplication.BuildConfig;
import com.example.android.myapplication.R;
import com.example.android.myapplication.adapter.ReviewAdapter;
import com.example.android.myapplication.adapter.TrailerAdapter;
import com.example.android.myapplication.model.Movie;
import com.example.android.myapplication.model.Review;
import com.example.android.myapplication.model.ReviewsResponse;
import com.example.android.myapplication.model.Trailer;
import com.example.android.myapplication.model.TrailersResponse;
import com.example.android.myapplication.rest.ApiClient;
import com.example.android.myapplication.rest.ApiInterface;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {

    /**
     * API KEY here
     */
    private static final String API_KEY = BuildConfig.API_KEY;
    private static final String TAG = MainActivity.class.getSimpleName();
    private String movieId;

    /**
     * Review
     */
    private RecyclerView reviewRecyclerView;
    private ReviewAdapter reviewAdapter;

    /**
     * Trailer
     */
    private RecyclerView trailerRecyclerView;
    private TrailerAdapter trailerAdapter;

    /**
     * Variable Declaration for Details layout
     */
    private ImageView backdropImage;
    private ImageView posterImage;
    private TextView originalTitle;
    private TextView voteAverage;
    private TextView releasedDate;
    private TextView synopsis;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        /**
         * Object reference for movie detail layout
         */
        backdropImage = (ImageView) findViewById(R.id.tv_detail_backdrop_image);
        posterImage = (ImageView) findViewById(R.id.tv_detail_poster_image);
        originalTitle = (TextView) findViewById(R.id.tv_detail_original_title);
        voteAverage = (TextView) findViewById(R.id.tv_detail_vote_average);
        releasedDate = (TextView) findViewById(R.id.tv_detail_released_date);
        synopsis = (TextView) findViewById(R.id.tv_detail_synopsis);
        progressBar = (ProgressBar) findViewById(R.id.detail_progress_bar);

        /**
         * getIntent from MainActivity
         */
        Bundle data = getIntent().getExtras();
        if ( data != null) {
            Movie movie = data.getParcelable("movie");
            Picasso.with(this).load(movie.getBackdropPath()).into(backdropImage);
            Picasso.with(this).load(movie.getPosterPath()).into(posterImage);
            originalTitle.setText(movie.getOriginalTitle());
            voteAverage.setText(movie.getVoteAverage().toString() + " / 10 ");
            releasedDate.setText(getParsedDate(movie.getReleaseDate()));
            synopsis.setText(movie.getOverview());
            setTitle(movie.getTitle());

            movieId = movie.getId().toString();
        } else {
            finish();
        }

        /**
         * Review Recycler View
         */
        reviewRecyclerView = (RecyclerView) findViewById(R.id.review_recycler_view);
        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this);
        reviewRecyclerView.setLayoutManager(reviewLayoutManager);
        reviewRecyclerView.setHasFixedSize(true);

        reviewAdapter = new ReviewAdapter(new ArrayList<Review>(), getApplicationContext());
        reviewRecyclerView.setAdapter(reviewAdapter);

        new FetchReviewsTask(movieId).execute();

        /**
         * Trailer Recycler View
         */
        trailerRecyclerView = (RecyclerView) findViewById(R.id.trailer_recycler_view);
        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        trailerRecyclerView.setLayoutManager(trailerLayoutManager);
        trailerRecyclerView.setHasFixedSize(true);

        trailerAdapter = new TrailerAdapter(new ArrayList<Trailer>(), getApplicationContext());
        trailerRecyclerView.setAdapter(trailerAdapter);

        new FetchTrailersTask(movieId).execute();
    }

    /**
     * Make (up button) behaviour like (back button)
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Parsed released DATE format
     * @param stringDate
     * @return
     */
    public String getParsedDate(String stringDate) {
        String parsedResult = null;
        DateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat toFormat = new SimpleDateFormat("MMM dd, yyyy");
        try {
            Date parseDate = fromFormat.parse(stringDate);
            parsedResult = toFormat.format(parseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parsedResult;
    }

    /**
     * AsyncTask for Review
     */
    public class FetchReviewsTask extends AsyncTask<Void, Void, List<Review>> {

        private String fetchMovieId;

        public FetchReviewsTask(String id) {
            fetchMovieId = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Review> doInBackground(Void... params) {
            ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
            Call<ReviewsResponse> call = apiservice.getReviews(fetchMovieId, API_KEY);

            try {
                Response<ReviewsResponse> response = call.execute();
                List<Review> reviews = response.body().getResults();
                return reviews;
            } catch (IOException e) {
                Log.e(TAG, "A problem occured ", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Review> reviews) {
            progressBar.setVisibility(View.INVISIBLE);
            if (reviews != null) {
                reviewAdapter.setReviewsData(reviews);
            }
        }
    }

    /**
     * AsyncTask for Trailer
     */
    public class FetchTrailersTask extends AsyncTask<Void, Void, List<Trailer>> {

        private String fetchMovieId;

        public FetchTrailersTask(String id) { fetchMovieId = id; }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Trailer> doInBackground(Void... params) {
            ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
            Call<TrailersResponse> call = apiservice.getTrailers(fetchMovieId, API_KEY);

            try {
                Response<TrailersResponse> response = call.execute();
                List<Trailer> trailers = response.body().getResults();
                return trailers;
            } catch (IOException e) {
                Log.e(TAG, "A problem occured ", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Trailer> trailers) {
            progressBar.setVisibility(View.INVISIBLE);
            if (trailers != null) {
                trailerAdapter.setTrailersData(trailers);
            }
        }
    }
}
