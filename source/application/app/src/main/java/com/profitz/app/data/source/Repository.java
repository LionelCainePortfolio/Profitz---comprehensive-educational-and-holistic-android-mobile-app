package com.profitz.app.data.source;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.profitz.app.data.model.Promo;
import com.profitz.app.data.model.Review;
import com.profitz.app.data.model.Video;

import java.util.List;

import io.reactivex.Observable;

public class Repository implements DataSource {

    @Nullable
    private static volatile Repository INSTANCE = null;

    @NonNull
    private final DataSource mRemoteDataSource;

    @NonNull
    private final DataSource mLocalDataSource;

    // Prevent direct instantiation.
    private Repository(@NonNull DataSource remoteDataSource,
                       @NonNull DataSource localDataSource) {
        mRemoteDataSource = remoteDataSource;
        mLocalDataSource = localDataSource;
    }

    public static Repository getInstance(@NonNull DataSource remoteDataSource,
                                         @NonNull DataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new Repository(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<List<Promo>> getPopularMovies() {
        return mRemoteDataSource.getPopularMovies();
    }

    @Override
    public Observable<List<Promo>> getTopRatedMovies() {
        return mRemoteDataSource.getTopRatedMovies();
    }

    @Override
    public Observable<List<Promo>> getFavoriteMovies() {
        return mLocalDataSource.getFavoriteMovies();
    }

    @Override
    public Observable<Promo> getFavoriteMovieById(int movieId) {
        return mLocalDataSource.getFavoriteMovieById(movieId);
    }

    @Override
    public Observable<List<Review>> getMovieReviews(int movieId) {
        return mRemoteDataSource.getMovieReviews(movieId);
    }

    @Override
    public Observable<List<Video>> getMovieVideos(int movieId) {
        return mRemoteDataSource.getMovieVideos(movieId);
    }

    @Override
    public Observable<Boolean> saveMovieAsFavorite(Promo promo) {
        return mLocalDataSource.saveMovieAsFavorite(promo);
    }

    @Override
    public Observable<Boolean> deleteMovieFromFavorites(int movieId) {
        return mLocalDataSource.deleteMovieFromFavorites(movieId);
    }
}