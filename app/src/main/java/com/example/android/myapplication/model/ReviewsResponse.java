package com.example.android.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Anugrah on 7/8/17.
 */

public class ReviewsResponse {
    /**
     * Variable Declaration
     */
    @SerializedName("id")
    private Integer id;
    @SerializedName("page")
    private Integer page;
    @SerializedName("results")
    private List<Review> results;
    @SerializedName("total_pages")
    private Integer totalPages;
    @SerializedName("total_results")
    private Integer totalResults;

    /**
     * Get method to fill data from API and set method to fill the variable with the data
     */
    public int getMovieId() {
        return id;
    }

    public void setMovieId(int page) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

}
