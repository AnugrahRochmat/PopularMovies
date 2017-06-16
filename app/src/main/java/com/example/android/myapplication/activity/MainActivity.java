package com.example.android.myapplication.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.android.myapplication.R;
import com.example.android.myapplication.adapter.PosterAdapter;
import com.example.android.myapplication.model.Movie;
import com.example.android.myapplication.model.MoviesResponse;
import com.example.android.myapplication.rest.ApiClient;
import com.example.android.myapplication.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    // API key here
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
        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please insert your API key", Toast.LENGTH_LONG).show();
            return;
        }

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.movies_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<MoviesResponse> call = apiService.getTopRatedMovies(API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                List<Movie> movies = response.body().getResults();
                recyclerView.setAdapter(new PosterAdapter(movies, getApplicationContext()));
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });

    }
}


