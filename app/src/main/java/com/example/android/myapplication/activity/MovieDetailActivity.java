package com.example.android.myapplication.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
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
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
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
    private static final String SAVED_TRAILERS_KEY = "SAVED_TRAILERS_KEY";
    private static final String SAVED_REVIEWS_KEY = "SAVED_REVIEWS_KEY";

    private String root = Environment.getExternalStorageDirectory().toString();
    private File myDir = new File(root + "/pop_movies");

    private ShareActionProvider mShareActionProvider;

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
    private Bitmap bitmapImage;

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
            File extPosterPath = new File(root + "/pop_movies/" + movie.getId() + ".jpg");
                if (isOnline()){
                    Picasso.with(this).load(url + moviePoster).into(posterImage);
                } else {
                    Picasso.with(this).load(extPosterPath).into(posterImage);
                }
            originalTitle.setText(movieTitle);
            voteAverage.setText(movieVoteAverage + " / 10 ");
            releasedDate.setText(getParsedDate(movie.getReleaseDate()));
            synopsis.setText(movieOverview);
            setTitle(movie.getTitle());

            // Load image into target
            Picasso.with(this)
                    .load(url + movie.getPosterPath())
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                            bitmapImage = bitmap;
                        }
                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }
                    });

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

        //new FetchReviewsTask(movieId).execute();

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVED_REVIEWS_KEY)){
                List<Review> reviews = savedInstanceState.getParcelableArrayList(SAVED_REVIEWS_KEY);
                reviewAdapter.setReviewsData(reviews);
            }
        } else {
            new FetchReviewsTask(movieId).execute();
        }

        /**
         * Trailer Recycler View
         */
        trailerRecyclerView = (RecyclerView) findViewById(R.id.trailer_recycler_view);
        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        trailerRecyclerView.setLayoutManager(trailerLayoutManager);
        trailerRecyclerView.setHasFixedSize(true);

        trailerAdapter = new TrailerAdapter(new ArrayList<Trailer>(), getApplicationContext());
        trailerRecyclerView.setAdapter(trailerAdapter);

        //new FetchTrailersTask(movieId).execute();

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVED_TRAILERS_KEY)){
                List<Trailer> trailers = savedInstanceState.getParcelableArrayList(SAVED_TRAILERS_KEY);
                trailerAdapter.setTrailersData(trailers);
            }
        } else {
            new FetchTrailersTask(movieId).execute();
        }

        /**
         * Favourites Button
         */
        updateFavourites();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isOnline()) {
            getMenuInflater().inflate(R.menu.share_trailer, menu);
            MenuItem item = menu.findItem(R.id.share_trailer);
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        }
        return true;
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
                Trailer trailer = trailers.get(0);
                setShareIntent(trailer);
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
                SaveImage(bitmapImage);
            }
        });

        remButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFavourite();

                // remove savedimages from file system
                String fname = movieId + ".jpg";
                File file = new File (myDir, fname);
                if (file.exists ()) file.delete ();
            }
        });
    }

    /**
     * save image to external storage
     */
    private void SaveImage(Bitmap finalBitmap) {

        myDir.mkdirs();

        String fname = movieId + ".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * check online status
     */
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    /**
     * set share intent
     */
    private void setShareIntent(Trailer trailer) {
        Intent shareIntent = new Intent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, trailer.getName() + ": " + trailer.getVideoUrl());
        shareIntent.setType("text/plain");

        mShareActionProvider.setShareIntent(shareIntent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        ArrayList<Trailer> trail = new ArrayList<>(trailerAdapter.getTrailers());
        if (trail != null && !trail.isEmpty()) {
            outState.putParcelableArrayList(SAVED_TRAILERS_KEY, trail);
        }

        ArrayList<Review> rev = new ArrayList<>(reviewAdapter.getReviews());
        if (rev != null && !rev.isEmpty()) {
            outState.putParcelableArrayList(SAVED_REVIEWS_KEY, rev);
        }
    }
}
