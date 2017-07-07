package com.example.android.myapplication.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Anugrah on 7/8/17.
 */

public class Review {
    /**
     * Variable Declaration
     */
    @SerializedName("id")
    private String id;
    @SerializedName("author")
    private String author;
    @SerializedName("content")
    private String content;
    @SerializedName("url")
    private String url;

    /**
     * Review Class Constructor
     */
    public Review(String id, String author, String content, String url){
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    /**
     * Get method to fill data from API and set method to fill the variable with the data
     */
    public String getReviewId() {
        return id;
    }

    public void setReviewId(int page) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
