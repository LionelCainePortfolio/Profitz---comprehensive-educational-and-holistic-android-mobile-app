package com.profitz.app.data.source;

import com.profitz.app.data.model.Promo;
import com.profitz.app.data.model.Review;
import com.profitz.app.data.model.Video;

import java.util.List;

import io.reactivex.Observable;

public interface DataSource {

    Observable<List<Promo>> getPopularMovies();

    Observable<List<Promo>> getTopRatedMovies();

    Observable<List<Promo>> getFavoriteMovies();

    Observable<Promo> getFavoriteMovieById(int movieId);

    Observable<List<Review>> getMovieReviews(int movieId);

    Observable<List<Video>> getMovieVideos(int movieId);

    Observable<Boolean> saveMovieAsFavorite(Promo promo);

    Observable<Boolean> deleteMovieFromFavorites(int movieId);
}