package com.example.android.myapplication.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Created by Anugrah on 7/18/17.
 */

public class MovieProvider extends ContentProvider {

    public static final int CODE_MOVIE = 100;
    public static final int CODE_MOVIE_WITH_ID = 101;

    private MovieDbHelper movieDbHelper;
    private static final UriMatcher sUriMatcher = buiildUriMatcher();

    public static UriMatcher buiildUriMatcher(){
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        // URI = content://com.example.android.myapplication/movie/
        matcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE, CODE_MOVIE);

        // URI = content://com.example.android.myapplication/movie/{movie_id}
        matcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE + "/#", CODE_MOVIE_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        movieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    /**
     * Handle request for insert method
     * @param uri
     * @param values
     * @return
     */
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri retUri;

        switch (match) {
            case CODE_MOVIE:
                long id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if ( id > 0 ) {
                    retUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return retUri;
    }

    /**
     * Handle request for query method
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = movieDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case CODE_MOVIE_WITH_ID:
                String lastPathSegment = uri.getLastPathSegment();
                String[] mSelectArgs = new String[]{lastPathSegment};

                retCursor = db.query(MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        mSelectArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_MOVIE:
                retCursor = db.query(MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /**
     * Handle request for delete method
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int movieDeleted;

        switch (match) {
            case CODE_MOVIE_WITH_ID:
                String lastPathSegment = uri.getLastPathSegment();
                String[] mSelectArgs = new String[]{lastPathSegment};

                movieDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        mSelectArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (movieDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return movieDeleted;
    }


    /**
     * Handle request for update method
     * @param uri
     * @param values
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle request for getType method
     * @param uri
     * @return
     */
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
