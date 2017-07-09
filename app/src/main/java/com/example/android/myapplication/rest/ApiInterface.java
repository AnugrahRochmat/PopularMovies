package com.example.android.myapplication.rest;

import com.example.android.myapplication.model.MoviesResponse;
import com.example.android.myapplication.model.ReviewsResponse;
import com.example.android.myapplication.model.TrailersResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Anugrah on 6/16/17.
 */

public interface ApiInterface {

    @GET("movie/{sort_by}")
    Call<MoviesResponse> getMovies(@Path("sort_by") String sortBy, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/videos")
    Call<TrailersResponse> getTrailers(@Path("movie_id") String movie_id, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    Call<ReviewsResponse> getReviews(@Path("movie_id") String movie_id, @Query("api_key") String apiKey);
}
