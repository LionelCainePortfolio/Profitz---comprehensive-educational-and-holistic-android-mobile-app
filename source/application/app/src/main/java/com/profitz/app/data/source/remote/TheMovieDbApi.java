package com.profitz.app.data.source.remote;

import com.profitz.app.data.model.PromosResponse;
import com.profitz.app.data.model.ReviewsResponse;
import com.profitz.app.data.model.VideosResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDbApi {


   //@GET("movie/popular")
    @GET("Promocje.php")
    Observable<PromosResponse> getPopularMovies(@Query("api_key") String apiKey);

   //@GET("movie/top_rated")
    @GET("Airdropy.php")
    Observable<PromosResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("{movie_id}/reviews")
    Observable<ReviewsResponse> getMovieReviews(@Path("movie_id") int movieId,
                                                @Query("api_key") String apiKey);

    @GET("{movie_id}/videos")
    Observable<VideosResponse> getMovieVideos(@Path("movie_id") int movieId,
                                              @Query("api_key") String apiKey);
}
