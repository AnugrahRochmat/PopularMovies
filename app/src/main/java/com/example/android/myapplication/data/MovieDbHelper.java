package com.example.android.myapplication.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.myapplication.data.MovieContract.*;

/**
 * Created by Anugrah on 7/10/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_BACKDROP + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_POSTER + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, "+
                MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE + " TEXT NOT NULL " +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}











