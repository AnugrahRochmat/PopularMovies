package com.example.android.myapplication.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.myapplication.BuildConfig;
import com.example.android.myapplication.R;
import com.example.android.myapplication.adapter.ReviewAdapter;
import com.example.android.myapplication.adapter.TrailerAdapter;
import com.example.android.myapplication.data.MovieContract;
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

    /**
     * Variable to store data from intent
     */
    private String movieId;
    private String movieTitle;
    private String movieOverview;
    private String movieBackdropPath;
    private String moviePoster;
    private String movieReleaseDate;
    private String movieVoteAverage;

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
    private Button favButton;
    private Button remButton;

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
        favButton = (Button) findViewById(R.id.favourite_button);
        remButton = (Button) findViewById(R.id.remove_favourite_button);

        /**
         * getIntent from MainActivity
         */
        Bundle data = getIntent().getExtras();
        if ( data != null) {
            Movie movie = data.getParcelable("movie");

            // Store data from intent to assigned variable
            movieId = movie.getId().toString();
            movieTitle = movie.getOriginalTitle();
            movieOverview = movie.getOverview();
            movieBackdropPath = movie.getBackdropPath();
            moviePoster = movie.getPosterPath();
            movieReleaseDate = movie.getReleaseDate();
            movieVoteAverage = movie.getVoteAverage().toString();

            // Display data from intent
            String url = "http://image.tmdb.org/t/p/w342/";
            Picasso.with(this).load(url + movieBackdropPath).into(backdropImage);
            Picasso.with(this).load(url + moviePoster).into(posterImage);
            originalTitle.setText(movieTitle);
            voteAverage.setText(movieVoteAverage + " / 10 ");
            releasedDate.setText(getParsedDate(movie.getReleaseDate()));
            synopsis.setText(movieOverview);
            setTitle(movie.getTitle());
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

        /**
         * Favourites Button
         */
        updateFavourites();


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

    /**
     * addFavourite
     */
    public void addFavourite(View view) {
        new AsyncTask<Void,Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if (!isFavourite()) {
                    ContentValues contentValues = new ContentValues();

                    contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieId);
                    contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movieTitle);
                    contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, movieOverview);
                    contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP, movieBackdropPath);
                    contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER, moviePoster);
                    contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movieReleaseDate);
                    contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, movieVoteAverage);

                    getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                updateFavourites();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * removeFavourite
     */
    public void removeFavourite() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if (isFavourite()) {
                    Uri uri = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendEncodedPath(movieId).build();
                    getContentResolver().delete(uri, null, null);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                updateFavourites();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * isFavourite
     */
    public boolean isFavourite(){
        Uri uri = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendEncodedPath(movieId).build();
        Cursor cursor = getContentResolver().query(uri,
                null,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                null,
                null
        );
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        }else {
            return false;
        }
    }

    /**
     * updateFavourites Button
     */
    private void updateFavourites() {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                return isFavourite();
            }

            @Override
            protected void onPostExecute(Boolean isFavourite) {
                if (isFavourite){
                    remButton.setVisibility(View.VISIBLE);
                    favButton.setVisibility(View.GONE);
                } else {
                    favButton.setVisibility(View.VISIBLE);
                    remButton.setVisibility(View.GONE);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFavourite(view);
            }
        });

        remButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFavourite();
            }
        });
    }

}
