package com.example.android.myapplication.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.myapplication.R;
import com.example.android.myapplication.adapter.PosterAdapter;
import com.example.android.myapplication.model.Movie;
import com.example.android.myapplication.model.MoviesResponse;
import com.example.android.myapplication.rest.ApiClient;
import com.example.android.myapplication.rest.ApiInterface;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressBar progressBar;

    /**
     * API KEY here
     */
    private  final static String API_KEY = "6ef4360865ba5932e05c5d9edb7eaaff";

    private RecyclerView recyclerView;
    private PosterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * RecyclerView with GridLayout Manager
         */
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        recyclerView = (RecyclerView) findViewById(R.id.movies_recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        loadMovies();

    }

    private void loadMovies(){
        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please insert your API key", Toast.LENGTH_LONG).show();
            return;
        }

        new FetchMoviesTask().execute();
    }

    public class FetchMoviesTask extends AsyncTask<Void, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(Void... params) {

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<MoviesResponse> call = apiService.getPopularMovies(API_KEY);

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
                adapter = new PosterAdapter(movies,getApplicationContext());
                recyclerView.setAdapter(adapter);
            }
        }

    }


}


