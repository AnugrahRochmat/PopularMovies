package com.example.android.myapplication.activity;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.myapplication.BuildConfig;
import com.example.android.myapplication.R;
import com.example.android.myapplication.adapter.PosterAdapter;
import com.example.android.myapplication.data.MovieContract;
import com.example.android.myapplication.model.Movie;
import com.example.android.myapplication.model.MoviesResponse;
import com.example.android.myapplication.rest.ApiClient;
import com.example.android.myapplication.rest.ApiInterface;
import com.facebook.stetho.Stetho;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    /**
     * API KEY here
     */
    private static final String API_KEY = BuildConfig.API_KEY;

    /**
     * Variable Declaration
     */
    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressBar progressBar;
    private TextView errorMessage;

    private RecyclerView recyclerView;
    private PosterAdapter adapter;

    private String mSortBy = "popular";
    //private String defaultSort = "popular";

    private static final int FAVOURITES_LOADER_ID = 3;
    private static final String SAVED_MOVIES_KEY = "SAVED_MOVIES_KEY";
    private static final String SAVED_SORT_KEY = "SAVED_SORT_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Trying stetho
         */
        // Create an InitializerBuilder
        Stetho.InitializerBuilder initializerBuilder = Stetho.newInitializerBuilder(this);
        // Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this));
        // Initialize Stetho with the Initializer
        Stetho.initialize(initializerBuilder.build());

        /**
         * RecyclerView with GridLayout Manager
         */
        errorMessage = (TextView) findViewById(R.id.error_messsage);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        recyclerView = (RecyclerView) findViewById(R.id.movies_recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new PosterAdapter(new ArrayList<Movie>(),getApplicationContext());
        recyclerView.setAdapter(adapter);

        //getSupportLoaderManager().initLoader(FAVOURITES_LOADER_ID,null,this);
        //defaultSort = "popular";
        //loadMovies(defaultSort);

        if (savedInstanceState != null) {
            mSortBy = savedInstanceState.getString(SAVED_SORT_KEY);
            if (savedInstanceState.containsKey(SAVED_MOVIES_KEY)){
                List<Movie> movies = savedInstanceState.getParcelableArrayList(SAVED_MOVIES_KEY);
                adapter.setMoviesData(movies);
            }
        } else {
            loadMovies(mSortBy);
        }


        Log.d(TAG, "Lifecycle Event: onCreate");

    }

    public void loadMovies(String sortBy) {
        new FetchMoviesTask(sortBy).execute();
    }

    private void showErrorMessage() {
        recyclerView.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }


    /**
     * AsyncTask
     */
    public class FetchMoviesTask extends AsyncTask<Void, Void, List<Movie>> {

        private String fetchSortBy;

        public FetchMoviesTask(String sb) {
            fetchSortBy = sb;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(Void... params) {

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<MoviesResponse> call = apiService.getMovies(fetchSortBy,API_KEY);

            try {
                Response<MoviesResponse> response = call.execute();
                List<Movie> movies = response.body().getResults();
                return movies;
            } catch (IOException e) {
                Log.e(TAG, "A problem occured ", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            progressBar.setVisibility(View.INVISIBLE);
            if (movies != null) {
                adapter.setMoviesData(movies);
            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
        switch (loaderId) {
            case FAVOURITES_LOADER_ID:
                return new CursorLoader(this,
                        MovieContract.MovieEntry.CONTENT_URI,
                        MovieContract.MovieEntry.MOVIE_COLUMNS,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.add(cursor);
        findViewById(R.id.progress_bar).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * Create sort_by menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_by, menu);

        switch (mSortBy) {
            case "popular":
                menu.findItem(R.id.sb_popular).setChecked(true);
                break;
            case "top_rated":
                menu.findItem(R.id.sb_top_rated).setChecked(true);
                break;
            case "favourites":
                menu.findItem(R.id.sb_favourites ).setChecked(true);
                break;
        }
        return true;
    }

    /**
     * Handle sort_by menu selected by user
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sb_popular:
                mSortBy = "popular";
                getSupportLoaderManager().destroyLoader(FAVOURITES_LOADER_ID);
                loadMovies(mSortBy);
                item.setChecked(true);
                break;
            case R.id.sb_top_rated:
                mSortBy = "top_rated";
                getSupportLoaderManager().destroyLoader(FAVOURITES_LOADER_ID);
                loadMovies(mSortBy);
                item.setChecked(true);
                break;
            case R.id.sb_favourites:
                mSortBy = "favourites";
                recyclerView.setVisibility(View.VISIBLE);
                errorMessage.setVisibility(View.INVISIBLE);
                getSupportLoaderManager().initLoader(FAVOURITES_LOADER_ID, null, this);
                item.setChecked(true);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


//    @Override
//    protected void onStart() {
//        super.onStart();
//        Log.d(TAG, "Lifecycle Event: onStart");
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.d(TAG, "Lifecycle Event: onResume";
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Log.d(TAG, "Lifecycle Event: onPause");
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Log.d(TAG, "Lifecycle Event: onStop");
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Log.d(TAG, "Lifecycle Event: onRestart");
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Log.d(TAG, "Lifecycle Event: onDestroy");
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Log.d(TAG, "Lifecycle Event: onSaveInstanceState");

        ArrayList<Movie> mov = new ArrayList<>(adapter.getMovies());
        if (mov != null && !mov.isEmpty()) {
            outState.putParcelableArrayList(SAVED_MOVIES_KEY, mov);
        }
        outState.putString(SAVED_SORT_KEY, mSortBy);
    }
}


