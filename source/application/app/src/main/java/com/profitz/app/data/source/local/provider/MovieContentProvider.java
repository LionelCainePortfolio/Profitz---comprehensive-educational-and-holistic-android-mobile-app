package com.profitz.app.data.source.local.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.profitz.app.data.source.local.provider.MovieContract.MovieEntry;


public class MovieContentProvider extends ContentProvider {

    private MovieDbHelper mMovieDbHelper;

    // Define final integer constants for the directory of tasks and a single item.
    public static final int MOVIES = 100;
    public static final int MOVIE_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();


    public static UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /*
          All paths added to the UriMatcher have a corresponding int.
          For each kind of uri we may want to access, add the corresponding match with addURI.
          The two calls below add matches for the movie directory and a single item by ID.
         */
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mMovieDbHelper = MovieDbHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        final SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        // Query for the movies directory and write a default case
        switch (match) {
            // Query for the movies directory
            case MOVIES:
                retCursor = db.query(MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // Add a case to query for a single row of data by ID
            // Use selections and selectionArgs to filter for that ID
            case MOVIE_WITH_ID:
                // Get the id from the URI
                String id = uri.getPathSegments().get(1);

                // Selection is the id column = ?, and the Selection args = the row ID from the URI
                String mSelection = "id=?";
                String[] mSelectionArgs = new String[]{id};

                // Construct a query as we would normally, passing in the selection/args
                retCursor = db.query(MovieEntry.TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri,
                      @Nullable ContentValues values) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned

        if (match == MOVIES) {// Insert new values into the database
            long id = db.insert(MovieEntry.TABLE_NAME, null, values);
            if (id > 0) {
                returnUri = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, id);
            } else {
                throw new SQLException("Failed to insert row into " + uri);
            }
            // Set the value for the returnedUri and write the default case for unknown URI's
            // Default case throws an UnsupportedOperationException
        } else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        // Keep track of the number of deleted movies
        int moviesDeleted; // starts as 0

        // Write the code to delete a single row of data
        // Handle the single item case, recognized by the ID included in the URI path
        if (match == MOVIE_WITH_ID) {// Get the movie ID from the URI path
            String id = uri.getPathSegments().get(1);

            // Selection is the id column = ?, and the Selection args = the movie ID from the URI
            String mSelection = "id=?";
            String[] mSelectionArgs = new String[]{id};

            // Use selections/selectionArgs to filter for this ID
            moviesDeleted = db.delete(MovieEntry.TABLE_NAME, mSelection, mSelectionArgs);
        } else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change
        if (moviesDeleted != 0) {
            // A movie was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of movies deleted
        return moviesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri,
                      @Nullable ContentValues values,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not used function");
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIES:
                // directory
                return "vnd.android.cursor.dir" + "/" +
                        MovieContract.AUTHORITY + "/" + MovieContract.PATH_MOVIES;
            case MOVIE_WITH_ID:
                // single item type
                return "vnd.android.cursor.item" + "/" +
                        MovieContract.AUTHORITY + "/" + MovieContract.PATH_MOVIES;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }
}
