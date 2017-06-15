package com.example.android.myapplication.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.android.myapplication.R;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    // Declaring imageview variable
    private ImageView imageView;

    //private static final String TAG = MainActivity.class.getSimpleName();

    // API key here
    //private  final static String API_KEY = "6ef4360865ba5932e05c5d9edb7eaaff";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Initializing image view
         */
        imageView = (ImageView) findViewById(R.id.imageView);

        /**
         * Loading image from URL
         */
        Picasso.with(this)
                .load("http://image.tmdb.org/t/p/w185//gfJGlDaHuWimErCr5Ql0I8x9QSy.jpg")
                .into(imageView);

/*        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please insert your API key", Toast.LENGTH_LONG).show();
            return;
        }

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.movies_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<MoviesResponse> call = apiService.getPopularMovies(API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                List<Movie> movies = response.body().getResults();
                recyclerView.setAdapter(new MoviesAdapter(movies, getApplicationContext()));
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });*/

    }
}
