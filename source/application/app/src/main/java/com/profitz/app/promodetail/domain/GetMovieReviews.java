package com.profitz.app.promodetail.domain;

import com.profitz.app.UseCase;import com.profitz.app.data.model.Review;
import com.profitz.app.data.source.DataSource;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class GetMovieReviews extends UseCase<List<Review>, GetMovieReviews.Params> {

    private final DataSource mRepository;

    public GetMovieReviews(DataSource repository,
                           Scheduler threadExecutor,
                           Scheduler postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    @Override
    public Observable<List<Review>> buildUseCaseObservable(Params params) {
        return mRepository.getMovieReviews(params.movieId);
    }

    public static final class Params {

        private final int movieId;

        private Params(int movieId) {
            this.movieId = movieId;
        }

        public static Params forMovie(int movieId) {
            return new Params(movieId);
        }
    }
}