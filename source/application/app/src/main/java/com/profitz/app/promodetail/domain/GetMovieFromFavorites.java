package com.profitz.app.promodetail.domain;

import com.profitz.app.UseCase;import com.profitz.app.data.model.Promo;
import com.profitz.app.data.source.DataSource;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class GetMovieFromFavorites extends UseCase<Promo, GetMovieFromFavorites.Params> {

    private final DataSource mRepository;

    public GetMovieFromFavorites(DataSource repository,
                                 Scheduler threadExecutor,
                                 Scheduler postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    @Override
    public Observable<Promo> buildUseCaseObservable(Params params) {
        return mRepository.getFavoriteMovieById(params.movieId);
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