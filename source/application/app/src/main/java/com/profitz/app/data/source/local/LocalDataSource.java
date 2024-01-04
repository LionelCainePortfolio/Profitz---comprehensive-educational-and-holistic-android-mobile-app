package com.profitz.app.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.Nullable;

import com.profitz.app.data.model.Promo;
import com.profitz.app.data.model.Review;
import com.profitz.app.data.model.Video;
import com.profitz.app.data.source.DataSource;
import com.profitz.app.data.source.local.provider.MovieContract.MovieEntry;
import com.profitz.app.util.RepositoryUtils;

import java.util.List;

import io.reactivex.Observable;

public class LocalDataSource implements DataSource {

    @Nullable
    private static LocalDataSource INSTANCE = null;

    private final Context mContext;

    // Prevent direct instantiation.
    private LocalDataSource(Context context) {
        mContext = context;
    }

    public static LocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LocalDataSource(context);
        }
        return INSTANCE;
    }


    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<List<Promo>> getPopularMovies() {
        // Not used yet
        return null;
    }

    @Override
    public Observable<List<Promo>> getTopRatedMovies() {
        // Not used yet
        return null;
    }

    @Override
    public Observable<List<Promo>> getFavoriteMovies() {
        return Observable.fromCallable(() -> {
            Cursor result = mContext.getContentResolver().query(
                    MovieEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
            return RepositoryUtils.toMoviesList(result);
        });
    }

    @Override
    public Observable<Promo> getFavoriteMovieById(int movieId) {
        return Observable.fromCallable(() -> {
            Cursor result = mContext.getContentResolver().query(
                    MovieEntry.CONTENT_URI.buildUpon().appendPath(Integer.toString(movieId)).build(),
                    null,
                    null,
                    null,
                    null);
            return RepositoryUtils.toMovie(result);
        });
    }

    @Override
    public Observable<List<Review>> getMovieReviews(int movieId) {
        // Not used yet
        return null;
    }

    @Override
    public Observable<List<Video>> getMovieVideos(int movieId) {
        // Not used yet
        return null;
    }

    @Override
    public Observable<Boolean> saveMovieAsFavorite(Promo promo) {
        return Observable.fromCallable(() -> {
            ContentValues movieContentValues = new ContentValues();

            /*
             * Sets the values of each column and inserts the promo.
             */
            movieContentValues.put(MovieEntry.COLUMN_VOTE_COUNT, promo.getVoteCount());
            movieContentValues.put(MovieEntry.COLUMN_DIFFICULT, promo.getDifficult());
            movieContentValues.put(MovieEntry.COLUMN_SHORT_DESC, promo.getShortDesc());
            movieContentValues.put(MovieEntry.COLUMN_PRICE_TYPE, promo.getPriceType());
            movieContentValues.put(MovieEntry.COLUMN_HOW_LONG, promo.getHow_long());
            movieContentValues.put(MovieEntry.COLUMN_ID, promo.getId());
            movieContentValues.put(MovieEntry.COLUMN_VIDEO, promo.hasVideo());
            movieContentValues.put(MovieEntry.COLUMN_VOTE_AVERAGE, promo.getVoteAverage());
            movieContentValues.put(MovieEntry.COLUMN_TITLE, promo.getTitle());
            movieContentValues.put(MovieEntry.COLUMN_POPULARITY, promo.getPopularity());
            movieContentValues.put(MovieEntry.COLUMN_POSTER_PATH, promo.getPosterPath());
            movieContentValues.put(MovieEntry.COLUMN_ORIGINAL_LANGUAGE, promo.getOriginalLanguage());
            movieContentValues.put(MovieEntry.COLUMN_ORIGINAL_TITLE, promo.getOriginalTitle());
            movieContentValues.put(MovieEntry.COLUMN_BACKDROP_PATH, promo.getBackdropPath());
            movieContentValues.put(MovieEntry.COLUMN_ADULT, promo.isAdult());
            movieContentValues.put(MovieEntry.COLUMN_OVERVIEW, promo.getOverview());
            movieContentValues.put(MovieEntry.COLUMN_INSTRUCTION_TYPE, promo.getInstructionType());
            movieContentValues.put(MovieEntry.COLUMN_RELEASE_DATE, promo.getReleaseDate());

            Uri resultUri = mContext.getContentResolver().insert(
                    MovieEntry.CONTENT_URI,
                    movieContentValues);

            return !Uri.EMPTY.equals(resultUri);
        });
    }

    @Override
    public Observable<Boolean> deleteMovieFromFavorites(int movieId) {
        return Observable.fromCallable(() -> {
            int rowsDeleted;

            rowsDeleted = mContext.getContentResolver().delete(
                    MovieEntry.CONTENT_URI.buildUpon().appendPath(Integer.toString(movieId)).build(),
                    null,
                    null);
            return rowsDeleted != 0;
        });
    }
}
