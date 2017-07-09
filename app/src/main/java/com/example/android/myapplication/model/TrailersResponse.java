package com.example.android.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Anugrah on 7/10/17.
 */

public class TrailersResponse {
    /**
     * Variable Declaration
     */
    @SerializedName("id")
    private Integer id;
    @SerializedName("results")
    private List<Trailer> results;

    /**
     * Get method to fill data from API and set method to fill the variable with the data
     */
    public int getMovieId() {
        return id;
    }

    public void setMovieId(int page) {
        this.id = id;
    }

    public List<Trailer> getResults() {
        return results;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }


}
