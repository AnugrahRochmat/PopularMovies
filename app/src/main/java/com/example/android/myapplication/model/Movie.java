package com.example.android.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anugrah on 6/15/17.
 */

public class Movie {
    /**
     * Initialized variable to be stored data from API
     */
    @SerializedName("adult")
    private boolean adult;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("genre_ids")
    private List<Integer> genreIds = new ArrayList<Integer>();
    @SerializedName("id")
    private Integer id;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("overview")
    private String overview;
    @SerializedName("popularity")
    private Double popularity;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("title")
    private String title;
    @SerializedName("video")
    private Boolean video;
    @SerializedName("vote_average")
    private Double voteAverage;
    @SerializedName("vote_count")
    private Integer voteCount;

    /**
     * Movie class constructor
     */
    public Movie(boolean adult, String backdropPath, List<Integer> genreIds, Integer id, String originalLanguage,
                 String originalTitle, String overview, Double popularity, String posterPath, String releaseDate,
                 String title, Boolean video, Double voteAverage, Integer voteCount){
        this.adult = adult;
        this.backdropPath = backdropPath;
        this.genreIds = genreIds;
        this.id = id;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.title = title;
        this.video = video;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
    }

    /**
     * Get method to fill data from API and set method to fill the variable with the data
     */
    public boolean getAdult() {
        return adult;
    }
    public void setAdult(boolean adult){
        this.adult = adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }
    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }
    public void setGenreIds(List<Integer> genreIds){
        this.genreIds = genreIds;
    }

    public Integer getId(){
        return id;
    }
    public void setId(Integer id){
        this.id = id;
    }

    public String getOriginalLanguage(){
        return originalLanguage;
    }
    public void setOriginalLanguage(String originalLanguage){
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle(){
        return originalTitle;
    }
    public void setOriginalTitle(String originalTitle){
        this.originalTitle = originalTitle;
    }

    public String getOverview(){
        return overview;
    }
    public void setOverview(String overview){
        this.overview = overview;
    }

    public Double getPopularity(){
        return popularity;
    }
    public void setPopularity(Double popularity){
        this.popularity = popularity;
    }

    public String getPosterPath(){
        return posterPath;
    }
    public void setPosterPath(String posterPath){
        this.posterPath = posterPath;
    }

    public String getReleaseDate(){
        return releaseDate;
    }
    public void setReleaseDate(String releaseDate){
        this.releaseDate = releaseDate;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public Boolean getVideo(){
        return video;
    }
    public void setVideo(Boolean video){
        this.video =video;
    }

    public Double getVoteAverage(){
        return voteAverage;
    }
    public void setVoteAverage(Double voteAverage){
        this.voteAverage = voteAverage;
    }

    public Integer getVoteCount(){
        return voteCount;
    }
    public void setVoteCount(Integer voteCount){
        this.voteCount = voteCount;
    }

}
