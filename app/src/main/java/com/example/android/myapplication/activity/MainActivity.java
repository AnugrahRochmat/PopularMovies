package com.example.android.myapplication.activity;

import android.os.AsyncTask;
import android.os.Bundle;
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
import com.example.android.myapplication.model.Movie;
import com.example.android.myapplication.model.MoviesResponse;
import com.example.android.myapplication.rest.ApiClient;
import com.example.android.myapplication.rest.ApiInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

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

    private String mSortBy;
    private String defaultSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        defaultSort = "popular";
        loadMovies(defaultSort);

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

    /**
     * Create sort_by menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_by, menu);
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
                loadMovies(mSortBy);
                item.setChecked(true);
                break;
            case R.id.sb_top_rated:
                mSortBy = "top_rated";
                loadMovies(mSortBy);
                item.setChecked(true);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}


