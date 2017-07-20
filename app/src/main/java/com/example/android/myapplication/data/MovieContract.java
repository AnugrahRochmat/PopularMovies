package com.example.android.myapplication.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Anugrah on 7/10/17.
 */

public class MovieContract {

    // Content Authority
    public static final String CONTENT_AUTHORITY = "com.example.android.myapplication";
    // Base Content URI
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    // Path directory
    public static final String PATH_MOVIE = "movie";

    public static final class MovieEntry implements BaseColumns {

        // Content URI to query movie table
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "original_title";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_BACKDROP = "backdrop_path";
        public static final String COLUMN_MOVIE_POSTER = "poster_path";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";
        public static final String COLUMN_MOVIE_VOTE_AVERAGE = "vote_average";

        public static final String[] MOVIE_COLUMNS = {
                COLUMN_MOVIE_ID,
                COLUMN_MOVIE_TITLE,
                COLUMN_MOVIE_OVERVIEW,
                COLUMN_MOVIE_BACKDROP,
                COLUMN_MOVIE_POSTER,
                COLUMN_MOVIE_RELEASE_DATE,
                COLUMN_MOVIE_VOTE_AVERAGE,
        };

    }
}
