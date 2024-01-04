package com.profitz.app.data.source.remote;

import androidx.annotation.Nullable;

import com.profitz.app.BuildConfig;
import com.profitz.app.data.model.Promo;
import com.profitz.app.data.model.PromosResponse;
import com.profitz.app.data.model.Review;
import com.profitz.app.data.model.ReviewsResponse;
import com.profitz.app.data.model.Video;
import com.profitz.app.data.model.VideosResponse;
import com.profitz.app.data.source.DataSource;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteDataSource implements DataSource {

    //private static final String THE_MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/";
    private static final String THE_MOVIE_DB_BASE_URL = "https://yoururl.com/api/";

    @Nullable
    private static RemoteDataSource INSTANCE = null;
    private final TheMovieDbApi mTheMovieDbApi;

    // Prevent direct instantiation.
    private RemoteDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(THE_MOVIE_DB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mTheMovieDbApi = retrofit.create(TheMovieDbApi.class);
    }

    public static RemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<List<Promo>> getPopularMovies() {
        return mTheMovieDbApi.getPopularMovies("BuildConfig.TMDB_API_KEY")
                .map(PromosResponse::getPromos);
    }

    @Override
    public Observable<List<Promo>> getTopRatedMovies() {
        return mTheMovieDbApi.getTopRatedMovies("BuildConfig.TMDB_API_KEY")
                .map(PromosResponse::getPromos);

    }

    @Override
    public Observable<List<Promo>> getFavoriteMovies() {
        // Not used yet
        return null;
    }

    @Override
    public Observable<Promo> getFavoriteMovieById(int movieId) {
        // Not used yet
        return null;
    }

    @Override
    public Observable<List<Review>> getMovieReviews(int movieId) {
        return mTheMovieDbApi.getMovieReviews(movieId, "BuildConfig.TMDB_API_KEY")
               .map(ReviewsResponse::getReviews);

    }

    @Override
    public Observable<List<Video>> getMovieVideos(int movieId) {
       return mTheMovieDbApi.getMovieVideos(movieId, "BuildConfig.TMDB_API_KEY")
               .map(VideosResponse::getVideos);

    }

    @Override
    public Observable<Boolean> saveMovieAsFavorite(Promo promo) {
        // Not used yet
        return null;
    }

    @Override
    public Observable<Boolean> deleteMovieFromFavorites(int movieId) {
        // Not used yet
        return null;
    }
}